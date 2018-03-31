package edu.ncsu.csc216.biosimulation.domain;

/**
 * Abstract class that contain the common state and behavior for all species.
 *
 * @author Collin
 *
 */
public abstract class Species implements Organism {
	/** initial species population */
	private double count;
	/** initial death rate of a species */
	private final double deathRate;
	/** initial birth rate of a species */
	private final double birthRate;

	/**
	 * Constructor for a species object
	 *
	 * @param count
	 *            the initial population
	 * @param birthRate
	 *            the initial birthRate
	 * @param deathRate
	 *            the initial deathRate
	 *
	 */
	public Species(double count, double birthRate, double deathRate) {
		this.count = count;
		this.deathRate = deathRate;
		this.birthRate = birthRate;
		putRatesInRange(deathRate);
		putRatesInRange(birthRate);

	}

	/**
	 * Accessor for birthRate
	 *
	 * @return the birthRate
	 */
	@Override
	public double getBirthRate() {
		return birthRate;
	}

	/**
	 * Accessor for count
	 *
	 * @return the count
	 */
	@Override
	public double getCount() {
		return count;
	}

	/**
	 * Accessor for deathRate
	 *
	 * @return the deathRate
	 */
	@Override
	public double getDeathRate() {
		return deathRate;
	}

	/**
	 * Accessor for a predator
	 *
	 * @return the predator
	 */
	protected abstract Species getPredator();

	/**
	 * Accessor for a prey
	 *
	 * @return the prey
	 */
	protected abstract Species getPrey();

	/**
	 * Ensures that any rate being passed into the constructor is a proportion
	 * i.e. greater than or equal to 0 or less than or equal to 1.0. If a value
	 * is passed outside of these ranges an Illegal Argument Exception is
	 * thrown.
	 *
	 * @param rate
	 *            to be assessed for validity
	 */
	public void putRatesInRange(double rate) {
		final double n = rate;
		if (n < 0.0 || n > 1.0) {
			throw new IllegalArgumentException("Birth/death rates must be between 0 and 1.");
		}
	}

	/**
	 * Specifies a Predator.
	 *
	 * @param s
	 *            the Predator species to register
	 */
	public abstract void registerPredator(Species s);

	/**
	 * Specifies a Prey.
	 *
	 * @param s
	 *            the Prey species to register.
	 */
	public abstract void registerPrey(Species s);

	/**
	 * Mutator for count
	 *
	 * @param count
	 *            the count to set
	 */
	@Override
	public void setCount(double count) {
		this.count = count;
	}

}
