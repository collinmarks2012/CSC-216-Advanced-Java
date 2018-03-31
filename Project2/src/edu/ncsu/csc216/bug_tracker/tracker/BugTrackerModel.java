package edu.ncsu.csc216.bug_tracker.tracker;

import java.util.ArrayList;

import edu.ncsu.csc216.bug_tracker.bug.TrackedBug;
import edu.ncsu.csc216.bug_tracker.command.Command;
import edu.ncsu.csc216.bug_tracker.xml.BugIOException;
import edu.ncsu.csc216.bug_tracker.xml.BugReader;
import edu.ncsu.csc216.bug_tracker.xml.BugWriter;

/**
 * Maintains the Bug List and handles interactions in regards to loading,
 * saving, and creating new Bug Lists.
 *
 * @author Collin
 *
 */
public class BugTrackerModel {
	private static BugTrackerModel model = new BugTrackerModel();
	private BugList bugList;

	private BugTrackerModel() {
	}

	/**
	 * Initializes a new Bug List
	 */
	public void createNewBugList() {
		bugList = new BugList();
	}

	/**
	 * Returns the instance of the BugTrackerModel, insuring that the model is a
	 * singleton.
	 *
	 * @return the instance of the model to return
	 */
	public static BugTrackerModel getInstance() {
		return model;
	}

	/**
	 * Loads Bugs from a file that is inputed into the method
	 *
	 * @param fileName
	 *            the file to be loaded
	 */
	public void loadBugsFromFile(String fileName) {
		BugReader read;
		try {
			if (bugList != null) {
				read = new BugReader(fileName);
				bugList.addXMLBugs(read.getBugs());
			}
		} catch (BugIOException e) {
			throw new IllegalArgumentException();
		}

	}

	/**
	 * Saves Bugs to a file that is inputed into the method
	 *
	 * @param fileName
	 *            the file to be saved to
	 */
	public void saveBugsToFile(String fileName) {
		BugWriter write = new BugWriter(fileName);
		for (int i = 0; i < bugList.getBugs().size(); i++) {
			write.addItem(bugList.getBugs().get(i).getXMLBug());
		}
		new BugWriter(fileName);
	}

	/**
	 * Deletes bugs from the Bug List by the bug Id being inputed into the
	 * method
	 *
	 * @param bugId
	 *            the bugs Identification Number to indicate which bug is to be
	 *            deleted
	 */
	public void deleteBugById(int bugId) {
		if (bugList != null) {
			bugList.deleteBugById(bugId);
		}
	}

	/**
	 * Returns Array of the Bug List with the BugId, the Bug State, and the Bug
	 * Summary.
	 *
	 * @return Array of Bug List containing each of the Bugs: id, state name,
	 *         and summary.
	 */
	public Object[][] getBugListAsArray() {
		if (bugList != null) {
			Object[][] out = new Object[bugList.getBugs().size()][3];
			for (int i = 0; i < (bugList.getBugs().size()); i++) {
				for (int j = 0; j < 2; j++) {
					out[i][0] = bugList.getBugs().get(i).getBugId();
					out[i][1] = bugList.getBugs().get(i).getState().getStateName();
					out[i][2] = bugList.getBugs().get(i).getSummary();
				}
			}
			return out;
		} else {
			return null;
		}
	}

	/**
	 * Returns Array of the Bug List, filtered by a specific owner with the
	 * BugId, the Bug State, and the Bug Summary.
	 *
	 * @param owner
	 *            the owner to filter by
	 * @return array Array of Bug List (filtered by one specific owner)
	 *         containing each of the Bugs: id, state name, and summary.
	 */
	public Object[][] getBugListByOwnerAsArray(String owner) {
		if (owner != null) {
			java.util.List<TrackedBug> sortList = new ArrayList<TrackedBug>();
			sortList = bugList.getBugsByOwner(owner);
			Object[][] out = new Object[sortList.size()][3];
			for (int i = 0; i < sortList.size(); i++) {
				for (int j = 0; j < 2; j++) {
					out[i][0] = sortList.get(i).getBugId();
					out[i][1] = sortList.get(i).getState().getStateName();
					out[i][2] = sortList.get(i).getSummary();
				}
			}
			return out;
		} else {
			throw new IllegalArgumentException();
		}
	}

	/**
	 * Executes a command on a bug based on its ID and the command to be issued.
	 *
	 * @param bugId
	 *            the bug to induce a command on
	 * @param c
	 *            the command to be induced on the bug
	 */
	public void executeCommand(int bugId, Command c) {
		bugList.executeCommand(bugId, c);
	}

	/**
	 * Returns a specific bug based on its id.
	 *
	 * @param bugId
	 *            the bugId to search for
	 * @return the bug of the corresponding ID
	 */
	public TrackedBug getBugById(int bugId) {
		return bugList.getBugById(bugId);
	}

	/**
	 * Adds bugs to the list given the bug to be added has a summary
	 *
	 * @param summary
	 *            the summary of the bug to be added
	 * @param reporter
	 *            the reporter of the bug to be added
	 */
	public void addBugToList(String summary, String reporter) {
		bugList.addBug(summary, reporter);
	}

}
