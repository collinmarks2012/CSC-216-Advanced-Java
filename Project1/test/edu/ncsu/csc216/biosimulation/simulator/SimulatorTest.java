package edu.ncsu.csc216.biosimulation.simulator;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import edu.ncsu.csc216.biosimulation.domain.Predator;
import edu.ncsu.csc216.biosimulation.domain.Prey;
import edu.ncsu.csc216.biosimulation.domain.Scavenger;

/**
 * Tests Simulator
 *
 * @author Collin Marks
 *
 */
public class SimulatorTest {
	private static final double DELTA = Math.pow(10, -8);
	private Simulator valids;
	final String[] validCounts = { Integer.toString(10), Integer.toString(250), Integer.toString(400) };
	final String[] predRates = { Double.toString(Predator.getDefaultParameters()[0]),
			Double.toString(Predator.getDefaultParameters()[1]) };
	final String[] preyRates = { Double.toString(Prey.getDefaultParameters()[0]),
			Double.toString(Prey.getDefaultParameters()[1]) };

	final String[] scavRates = { Double.toString(Scavenger.getDefaultParameters()[0]),
			Double.toString(Scavenger.getDefaultParameters()[1]), Double.toString(Scavenger.getDefaultParameters()[2]),
			Double.toString(Scavenger.getDefaultParameters()[3]) };
	final String[][] validRates = { predRates, preyRates, scavRates };
	final String[] invalidCounts1 = { Integer.toString(-1), Integer.toString(10), Integer.toString(10) };
	final String[] invalidCounts2 = { Double.toString(4.5), Integer.toString(10), Integer.toString(10) };
	final String[] invalidCounts3 = { Integer.toString(10), Integer.toString(10), "a number" };
	final String[] invalidPredRates = { Double.toString(1.23), Double.toString(Predator.getDefaultParameters()[1]) };
	final String[][] invalidRates1 = { invalidPredRates, preyRates, scavRates };
	final String[] invalidPreyRates = { Double.toString(Prey.getDefaultParameters()[0]), "1.A23" };
	final String[][] invalidRates2 = { predRates, invalidPreyRates, scavRates };

	/**
	 * Constructs a Simulator by passing in valid arrays into both its
	 * parameters.
	 *
	 * @throws Exception
	 *             if elements in the array are invalid
	 */
	@Before
	public void setUp() throws Exception {
		valids = new Simulator(validCounts, validRates);
	}

	/**
	 * Tests getDefaultInitialCounts method.
	 */
	@Test
	public void testGetDefaultInitialCounts() {
		assertEquals(10, Simulator.getDefaultInitialCounts()[0], DELTA);
		assertEquals(250, Simulator.getDefaultInitialCounts()[1], DELTA);
		assertEquals(400, Simulator.getDefaultInitialCounts()[2], DELTA);
	}

	/**
	 * Tests getDefaultParameters method.
	 */
	@Test
	public void testGetDefaultParameters() {
		assertEquals(.00068, Simulator.getDefaultParameters()[0][0], DELTA);
		assertEquals(.23, Simulator.getDefaultParameters()[0][1], DELTA);
		assertEquals(.165, Simulator.getDefaultParameters()[1][0], DELTA);
		assertEquals(.0006, Simulator.getDefaultParameters()[1][1], DELTA);
		assertEquals(.000002, Simulator.getDefaultParameters()[2][0], DELTA);
		assertEquals(.1, Simulator.getDefaultParameters()[2][1], DELTA);
		assertEquals(.0006, Simulator.getDefaultParameters()[2][2], DELTA);
		assertEquals(.0003, Simulator.getDefaultParameters()[2][3], DELTA);
	}

	/**
	 * Tests get Population method.
	 */
	@Test
	public void testGetPopulation() {
		valids.step();
		assertEquals(10, valids.getPopulations()[0]);
		assertEquals(290, valids.getPopulations()[1]);
		assertEquals(264, valids.getPopulations()[2]);
		// Test to ensure population is right after 600 steps
		for (int i = 1; i <= 600; i++) {
			valids.step();
		}
		assertEquals(245, valids.getPopulations()[0]);
		assertEquals(349, valids.getPopulations()[1]);
		assertEquals(338, valids.getPopulations()[2]);
	}

	/**
	 * Tests Simulator method
	 */
	@Test
	public void testSimulator() {
		// trial to pass -1 into count value
		try {
			valids = new Simulator(invalidCounts1, validRates);
			fail();
		} catch (final IllegalArgumentException e) {
			final int[] exp = { 10, 250, 400 };
			final int[] act = Simulator.getDefaultInitialCounts();
			assertArrayEquals(exp, act);
		}
		// trial to pass non-integer (a double) into count value
		try {
			valids = new Simulator(invalidCounts2, validRates);
			fail();
		} catch (final IllegalArgumentException e) {
			final int[] exp = { 10, 250, 400 };
			final int[] act = Simulator.getDefaultInitialCounts();
			assertArrayEquals(exp, act);
		}
		// trial to pass a String into count value
		try {
			valids = new Simulator(invalidCounts3, validRates);
			fail();
		} catch (final IllegalArgumentException e) {
			final int[] exp = { 10, 250, 400 };
			final int[] act = Simulator.getDefaultInitialCounts();
			assertArrayEquals(exp, act);
		}
		// trial to pass a number greater than 1.0 into rate value
		try {
			valids = new Simulator(validCounts, invalidRates1);
			fail();
		} catch (final IllegalArgumentException e) {
			final String[][] exp = { predRates, preyRates, scavRates };
			final String[][] act = validRates;
			assertArrayEquals(exp, act);
		}
		// trial to pass a string with a letter into rate value
		try {
			valids = new Simulator(validCounts, invalidRates2);
			fail();
		} catch (final IllegalArgumentException e) {
			// ensure no values were changed
			final String[][] exp = { predRates, preyRates, scavRates };
			final String[][] act = validRates;
			assertArrayEquals(exp, act);
		}
	}

	/**
	 * Tests step method.
	 */
	@Test
	public void testStep() {
		valids.step();
		assertEquals(10, valids.getPopulations()[0]);
		assertEquals(290, valids.getPopulations()[1]);
		assertEquals(264, valids.getPopulations()[2]);
	}

}
