/**
 Student Name - Guancheng Rong
 Student Id - 1856981
 Student email - guancheng.rong@student.unimelb.edu.au
*/

import java.util.Random;

/**
 * Generates reproducible random values for warehouse generation.
 * <p>
 * This class encapsulates all randomness used in the warehouse so that
 * providing the same seed always produces the same warehouse layout.
 */
public class WarehouseGenerator {

    /**
     * Random number generator used for all random operations.
     * <p>
     * A single Random instance is used to ensure consistent, reproducible results.
     */
    private final Random random;

    /**
     * Creates a generator with a fixed seed.
     * <p>
     * Using a seed allows the warehouse generation to be deterministic:
     * the same seed will always produce the same sequence of random values.
     *
     * @param seed random seed
     */
    public WarehouseGenerator(long seed) {
        // Initialise Random with a seed for reproducible behaviour
        this.random = new Random(seed);
    }

    /**
     * Generates a random integer in the range [min, max).
     * <p>
     * The lower bound is inclusive and the upper bound is exclusive,
     * following Java's standard convention.
     *
     * @param min inclusive lower bound
     * @param max exclusive upper bound
     * @return generated random integer
     */
    public int generateInt(int min, int max) {
        // nextInt(max - min) produces a value in [0, max - min)
        // adding min shifts the range to [min, max)
        return min + random.nextInt(max - min);
    }

    /**
     * Selects a random item name from the predefined list of item names.
     *
     * @return randomly chosen item name
     */
    public String randomItemName() {
        // Generate a valid random index for the item name array
        int index = generateInt(0, Constants.DEFAULT_ITEM_NAMES.length);

        // Return the item name at the selected index
        return Constants.DEFAULT_ITEM_NAMES[index];
    }
}

