package edu.ncsu.csc216.flix_2.customer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import edu.ncsu.csc216.flix_2.inventory.Movie;

/**
 * Tests Customer
 *
 * @author Collin Marks
 *
 */
public class CustomerTest {

	private Customer collin;
	private Customer casey;
	private Movie toy;
	private Movie bug;

	/**
	 * Test
	 *
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		collin = new Customer("camarks2", "Camdog180", 1);
		casey = new Customer("casey", "casey", 5);
		toy = new Movie("1 Toy Story");
		bug = new Movie("2 Its A Bugs Life");
	}

	/**
	 * test
	 */
	@Test
	public void test() {
		assertEquals("camarks2", collin.getId());
		assertEquals(-6, collin.compareToByName(casey));
		collin.login();
		collin.reserve(toy);
		collin.reserve(bug);
		String s = collin.traverseReserveQueue();
		assertEquals("Its A Bugs Life" + "\n", s);
		s = collin.traverseAtHomeQueue();
		assertEquals("Toy Story" + "\n", s);
		collin.returnDVD(0);
		collin.login();
		s = collin.traverseAtHomeQueue();
		assertEquals("Its A Bugs Life" + "\n", s);
	}

	/**
	 * Test
	 */
	@Test
	public void testCloseAccount() {
		collin.login();
		collin.reserve(toy);
		collin.reserve(bug);
		collin.closeAccount();
		assertEquals("", collin.traverseAtHomeQueue());
	}

	/**
	 * Test
	 */
	@Test
	public void testReturnDVD() {
		Movie star = new Movie("4 Star Wars");
		casey.login();
		casey.reserve(toy);
		casey.reserve(bug);
		casey.reserve(star);
		casey.returnDVD(1);
		casey.login();
		assertEquals("Toy Story" + "\n" + "Star Wars" + "\n", casey.traverseAtHomeQueue());
	}

	/**
	 * Test
	 */
	@Test
	public void testReturnDVD2() {
		Movie movie1 = new Movie("3 Movie 1");
		Movie movie2 = new Movie("3 Movie 2");
		Movie movie3 = new Movie("3 Movie 3");
		casey.login();
		casey.reserve(movie1);
		casey.reserve(movie2);
		casey.reserve(movie3);
		casey.returnDVD(0);
		assertEquals("Movie 2\nMovie 3\n", casey.traverseAtHomeQueue());
	}

	/**
	 * Test
	 */
	@Test
	public void testCloseAccount2() {
		Movie movie1 = new Movie("3 Movie 1");
		Movie movie2 = new Movie("3 Movie 2");
		casey.login();
		casey.reserve(movie1);
		casey.reserve(movie2);
		assertEquals("Movie 1\nMovie 2\n", casey.traverseAtHomeQueue());
		casey.closeAccount();
		assertEquals("", casey.traverseAtHomeQueue());
	}

	/**
	 * Test
	 */
	@Test
	public void testConstructor() {
		collin = new Customer("camarks2", "Camdog180", 16);
		Movie movie1 = new Movie("3 Movie 1");
		Movie movie2 = new Movie("3 Movie 2");
		Movie movie3 = new Movie("3 Movie 3");
		Movie movie4 = new Movie("3 Movie 4");
		Movie movie5 = new Movie("3 Movie 5");
		Movie movie6 = new Movie("3 Movie 6");
		collin.reserve(movie1);
		collin.reserve(movie2);
		collin.reserve(movie3);
		collin.reserve(movie4);
		collin.reserve(movie5);
		collin.reserve(movie6);
		assertEquals("Movie 1\nMovie 2\nMovie 3\nMovie 4\nMovie 5\n", collin.traverseAtHomeQueue());
		assertEquals("Movie 6\n", collin.traverseReserveQueue());
		Movie movie7 = new Movie("3 Movie 7");
		collin.reserve(movie7);
		collin.moveAheadOneInReserves(1);
		collin.returnDVD(2);
		collin.login();
		assertEquals("Movie 1\nMovie 2\nMovie 4\nMovie 5\nMovie 7\n", collin.traverseAtHomeQueue());
		assertEquals("Movie 6\n", collin.traverseReserveQueue());
		collin.unReserve(0);
		collin.login();
		assertEquals("", collin.traverseReserveQueue());
	}

	/**
	 * Test
	 */
	@Test
	public void testUnReserve() {
		// collin has 1 casey has 5
		Movie movie1 = new Movie("1 Movie 1");
		Movie movie2 = new Movie("2 Movie 2");
		collin.login();
		collin.reserve(movie1);
		collin.reserve(movie2);
		casey.login();
		casey.reserve(movie1);
		assertEquals("Movie 1\n", casey.traverseReserveQueue());
		assertEquals("", casey.traverseAtHomeQueue());
		casey.closeAccount();

	}

	/**
	 * Test
	 */
	@Test
	public void testVerifyPassword() {
		assertTrue(casey.verifyPassword("casey"));
	}

}
