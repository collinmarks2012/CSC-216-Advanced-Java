package edu.ncsu.csc216.biosimulation.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests Scavenger
 * 
 * @author Collin
 *
 */
public class ScavengerTest {
	private static final double VALID_DEATHRATE_PREY = .0006;
	private static final double VALID_BIRTHRATE_PREY = .165;
	private static final int VALID_COUNT_PREY = 250;
	private static final double VALID_DEATHRATE_PREDATOR = .23;
	private static final double VALID_BIRTHRATE_PREDATOR = .00068;
	private static final int VALID_COUNT_PREDATOR = 10;
	private static final int VALID_COUNT_SCAVENGER = 400;
	private static final double VALID_DEATHRATE_SCAVENGER = .1;
	private static final double VALID_BIRTHRATE_KILLS_SCAVENGER = .000002;
	private static final double VALID_BIRTHRATE_PREY_SCAVENGER = .0006;
	private static final double VALID_BIRTHRATE_PREDATOR_SCAVENGER = .0003;
	/** Acceptable margin of error for round off error */
	private static final double DELTA = Math.pow(10, -8);
	private Predator wolf;
	private Prey elk;
	private Scavenger magpie;

	/**
	 * Sets up Predator, Prey, and Scavenger objects to be used for testing.
	 *
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		wolf = new Predator(VALID_COUNT_PREDATOR, VALID_BIRTHRATE_PREDATOR, VALID_DEATHRATE_PREDATOR);
		elk = new Prey(VALID_COUNT_PREY, VALID_BIRTHRATE_PREY, VALID_DEATHRATE_PREY);
		magpie = new Scavenger(VALID_COUNT_SCAVENGER, VALID_BIRTHRATE_KILLS_SCAVENGER, VALID_DEATHRATE_SCAVENGER,
				VALID_BIRTHRATE_PREY_SCAVENGER, VALID_BIRTHRATE_PREDATOR_SCAVENGER);
	}

	/**
	 * Tests getDefaultParameters method in Scavenger
	 */
	@Test
	public void testGetDefualtParameters() {
		assertEquals(VALID_BIRTHRATE_KILLS_SCAVENGER, Scavenger.getDefaultParameters()[0], DELTA);
		assertEquals(VALID_DEATHRATE_SCAVENGER, Scavenger.getDefaultParameters()[1], DELTA);
		assertEquals(VALID_BIRTHRATE_PREDATOR_SCAVENGER, Scavenger.getDefaultParameters()[3], DELTA);
		assertEquals(VALID_BIRTHRATE_PREY_SCAVENGER, Scavenger.getDefaultParameters()[2], DELTA);
	}

	/**
	 * Tests getPredator method in Scavenger
	 */
	@Test
	public void testGetPredator() {
		magpie.registerPredator(wolf);
		assertEquals(VALID_COUNT_PREDATOR, magpie.getPredator().getCount(), DELTA);
		assertEquals(VALID_BIRTHRATE_PREDATOR, magpie.getPredator().getBirthRate(), DELTA);
		assertEquals(VALID_DEATHRATE_PREDATOR, magpie.getPredator().getDeathRate(), DELTA);
	}

	/**
	 * Tests getPrey method in Scavenger
	 */
	@Test
	public void testGetPrey() {
		magpie.registerPrey(elk);
		assertEquals(VALID_COUNT_PREY, magpie.getPrey().getCount(), DELTA);
		assertEquals(VALID_BIRTHRATE_PREY, magpie.getPrey().getBirthRate(), DELTA);
		assertEquals(VALID_DEATHRATE_PREY, magpie.getPrey().getDeathRate(), DELTA);
	}

	/**
	 * Tests getProjectedPopulation method in Scavenger
	 */
	@Test
	public void testGetProjectedPopulation() {
		magpie.registerPredator(wolf);
		magpie.registerPrey(elk);
		assertEquals(263.2, magpie.getProjectedPopulation(), DELTA);
	}

	/**
	 * Tests registerPredator method in Scavenger
	 */
	@Test
	public void testRegisterPredator() {
		magpie.registerPredator(wolf);
		assertEquals(VALID_COUNT_PREDATOR, magpie.getPredator().getCount(), DELTA);
		assertEquals(VALID_BIRTHRATE_PREDATOR, magpie.getPredator().getBirthRate(), DELTA);
		assertEquals(VALID_DEATHRATE_PREDATOR, magpie.getPredator().getDeathRate(), DELTA);
	}

	/**
	 * Tests registerPrey method in Scavenger
	 */
	@Test
	public void testRegisterPrey() {
		magpie.registerPrey(elk);
		assertEquals(VALID_COUNT_PREY, magpie.getPrey().getCount(), DELTA);
		assertEquals(VALID_BIRTHRATE_PREY, magpie.getPrey().getBirthRate(), DELTA);
		assertEquals(VALID_DEATHRATE_PREY, magpie.getPrey().getDeathRate(), DELTA);
	}

	/**
	 * Test default constructor for Scavenger in Scavenger
	 */
	@Test
	public void testScavenger() {
		assertEquals(VALID_COUNT_SCAVENGER, magpie.getCount(), DELTA);
		assertEquals(VALID_DEATHRATE_SCAVENGER, magpie.getDeathRate(), DELTA);
		if (magpie.getPredator() != null) {
			assertEquals(VALID_BIRTHRATE_PREDATOR_SCAVENGER, magpie.getPredator().getBirthRate(), DELTA);
		}
		final Scavenger aboveCountRange = new Scavenger(999, VALID_BIRTHRATE_KILLS_SCAVENGER, VALID_DEATHRATE_SCAVENGER,
				VALID_BIRTHRATE_PREY_SCAVENGER, VALID_BIRTHRATE_PREDATOR_SCAVENGER);
		assertEquals(500, aboveCountRange.getCount(), DELTA);
		final Scavenger lessThanFourGreaterThanZero = new Scavenger(2, VALID_BIRTHRATE_KILLS_SCAVENGER,
				VALID_DEATHRATE_SCAVENGER, VALID_BIRTHRATE_PREY_SCAVENGER, VALID_BIRTHRATE_PREDATOR_SCAVENGER);
		assertEquals(4, lessThanFourGreaterThanZero.getCount(), DELTA);
		try {
			new Scavenger(5, 1.54, .3, .2, .2);
			fail("Need to look at Scavenger");
		} catch (final IllegalArgumentException e) {
			assertEquals(1, 1, DELTA);
		}

	}

}
