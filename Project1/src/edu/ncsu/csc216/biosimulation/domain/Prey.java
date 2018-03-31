package edu.ncsu.csc216.biosimulation.domain;

/**
 * Manages all attributes and procedures for a Prey in a Prey/Predator/Scavenger
 * Simulation.
 *
 * Implements abstract class Species
 *
 * @author Collin Marks
 *
 */
public class Prey extends Species {
	/** Constant representing the minimum count of prey */
	public static final int MIN_PREY = 0;
	/** Constant representing maximum count of prey */
	public static final int MAX_PREY = 500;
	/** Initial values for count, birth rate, and death rate respectively */
	private static final double[] DEFAULTS = { .165, .0006 };

	/**
	 * Getter to return default birth rate and death rate respectively.
	 *
	 * @return double array of rates
	 */
	public static double[] getDefaultParameters() {
		return DEFAULTS;
	}

	/** Predator object needed to compute the population of Prey */
	private Species killer;

	/**
	 * Constructor for a Prey object with a count, birth rate, and death rate.
	 * The Constructor will ensure that the count is greater than or equal to 0
	 * and less than or equal to 500. Additionally it will ensure both the birth
	 * rate and death rate is being passed in as a proportion i.e. greater than
	 * or equal to 0 and less than or equal to 1.0
	 *
	 *
	 * @param count
	 *            the number of prey
	 * @param birthRate
	 *            the birth rate of the Prey object
	 * @param deathRate
	 *            the death rate of the Prey object
	 */
	public Prey(int count, double birthRate, double deathRate) {
		super(count, birthRate, deathRate);
		setCount((int) putCountInRange(count));
	}

	/**
	 * Getter for the current Predator object in Prey. Implemented from the
	 * Organism interface
	 *
	 * @return the current Predator
	 */
	@Override
	protected Species getPredator() {
		return killer;
	}

	/**
	 * Getter for the curent Prey object in Prey.
	 *
	 * Implemented from the Organism interface.
	 *
	 * @return the current Prey object
	 */
	@Override
	protected Species getPrey() {
		return this;
	}

	/**
	 * Calculates the projected population of Prey based on its current count,
	 * birth rate, death rate, and the Predator's count. Based off the following
	 * formula: dx/dt = ax - bxy, where dx/dt is the change with respect to time
	 * in the population of Prey, a is the birth rate of the Predator, x is the
	 * current Predator count, b is the Prey death rate, and y is the Predator
	 * count.
	 *
	 * Implemented from the Organism interface.
	 *
	 * @return the projected population of the Prey.
	 */
	@Override
	public double getProjectedPopulation() {
		final double x = this.getCount();
		final double a = this.getBirthRate();
		final double ax = a * x;
		final double b = this.getDeathRate();
		final double y = killer.getCount();
		final double bxy = b * x * y;
		final double tot = x + ax - bxy;
		final double tota = tot + 1;
		return putCountInRange(tot);
	}

	/**
	 * Ensures that the count being passed into the constructor is greater than
	 * or equal to 0 and less than or equal to 500. If a value greater than 300
	 * is passed into count the count is defaulted to 500. If a value less than
	 * 0 is passed into count then the count is defaulted to 0.
	 *
	 * @param n
	 *            the count to evaluate
	 * @return the count in range
	 */
	private double putCountInRange(double n) {
		if (n >= MIN_PREY && n <= MAX_PREY) {
			return n;
		}
		if (n > MAX_PREY) {
			return MAX_PREY;
		}
		if (n < MIN_PREY) {
			return MIN_PREY;
		}
		// Case should never be reached since a values in the reals are covered
		// in the conditionals
		return 0.0;
	}

	/**
	 * Registers the current Predator into Prey to provide information to
	 * compute the Prey's population.
	 *
	 * Implements abstract method form the Prey's abstract parent class,
	 * Species.
	 */
	@Override
	public void registerPredator(Species s) {
		killer = s;
	}

	/**
	 * Registers the current Prey into itself i.e. it does nothing. Implemented
	 * from the abstract Species parent class.
	 */
	@Override
	public void registerPrey(Species s) {
		// Do nothing
	}

}
