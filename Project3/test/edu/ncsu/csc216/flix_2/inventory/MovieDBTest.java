package edu.ncsu.csc216.flix_2.inventory;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests MovieDB
 *
 * @author Collin Marks
 *
 */
public class MovieDBTest {
	private MovieDB movies;

	/**
	 * Sets up data
	 *
	 * @throws Exception
	 *             if file not found
	 */
	@Before
	public void setUp() throws Exception {
		movies = new MovieDB("Files/movies.txt");
	}

	/**
	 * Test
	 */
	@Test
	public void test() {
		assertEquals("12 Years a Slave", movies.findItemAt(0).getName());
	}

}
