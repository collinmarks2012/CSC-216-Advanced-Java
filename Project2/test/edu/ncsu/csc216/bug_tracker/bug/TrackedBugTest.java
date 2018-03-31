package edu.ncsu.csc216.bug_tracker.bug;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import edu.ncsu.csc216.bug_tracker.command.Command;
import edu.ncsu.csc216.bug_tracker.command.Command.CommandValue;
import edu.ncsu.csc216.bug_tracker.command.Command.Resolution;
import edu.ncsu.csc216.bug_tracker.xml.Bug;
import edu.ncsu.csc216.bug_tracker.xml.BugIOException;
import edu.ncsu.csc216.bug_tracker.xml.BugReader;

/**
 * Tests Tracked Bug
 *
 * @author Collin
 *
 */
public class TrackedBugTest {
	TrackedBug validBug;

	/**
	 * Sets up a Tracked Bug for testing.
	 *
	 * @throws Exception
	 *             if a Tracked Bug throws an exception
	 */
	@Before
	public void setUp() throws Exception {
		validBug = new TrackedBug("Summary", "Reporter");
	}

	/**
	 * Tests the Constructor
	 */
	@Test
	public void testConstructor() {
		assertEquals("Summary", validBug.getSummary());
		assertEquals("Reporter", validBug.getReporter());
		assertEquals("Unconfirmed", validBug.getState().getStateName());
		assertNull(validBug.getResolution());
		try {
			validBug = new TrackedBug(null, null);
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals("Summary", validBug.getSummary());
			assertEquals("Reporter", validBug.getReporter());
		}
		TrackedBug.setCounter(0);
	}

	/**
	 * Tests a bug in the unconfirmed state.
	 */
	@Test
	public void testUnconfirmed() {
		assertEquals("Unconfirmed", validBug.getState().getStateName());
		assertEquals(1, validBug.getVotes());
		Command c = new Command(CommandValue.VOTE, null, null, null);
		assertEquals(CommandValue.VOTE, c.getCommand());
		validBug.update(c);
		assertNull(c.getNote());
		assertEquals(2, validBug.getVotes());
		assertEquals("Unconfirmed", validBug.getState().getStateName());
		c = new Command(CommandValue.VOTE, null, null, null);
		validBug.update(c);
		assertEquals(3, validBug.getVotes());
		assertEquals("New", validBug.getState().getStateName());
		TrackedBug validBug2 = new TrackedBug("Summary", "Reporter");
		assertEquals("Unconfirmed", validBug2.getState().getStateName());
		assertEquals(1, validBug2.getVotes());
		Command d = new Command(CommandValue.CONFIRM, null, null, null);
		validBug2.update(d);
		assertEquals("New", validBug2.getState().getStateName());
		TrackedBug validBug3 = new TrackedBug("Summary", "Reporter");
		assertEquals("Unconfirmed", validBug3.getState().getStateName());
		assertEquals(1, validBug3.getVotes());
		Command e = new Command(CommandValue.VOTE, null, null, null);
		assertEquals(CommandValue.VOTE, e.getCommand());
		// Vote twice
		validBug3.update(e);
		validBug3.update(e);
		assertEquals(3, validBug3.getVotes());
		assertEquals("New", validBug3.getState().getStateName());
		assertFalse(validBug3.isConfirmed());
		Command f = new Command(CommandValue.POSSESSION, "camarks2", null, null);
		validBug3.update(f);
		assertEquals("camarks2", f.getDeveloperId());
		assertEquals("Assigned", validBug3.getState().getStateName());
		Command g = new Command(CommandValue.RESOLVED, "camarks2", Resolution.FIXED, null);
		validBug3.update(g);
		assertEquals(Command.Resolution.FIXED, validBug3.getResolution());
		assertEquals("Resolved", validBug3.getState().getStateName());

		TrackedBug.setCounter(0);
	}

	/**
	 * Tests a bugs transition from new to assigned.
	 */
	@Test
	public void testNewToAssigned() {
		Command c = new Command(CommandValue.VOTE, null, null, null);
		validBug.update(c);
		validBug.update(c);
		assertEquals(3, validBug.getVotes());
		assertEquals("New", validBug.getState().getStateName());
		assertNull(validBug.getOwner());
		try {
			c = new Command(CommandValue.POSSESSION, null, null, null);
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals(c.getCommand(), CommandValue.VOTE);
		}
		c = new Command(CommandValue.POSSESSION, "collin", null, null);
		validBug.update(c);
		assertEquals(CommandValue.POSSESSION, c.getCommand());
		assertEquals("collin", c.getDeveloperId());
		assertEquals("collin", validBug.getOwner());
		assertEquals(TrackedBug.ASSIGNED_NAME, validBug.getState().getStateName());
		TrackedBug.setCounter(0);
	}

	/**
	 * Tests the get notes method of TrackedBug
	 */
	@Test
	public void testGetNotes() {
		Command c = new Command(Command.CommandValue.VOTE, null, null, "notes");
		validBug.update(c);
		assertEquals(1, validBug.getNotes().size());
		assertEquals("notes", validBug.getNotes().get(0));
	}

	/**
	 * Tests the transition of a bug from unconfirmed to assigned.
	 */
	@Test
	public void testUpdateUnconfirmedToAssignedThroughVotes() {
		Command c = new Command(CommandValue.VOTE, null, null, null);
		validBug.update(c);
		validBug.update(c);
		assertEquals(3, validBug.getVotes());
		assertEquals("New", validBug.getState().getStateName());
		assertFalse(validBug.isConfirmed());
		c = new Command(CommandValue.POSSESSION, "collin", null, null);
		validBug.update(c);
		assertEquals(TrackedBug.ASSIGNED_NAME, validBug.getState().getStateName());
		assertEquals("collin", validBug.getOwner());
		c = new Command(CommandValue.RESOLVED, "collin", Resolution.FIXED, null);
		validBug.update(c);
		assertEquals(TrackedBug.RESOLVED_NAME, validBug.getState().getStateName());
		assertEquals(Command.Resolution.FIXED, validBug.getResolution());
		c = new Command(CommandValue.REOPEN, "collin", null, null);
		validBug.update(c);
		assertEquals(TrackedBug.UNCONFIRMED_NAME, validBug.getState().getStateName());
		assertNull(validBug.getResolution());
		assertFalse(validBug.isConfirmed());
		assertEquals("collin", validBug.getOwner());
		assertEquals(3, validBug.getVotes());
		c = new Command(CommandValue.VOTE, "collin", null, null);
		validBug.update(c);
		assertEquals(4, validBug.getVotes());
		assertFalse(validBug.isConfirmed());
		assertEquals("collin", validBug.getOwner());
		assertEquals(TrackedBug.ASSIGNED_NAME, validBug.getState().getStateName());
		TrackedBug.setCounter(0);
	}

	/**
	 * Tests the transition from unconfirmed to assigned.
	 */
	@Test
	public void testUpdateUnconfirmedToAssignedThroughConfirm() {
		Command c = new Command(CommandValue.VOTE, null, null, null);
		validBug.update(c);
		validBug.update(c);
		assertEquals(3, validBug.getVotes());
		assertEquals("New", validBug.getState().getStateName());
		assertFalse(validBug.isConfirmed());
		c = new Command(CommandValue.POSSESSION, "collin", null, null);
		validBug.update(c);
		assertEquals(TrackedBug.ASSIGNED_NAME, validBug.getState().getStateName());
		assertEquals("collin", validBug.getOwner());
		c = new Command(CommandValue.RESOLVED, "collin", Resolution.FIXED, null);
		validBug.update(c);
		assertEquals(TrackedBug.RESOLVED_NAME, validBug.getState().getStateName());
		assertEquals(Command.Resolution.FIXED, validBug.getResolution());
		c = new Command(CommandValue.REOPEN, "collin", null, null);
		validBug.update(c);
		assertEquals(3, validBug.getVotes());
		assertEquals("collin", validBug.getOwner());
		assertEquals(TrackedBug.UNCONFIRMED_NAME, validBug.getState().getStateName());
		c = new Command(CommandValue.CONFIRM, "collin", null, null);
		validBug.update(c);
		assertEquals(3, validBug.getVotes());
		assertEquals("collin", validBug.getOwner());
		assertTrue(validBug.isConfirmed());
		assertEquals(TrackedBug.ASSIGNED_NAME, validBug.getState().getStateName());
		TrackedBug.setCounter(0);
	}

	/**
	 * Tests setResolution method
	 */
	@Test
	public void testSetResolution() {
		Command c = new Command(CommandValue.VOTE, "collin", null, null);
		validBug.update(c);
		validBug.update(c);
		assertEquals(TrackedBug.NEW_NAME, validBug.getState().getStateName());
		c = new Command(CommandValue.POSSESSION, "casey", null, null);
		validBug.update(c);
		assertEquals(TrackedBug.ASSIGNED_NAME, validBug.getState().getStateName());
		c = new Command(CommandValue.RESOLVED, "casey", Resolution.FIXED, null);
		validBug.update(c);
		assertEquals("Resolved", validBug.getState().getStateName());
		assertEquals(Command.Resolution.FIXED, validBug.getResolution());
		TrackedBug.setCounter(0);
	}

	/**
	 * Tests a Bug in the resolved state with a resolution of duplicate.
	 */
	@Test
	public void testResolutionDuplicate() {
		Command c = new Command(CommandValue.VOTE, null, null, null);
		validBug.update(c);
		validBug.update(c);
		c = new Command(CommandValue.POSSESSION, "collin", null, null);
		validBug.update(c);
		c = new Command(CommandValue.RESOLVED, null, Resolution.DUPLICATE, null);
		validBug.update(c);
		assertEquals("Closed", validBug.getState().getStateName());
		assertEquals("Duplicate", validBug.getResolutionString());
	}

	/**
	 * Tests a bug in the resolved state with a resolution of wontfix.
	 */
	@Test
	public void testResolutionWontFix() {
		Command c = new Command(CommandValue.VOTE, null, null, null);
		validBug.update(c);
		validBug.update(c);
		c = new Command(CommandValue.POSSESSION, "collin", null, null);
		validBug.update(c);
		c = new Command(CommandValue.RESOLVED, null, Resolution.WONTFIX, null);
		validBug.update(c);
		assertEquals("Closed", validBug.getState().getStateName());
		assertEquals("WontFix", validBug.getResolutionString());
	}

	/**
	 * Tests a bug in the resolved state with a resolution of worksforme.
	 */
	@Test
	public void testResolutionWorksForMe() {
		Command c = new Command(CommandValue.VOTE, null, null, null);
		validBug.update(c);
		validBug.update(c);
		c = new Command(CommandValue.POSSESSION, "collin", null, null);
		validBug.update(c);
		c = new Command(CommandValue.RESOLVED, null, Resolution.WORKSFORME, null);
		validBug.update(c);
		assertEquals("Closed", validBug.getState().getStateName());
		assertEquals("WorksForMe", validBug.getResolutionString());
	}

	/**
	 * Test the getNotesString method.
	 */
	@Test
	public void testNotes() {
		Command c = new Command(CommandValue.VOTE, "collin", null, "note");
		validBug.update(c);
		assertEquals("note\n------\n", validBug.getNotesString());
		TrackedBug.setCounter(0);
	}

	/**
	 * Tests the transition of a bug from reopen to assigned.
	 */
	@Test
	public void testUpdateReopenToAssigned() {
		Command c = new Command(CommandValue.CONFIRM, "collin", null, null);
		validBug.update(c);
		assertEquals(TrackedBug.NEW_NAME, validBug.getState().getStateName());
		c = new Command(CommandValue.POSSESSION, "collin", null, null);
		validBug.update(c);
		c = new Command(CommandValue.RESOLVED, "collin", Resolution.FIXED, null);
		validBug.update(c);
		c = new Command(CommandValue.REOPEN, "collin", null, null);
		validBug.update(c);
		assertEquals(TrackedBug.REOPEN_NAME, validBug.getState().getStateName());
		assertTrue(validBug.isConfirmed());
		assertEquals("collin", validBug.getOwner());
		assertNull(validBug.getResolution());
		c = new Command(CommandValue.POSSESSION, "casey", null, null);
		validBug.update(c);
		assertTrue(validBug.isConfirmed());
		assertEquals("casey", validBug.getOwner());
		assertNull(validBug.getResolution());
		assertEquals(TrackedBug.ASSIGNED_NAME, validBug.getState().getStateName());
		TrackedBug.setCounter(0);
	}

	/**
	 * Tests a bug in the reopen state.
	 */
	@Test
	public void testReopenState() {
		assertEquals(TrackedBug.UNCONFIRMED_NAME, validBug.getState().getStateName());
		Command c = new Command(CommandValue.CONFIRM, "collin", null, null);
		validBug.update(c);
		c = new Command(CommandValue.POSSESSION, "collin", null, null);
		validBug.update(c);
		assertEquals(TrackedBug.ASSIGNED_NAME, validBug.getState().getStateName());
		c = new Command(CommandValue.RESOLVED, "collin", Command.Resolution.FIXED, null);
		validBug.update(c);
		assertEquals(TrackedBug.RESOLVED_NAME, validBug.getState().getStateName());
		c = new Command(CommandValue.REOPEN, "collin", null, null);
		validBug.update(c);
		assertEquals(TrackedBug.REOPEN_NAME, validBug.getState().getStateName());
		TrackedBug.setCounter(0);
	}

	/**
	 * Tests the notes feature of getXMLBug method.
	 */
	@Test
	public void testNotesGetXMLBug() {
		TrackedBug a = new TrackedBug("s", "r");
		Command c = new Command(Command.CommandValue.VOTE, null, null, "note");
		a.update(c);
		Bug xml = a.getXMLBug();
		assertEquals("note", xml.getNoteList().getNote().get(0));

	}

	/**
	 * Tests the Bug Reader and how it adds Bugs to TrackedBug.
	 */
	@Test
	public void testBugReader() {
		BugReader readMe;
		List<TrackedBug> a = new ArrayList<TrackedBug>();
		try {
			readMe = new BugReader("C:/Users/Collin/git/csc216-601-P2-08/Project2/lib/test_files/bug1.xml");
			for (Bug b : readMe.getBugs()) {
				a.add(new TrackedBug(b));
			}
			assertEquals(TrackedBug.UNCONFIRMED_NAME, a.get(0).getState().getStateName());
			assertEquals(TrackedBug.NEW_NAME, a.get(1).getState().getStateName());
			assertEquals(TrackedBug.ASSIGNED_NAME, a.get(2).getState().getStateName());
			assertEquals(TrackedBug.RESOLVED_NAME, a.get(3).getState().getStateName());
			assertEquals(TrackedBug.REOPEN_NAME, a.get(4).getState().getStateName());
			assertEquals(TrackedBug.CLOSED_NAME, a.get(5).getState().getStateName());
			assertEquals(1, a.get(0).getVotes());
			assertEquals("Bug description", a.get(0).getSummary());
			assertEquals("reporter", a.get(0).getReporter());
			assertFalse(a.get(0).isConfirmed());
			// assertEquals("Note1\n------", a.get(0).getNotesString());

		} catch (BugIOException e) {
			e.printStackTrace();
		}
		TrackedBug.setCounter(0);
	}

	/**
	 * Tests the summary and reporter fields of a TrackedBug Constructor
	 */
	@Test
	public void testConstructorFields() {
		Bug xmlValidBug = validBug.getXMLBug();
		assertEquals("Reporter", xmlValidBug.getReporter());
		assertEquals("Summary", xmlValidBug.getSummary());
	}

	/**
	 * Tests a bug in the reopen state.
	 */
	@Test
	public void testReopen() {
		Command c = new Command(CommandValue.CONFIRM, null, null, "note1");
		validBug.update(c);
		c = new Command(CommandValue.POSSESSION, "casey", null, "note2");
		validBug.update(c);
		c = new Command(CommandValue.RESOLVED, "casey", Command.Resolution.FIXED, "note3");
		validBug.update(c);
		c = new Command(CommandValue.REOPEN, "casey", null, "note4");
		validBug.update(c);
		assertEquals(TrackedBug.REOPEN_NAME, validBug.getState().getStateName());
		c = new Command(CommandValue.RESOLVED, "casey", Command.Resolution.FIXED, "note5");
		validBug.update(c);
		assertEquals(TrackedBug.RESOLVED_NAME, validBug.getState().getStateName());
		String expNotes = "note1\n------\nnote2\n------\nnote3\n------\nnote4\n------\nnote5\n------\n";
		assertEquals(expNotes, validBug.getNotesString());
	}

	/**
	 * Tests a bug in the Reopen state with a resolution of dulicate.
	 */
	@Test
	public void testReopenDuplicate() {
		Command c = new Command(CommandValue.CONFIRM, null, null, "note1");
		validBug.update(c);
		c = new Command(CommandValue.POSSESSION, "casey", null, "note2");
		validBug.update(c);
		c = new Command(CommandValue.RESOLVED, "casey", Command.Resolution.FIXED, "note3");
		validBug.update(c);
		c = new Command(CommandValue.REOPEN, "casey", null, "note4");
		validBug.update(c);
		assertEquals(TrackedBug.REOPEN_NAME, validBug.getState().getStateName());
		c = new Command(CommandValue.RESOLVED, null, Command.Resolution.DUPLICATE, null);
		validBug.update(c);
		assertEquals(TrackedBug.CLOSED_NAME, validBug.getState().getStateName());
		assertEquals("Duplicate", validBug.getResolutionString());
	}

	/**
	 * Tests a bug in the reopen state with a resolution of wont fix.
	 */
	@Test
	public void testReopenWontFix() {
		Command c = new Command(CommandValue.CONFIRM, null, null, "note1");
		validBug.update(c);
		c = new Command(CommandValue.POSSESSION, "casey", null, "note2");
		validBug.update(c);
		c = new Command(CommandValue.RESOLVED, "casey", Command.Resolution.FIXED, "note3");
		validBug.update(c);
		c = new Command(CommandValue.REOPEN, "casey", null, "note4");
		validBug.update(c);
		assertEquals(TrackedBug.REOPEN_NAME, validBug.getState().getStateName());
		c = new Command(CommandValue.RESOLVED, null, Command.Resolution.WONTFIX, null);
		validBug.update(c);
		assertEquals(TrackedBug.CLOSED_NAME, validBug.getState().getStateName());
		assertEquals("WontFix", validBug.getResolutionString());
	}

	/**
	 * Tests a transition of a bug from closed to reopen.
	 */
	@Test
	public void testClosedToReopen() {
		Command c = new Command(CommandValue.CONFIRM, null, null, "note1");
		validBug.update(c);
		c = new Command(CommandValue.POSSESSION, "casey", null, "note2");
		validBug.update(c);
		c = new Command(CommandValue.RESOLVED, "casey", Command.Resolution.FIXED, "note3");
		validBug.update(c);
		c = new Command(CommandValue.REOPEN, "casey", null, "note4");
		validBug.update(c);
		assertEquals(TrackedBug.REOPEN_NAME, validBug.getState().getStateName());
		c = new Command(CommandValue.RESOLVED, null, Command.Resolution.WONTFIX, null);
		validBug.update(c);
		assertEquals(TrackedBug.CLOSED_NAME, validBug.getState().getStateName());
		c = new Command(CommandValue.REOPEN, null, null, null);
		validBug.update(c);
		assertEquals(TrackedBug.REOPEN_NAME, validBug.getState().getStateName());
	}
}
