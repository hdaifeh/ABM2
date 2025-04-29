// Import the Arrays utility class to use methods like Arrays.fill()

import java.util.Arrays;

/**
 * This class represents an individual entity in a waste management simulation
 * model. It tracks population growth, waste production, and behavioral
 * intentions related to waste management practices like composting and sorting.
 */
public class Individual {

    // Instance variable to track the amount of waste produced by this individual
    private double wasteProduction;

    // Intention variables that represent the individual's propensity to compost waste
    private double compostingIntention;

    // Intention variable that represents the individual's propensity to sort waste for dedicated collection bin (eventually to methanisation)
    private double sortingIntention;

    // Array to store population values over time (for each year in the simulation)
    //private double[] P;
    // Array to store food waste production values over time
    double[] Bpf;

    // Array to store green waste production values over time
    double[] Bpg;

    // Base food waste production coefficient per capita
    private double b_pf;

    // Base green waste production coefficient per capita
    private double b_pg;

    // Initial value for food composting intention parameter
    private double αcf_initial;

    // Initial value for green composting intention parameter
    private double αcg_initial;

    // Initial value for food sorting intention parameter
    private double αsf_initial;

    // Initial value for green sorting intention parameter
    private double αsg_initial;

    double composterCap;
    double sortingGreenAndFoodCap;
    double compostedQty;
    double sortedGreenAndFoodQty;
    // Same for green waste.....

    /**
     * Constructor that initializes an Individual with specified parameters.
     *
     * @param sizeData The number of time periods (years) to simulate
     */
    public Individual(int sizeData, double bpf, double bpg, double αcfinitial, double αcginitial, double αsfinitial, double αsginitial) {
        // Initialize base coefficients and parameters from the params array
        b_pf = bpf;        // Base food waste production coefficient
        b_pg = bpg;        // Base green waste production coefficient
        αcf_initial = αcfinitial; // Initial food composting intention
        αcg_initial = αcginitial; // Initial green composting intention
        αsf_initial = αsfinitial; // Initial food sorting intention
        αsg_initial = αsginitial; // Initial green sorting intention

        // Initialize arrays to track values over simulation time periods
        Bpf = new double[sizeData];  // Food waste array
        Bpg = new double[sizeData];  // Green waste array

        // Initialize all array values to zero
        Arrays.fill(Bpf, b_pf);
        Arrays.fill(Bpg, b_pg);
    }

    /**
     * Calculates and updates the waste production for a specific year based on
     * population growth and waste reduction targets.
     *
     * @param year The current year in the simulation
     * @param αpf_target Target reduction for waste production
     * @param sigmoideABP Array of sigmoid function values modeling behavioral
     * change over time
     */
    public void computeWaste(int year, double αpf_target, double[] sigmoideABP) {
        //ABP[year] = αpf_target * sigmoideABP[year] ; 
        // Calculate food waste, reduced by target value and sigmoid-modeled behavior change
        Bpf[year] = Bpf[year] * (1 - αpf_target * sigmoideABP[year]);
        // Calculate green waste, using same reduction model as food waste
        Bpg[year] = Bpg[year] * (1 - αpf_target * sigmoideABP[year]);
    }

    /**
     * Updates the behavioral intentions (composting and sorting) based on
     * sigmoid models that represent how these intentions change over time.
     *
     * @param year Current year in the simulation
     * @param sigmoide_mcf Sigmoid values for food composting behavior change
     * @param sigmoide_mcg Sigmoid values for green composting behavior change
     * @param sigmoide_msf Sigmoid values for food sorting behavior change
     * @param sigmoide_msg Sigmoid values for green sorting behavior change
     */
    public void computeBehavioralIntentions(int year, double[] sigmoide_mcf, double[] sigmoide_mcg,
            double[] sigmoide_msf, double[] sigmoide_msg) {
        // Calculate composting intention by starting with initial value and adding sigmoid-modeled increase
        compostingIntention = αcf_initial + ((1 - αcf_initial) * sigmoide_mcf[year - 1]);

        // Ensure composting intention doesn't exceed 1.0 (100%)
        compostingIntention = Math.min(compostingIntention, 1.0);

        // Calculate sorting intention similarly
        sortingIntention = αsf_initial + ((1 - αsf_initial) * sigmoide_msf[year]);

        // Check if total intentions exceed 100%, which would be unrealistic
        double totalIntention = compostingIntention + sortingIntention;
        if (totalIntention > 1.0) {
            // Adjust sorting intention to ensure total intentions sum to 1.0 (100%)
            sortingIntention = 1 - compostingIntention;
        }
    }

    public void compost(int y) { // y stands for year
        Bcg[y] = αcg[y] * Bpg[y]; // Quantity of green waste going towards local composting
        Bcf[y] = αcf[y] * Bpf[y]; // Quantity of food waste going towards local composting
        // → HYPOTHESIS: If L[y] > K1: We have a surplus, then we will: First put green biowaste Bcg[y] in the recycling centre then if Bcg[y] is empty and there is still a surplus and L[y] is still greater than K1 then we put food biowaste Bcf[y] in the collection.
        if (y == yearRef) { // Calibration for the SBA case
            Kc_initial = Bcg[y] + Bcf[y];
        }
        Kct[y] = Kc_initial + ((αc_target - Kc_initial) * LinearHomeComposter[y]); // here sigmoid becomes linear see iteration
        // System.err.println("sigmoid " + LinearHomeComposter[y] + " Kct " + Kct + " Bcg " + Bcg[y] + " Bcf " + Bcf[y]+" Py "+P[y-1]+" "+αcg[y]+" Bpg "+Bpg[y]+" αcg "+αcg[y]+" αvg "+αvg[y]);
        if ((Bcg[y] + Bcf[y]) > Kct[y]) {
            Uc[y] = Bcg[y] + Bcf[y] - Kct[y]; // First calculation of surplus
            Bcg_composted[y] = Math.max(Bcg[y] - Uc[y], 0.0); // Quantity of green biowaste after applying the surplus
            sLbis[y] = Math.max(0.0, (Bcg_composted[y] + Bcf[y] - Kct[y])); // Second calculation of surplus to see if there is still surplus after removing green biowaste
            Bcf_composted[y] = Math.max(Bcf[y] - sLbis[y], 0.0); // Quantity of food biowaste after applying the surplus
            Ucf[y] = Math.min(sLbis[y], Bcf[y]); // Quantity of food biowaste removed due to surplus
            Ucg[y] = Math.min(Uc[y], Bcg[y]); // Quantity of green biowaste removed due to surplus
            Bcg[y] = Bcg_composted[y];
            Bcf[y] = Bcf_composted[y];
        }
        Bc_composted[y] = Bcf[y] + Bcg[y]; // Values of L after removing the surplus 
    }

    public void sortingFoodAndGreenWaste(int y) { // y stands for year
        Bsg[y] = αsg[y] * Bpg[y]; // Quantity of green waste going towards collection
        Bsf[y] = (αsf[y] * Bpf[y]) + Ucf[y]; // → Quantity of food biowaste going towards collection
        // if (y == 1) {
        // System.err.println(αsg[y] + " ka " + Ks_initial + " Bsg " + Bsg[y] + " Bsf " + Bsf[y] + " a3dv " + αvg[y] + " a1dv " + αcg[y]);
        // }
        // → HYPOTHESIS: if A[y] > KA: We have a surplus, then we will: First put green biowaste Bsg[y] in the recycling centre then if Bsg[y] is empty and there is still a surplus and A[y] is still greater than KA then we put food biowaste Bsf[y] in the household residual waste.
        Kst[y] = Ks_initial + ((αs_target - Ks_initial) * LinearDedicatedCollection[y]);
        // if (ident==1) System.err.println("year "+y+" ident terr "+ident+" Kacourant "+Kst);
        if ((Bsg[y] + Bsf[y]) > Kst[y]) {
            Us[y] = Bsf[y] + Bsg[y] - Kst[y]; // → First calculation of surplus
            Bsg_sorted[y] = Math.max(Bsg[y] - Us[y], 0.0); // Quantity of green waste after applying the surplus
            sAbis[y] = Math.max(0.0, (Bsf[y] + Bsg_sorted[y] - Kst[y])); // Second calculation of surplus to see if there is still surplus after removing green biowaste
            // Av_bis[y] = Math.max(Bsg[y] - Us[y], 0.0); // Quantity of green biowaste after applying the surplus
            Bsf_sorted[y] = Math.max(Bsf[y] - sAbis[y], 0.0); // → Quantity of food biowaste after applying the surplus
            Usg[y] = Math.min(Us[y], Bsg[y]); // Quantity of green biowaste removed due to surplus GOES TO THE RECYCLING CENTRE!!!!
            // if (Usg[y]<0.0) { System.err.println(" jfjqkdksdj "+Us[y]+" "+Bsg[y]); }
            // Dv[y]=Bv[y]+Usg[y]; // putting surplus back to the recycling centre
            Usf[y] = Math.min(sAbis[y], Bsf[y]); // → Quantity of food biowaste removed due to surplus
            Bsg[y] = Bsg_sorted[y];
            Bsf[y] = Bsf_sorted[y];
        }
        Bs_sorted[y] = Bsg[y] + Bsf[y]; // → Value of A[y] after removing the surplus
    }

    public void putInValCenter(int y) {
        Bv[y] = αvg[y] * Bpg[y] + Ucg[y] + Usg[y]; // Quantity of green biowaste going towards the recycling centre
    }

    public void putInBlackBin(int y) {
        Br[y] = (1 - αcf[y] - αsf[y]) * Bpf[y] + Usf[y]; // Quantity of food biowaste going towards residual household waste
        if (Br[y] < 0) {
            System.err.println(αsf[y] + " alpha1 " + αcf[y] + " Ba " + Bpf[y] + " sAa " + Usf[y]);
        }
    }

}
