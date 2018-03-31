package edu.ncsu.csc216.bug_tracker.tracker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import edu.ncsu.csc216.bug_tracker.bug.TrackedBug;
import edu.ncsu.csc216.bug_tracker.command.Command;
import edu.ncsu.csc216.bug_tracker.command.Command.CommandValue;
import edu.ncsu.csc216.bug_tracker.xml.Bug;
import edu.ncsu.csc216.bug_tracker.xml.BugIOException;
import edu.ncsu.csc216.bug_tracker.xml.BugReader;

/**
 * Tests BugList
 * 
 * @author Collin Marks
 *
 */
public class BugListTest extends BugList {
	TrackedBug validBug1;
	TrackedBug validBug2;
	List<TrackedBug> list;
	BugList aList;

	/**
	 * Sets up a bug list and some tracked bugs.
	 *
	 * @throws Exception
	 *             if not working
	 */
	@Before
	public void setUp() throws Exception {
		list = new ArrayList<TrackedBug>();
		validBug1 = new TrackedBug("Summary", "Reporter");
		validBug2 = new TrackedBug("Second Summary", "Second Reporter");
		aList = new BugList();
	}

	/**
	 * Tests buglist default constructor.
	 */
	@Test
	public void testBugList() {
		aList.addBug("summary", "reporter");
		assertEquals("summary", aList.getBugs().get(0).getSummary());
		assertEquals("Unconfirmed", aList.getBugs().get(0).getState().getStateName());
		Command c = new Command(Command.CommandValue.VOTE, null, null, null);
		aList.executeCommand(0, c);
		assertEquals(2, aList.getBugs().get(0).getVotes());
		aList.executeCommand(0, c);
		assertEquals(3, aList.getBugs().get(0).getVotes());
		assertEquals("New", aList.getBugs().get(0).getState().getStateName());
		c = new Command(Command.CommandValue.POSSESSION, "collin", null, null);
		aList.executeCommand(0, c);
		assertEquals("Assigned", aList.getBugs().get(0).getState().getStateName());
		assertEquals("collin", aList.getBugs().get(0).getOwner());
		Bug bug = new Bug();
		bug.setId(2);
		bug.setConfirmed(false);
		bug.setState("Unconfirmed");
		bug.setSummary("Summary");
		bug.setReporter("reporter");
		List<Bug> bugArr = new ArrayList<Bug>();
		bugArr.add(bug);
		aList.addXMLBugs(bugArr);
		assertEquals(2, aList.getBugs().size());
		assertEquals("collin", aList.getBugs().get(0).getOwner());

	}

	/**
	 * Tests adding a bug.
	 */
	@Test
	public void testAddBug() {
		aList.addBug("Description", "Reporter");
		assertEquals("Description", aList.getBugs().get(0).getSummary());
		assertEquals(1, aList.getBugs().size());
	}

	/**
	 * Tests adding an XML bug.
	 */
	@Test
	public void testAddXMLBugs() {

		BugReader readMe;
		List<Bug> a = new LinkedList<Bug>();
		try {
			readMe = new BugReader("C:/Users/Collin/git/csc216-601-P2-08/Project2/lib/test_files/bug1.xml");
			for (Bug b : readMe.getBugs()) {
				a.add(b);
			}
			aList.addXMLBugs(a);
			assertEquals(TrackedBug.UNCONFIRMED_NAME, aList.getBugs().get(0).getState().getStateName());
			assertEquals(TrackedBug.NEW_NAME, aList.getBugs().get(1).getState().getStateName());
			assertEquals(TrackedBug.ASSIGNED_NAME, aList.getBugs().get(2).getState().getStateName());
			assertEquals(TrackedBug.RESOLVED_NAME, aList.getBugs().get(3).getState().getStateName());
			assertEquals(TrackedBug.REOPEN_NAME, aList.getBugs().get(4).getState().getStateName());
			assertEquals(TrackedBug.CLOSED_NAME, aList.getBugs().get(5).getState().getStateName());
		} catch (BugIOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Tests executing a command.
	 */
	@Test
	public void testExecuteCommand() {
		Bug bug = new Bug();
		bug.setState("Unconfirmed");
		bug.setId(1);
		bug.setNoteList(null);
		bug.setReporter("Reporter");
		bug.setSummary("Summary");
		bug.setVotes(2);
		TrackedBug validBug = new TrackedBug(bug);
		assertEquals("Unconfirmed", validBug.getState().getStateName());
		Command vote = new Command(Command.CommandValue.VOTE, null, null, null);
		Command confirm = new Command(Command.CommandValue.CONFIRM, null, null, null);
		Command possesionCollin = new Command(Command.CommandValue.POSSESSION, "collin", null, null);
		Command resolvedFixed = new Command(Command.CommandValue.RESOLVED, "collin", Command.Resolution.FIXED, null);
		Command verify = new Command(Command.CommandValue.VERIFIED, "collin", null, null);
		Command reopen = new Command(Command.CommandValue.REOPEN, null, null, null);
		try {
			validBug.update(possesionCollin);
			validBug.update(resolvedFixed);
			validBug.update(verify);
			validBug.update(reopen);
			fail();
		} catch (UnsupportedOperationException e) {
			validBug.update(vote);
			assertEquals("New", validBug.getState().getStateName());
		}
		try {
			validBug.update(vote);
			validBug.update(confirm);
			validBug.update(resolvedFixed);
			validBug.update(verify);
			validBug.update(reopen);
			fail();
		} catch (UnsupportedOperationException e) {
			validBug.update(possesionCollin);
			assertEquals("Assigned", validBug.getState().getStateName());
		}
		TrackedBug validBugSplit = validBug;
		try {
			validBug.update(vote);
			validBug.update(confirm);
			validBug.update(possesionCollin);
			validBug.update(reopen);
			fail();
		} catch (UnsupportedOperationException e) {
			validBug.update(resolvedFixed);
			assertEquals("Resolved", validBug.getState().getStateName());
			assertEquals("Fixed", validBug.getResolutionString());
		}
		try {
			validBugSplit.update(vote);
			validBugSplit.update(confirm);
			validBugSplit.update(possesionCollin);
			validBugSplit.update(reopen);
			fail();
		} catch (UnsupportedOperationException e) {
			validBugSplit.update(verify);
			assertEquals("Closed", validBugSplit.getState().getStateName());
		}
		try {
			validBug.update(vote);
			validBug.update(confirm);
			validBug.update(possesionCollin);
			validBug.update(resolvedFixed);
			validBug.update(verify);
			fail();
		} catch (UnsupportedOperationException e) {
			validBug.update(reopen);
			assertEquals("Unconfirmed", validBug.getState().getStateName());
		}

	}

	/**
	 * Tests deleting bugs by its id.
	 */
	@Test
	public void testDeleteBugById() {
		aList.addBug("s1", "r1");
		aList.addBug("s2", "r2");
		aList.addBug("s3", "r3");
		aList.deleteBugById(1);
		assertNull(aList.getBugById(1));
	}

	/**
	 * Tests getting bugs by its id.
	 */
	@Test
	public void testGetBugById() {
		aList.addBug("s1", "r1");
		aList.addBug("s2", "r2");
		aList.addBug("s3", "r3");
		assertEquals(0, aList.getBugs().get(0).getBugId());
		assertEquals(1, aList.getBugs().get(1).getBugId());
		assertEquals(2, aList.getBugs().get(2).getBugId());
		assertEquals(0, aList.getBugById(0).getBugId());
		assertEquals(1, aList.getBugById(1).getBugId());
		assertEquals(2, aList.getBugById(2).getBugId());
		assertNull(aList.getBugById(4));
	}

	/**
	 * Tests getting bugs by its owner.
	 */
	@Test
	public void testGetBugsByOwner() {
		aList.addBug("s1", "r1");
		aList.addBug("s2", "r2");
		aList.addBug("s3", "r3");
		Command c = new Command(CommandValue.CONFIRM, null, null, null);
		aList.getBugById(0).update(c);
		c = new Command(CommandValue.POSSESSION, "collin", null, null);
		aList.getBugById(0).update(c);
		assertEquals("collin", aList.getBugById(0).getOwner());
		List<TrackedBug> res = new ArrayList<TrackedBug>();
		res = aList.getBugsByOwner("collin");
		assertEquals(1, res.size());
		assertEquals("collin", res.get(0).getOwner());
	}

}
