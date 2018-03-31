package edu.ncsu.csc216.flix_2.rental_system;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import edu.ncsu.csc216.flix_2.customer.Customer;

/**
 * Tests DVDRentalSystem
 *
 * @author Collin
 *
 */
public class DVDRentalSystemTest {
	private DVDRentalSystem mo;
	private Customer collin;

	/**
	 * Sets up data
	 *
	 * @throws Exception
	 *             if something goes wrong
	 */
	@Before
	public void setUp() throws Exception {
		mo = new DVDRentalSystem("Files/movies2.txt");
		collin = new Customer("camarks2", "Camdog180", 3);
	}

	/**
	 * Tests
	 */
	@Test
	public void testShowInventory() {
		mo.setCustomer(collin);
		assertEquals(
				"American Sniper\nBig Hero 6\nBoyhood\nFrozen\nGravity\nHow to Train Your Dragon 2\nMaleficent\nSelma\nSpectre (currently unavailable)\nTransformers: Age of Extinction\nWarcraft (currently unavailable)\n",
				mo.showInventory());
		mo.addToCustomerQueue(0);
		mo.addToCustomerQueue(1);
		mo.addToCustomerQueue(2);
		assertEquals("American Sniper\nBig Hero 6\nBoyhood\n", mo.traverseAtHomeQueue());
		mo.addToCustomerQueue(3);
		assertEquals("Frozen\n", mo.traverseReserveQueue());
		mo.addToCustomerQueue(4);
		assertEquals("Frozen\nGravity\n", mo.traverseReserveQueue());
		mo.reserveMoveAheadOne(1);
		assertEquals("Gravity\nFrozen\n", mo.traverseReserveQueue());
		mo.returnItemToInventory(0);
		collin.login();
		assertEquals("Big Hero 6\nBoyhood\nGravity\n", mo.traverseAtHomeQueue());
		mo.removeSelectedFromReserves(0);
		assertEquals("", mo.traverseReserveQueue());
		collin = null;
		mo.setCustomer(collin);
		try {
			mo.traverseAtHomeQueue();
			fail();
		} catch (IllegalStateException e) {
			assertNull(collin);
		}
		try {
			mo.addToCustomerQueue(0);
			fail();
		} catch (IllegalStateException e) {
			assertNull(collin);
		}
	}

}
