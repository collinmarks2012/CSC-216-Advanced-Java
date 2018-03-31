package edu.ncsu.csc216.bug_tracker.tracker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import edu.ncsu.csc216.bug_tracker.command.Command;

/**
 * Tests BugTrackerModel.
 *
 * @author Collin Marks
 *
 */
public class BugTrackerModelTest {

	private BugTrackerModel a;

	/**
	 * Sets up a BugTrackerModel
	 *
	 * @throws Exception
	 *             if exceptions are thrown.
	 */

	@Before
	public void setUp() throws Exception {
		a = BugTrackerModel.getInstance();
		a.createNewBugList();
	}

	/**
	 * Tests adding bugs to the model list.
	 */
	@Test
	public void testAddBugToList() {
		a.addBugToList("summary", "reporter");
		a.addBugToList("second summary", "second reporter");
		assertEquals("summary", a.getBugById(0).getSummary());
		assertEquals("Unconfirmed", a.getBugById(1).getState().getStateName());
	}

	/**
	 * Tests getting the bug list as an array with specified information posted
	 * to the GUI.
	 */
	@Test
	public void testGetBugListAsArray() {
		a.addBugToList("summary", "reporter");
		a.addBugToList("second summary", "second reporter");
		assertEquals("summary", a.getBugById(0).getSummary());
		assertEquals("Unconfirmed", a.getBugById(0).getState().getStateName());
		Object[][] arr = a.getBugListAsArray();
		if (arr != null) {
			assertEquals(0, arr[0][0]);
			assertEquals(1, arr[1][0]);
			assertEquals("Unconfirmed", arr[0][1]);
			assertEquals("Unconfirmed", arr[1][1]);
			assertEquals("summary", arr[0][2]);
			assertEquals("second summary", arr[1][2]);
		} else {
			fail();
		}
		Command c = new Command(Command.CommandValue.VOTE, null, null, null);
		a.executeCommand(1, c);
		a.executeCommand(1, c);
		Command d = new Command(Command.CommandValue.POSSESSION, "collin", null, null);
		a.executeCommand(1, d);
	}

	/**
	 * Tests deleting a bug by an id.
	 */
	@Test
	public void testDeleteBugByID() {
		a.addBugToList("summary", "reporter");
		a.addBugToList("second summary", "second reporter");
		assertEquals(0, a.getBugById(0).getBugId());
		assertEquals(1, a.getBugById(1).getBugId());
		a.deleteBugById(1);
		assertEquals(1, a.getBugListAsArray().length);
	}

	/**
	 * Tests get bugs by owner as an array.
	 */
	@Test
	public void testGetBugListByOwnerAsArray() {
		a.addBugToList("s1", "r1");
		a.addBugToList("s2", "s2");
		a.addBugToList("s3", "r3");
		Command c = new Command(Command.CommandValue.CONFIRM, null, null, null);
		a.getBugById(1).update(c);
		c = new Command(Command.CommandValue.VOTE, null, null, null);
		a.getBugById(2).update(c);
		a.getBugById(2).update(c);
		a.getBugById(0).update(c);
		a.getBugById(0).update(c);
		c = new Command(Command.CommandValue.POSSESSION, "collin", null, null);
		a.getBugById(1).update(c);
		a.getBugById(2).update(c);
		c = new Command(Command.CommandValue.POSSESSION, "casey", null, null);
		a.getBugById(0).update(c);
		Object[][] arr = a.getBugListByOwnerAsArray("collin");
		assertEquals(2, arr.length);
		assertEquals(2, arr[1][0]);
		assertEquals(1, arr[0][0]);
		assertEquals("Assigned", arr[0][1]);
		assertEquals("Assigned", arr[1][1]);
		assertEquals("s2", arr[0][2]);
		assertEquals("s3", arr[1][2]);
		Object[][] nar = a.getBugListByOwnerAsArray("carrie");
		assertEquals(0, nar.length);
		try {
			arr = a.getBugListByOwnerAsArray(null);
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals(2, arr.length);
			assertEquals(2, arr[1][0]);
			assertEquals(1, arr[0][0]);
			assertEquals("Assigned", arr[0][1]);
			assertEquals("Assigned", arr[1][1]);
			assertEquals("s2", arr[0][2]);
			assertEquals("s3", arr[1][2]);
			Object[][] ppp = a.getBugListByOwnerAsArray("carrie");
			assertEquals(0, ppp.length);
		}
	}

	/**
	 * Tests saving a bug to file.
	 */
	@Test
	public void testSaveBugsToFile() {
		a.addBugToList("s1", "r1");
		a.addBugToList("s2", "s2");
		a.addBugToList("s3", "r3");
		Command c = new Command(Command.CommandValue.CONFIRM, null, null, null);
		a.getBugById(1).update(c);
		c = new Command(Command.CommandValue.VOTE, null, null, null);
		a.getBugById(2).update(c);
		a.getBugById(2).update(c);
		a.getBugById(0).update(c);
		a.getBugById(0).update(c);
		c = new Command(Command.CommandValue.POSSESSION, "collin", null, null);
		a.getBugById(1).update(c);
		a.getBugById(2).update(c);
		c = new Command(Command.CommandValue.POSSESSION, "casey", null, null);
		a.getBugById(0).update(c);
		a.saveBugsToFile("XMLBugSaveFileTest");
		assertEquals("casey", a.getBugById(0).getOwner());
	}
}
