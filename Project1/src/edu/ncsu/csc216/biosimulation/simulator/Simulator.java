/**
 *
 */
package edu.ncsu.csc216.biosimulation.simulator;

import edu.ncsu.csc216.biosimulation.domain.Predator;
import edu.ncsu.csc216.biosimulation.domain.Prey;
import edu.ncsu.csc216.biosimulation.domain.Scavenger;
import edu.ncsu.csc216.biosimulation.domain.Species;

/**
 * Simulator is the client that handles interactions between Prey, Predator, and
 * Scavenger, with the GUI.
 *
 * @author Collin Marks
 *
 */
public class Simulator {
	private static final int[] DEFAULT_INITIAL_COUNT = { 10, 250, 400 };
	private static final double[][] DEFAULT_PARAMETERS = { Predator.getDefaultParameters(), Prey.getDefaultParameters(),
			Scavenger.getDefaultParameters() };

	/**
	 * Getter for the default inital counts
	 *
	 * @return the inital counts as an int array
	 */
	public static int[] getDefaultInitialCounts() {
		return DEFAULT_INITIAL_COUNT;
	}

	/**
	 * Getter for default parameters containing all rates for Prey, Predator,
	 * and Scavenger.
	 *
	 * @return 2D jagged array of rate parameters
	 */
	public static double[][] getDefaultParameters() {
		return DEFAULT_PARAMETERS;
	}

	private Species victim;
	private Species scrounger;
	private Species killer;
	private final int predCount;
	private final int preyCount;
	private final int scavCount;
	private final double predBR;
	private final double predDR;
	private final double preyBR;
	private final double preyDR;
	private final double scavBRKills;
	private final double scavDR;

	private final double scavBRPred;

	private final double scavBRPrey;

	/**
	 * Constructor for Simulator to handle interactions between classes and GUI
	 *
	 * @param a
	 *            the 1D array of counts
	 * @param b
	 *            the 2D jagged array of rates
	 */
	public Simulator(String[] a, String[][] b) {
		try {
			predCount = Integer.parseInt(a[0]);
			preyCount = Integer.parseInt(a[1]);
			scavCount = Integer.parseInt(a[2]);
		} catch (NumberFormatException nfe) {
			throw new IllegalArgumentException("Initial population counts must be integers.", nfe);
		}
		try {
			predBR = Double.parseDouble(b[0][0]);
			predDR = Double.parseDouble(b[0][1]);
			preyBR = Double.parseDouble(b[1][0]);
			preyDR = Double.parseDouble(b[1][1]);
			scavBRKills = Double.parseDouble(b[2][0]);
			scavDR = Double.parseDouble(b[2][1]);
			scavBRPrey = Double.parseDouble(b[2][2]);
			scavBRPred = Double.parseDouble(b[2][3]);
		} catch (NumberFormatException nfe) {
			throw new IllegalArgumentException("Birth/death rates must be numbers.", nfe);
		}
		if (predCount < 0 || preyCount < 0 || scavCount < 0) {
			throw new IllegalArgumentException("Population counts cannot be negative.");
		}
		if (predBR < 0 || predDR < 0 || preyBR < 0 || preyDR < 0 || scavBRKills < 0 || scavDR < 0 || scavBRPrey < 0
				|| scavBRPred < 0 || predBR > 1 || predDR > 1 || preyBR > 1 || preyDR > 1 || scavBRKills > 1
				|| scavDR > 1 || scavBRPrey > 1 || scavBRPred > 1) {
			throw new IllegalArgumentException("Birth/death rates must be between 0 and 1.");
		}
		instantiateSpecies();
	}

	/**
	 * Getter for population of Prey, Predator, and Scavenger
	 *
	 * @return the 1D array of counts
	 */
	public int[] getPopulations() {
		final int[] a = new int[3];
		a[0] = (int) Math.ceil(killer.getCount());
		a[1] = (int) Math.ceil(victim.getCount());
		a[2] = (int) Math.ceil(scrounger.getCount());
		return a;
	}

	/**
	 * Instantiates Predator, Prey, and Scavenger to the necessary fields in
	 * their constructors.
	 */
	private void instantiateSpecies() {
		killer = new Predator(predCount, predBR, predDR);
		victim = new Prey(preyCount, preyBR, preyDR);
		scrounger = new Scavenger(scavCount, scavBRKills, scavDR, scavBRPrey, scavBRPred);
		killer.registerPrey(victim);
		victim.registerPredator(killer);
	}

	/**
	 * Handles "one-step" of time by calculating all projected populations of
	 * Predator, Prey, and Scavenger.
	 */
	public void step() {
		final double a = killer.getProjectedPopulation();
		victim.registerPredator(killer);
		final double b = victim.getProjectedPopulation();
		scrounger.registerPredator(killer);
		scrounger.registerPrey(victim);
		final double c = scrounger.getProjectedPopulation();
		killer.setCount(a);
		victim.setCount(b);
		scrounger.setCount(c);

	}
}
