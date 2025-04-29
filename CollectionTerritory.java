
import java.util.Arrays;

/**
 * @author shuet
 */
public class CollectionTerritory {

    Territory myTerre;
    Individual[] myIndividuals;
    // Define the starting point of the simulation
    int timeBeforeInit_Î±cf_initial; // here we consider the evolution of behavioural intention of food composting before the initial observation
    int timeBeforeInit_Î±cg_initial; // here we consider the evolution of behavioural intention of green composting before the initial observation
    int timeBeforeInit_Î±sf_initial; // here we consider the evolution of behavioural intention of food sorting for dedicated collection before the initial observation (it's 2 for CAM)
    int timeBeforeInit_Î±sg_initial; // here we consider the evolution of behavioural intention of green sorting for dedicated collection before the initial observation

    double Kc_initial; // (parameter) initial capacity of home composting 
    double Ks_initial; // (parameter) initial dedicated collection capacity 
    double[] Kct; // (variable) linear evolution of home composter until planned capacity  
    double[] Kst; // (variable) linear evolution of dedicated collection until planned capacity 
    double Î±c_target; // (parameter) the planned maximum capacity of home composter 
    double Î±s_target; // (parameter) the planned maximum capacity of dedicated collection
    int yearRef; // year of departure for individual composting capacity non-limiting to departure

    double[] LinearHomeComposter; // linear function for planned capacity evolution of home composter 

    double[] sigmoide_mcf; // innovation diffusion function of food composting behavioural intention evolution 
    double[] sigmoide_mcg; // innovation diffusion function of green composting behavioural intention evolution 

    double[] LinearDedicatedCollection; // linear function for planned capacity evolution of dedicated collection

    double[] sigmoide_msf; // innovation diffusion function of evolution of food sorting for dedicated collection behavioural intention  
    double[] sigmoide_msg; // innovation diffusion function of evolution of green sorting for dedicated collection behavioural intention 

    double[] sigmoide_mpg; // innovation diffusion function of evolution of reduction behaviour of green waste
    double Î±cf_initial; // initial behavioural intention of food composting 
    double Î±cg_initial; // initial behavioural intention of green composting
    double Î±cf_max; // maximum evolution of food composting behavioural intention (here we consider it as one)
    double Î±cg_max; // maximum evolution of green composting behavioural intention (here we consider it as one)
    double Î±sf_initial; // initial behavioural intention of food sorting for dedicated collection
    double Î±sf_max; // maximum evolution of food sorting for dedicated collection behavioural intention (here we consider it as one)
    double Î±sg_initial; // initial behavioural intention of green sorting for dedicated collection
    double Î±sg_max; // maximum evolution of green sorting for dedicated collection behavioural intention (here we consider it as one)

    // TODO HYPOTHESIS: NO CAPACITY FOR RECYCLING CENTRE + ONLY ONE RECYCLING CENTRE PER TERRITORY
    double b_pf; // baseline food waste production per capita (which varies for each collection territory). per inhabitant/year in tonnes   
    double b_pg; // baseline green waste production per capita (which varies for each collection territory). per inhabitant/year in tonnes            

    double Î±v; // volume of green waste sent to the valorisation centre 

    double r; // the annual growth rate of the population for each collection territory
    int sizePop; // size of population for each collection territory

    double duraImplemCompo; // linear function for home composter capacity development 
    double mc; // inflexion point of the practical sigmoid curve of home composting
    double duraImplemCollect; // linear function for dedicated collection capacity development 
    double ms; // inflexion point of the practical sigmoid curve of sorting for dedicated collection
    double mpg; // inflexion point of the green waste reduction sigmoid curve
    double Î±pg_target; // the ABP's target reduction for green waste to be achieved by the target year

    double[] P; // the change in population size of each collection territory at year t
    double[] B; // Quantity of biowaste produced per households
    double[] Bpg; // The household's green waste production from each collection territory 
    double[] Bpf; // The household's food waste production from each collection territory 
    //double[] ABP; // Food waste to be removed as a result of reducing food waste
    double[] R; // rate of reduction of food waste at t as a function of objGaspi at term (term = 2xtiantigaspi of the sigmoid) AFTER THAT CONSTANT RATE
    double[] Î±cf; // the sorting of food waste for home composting intentions 
    double[] Î±cg; // the sorting of green waste for home composting intentions 
    double[] Î±vg; // the volume of green waste sent to the valorisation centre in each collection territory as consequences of other intentions
    double[] Î±sf; // the intentions of practical sorting for dedicated collection behaviour for food waste at time 
    double[] Î±sg; // the intentions of practical sorting for dedicated collection behaviour for green waste at time 
    double[] C_log; // From sigmoid for evolution of individual composting logistics
    double[] C_pop; // From sigmoid giving speed of individual evolution for composting practice
    double[] Bcg; // The biomass of home compostable green waste 
    double[] Bcf; // The biomass of home compostable food waste 
    double[] Bcf_composted; // the biomass of home composted food waste
    double[] Bcg_composted; // the biomass of home composted green waste
    double[] Bc_composted; // the biomass of home composted biowaste
    double[] Uc; // home composting-part surplus 
    double[] Ucg; // Quantity of green biowaste removed from local composting due to surplus
    double[] Ucf; // Quantity of food biowaste removed from local composting due to surplus
    double[] sLbis; // Intermediate management of local composting surpluses (adjusted composted)
    double[] Bv; // Green waste directed to the green valorisation centres 
    double[] Bsg; // sortable green waste for dedicated collection
    double[] Bsf; // sortable food waste for dedicated collection
    double[] Bs_sorted; // biomass of sorted green and food waste in dedicated collection 
    double[] Bsf_sorted; // biomass of sorted food waste in dedicated collection 
    double[] Bsg_sorted; // biomass of sorted green waste in dedicated collection 
    double[] Usf; // sorting-part food waste surplus 
    double[] Usg; // sorting-part green waste surplus 
    double[] sAa_bis; // Quantity of food waste removed from collection due to surplus
    double[] sAv_bis; // Quantity of green biowaste removed from collection due to surplus
    double[] Us; // Surplus from collection #1
    double[] sAbis; // Surplus from collection #2
    double[] Br; // food waste directed to the residual household waste 
    double Î±pf_target; // the ABP's target reduction for food waste to be achieved by the target

    int collectionTerritoryName;

    double[] propPopDesserviCollDA; // proportion of the population served by food waste collection in a given year
    double[] nbKgCollectHabDesservi; // number of kilograms of food waste collected per inhabitant served by the collection in a given year
    double[] nbKgOMRHab; // number of kilograms of food waste collected per inhabitant served by the collection in a given year
    // EquipmentValorisation myOwnEquip; // FOR NOW WE CONSIDER THAT SUBTERRITORIES DO NOT HAVE THEIR OWN EQUIPMENT OR THEIR CAPACITIES ARE SUMMED TO MAKE A COMMON EQUIPMENT
    double[] tauxReductionDechetVert; // rate of reduction of green waste entering the recycling centre
    int ident; // sub-territory number

    public CollectionTerritory(Territory mt, int id) {
        myTerre = mt;
        ident = id;
    }

    // public CollectionTerritory(Individual mt, int idd) {
    // myIndividual = mt;
    //  ident = idd;
    //  }

    /*
        public CollectionTerritory(int nbYears, int nbIndividuals, double[] paramsTerritory, double[][] paramsSubTerritories, boolean printTraj) {

        refYear = 0; //
        printTrajectory = printTraj;
        myIndividual = new CollectionTerritory[nbIndividuals];
        int sizeData = nbYears + 1;
        for (int i = 0; i < nbIndividuals; i++) {
            myIndividual[i] = new CollectionTerritory(this, i);
            myIndividual[i].init(sizeData, paramsSubTerritories[i], refYear);
        }
        // initialisation of 
        myCommonEquip = new LargeScaleInfrastructure();
        init(sizeData, paramsTerritory);
        // iterate over the time
        for (int i = 1; i <= nbYears; i++) {
            computenbIndividuals(i);
            // calculation of the sum of fluxes from territories contributing to equipment
            computeFluxesForCommonEquipment(i);
            // calculation of common valorisation or incinerator
            myCommonEquip.iterate(i, totCollecteVert, totCollecteFood, totDechetterie, totOMR);
            computeTotalFluxSubTerritories(i);
            // Bug control
            checkConservationFlux(i);
        }

    }
     */
    // TODO I HAVE MADE THE HYPOTHESIS OF ONE RECYCLING CENTRE PER TERRITORY // calling the function
    public void iterate(int year) {
        LinearHomeComposter[year] = linear(year, duraImplemCompo);
        LinearDedicatedCollection[year] = linear(year, duraImplemCollect); // logistic sorting capacity: duration = 7 
        if (myTerre.useSocialDynamics) {
            // sigmoideLogCompostLocal[year] = sigmoide(year, duraImplemCompo); // logistic composting capacity
            // logistic composting capacity: duration = 7

            sigmoide_mcf[year] = sigmoide(year + timeBeforeInit_Î±cf_initial, mc); // Practical composting behaviour: should I add +timeBeforeInit_Î±cf_initial to mc?

            sigmoide_mcg[year] = sigmoide(year + timeBeforeInit_Î±cg_initial, mc); // Practical composting behaviour: Should I add timeBeforeInit_Î±cg_initial to mc?

            // sigmoideLogCollecte[year] = sigmoide(year, duraImplemCollect); // Logistic collection capacity
            sigmoide_msf[year] = sigmoide(year + timeBeforeInit_Î±sf_initial, ms); // Practical sorting behaviour
            sigmoide_msg[year] = sigmoide(year + timeBeforeInit_Î±sg_initial, ms); // Practical sorting behaviour
            // System.err.println(year+" "+ms+" timebefore "+timeBeforeInit_Î±sg_initial+" sig "+sigmoide(year + timeBeforeInit_Î±sg_initial, ms));
            sigmoide_mpg[year] = sigmoide(year, mpg); // reduction green waste adoption 
        }
        for (int i = 0; i < sizePop; i++) {
            myIndividuals[i].computeWaste(year, Î±pf_target, myTerre.sigmoideABP);
            myIndividuals[i].computeBehavioralIntentions(year, sigmoide_mcf, sigmoide_mcg, sigmoide_msf, sigmoide_msg);
            myIndividuals[i].compost(year); // C composter qty
            myIndividuals[i].sortingFoodAndGreenWaste(year); // S = green and food bin qty
            myIndividuals[i].putInValCenter(year); // DV
            myIndividuals[i].putInBlackBin(year); // OMR
        }

        //computeProducedBioWaste(year);
        // Step 1: Distribution of biowaste
        //computeFluxRates(year); // alpha calcule les intentions 
        //localCompost(year); // C
        //collect(year); // S
        //recyclingCentre(year); // DV
        //residualHouseholdWaste(year); // OMR
        // myOwnEquip.iterate(year, this); // FOR NOW WE CONSIDER THAT SUBTERRITORIES DO NOT HAVE THEIR OWN EQUIPMENT OR THEIR CAPACITIES ARE SUMMED TO MAKE A COMMON EQUIPMENT
        computeCollectionTerritoryIndicator(year);
    }

    // computation of the intention to act (realised if the capacity is sufficient)
    // TODO Supress
    
    public void computeFluxRates(int y) {// social behaviour

        // TODO CHECK THIS CODE IS THE SAME THAN THE INDIVIDUAL ONE (then it should  be suppressed from the collectionTerritory
        double trucDa;
        double trucDv;
        Î±cf[y] = Math.min((Î±cf_initial + ((1 - Î±cf_initial) * sigmoide_mcf[y - 1])), 1.0);
        Î±cg[y] = Math.min((Î±cg_initial + ((1 - Î±cg_initial) * sigmoide_mcg[y - 1])), 1.0); // Proportion of biowaste going to local composting taking into account actors   
        Î±sf[y] = Î±sf_initial + ((1 - Î±sf_initial) * sigmoide_msf[y]); // we prioritise the desire to compost and assume that people compost or participate in collection
        trucDa = Î±cf[y] + Î±sf[y];
        if (trucDa > 1.0) {
            Î±sf[y] = (1 - Î±cf[y]);
        }
        Î±sg[y] = Î±sg_initial + ((Î±sg_max - Î±sg_initial) * sigmoide_msg[y]);
        // System.err.println(y+" "+Î±sg_initial+" "+Î±sg[y]+" "+sigmoide_msg[y]);
        trucDv = Î±cg[y] + Î±sg[y];
        if (trucDv > 1.0) {
            Î±sg[y] = 1.0 - Î±cg[y];
        }
        Î±vg[y] = 1 - Î±cg[y] - Î±sg[y]; // concerns only green waste which goes to the recycling centre

        // System.err.println("Î±sf_initial: " + Î±sf_initial);
        // System.err.println("Î±sg_initial: " + Î±sg_initial);
        // System.err.println("Î±sf_max: " + Î±sf_max);
        // System.err.println("Î±sg_max: " + Î±sg_max);
    }

    /**
     * Step 1: Distribution of biowaste
     */
    // TODO Supress
    public void localCompost(int y) {
        Bcg[y] = Î±cg[y] * Bpg[y]; // Quantity of green waste going towards local composting
        Bcf[y] = Î±cf[y] * Bpf[y]; // Quantity of food waste going towards local composting
        // â†’ HYPOTHESIS: If L[y] > K1: We have a surplus, then we will: First put green biowaste Bcg[y] in the recycling centre then if Bcg[y] is empty and there is still a surplus and L[y] is still greater than K1 then we put food biowaste Bcf[y] in the collection.
        if (y == yearRef) { // Calibration for the SBA case
            Kc_initial = Bcg[y] + Bcf[y];
        }
        Kct[y] = Kc_initial + ((Î±c_target - Kc_initial) * LinearHomeComposter[y]); // here sigmoid becomes linear see iteration
        // System.err.println("sigmoid " + LinearHomeComposter[y] + " Kct " + Kct + " Bcg " + Bcg[y] + " Bcf " + Bcf[y]+" Py "+P[y-1]+" "+Î±cg[y]+" Bpg "+Bpg[y]+" Î±cg "+Î±cg[y]+" Î±vg "+Î±vg[y]);
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

    // TODO Supress
    public void collect(int y) {
        Bsg[y] = Î±sg[y] * Bpg[y]; // Quantity of green waste going towards collection
        Bsf[y] = (Î±sf[y] * Bpf[y]) + Ucf[y]; // â†’ Quantity of food biowaste going towards collection
        // if (y == 1) {
        // System.err.println(Î±sg[y] + " ka " + Ks_initial + " Bsg " + Bsg[y] + " Bsf " + Bsf[y] + " a3dv " + Î±vg[y] + " a1dv " + Î±cg[y]);
        // }
        // â†’ HYPOTHESIS: if A[y] > KA: We have a surplus, then we will: First put green biowaste Bsg[y] in the recycling centre then if Bsg[y] is empty and there is still a surplus and A[y] is still greater than KA then we put food biowaste Bsf[y] in the household residual waste.
        Kst[y] = Ks_initial + ((Î±s_target - Ks_initial) * LinearDedicatedCollection[y]);
        // if (ident==1) System.err.println("year "+y+" ident terr "+ident+" Kacourant "+Kst);
        if ((Bsg[y] + Bsf[y]) > Kst[y]) {
            Us[y] = Bsf[y] + Bsg[y] - Kst[y]; // â†’ First calculation of surplus
            Bsg_sorted[y] = Math.max(Bsg[y] - Us[y], 0.0); // Quantity of green waste after applying the surplus
            sAbis[y] = Math.max(0.0, (Bsf[y] + Bsg_sorted[y] - Kst[y])); // Second calculation of surplus to see if there is still surplus after removing green biowaste
            // Av_bis[y] = Math.max(Bsg[y] - Us[y], 0.0); // Quantity of green biowaste after applying the surplus
            Bsf_sorted[y] = Math.max(Bsf[y] - sAbis[y], 0.0); // â†’ Quantity of food biowaste after applying the surplus
            Usg[y] = Math.min(Us[y], Bsg[y]); // Quantity of green biowaste removed due to surplus GOES TO THE RECYCLING CENTRE!!!!
            // if (Usg[y]<0.0) { System.err.println(" jfjqkdksdj "+Us[y]+" "+Bsg[y]); }
            // Dv[y]=Bv[y]+Usg[y]; // putting surplus back to the recycling centre
            Usf[y] = Math.min(sAbis[y], Bsf[y]); // â†’ Quantity of food biowaste removed due to surplus
            Bsg[y] = Bsg_sorted[y];
            Bsf[y] = Bsf_sorted[y];
        }
        Bs_sorted[y] = Bsg[y] + Bsf[y]; // â†’ Value of A[y] after removing the surplus
    }

    // TODO Supress
    public void recyclingCentre(int y) {
        Bv[y] = Î±vg[y] * Bpg[y] + Ucg[y] + Usg[y]; // Quantity of green biowaste going towards the recycling centre
        // System.err.println(Bv[y]+" Î±vg "+Î±vg[y]+" Ucg "+Ucg[y]+" Bpg "+Bpg[y]+" Usg "+Usg[y]);
        // System.err.println(Bv[y]+" "+Î±vg[y]+" "+Bpg[y]+" "+Ucg[y]+" "+Usg[y]);
        // if(Bv[y]<0.0) System.err.println(Î±vg[y]+" "+Bpg[y]+" "+Ucg[y]+" "+Usg[y]); //System.err.println(Ucg[y]+" "+Usg[y]+);
    }

    // TODO Supress
    public void residualHouseholdWaste(int y) {
        Br[y] = (1 - Î±cf[y] - Î±sf[y]) * Bpf[y] + Usf[y]; // Quantity of food biowaste going towards residual household waste
        if (Br[y] < 0) {
            System.err.println(Î±sf[y] + " alpha1 " + Î±cf[y] + " Ba " + Bpf[y] + " sAa " + Usf[y]);
        }
    }

    public double sigmoide(double x, double ti) {
        double t = Math.pow(x, 5);
        double z = t / (t + Math.pow(ti, 5)); // ti is the inflexion point of the sigmoid (the value 0.5 is returned in the ti-th year)
        return z;
    }

    public double linear(double t, double duration) {
        return Math.min(t / duration, 1.0);
    }

    public int calculateTimeBeforeInit(double alpha_base, double ti) { // time process
        int timeBeforeInit = 0;
        // Continuously calculate the sigmoid value at increasing time steps until it meets or exceeds alpha_base.
        if (alpha_base > 0) {

            double sigmoideValue = sigmoide(timeBeforeInit, ti);
            while (sigmoideValue < alpha_base) { // (the while-loop will not be entered if alpha_base is less than or equal to zero).
                timeBeforeInit++;
                sigmoideValue = sigmoide(timeBeforeInit, ti);
                // System.err.println(alpha_base + " " + ti + " " + timeBeforeInit + " " + sigmoideValue);
            }
        }
        return timeBeforeInit;
    }

    public void init(int sizeData, double[] params, int refYear) {
        yearRef = refYear;
        collectionTerritoryName = (int) params[0]; // numerical identifier of the sub-territory
        duraImplemCompo = params[1]; // inflexion point of the sigmoid curve
        duraImplemCollect = params[2]; // inflexion point of the sigmoid curve
        mc = params[3]; // inflexion point of the sigmoid curve
        ms = params[4]; // inflexion point of the sigmoid curve
        b_pf = params[5]; // Quantity of biowaste produced per inhabitant
        b_pg = params[6]; // proportion of green waste in b
        Î±cf_initial = params[7];
        Î±cg_initial = params[8];
        Î±sf_initial = params[9]; // practice of sorting for door-to-door collection init (from 70% to 95%)
        Î±sf_max = params[10]; // practice of sorting for door-to-door collection target (from 70% to 95%)
        Î±cf_max = params[11]; // desire to increase local composting practice for food waste
        Î±cg_max = params[12]; // same as above but for green waste
        Î±sg_initial = params[13]; // initial sorting of green waste
        Î±sg_max = params[14];
        Kc_initial = params[15]; // annual population growth according to national statistics
        Î±c_target = params[16]; // K(expected)^c
        Ks_initial = params[17]; // annual population growth according to national statistics
        Î±s_target = params[18];
        sizePop = (int) params[19]; // population size of sub-territory
        r = params[20]; // population size of sub-territory
        mpg = params[21]; // tiActionsAvoidanceGreenWaste
        Î±pg_target = params[22]; // rateAvoidanceGreenWasteHorizon2024        
        Î±pf_target = params[23];
        // Calculating time before the simulation starts for each alpha value (calculating time process)
        timeBeforeInit_Î±cf_initial = calculateTimeBeforeInit(Î±cf_initial, mc);
        timeBeforeInit_Î±cg_initial = calculateTimeBeforeInit(Î±cg_initial, mc);
        timeBeforeInit_Î±sf_initial = calculateTimeBeforeInit(Î±sf_initial, ms);
        timeBeforeInit_Î±sg_initial = calculateTimeBeforeInit(Î±sg_initial, ms);
        // System.err.println("ms "+ms);

        P = new double[sizeData]; // sizeData = number of years of simulation + 1 (for the initial state)
        Arrays.fill(P, 0.0);
        P[0] = sizePop;
        R = new double[sizeData];
        Arrays.fill(R, 0.0);
        //ABP = new double[sizeData];
        //Arrays.fill(ABP, 0.0);
        B = new double[sizeData];
        Arrays.fill(B, 0.0);
        Bpg = new double[sizeData];
        Arrays.fill(Bpg, 0.0);
        Bpf = new double[sizeData];
        Arrays.fill(Bpf, 0.0);
        Î±cf = new double[sizeData];
        Arrays.fill(Î±cf, 0.0);
        Î±cg = new double[sizeData];
        Arrays.fill(Î±cg, 0.0);
        Î±vg = new double[sizeData];
        Arrays.fill(Î±vg, 0.0);
        C_log = new double[sizeData];
        Arrays.fill(C_log, 0.0);
        C_pop = new double[sizeData];
        Arrays.fill(C_pop, 0.0);
        Bc_composted = new double[sizeData];
        Arrays.fill(Bc_composted, 0.0);
        Bcg = new double[sizeData];
        Arrays.fill(Bcg, 0.0);
        Bcf = new double[sizeData];
        Arrays.fill(Bcf, 0.0);
        Uc = new double[sizeData];
        Arrays.fill(Uc, 0.0);
        Ucf = new double[sizeData];
        Arrays.fill(Ucf, 0.0);
        Ucg = new double[sizeData];
        Arrays.fill(Ucg, 0.0);
        Bcg_composted = new double[sizeData];
        Arrays.fill(Bcg_composted, 0.0);
        Bcf_composted = new double[sizeData];
        Arrays.fill(Bcf_composted, 0.0);
        sLbis = new double[sizeData];
        Arrays.fill(sLbis, 0.0);
        Bv = new double[sizeData];
        Arrays.fill(Bv, 0.0);
        Usg = new double[sizeData];
        Arrays.fill(Usg, 0.0);
        Br = new double[sizeData];
        Arrays.fill(Br, 0.0);
        Kst = new double[sizeData];
        Arrays.fill(Kst, 0.0);
        Kct = new double[sizeData];
        Arrays.fill(Kct, 0.0); // to stock the result 
        // Fv_bis = new double[sizeData];
        // Arrays.fill(Fv_bis, 0.0);

        LinearHomeComposter = new double[sizeData];
        Arrays.fill(LinearHomeComposter, 0.0);
        sigmoide_mcf = new double[sizeData];
        Arrays.fill(sigmoide_mcf, 0.0);
        sigmoide_mcg = new double[sizeData];
        Arrays.fill(sigmoide_mcg, 0.0);
        LinearDedicatedCollection = new double[sizeData];
        Arrays.fill(LinearDedicatedCollection, 0.0);
        sigmoide_msf = new double[sizeData];
        Arrays.fill(sigmoide_msf, 0.0);
        sigmoide_msg = new double[sizeData];
        Arrays.fill(sigmoide_msg, 0.0);
        sigmoide_mpg = new double[sizeData];
        Arrays.fill(sigmoide_mpg, 0.0);
        Bsg = new double[sizeData];
        Arrays.fill(Bsg, 0.0);
        Bsf = new double[sizeData];
        Arrays.fill(Bsf, 0.0);
        Bsf_sorted = new double[sizeData];
        Arrays.fill(Bsf_sorted, 0.0);
        Bsg_sorted = new double[sizeData];
        Arrays.fill(Bsg_sorted, 0.0);
        Bs_sorted = new double[sizeData];
        Arrays.fill(Bs_sorted, 0.0);
        Us = new double[sizeData];
        Arrays.fill(Us, 0.0);
        sAbis = new double[sizeData];
        Arrays.fill(sAbis, 0.0);
        Usf = new double[sizeData];
        Arrays.fill(Usf, 0.0);

        propPopDesserviCollDA = new double[sizeData];
        Arrays.fill(propPopDesserviCollDA, 0.0);
        nbKgCollectHabDesservi = new double[sizeData];
        Arrays.fill(nbKgCollectHabDesservi, 0.0);
        nbKgOMRHab = new double[sizeData];
        Arrays.fill(nbKgOMRHab, 0.0);
        tauxReductionDechetVert = new double[sizeData];
        Arrays.fill(tauxReductionDechetVert, 0.0);
        Î±sg = new double[sizeData];
        Arrays.fill(Î±sg, 0.0);
        Î±sf = new double[sizeData];
        Arrays.fill(Î±sf, 0.0);
        for (int i = 0; i < sizePop; i++) {
            myIndividuals[i] = new Individual(sizeData, b_pf, b_pg, Î±cf_initial, Î±cg_initial, Î±sf_initial, Î±sg_initial);
        }
        computeCollectionTerritoryIndicator(0);
    }

    public void printVector(double[] edit) {
        for (int i = 0; i < edit.length; i++) {
            System.err.print(edit[i] + "\t");
        }
        System.err.println();
    }

    public void indicSubTerritories(int year) {
        //System.err.print("year "+year+" "+" Kst "+Kst+" KA "+KA+" nb hab desservi ") ;
        double nbHabDesservi = Math.min(P[year], (double) Kst[year] / (39.0 / 1000.0)); // 39 kg [converti en tonnes] correspond Ã  quantitÃ© article 5 arrÃªtÃ© du 7 juillet 2021 (base calcul pour qtÃ© dÃ©tournÃ©e par habitant desservi par la collecte de dechets alimentaires)  
        //System.err.print(nbHabDesservi) ;
        propPopDesserviCollDA[year] = nbHabDesservi / P[year];
        if (nbHabDesservi > 0) {
            nbKgCollectHabDesservi[year] = (Bsf[year] * 1000.0) / nbHabDesservi;
        }
        nbKgOMRHab[year] = (Br[year] * 1000.0) / P[year];
        tauxReductionDechetVert[year] = (Bv[year] - Bv[0]) / Bv[0]; // this evolution perecentage of green waste in dechetre, negative value means reduction

    }

    public void printTrajectory(int year) {
        System.out.print(ident + ";");
        System.out.print(P[year] + ";");
        System.out.print(Bpf[year] + ";");
        System.out.print(Bpg[year] + ";");
        System.out.print(Î±cf[year] + ";");
        System.out.print(Î±cg[year] + ";");
        System.out.print(Bcf[year] + ";");
        System.out.print(Ucf[year] + ";");
        //System.err.println("je viens d'Ã©crire SLA") ;
        System.out.print(Bcg[year] + ";");
        System.out.print(Ucg[year] + ";");
        System.out.print(Kct[year] + ";");
        System.out.print(Î±sf[year] + ";");
        System.out.print(Î±sg[year] + ";");
        System.out.print(Bsf[year] + ";");
        System.out.print(Usf[year] + ";");
        System.out.print(Bsg[year] + ";");
        System.out.print(Usg[year] + ";");
        System.out.print(Kst[year] + ";");
        System.out.print(Br[year] + ";");
        System.out.print(Î±vg[year] + ";");
        System.out.print(Bv[year] + ";");
    }

    public void computeCollectionTerritoryIndicator(int time) {
        for (int i = 0; i < sizePop; i++) {
            Bpf[time] = Bpf[time] + myIndividuals[i].Bpf[time];
            /* TODO
            Bpg[time] = b_pg * P[0];
            Bcg[time] = Bpg[0] * Î±cg_initial;
            Bcf[time] = Bpf[0] * Î±cf_initial;
            Bsf[time] = Bpf[0] * Î±sf_initial;
            Bsg[time] = Bpg[0] * Î±sg_initial;
            Bv[time] = Bpg[0] - Bcg[0] - Bsg[0];
            Br[time] = Bpf[0] - Bcf[0] - Bsf[0];
B[time] = somme des biowastes ;
             */
        }
    }
}
