package edu.ncsu.csc216.flix_2.customer;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests CustomerDB
 *
 * @author Collin Marks
 *
 */
public class CustomerDBTest {

	private CustomerDB list;
	private Customer collin;

	/**
	 * Sets up cases for testing
	 *
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		collin = new Customer("camarks2", "Camdog180", 5);
		list = new CustomerDB();
		list.addNewCustomer("camarks2", "Camdog180", 5);
		list.addNewCustomer("casey", "casey", 5);
	}

	/**
	 * Tests
	 */
	@Test
	public void test() {
		assertEquals("camarks2\ncasey\n", list.listAccounts());
		String s = list.verifyCustomer("camarks2", "Camdog180").getId();
		assertEquals("camarks2", s);
		String d = list.verifyCustomer("casey", "casey").getId();
		assertEquals("casey", d);
		assertEquals(collin.getId(), list.verifyCustomer("camarks2", "Camdog180").getId());
	}

	/**
	 * Tests
	 */
	@Test
	public void testCancelAccount() {
		assertEquals("camarks2\ncasey\n", list.listAccounts());
		list.cancelAccount("casey");
		assertEquals("camarks2\n", list.listAccounts());
		list.cancelAccount("camarks2");
		assertEquals("", list.listAccounts());
		list.addNewCustomer("A", "A", 4);
		list.addNewCustomer("C", "C", 4);
		list.addNewCustomer("B", "B", 4);
		assertEquals("A\nB\nC\n", list.listAccounts());
	}

}
