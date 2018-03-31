package edu.ncsu.csc216.bug_tracker.tracker;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import edu.ncsu.csc216.bug_tracker.bug.TrackedBug;
import edu.ncsu.csc216.bug_tracker.command.Command;
import edu.ncsu.csc216.bug_tracker.xml.Bug;

/**
 * Maintains a list of Tracked Bugs.
 *
 * @author Collin Marks
 *
 */
public class BugList {

	private List<TrackedBug> list;

	/**
	 * Default constructor for a bug list. Initializes the list that will be
	 * used and sets the counter of tracked bug to 0, per the project
	 * requirements.
	 */
	public BugList() {
		list = new ArrayList<TrackedBug>();
		TrackedBug.setCounter(0);
	}

	/**
	 * Adds a bug to the list. The summary and reporter for the bug must be non
	 * empty String's for bug to be added.
	 *
	 * @param summary
	 *            the summary of the bug to be added
	 * @param reporter
	 *            the reporter of the bug to be added
	 * @return the id of the bug that was just added
	 */
	public int addBug(String summary, String reporter) {
		TrackedBug bug = new TrackedBug(summary, reporter);
		list.add(bug);
		return bug.getBugId();
	}

	/**
	 * Adds a list of Bug objects. To be used with XMLBugs. Will transform a Bug
	 * into a Tracked Bug.
	 *
	 * @param lb
	 *            the list of type bug to be added
	 */
	public void addXMLBugs(List<Bug> lb) {
		int max = 0;
		// Finds Maximum id of the current bugs in the bug list
		for (int i = 0; i < list.size(); i++) {
			if (max < list.get(i).getBugId()) {
				max = list.get(i).getBugId();
			}
		}
		TrackedBug.setCounter(max + 1);
		// Goes through the list to be added and transforms a Bug into a Tracked
		// Bug
		for (int i = 0; i < lb.size(); i++) {
			if (lb.get(i) != null) {
				list.add(new TrackedBug(lb.get(i)));
			}
		}
	}

	/**
	 * Getter for the current bug list
	 *
	 * @return the bug list
	 */
	public List<TrackedBug> getBugs() {
		return list;
	}

	/**
	 * Filter method that will return a list of Tracked Bug objects that have
	 * are owned by the owner that is inputed into this method.
	 *
	 * @param name
	 *            the name of the owner to be filtered by
	 * @return the filtered list of Tracked Bugs
	 */
	public List<TrackedBug> getBugsByOwner(String name) {
		List<TrackedBug> sortList = new LinkedList<TrackedBug>();
		if (name == null) {
			throw new IllegalArgumentException();
		}
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getOwner() != null && list.get(i).getOwner().equals(name)) {
				sortList.add(list.get(i));
			}
		}
		return sortList;
	}

	/**
	 * Search method that will return the Tracked Bug object that has the given
	 * Id inputed into the method.
	 *
	 * @param id
	 *            the bug's Identification Number to search for
	 * @return the tracked bug if the id is valid, else it will return null
	 */
	public TrackedBug getBugById(int id) {
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getBugId() == id) {
				return list.get(i);
			}
		}
		return null;
	}

	/**
	 * Executes a command on a bug in the bug list
	 *
	 * @param id
	 *            the bug in which a command is being induced upon
	 * @param c
	 *            the command to occur on the bug of interest
	 */
	public void executeCommand(int id, Command c) {
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i) != null && list.get(i).getBugId() == id) {
				list.get(i).update(c);
			}
		}
	}

	/**
	 * Deletes a bug in the bug list
	 * 
	 * @param id
	 *            the Identification Number of the bug to be deleted in the
	 *            list.
	 */
	public void deleteBugById(int id) {
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getBugId() == id) {
				list.remove(i);
			}
		}
	}
}
