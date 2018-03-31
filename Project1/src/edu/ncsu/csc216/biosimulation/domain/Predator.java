
package edu.ncsu.csc216.biosimulation.domain;

/**
 * Manages all attributes and procedures for a Predator in
 * Prey/Predator/Scavenger Simulation.
 *
 * Implements abstract class Species
 *
 * @author Collin Marks
 *
 */
public class Predator extends Species {
	/** Constant representing minimum count of predators */
	public static final double MIN_PREDATOR = 0;
	/** Constant representing maximum count of predators */
	public static final double MAX_PREDATOR = 300;
	/** Initial values for birth rate, and death rate respectively */
	private static final double[] DEFAULTS = { .00068, .23 };

	/**
	 * Getter to return default birth rate and death rate
	 *
	 * @return double array of rates
	 */
	public static double[] getDefaultParameters() {
		return DEFAULTS;
	}

	/** Prey object needed to compute the population of Predator */
	private Species food;

	/**
	 * Constructor for a Predator object with a count, birth rate, and death
	 * rate. The Constructor will ensure that the count is greater or equal to 0
	 * and less than or equal to 300. Additionally it will ensure both the birth
	 * rate and death rate is being passed in as a proportion i.e greater than
	 * or equal to 0 and less than or equal to 1.0
	 *
	 * @param count
	 *            the count of the Predator object
	 * @param birthRate
	 *            the birth rate of the Predator object
	 * @param deathRate
	 *            the death rate of the Predator object
	 */
	public Predator(int count, double birthRate, double deathRate) {
		super(count, birthRate, deathRate);
		setCount(putCountInRange(count));
	}

	/**
	 * Getter for the current Predator object in Predator.
	 *
	 * Implemented from the Organism interface.
	 *
	 * @return the current Predator
	 */
	@Override
	protected Species getPredator() {
		return this;
	}

	/**
	 * Getter for the current Prey object in Predator.
	 *
	 * Implemented from the Organism interface.
	 *
	 * @return the current Prey
	 */
	@Override
	protected Species getPrey() {
		return food;
	}

	/**
	 * Calculates the projected population of Predator based on its current
	 * count, birth rate, death rate, and the Preys count. Based off the
	 * following formula: dy/dt = -cy + pxy Where dy/dt is the change with
	 * respect to time in the population of predators, c is the death rate of
	 * the Predator, y is the current population of the Predator, p is the death
	 * rate of the Predator, and x is the current count of the Prey.
	 *
	 * Implemented from the Organism Interface.
	 *
	 * @return the projected population of the Predator.
	 */
	@Override
	public double getProjectedPopulation() {
		double tot = 0;
		final double c = getDeathRate();
		final double y = getCount();
		final double p = getBirthRate();
		final double x = getPrey().getCount();
		final double cy = c * y;
		final double pxy = p * x * y;
		tot = y + (-1 * cy) + pxy;
		return putCountInRange(tot);
	}

	/**
	 * Ensures that the count being passed into the constructor is greater than
	 * or equal to 0 and less than or equal to 300. If a value greater than 300
	 * is passed into count the count is defaulted to 300. If a value less than
	 * 0 is passed into count then the count is defaulted to 0.
	 *
	 * @param n
	 *            the count to evaluate
	 * @return the count in range
	 */
	private double putCountInRange(double n) {
		if (n >= MIN_PREDATOR && n <= MAX_PREDATOR) {
			return n;
		}
		if (n > MAX_PREDATOR) {
			return MAX_PREDATOR;
		}
		if (n < MIN_PREDATOR) {
			return MIN_PREDATOR;
		}
		// Will never happen since all real numbers are covered in conditionals
		return 0.0;
	}

	/**
	 * Registers the current Predator into itself. i.e. it does nothing. Must be
	 * implemented from the abstract Species parent class
	 */
	@Override
	public void registerPredator(Species s) {
		// Do nothing
	}

	/**
	 * Registers the Prey object into Predator to provide information nessesary
	 * to compute the Predator's population
	 *
	 * Implements abstract method from Predator's abstract parent class, Species
	 */
	@Override
	public void registerPrey(Species s) {
		food = s;
	}

}
