package edu.ncsu.csc216.flix_2.inventory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests Movie
 *
 * @author Collin Marks
 *
 */
public class MovieTest {

	private String line;

	/**
	 * Sets up data for testing
	 *
	 * @throws Exception
	 *             if something goes wrong
	 */
	@Before
	public void setUp() throws Exception {
		line = "1   Bug's  Life";
	}

	/**
	 * Test
	 */
	@Test
	public void testConstructor() {
		Movie a = new Movie(line);
		assertTrue(a.isAvailable());
		assertEquals("Bug's  Life", a.getName());
		a.removeOneCopyFromInventory();
		assertFalse(a.isAvailable());
		try {
			a.removeOneCopyFromInventory();
			fail();
		} catch (IllegalStateException e) {
			assertFalse(a.isAvailable());
			assertEquals("Bug's  Life", a.getName());
			assertEquals("No copy of this movie currently available.", e.getMessage());
		}
		a.backToInventory();
		assertTrue(a.isAvailable());
		assertEquals("Bug's  Life", a.getDisplayName());
		a.removeOneCopyFromInventory();
		assertEquals("Bug's  Life (currently unavailable)", a.getDisplayName());
	}
}
