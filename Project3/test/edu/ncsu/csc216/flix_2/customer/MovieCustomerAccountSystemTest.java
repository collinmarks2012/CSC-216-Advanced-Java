package edu.ncsu.csc216.flix_2.customer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import edu.ncsu.csc216.flix_2.rental_system.RentalManager;

/**
 * Tests MovieCustomerAccountSystem
 *
 * @author Collin
 *
 */
public class MovieCustomerAccountSystemTest {

	private RentalManager rm;
	private MovieCustomerAccountSystem manager;

	/**
	 * Sets up test cases
	 *
	 * @throws Exception
	 *             if something goes wrong
	 */
	@Before
	public void setUp() throws Exception {
		manager = new MovieCustomerAccountSystem(rm);
	}

	/**
	 * Test
	 */
	@Test
	public void testLogin() {
		manager.login("admin", "aDmIn");
		assertTrue(manager.isAdminLoggedIn());
		assertFalse(manager.isCustomerLoggedIn());
	}

	/**
	 * test
	 */
	@Test
	public void testLogout() {
		manager.login("admin", "aDmIn");
		manager.logout();
		assertFalse(manager.isAdminLoggedIn());
		assertFalse(manager.isCustomerLoggedIn());
	}

	/**
	 * Test
	 */
	@Test
	public void testAddNewCustomer() {
		manager.login("admin", "aDmIn");
		manager.addNewCustomer("camarks2", "Camdog180", 5);
		assertEquals("camarks2\n", manager.listAccounts());
		manager.logout();
		assertFalse(manager.isAdminLoggedIn());
		assertFalse(manager.isCustomerLoggedIn());
		manager.login("camarks2", "Camdog180");
		assertTrue(manager.isCustomerLoggedIn());
		assertFalse(manager.isAdminLoggedIn());
		try {
			manager.addNewCustomer("s", "s", 4);
			fail();
		} catch (IllegalStateException e) {
			assertEquals("camarks2\n", manager.listAccounts());
		}
	}

	/**
	 * Test
	 */
	@Test
	public void testCancelAccount() {
		manager.login("admin", "aDmIn");
		manager.addNewCustomer("camarks2", "Camdog180", 5);
		manager.cancelAccount("camarks2");
		assertEquals("", manager.listAccounts());
	}

	/**
	 * Test
	 */
	@Test
	public void testAccess() {
		manager.login("admin", "aDmIn");
		manager.addNewCustomer("camarks2", "Camdog180", 5);
		manager.logout();
		manager.login("camarks2", "Camdog180");
		try {
			manager.addNewCustomer("d", "d", 4);
			fail();
		} catch (IllegalStateException e) {
			assertEquals("camarks2\n", manager.listAccounts());
		}
		try {
			manager.cancelAccount("camarks2");
			fail();
		} catch (IllegalStateException e) {
			assertEquals("camarks2\n", manager.listAccounts());
		}
		manager.logout();
		manager.login("ADMIN", "ADMIN");
		manager.addNewCustomer("d", "d", 4);
		assertEquals("camarks2\nd\n", manager.listAccounts());
		manager.cancelAccount("d");
		assertEquals("camarks2\n", manager.listAccounts());
	}
}