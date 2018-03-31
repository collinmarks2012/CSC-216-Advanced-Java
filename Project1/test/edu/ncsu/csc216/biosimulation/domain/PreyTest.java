/**
 *
 */
package edu.ncsu.csc216.biosimulation.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests behavior and methods of Prey
 * 
 * @author Collin
 *
 */
public class PreyTest {
	private static final double VALID_DEATHRATE_PREY = .0006;
	private static final double VALID_BIRTHRATE_PREY = .165;
	private static final int VALID_COUNT_PREY = 250;
	private static final double VALID_DEATHRATE_PREDATOR = .23;
	private static final double VALID_BIRTHRATE_PREDATOR = .00068;
	private static final int VALID_COUNT_PREDATOR = 10;
	/** Acceptable margin of error for round off error */
	private static final double DELTA = Math.pow(10, -8);
	private Predator wolf;
	private Prey elk;

	/**
	 * Sets up Prey object for testing
	 * 
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		elk = new Prey(VALID_COUNT_PREY, VALID_BIRTHRATE_PREY, VALID_DEATHRATE_PREY);
	}

	/**
	 * Test method for
	 * {@link edu.ncsu.csc216.biosimulation.domain.Predator#getDeafaultParameters()}
	 * .
	 *
	 */
	@Test
	public void testGetDefaultParameters() {
		assertEquals(Prey.getDefaultParameters()[0], VALID_BIRTHRATE_PREY, DELTA);
		assertEquals(Prey.getDefaultParameters()[1], VALID_DEATHRATE_PREY, DELTA);
	}

	/**
	 * Test method for
	 * {@link edu.ncsu.csc216.biosimulation.domain.Predator#getPredator()}.
	 */
	@Test
	public void testGetPredator() {
		elk.registerPredator(wolf);
		assertEquals(elk.getPredator(), wolf);
	}

	/**
	 * Test method for
	 * {@link edu.ncsu.csc216.biosimulation.domain.Predator#getPrey()}.
	 */
	@Test
	public void testGetPrey() {
		assertEquals(elk.getPrey(), elk);
	}

	/**
	 * Test method for
	 * {@link edu.ncsu.csc216.biosimulation.domain.Predator#getProjectedPopulation()}
	 * .
	 */
	@Test
	public void testGetProjectedPopulation() {
		wolf = new Predator(VALID_COUNT_PREDATOR, VALID_BIRTHRATE_PREDATOR, VALID_DEATHRATE_PREDATOR);
		elk.registerPredator(wolf);
		assertEquals(elk.getPrey().getBirthRate(), VALID_BIRTHRATE_PREY, DELTA);
		assertEquals(elk.getPrey().getCount(), VALID_COUNT_PREY, DELTA);
		assertEquals(elk.getPrey().getDeathRate(), VALID_DEATHRATE_PREY, DELTA);
		assertEquals(elk.getPredator().getCount(), VALID_COUNT_PREDATOR, DELTA);
		assertEquals(289.75, elk.getProjectedPopulation(), DELTA);
	}

	private boolean testNegativeBirthRatePreyConstructor(boolean thrown) {
		try {
			new Prey(10, -.23, .23);
		} catch (final IllegalArgumentException e) {
			thrown = true;
		}
		return thrown;
	}

	private boolean testNegativeDeathRatePreyConstructor(boolean thrown) {
		try {
			new Prey(10, .23, -.23);
		} catch (final IllegalArgumentException e) {
			thrown = true;
		}
		return thrown;
	}

	private boolean testOutOfRangeBirthRatePreyConstructor(boolean thrown) {
		try {
			new Prey(10, 3.0, .23);
		} catch (final IllegalArgumentException e) {
			thrown = true;
		}
		return thrown;
	}

	private boolean testOutOfRangeDeathRatePreyConstructor(boolean thrown) {
		try {
			new Prey(10, .23, 3.0);
		} catch (final IllegalArgumentException e) {
			thrown = true;
		}
		return thrown;
	}

	/**
	 * Test method for
	 * {@link edu.ncsu.csc216.biosimulation.domain.Predator#Predator(int, double, double)}
	 * .
	 */
	@Test
	public void testPrey() {
		Prey outOfRangeCount;
		Prey zeroCount;
		Prey zeroBirthRate;
		Prey zeroDeathRate;
		boolean thrown = false;
		outOfRangeCount = new Prey(Prey.MAX_PREY + 1, .002, .23);
		assertEquals(Prey.MAX_PREY, outOfRangeCount.getCount(), DELTA);
		zeroCount = new Prey(0, .002, .23);
		assertEquals(zeroCount.getCount(), 0, DELTA);
		assertTrue(testNegativeBirthRatePreyConstructor(thrown));
		thrown = false;
		zeroBirthRate = new Prey(10, 0, .23);
		assertEquals(zeroBirthRate.getBirthRate(), 0, DELTA);
		assertTrue(testOutOfRangeBirthRatePreyConstructor(thrown));
		thrown = false;
		assertTrue(testNegativeDeathRatePreyConstructor(thrown));
		thrown = false;
		zeroDeathRate = new Prey(10, .23, 0);
		assertEquals(zeroDeathRate.getDeathRate(), 0, DELTA);
		assertTrue(testOutOfRangeDeathRatePreyConstructor(thrown));
		thrown = false;
		assertEquals(elk.getCount(), VALID_COUNT_PREY, DELTA);
		assertEquals(elk.getBirthRate(), VALID_BIRTHRATE_PREY, DELTA);
		assertEquals(elk.getDeathRate(), VALID_DEATHRATE_PREY, DELTA);
	}

	/**
	 * Test method for
	 * {@link edu.ncsu.csc216.biosimulation.domain.Predator#registerPredator(edu.ncsu.csc216.biosimulation.domain.Species)}
	 * .
	 */
	@Test
	public void testRegisterPredator() {
		wolf = new Predator(VALID_COUNT_PREDATOR, VALID_BIRTHRATE_PREDATOR, VALID_DEATHRATE_PREDATOR);
		elk.registerPredator(wolf);
		assertEquals(elk.getPredator().getCount(), VALID_COUNT_PREDATOR, DELTA);
		assertEquals(elk.getPredator().getBirthRate(), VALID_BIRTHRATE_PREDATOR, DELTA);
		assertEquals(elk.getPredator().getDeathRate(), VALID_DEATHRATE_PREDATOR, DELTA);
	}

	/**
	 * Test method for
	 * {@link edu.ncsu.csc216.biosimulation.domain.Predator#registerPrey(edu.ncsu.csc216.biosimulation.domain.Species)}
	 * .
	 */
	@Test
	public void testRegisterPrey() {
		assertEquals(elk.getCount(), VALID_COUNT_PREY, DELTA);
		assertEquals(elk.getBirthRate(), VALID_BIRTHRATE_PREY, DELTA);
		assertEquals(elk.getDeathRate(), VALID_DEATHRATE_PREY, DELTA);
	}

}
