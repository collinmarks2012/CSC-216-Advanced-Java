package edu.ncsu.csc216.biosimulation.domain;

/**
 * Manages all attriutes and procedures for a Scavenger in a
 * Prey/Predator/Scavenger Population Simulation.
 *
 * Implements Species, Scavenger's abstract parent class
 *
 * @author Collin Marks
 *
 */
public class Scavenger extends Species {
	/** Constant representing the minimum count of Scavengers */
	public static final int MAX_SCAVENGER = 500;
	/** Constant representing the maximum count of Scavengers */
	public static final int MIN_SCAVENGER = 4;
	/**
	 * Initial values for birth rate from kills, death rate, birth rate from
	 * Prey, and birth rate from Predator.
	 */
	public static final double[] DEFAULTS = { .000002, .1, .0006, .0003 };
	/** Field for limiting factor of a Scavenger */
	private static final double LIMITING_FACTOR = .001;

	/**
	 * Getter to return default birth rate from kills, death rate, birth rate
	 * contributed from Prey, and birth rate contributed from Predator.
	 *
	 * @return double array of rates
	 */
	public static double[] getDefaultParameters() {
		return DEFAULTS;
	}

	/** Predator object needed to compute the population of Scavenger */
	private Species predatorCarcass;

	/** Prey object needed to compute the population of Scavenger */
	private Species preyCarcass;

	/** Field for birth rate from prey */
	private final double preyCarcassBirthRate;

	/** Field for birth rate from predator */
	private final double predatorCarcassBirthRate;

	/**
	 * Constructor for a Scavenger object with a count, birth rate from kills,
	 * death rate, birth rate from Prey, and birth rate from Predator's.
	 *
	 * @param count
	 *            the number of Scavengers
	 * @param deathRate
	 *            death rate of Scavengers
	 * @param birthKill
	 *            population growth rate from scavenging corpses of Prey that
	 *            die from predation
	 * @param birthPrey
	 *            population growth rate from scavenging corpses of Prey that
	 *            die naturally
	 * @param birthPredator
	 *            population growth rate from scavenging corpses of Predators
	 */
	public Scavenger(int count, double birthKill, double deathRate, double birthPrey, double birthPredator) {
		super(count, birthKill, deathRate);
		predatorCarcassBirthRate = birthPredator;
		preyCarcassBirthRate = birthPrey;
		setCount(putCountInRange(count));
		putRatesInRange(birthPredator);
		putRatesInRange(birthPrey);
	}

	/**
	 * Getter for Predator object in Scavenger.
	 *
	 * Implemented from the Organism interface.
	 *
	 * @return the current Predator
	 */
	@Override
	protected Species getPredator() {
		return predatorCarcass;
	}

	/**
	 * Getter for Prey object in Scavenger.
	 *
	 * Implemented from the Organism interface.
	 *
	 * @return the current Prey
	 */
	@Override
	protected Species getPrey() {
		return preyCarcass;
	}

	/**
	 * Calculates the projected population of Scavenger's based on its current
	 * count, birth rate from kills, death rate, birth rate from Prey, birth
	 * rate from Predator's, the Predator's count, and the Prey's count. Based
	 * off the formula: dz/dt = -ez + fxyz + gxz + hyz - izz, where dz/dt is the
	 * change in population of Scavenger's with respect to time, e is the
	 * Scavenger death rate, z is the current Scavenger population, f is the
	 * Scavenger birth rate from kills, x is the current population of Prey, y
	 * is the current population of Predator's, g is the Scavenger birth rate
	 * from Prey, h is the Scavenger birth rate from Predator's, and i is the
	 * Scavenger population limiting factor.
	 */
	@Override
	public double getProjectedPopulation() {
		registerPrey(preyCarcass);
		registerPredator(predatorCarcass);
		double tot = 0;
		final double x = preyCarcass.getCount();
		final double y = predatorCarcass.getCount();
		final double z = getCount();
		final double e = getDeathRate();
		final double f = getBirthRate();
		final double g = preyCarcassBirthRate;
		final double h = predatorCarcassBirthRate;
		final double i = LIMITING_FACTOR;
		final double fxyz = f * x * y * z;
		final double gxz = g * x * z;
		final double hyz = h * y * z;
		final double ez = e * z;
		final double izz = i * z * z;
		tot = z + fxyz + gxz + hyz - ez - izz;
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
		if (n >= MIN_SCAVENGER && n <= MAX_SCAVENGER) {
			return n;
		}
		if (n > MAX_SCAVENGER) {
			return MAX_SCAVENGER;
		}
		if (n < MIN_SCAVENGER) {
			return MIN_SCAVENGER;
		}
		return 0.0;

	}

	/**
	 * Registers the current Predator into Scavenger to provide information to
	 * compute the Scavenger's population.
	 *
	 * Implements abstract method form the Scavenger's abstract parent class,
	 * Species.
	 */
	@Override
	public void registerPredator(Species s) {
		predatorCarcass = s;
	}

	/**
	 * Registers the current Prey into Scavenger to provide information to
	 * compute the Scavenger's population.
	 *
	 * Implements abstract method form the Scavenger's abstract parent class,
	 * Species.
	 */
	@Override
	public void registerPrey(Species s) {
		preyCarcass = s;
	}

}
