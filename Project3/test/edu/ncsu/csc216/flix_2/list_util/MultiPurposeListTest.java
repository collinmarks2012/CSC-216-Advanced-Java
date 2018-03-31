package edu.ncsu.csc216.flix_2.list_util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import edu.ncsu.csc216.flix_2.inventory.Movie;

/**
 * Tests MultiPurposeList
 *
 * @author Collin Marks
 *
 */
public class MultiPurposeListTest {
	private MultiPurposeList<Movie> movies;

	/**
	 * Sets up data for testing
	 *
	 * @throws Exception
	 *             if something goes wrong
	 */
	@Before
	public void setUp() throws Exception {
		movies = new MultiPurposeList<Movie>();
	}

	/**
	 * Test
	 */
	@Test
	public void test() {
		assertTrue(movies.isEmpty());
		assertFalse(movies.hasNext());
		assertEquals(0, movies.size());
		Movie toy = new Movie("2 Toy Story");
		movies.addItem(0, toy);
		movies.resetIterator();
		assertFalse(movies.isEmpty());
		assertEquals(1, movies.size());
		assertEquals(toy, movies.lookAtItemN(0));
		movies.remove(0);
		movies.resetIterator();
		assertFalse(movies.hasNext());
		assertEquals(0, movies.size());
		Movie bug = new Movie(" 3           A Bug's Life");
		Movie shrek = new Movie("  5      Shrek                ");
		movies.addItem(0, toy);
		movies.addItem(1, bug);
		movies.addItem(2, shrek);
		movies.addItem(0, shrek);
		movies.resetIterator();
		assertEquals(4, movies.size());
		movies.remove(0);
	}

	/**
	 * test
	 */
	@Test
	public void testNext() {
		assertTrue(movies.isEmpty());
		assertFalse(movies.hasNext());
		assertEquals(0, movies.size());
		Movie toy = new Movie("2 Toy Story");
		movies.addItem(0, toy);
		// Only 1 item in list
		assertEquals(1, movies.size());
		assertFalse(movies.hasNext());
		assertNull(movies.next());
		movies.resetIterator();
		Movie bug = new Movie("3       Bugs");
		movies.addItem(1, bug);
		movies.resetIterator();
		assertEquals(2, movies.size());
		assertTrue(movies.hasNext());
		// assertEquals(toy.getName(), movies.next().getName());
		movies.next();
		// assertEquals(bug.getName(), movies.next().getName());

	}

	/**
	 * Test
	 */
	@Test
	public void testIntList2() {
		MultiPurposeList<Integer> ints = new MultiPurposeList<Integer>();
		ints.addItem(0, 0);
		ints.addItem(1, 1);
		ints.addItem(2, 2);
		ints.remove(0);
		assertEquals(2, ints.lookAtItemN(1).intValue());
		assertEquals(2, ints.size());
		// assertNull(ints.lookAtItemN(2));
	}

	/**
	 * Test
	 */
	@Test
	public void test2() {
		MultiPurposeList<Integer> ints = new MultiPurposeList<Integer>();
		ints.addItem(0, 0);
		ints.addItem(1, 1);
		ints.addItem(2, 2);
		ints.remove(0);
		ints.resetIterator();
		assertEquals(1, ints.next().intValue());
		assertEquals(2, ints.next().intValue());
		assertFalse(ints.hasNext());
		ints.addItem(0, 0);
		ints.resetIterator();
		assertEquals(0, ints.next().intValue());
		assertEquals(1, ints.next().intValue());
		assertEquals(2, ints.next().intValue());
		assertEquals(0, ints.lookAtItemN(0).intValue());
		assertEquals(1, ints.lookAtItemN(1).intValue());
		assertEquals(2, ints.lookAtItemN(2).intValue());
	}

	/**
	 * Test
	 */
	@Test
	public void testAddItem() {
		MultiPurposeList<String> strings = new MultiPurposeList<String>();
		strings.addItem(0, "s0");
		strings.addItem(1, "s1");
		strings.addItem(2, "s2");
		strings.addItem(3, "s3");
		strings.resetIterator();
		assertTrue(strings.hasNext());
		assertEquals("s0", strings.next());
		assertTrue(strings.hasNext());
		assertEquals("s1", strings.next());
		assertTrue(strings.hasNext());
		assertEquals("s2", strings.next());
		assertTrue(strings.hasNext());
		assertEquals("s3", strings.next());
		assertFalse(strings.hasNext());
		strings.addToRear("s4");
		strings.resetIterator();
		assertTrue(strings.hasNext());
		assertEquals("s0", strings.next());
		assertTrue(strings.hasNext());
		assertEquals("s1", strings.next());
		assertTrue(strings.hasNext());
		assertEquals("s2", strings.next());
		assertTrue(strings.hasNext());
		assertEquals("s3", strings.next());
		assertTrue(strings.hasNext());
		assertEquals("s4", strings.next());
		assertFalse(strings.hasNext()); // Now testing look at ItemN method
		assertEquals("s0", strings.lookAtItemN(0));
		assertEquals("s1", strings.lookAtItemN(1));
		assertEquals("s2", strings.lookAtItemN(2));
		assertEquals("s3", strings.lookAtItemN(3));
		strings.moveAheadOne(1);
		strings.resetIterator();
		assertTrue(strings.hasNext());
		assertEquals("s1", strings.next());
		assertTrue(strings.hasNext());
		assertEquals("s0", strings.next());
		assertTrue(strings.hasNext());
		assertEquals("s2", strings.next());
		assertTrue(strings.hasNext());
		assertEquals("s3", strings.next());
		assertTrue(strings.hasNext());
		assertEquals("s4", strings.next());
		assertFalse(strings.hasNext()); // Now testing look at ItemN method
		assertEquals("s1", strings.lookAtItemN(0));
		assertEquals("s0", strings.lookAtItemN(1));
		assertEquals("s2", strings.lookAtItemN(2));
		assertEquals("s3", strings.lookAtItemN(3));

	}

	/**
	 * Tests
	 */
	@Test
	public void testAddMiddle() {
		MultiPurposeList<String> strings = new MultiPurposeList<String>();
		strings.addItem(0, "s0");
		strings.addItem(1, "s1");
		strings.addItem(2, "s2");
		strings.addItem(3, "s3");
		strings.resetIterator();
		strings.addItem(2, "A");
		strings.resetIterator();
		assertTrue(strings.hasNext());
		assertEquals("s0", strings.next());
		assertTrue(strings.hasNext());
		assertEquals("s1", strings.next());
		assertTrue(strings.hasNext());
		assertEquals("A", strings.next());
		assertTrue(strings.hasNext());
		assertEquals("s2", strings.next());
		assertTrue(strings.hasNext());
		assertEquals("s3", strings.next());
		assertFalse(strings.hasNext());
	}

	/**
	 * Test
	 */
	@Test
	public void testMoveAhead() {
		MultiPurposeList<String> chars = new MultiPurposeList<String>();
		chars.addItem(0, "A");
		chars.addItem(1, "B");
		chars.moveAheadOne(1);
		chars.resetIterator();
		assertEquals("B", chars.lookAtItemN(0));
		assertEquals("A", chars.lookAtItemN(1));
	}

	/**
	 * Test
	 */
	@Test
	public void testMoveAheadMultiple() {
		MultiPurposeList<String> chars = new MultiPurposeList<String>();
		chars.addItem(0, "A");
		chars.addItem(1, "B");
		chars.addItem(2, "C");
		chars.addToRear("D");
		chars.resetIterator();
		assertEquals(4, chars.size());
		chars.moveAheadOne(3);
		chars.resetIterator();
		assertEquals(4, chars.size());
		assertEquals("D", chars.lookAtItemN(2));
		assertEquals("C", chars.lookAtItemN(3));
	}

	/**
	 * Test
	 */
	@Test
	public void testRemove() {
		MultiPurposeList<String> chars = new MultiPurposeList<String>();
		chars.addItem(0, "A");
		chars.addItem(1, "B");
		chars.addItem(2, "C");
		chars.addToRear("D");
		chars.resetIterator();
		chars.remove(0);
		chars.resetIterator();
		assertEquals("B", chars.lookAtItemN(0));
		assertEquals("C", chars.lookAtItemN(1));
		assertEquals("D", chars.lookAtItemN(2));
		assertEquals("D", chars.lookAtItemN(3));
	}
}
