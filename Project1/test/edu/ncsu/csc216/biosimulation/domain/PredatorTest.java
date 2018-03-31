/**
 *
 */
package edu.ncsu.csc216.biosimulation.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests methods and behavior of Predator
 *
 * @author Collin Marks
 *
 */
public class PredatorTest {
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
	 * Sets up Predator object for testing
	 * 
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {

		wolf = new Predator(VALID_COUNT_PREDATOR, VALID_BIRTHRATE_PREDATOR, VALID_DEATHRATE_PREDATOR);
	}

	/**
	 * Test method for
	 * {@link edu.ncsu.csc216.biosimulation.domain.Predator#getDeafaultParameters()}
	 * .
	 *
	 */
	@Test
	public void testGetDeafaultParameters() {
		assertEquals(Predator.getDefaultParameters()[0], .00068, DELTA);
		assertEquals(Predator.getDefaultParameters()[1], .23, DELTA);
	}

	/**
	 * Test method for
	 * {@link edu.ncsu.csc216.biosimulation.domain.Predator#getPredator()}.
	 */
	@Test
	public void testGetPredator() {
		assertEquals(wolf.getPredator(), wolf);
	}

	/**
	 * Test method for
	 * {@link edu.ncsu.csc216.biosimulation.domain.Predator#getPrey()}.
	 */
	@Test
	public void testGetPrey() {
		wolf.registerPrey(elk);
		assertEquals(wolf.getPrey(), elk);
	}

	/**
	 * Test method for
	 * {@link edu.ncsu.csc216.biosimulation.domain.Predator#getProjectedPopulation()}
	 * .
	 */
	@Test
	public void testGetProjectedPopulation() {
		elk = new Prey(VALID_COUNT_PREY, VALID_BIRTHRATE_PREY, VALID_DEATHRATE_PREY);
		wolf.registerPrey(elk);
		assertEquals(wolf.getPredator().getBirthRate(), .00068, DELTA);
		assertEquals(wolf.getPredator().getCount(), 10.0, DELTA);
		assertEquals(wolf.getPredator().getDeathRate(), .23, DELTA);
		assertEquals(wolf.getPrey().getCount(), 250, DELTA);
		assertEquals(9.4, wolf.getProjectedPopulation(), DELTA);
	}

	private boolean testNegativeBirthRatePredatorConstructor(boolean thrown) {
		try {
			new Predator(10, -.23, .23);
		} catch (final IllegalArgumentException e) {
			thrown = true;
		}
		return thrown;
	}

	private boolean testNegativeDeathRatePredatorConstructor(boolean thrown) {
		try {
			new Predator(10, .23, -.23);
		} catch (final IllegalArgumentException e) {
			thrown = true;
		}
		return thrown;
	}

	private boolean testOutOfRangeBirthRatePredatorConstructor(boolean thrown) {
		try {
			new Predator(10, 3.0, .23);
		} catch (final IllegalArgumentException e) {
			thrown = true;
		}
		return thrown;
	}

	private boolean testOutOfRangeDeathRatePredatorConstructor(boolean thrown) {
		try {
			new Predator(10, .23, 3.0);
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
	public void testPredator() {
		Species outOfRangeCount;
		Species zeroCount;
		Species zeroBirthRate;
		Species zeroDeathRate;
		Species trial;
		trial = new Predator(3270, .23, .23);
		assertEquals(300, trial.getPredator().getCount(), DELTA);
		boolean thrown = false;
		thrown = false;
		outOfRangeCount = new Predator((int) Predator.MAX_PREDATOR + 1, .002, .23);
		assertEquals(outOfRangeCount.getCount(), Predator.MAX_PREDATOR, DELTA);
		zeroCount = new Predator(0, .002, .23);
		assertEquals(zeroCount.getCount(), 0, DELTA);
		assertTrue(testNegativeBirthRatePredatorConstructor(thrown));
		thrown = false;
		zeroBirthRate = new Predator(10, 0, .23);
		assertEquals(zeroBirthRate.getBirthRate(), 0, DELTA);
		assertTrue(testOutOfRangeBirthRatePredatorConstructor(thrown));
		thrown = false;
		assertTrue(testNegativeDeathRatePredatorConstructor(thrown));
		thrown = false;
		zeroDeathRate = new Predator(10, .23, 0);
		assertEquals(zeroDeathRate.getDeathRate(), 0, DELTA);
		assertTrue(testOutOfRangeDeathRatePredatorConstructor(thrown));
		thrown = false;
		assertEquals(wolf.getCount(), 10, 0);
		assertEquals(wolf.getBirthRate(), .00068, DELTA);
		assertEquals(wolf.getDeathRate(), .23, DELTA);
	}

	/**
	 * Test method for
	 * {@link edu.ncsu.csc216.biosimulation.domain.Predator#registerPredator(edu.ncsu.csc216.biosimulation.domain.Species)}
	 * .
	 */
	@Test
	public void testRegisterPredator() {
		assertEquals(wolf.getCount(), 10, 0);
		assertEquals(wolf.getBirthRate(), .00068, DELTA);
		assertEquals(wolf.getDeathRate(), .23, DELTA);
	}

	/**
	 * Test method for
	 * {@link edu.ncsu.csc216.biosimulation.domain.Predator#registerPrey(edu.ncsu.csc216.biosimulation.domain.Species)}
	 * .
	 */
	@Test
	public void testRegisterPrey() {
		wolf.registerPrey(elk);
		assertEquals(wolf.getPrey(), elk);
	}

}
