/**
 * The WasteManager class handles the management of waste processing equipment and their capacities.
 * It tracks equipment capacities and provides functionality to update them over time.
 */
public class WasteManager {
    // Array to store the capacities of three different waste processing equipment
    private double[] equipmentCapacities;
    
    // Rate at which equipment capacity increases (as a decimal fraction)
    private double capacityIncreaseRate;
    
    /**
     * Constructor initializes the WasteManager with starting equipment capacities and an increase rate.
     * 
     * @param initialCapacities Array containing the initial capacities for three equipment types
     * @param increaseRate The rate at which capacities will increase over time
     */
    public WasteManager(double[] initialCapacities, double increaseRate) {
        // Initialize the equipment capacities array with space for three equipment types
        equipmentCapacities = new double[3];
        
        // Copy the initial capacities from the input array to our class field
        System.arraycopy(initialCapacities, 0, equipmentCapacities, 0, 3);
        
        // Store the capacity increase rate for future calculations
        capacityIncreaseRate = increaseRate;
    }
    
    /**
     * Returns the current equipment capacities for a given year.
     * 
     * @param year The year for which to retrieve equipment capacities
     * @return The current array of equipment capacities
     */
    public double[] decideEquipmentCapacity(int year) {
        // Currently this method just returns the current capacities without using the year parameter
        return equipmentCapacities;
    }
    
    /**
     * Updates the capacities of all equipment based on the increase rate.
     * This simulates equipment capacity growth over time.
     * 
     * @param year The year for which to update capacities
     */
    public void updateCapacities(int year) {
        // Loop through each equipment type and increase its capacity
        for (int i = 0; i < equipmentCapacities.length; i++) {
            // Multiply current capacity by (1 + rate) to get the new capacity
            equipmentCapacities[i] *= (1 + capacityIncreaseRate);
        }
    }
} 
