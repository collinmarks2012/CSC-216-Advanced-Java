package edu.ncsu.csc216.bug_tracker.bug;

import java.util.ArrayList;

import edu.ncsu.csc216.bug_tracker.command.Command;
import edu.ncsu.csc216.bug_tracker.command.Command.CommandValue;
import edu.ncsu.csc216.bug_tracker.command.Command.Resolution;
import edu.ncsu.csc216.bug_tracker.xml.Bug;
import edu.ncsu.csc216.bug_tracker.xml.NoteList;

/**
 * TrackedBug holds the context class of the Finite State Machine. Each of the 6
 * concrete inner states(Unconfirmed, New, Assigned, Resolved, Closed, Reopen)
 * are implemented in this class.
 *
 * @author Collin Marks
 *
 */
public class TrackedBug {
	private int bugId;
	private BugState state;
	private String summary;
	private String reporter;
	private String owner;
	private int votes;
	private boolean confirmed;
	private ArrayList<String> notes;
	private final BugState unconfirmedState = new UnconfirmedState();
	private final BugState newState = new NewState();
	private final BugState assignedState = new AssignedState();
	private final BugState resolvedState = new ResolvedState();
	private final BugState reopenState = new ReopenState();
	private final BugState closedState = new ClosedState();
	/** Constant for the name of the Unconfirmed State */
	public static final String UNCONFIRMED_NAME = "Unconfirmed";
	/** Constant for the name of the New State */
	public static final String NEW_NAME = "New";
	/** Constant for the name of the Assigned State */
	public static final String ASSIGNED_NAME = "Assigned";
	/** Constant for the name of the Resolved State */
	public static final String RESOLVED_NAME = "Resolved";
	/** Constant for the name of the Reopen State */
	public static final String REOPEN_NAME = "Reopen";
	/** Constant for the name of the Closed State */
	public static final String CLOSED_NAME = "Closed";
	/**
	 * Constant for the amount of votes necessary to move out of the Unconfirmed
	 * state
	 */
	public static final int VOTE_THRESHOLD = 3;
	private static int counter = 0;
	private Resolution reso;

	/**
	 * This Tracked Bug constructor will construct a new Tracked Bug given the
	 * summary and the reporter inputed. If the summary or reporter is
	 * empty/null an appropriate exception is thrown.
	 *
	 * @param summary
	 *            the summary of the new Bug
	 * @param reporter
	 *            the reporter of the new Bug
	 */
	public TrackedBug(String summary, String reporter) {
		if (summary != null & reporter != null && !summary.isEmpty() && !reporter.isEmpty()) {
			this.summary = summary;
			this.reporter = reporter;
			setState(UNCONFIRMED_NAME);
			votes++;
			this.bugId = counter;
			incrementCounter();
			notes = new ArrayList<String>();
		} else {
			throw new IllegalArgumentException();
		}
	}

	/**
	 * This TrackedBug constructor will construct bugs that will be used for
	 * when bugs are loaded via XML files.
	 *
	 * @param bug
	 *            the bug to become a TrackedBug
	 */
	public TrackedBug(Bug bug) {
		if (bug != null && bug.getReporter() != null && bug.getSummary() != null && !bug.getSummary().isEmpty()
				&& !bug.getReporter().isEmpty()) {
			this.bugId = bug.getId();
			setState(bug.getState());
			summary = bug.getSummary();
			reporter = bug.getReporter();
			owner = bug.getOwner();
			votes = bug.getVotes();
			confirmed = bug.isConfirmed();
			incrementCounter();
			notes = new ArrayList<String>();
			setResolution(bug.getResolution());
			// set all fields
			if (bug.getNoteList() != null) {
				for (int i = 0; i < bug.getNoteList().getNote().size(); i++) {
					notes.add(bug.getNoteList().getNote().get(i));
				}
			}
			/*
			 * for (String str : bug.getNoteList().getNote()) { notes.add(str);
			 * }
			 */
		} else {
			throw new IllegalArgumentException();
		}
	}

	/**
	 * The update command will manage communication(by allowing the GUI to pass
	 * Commands to TrackedBug) between the GUI and the private methods of the 6
	 * inner concrete classes.
	 *
	 * @param c
	 *            the command to be updated
	 */
	public void update(Command c) {
		state.updateState(c);
		if (c.getNote() != null && !c.getNote().isEmpty()) {
			String note = c.getNote();
			notes.add(note);
		}
	}

	/**
	 * Will return the XML bug
	 *
	 * @return bug the XML bug
	 */
	public Bug getXMLBug() {
		Bug bug = new Bug();
		bug.setConfirmed(isConfirmed());
		bug.setId(getBugId());
		bug.setOwner(getOwner());
		bug.setReporter(getReporter());
		bug.setResolution(getResolutionString());
		bug.setState(state.getStateName());
		bug.setSummary(getSummary());
		bug.setVotes(getVotes());
		NoteList aaa = new NoteList();
		for (String str : notes) {
			aaa.getNote().add(str);
		}
		bug.setNoteList(aaa);
		return bug;
	}

	/**
	 * Increments the counter to allow TrackedBug to increase the bugID each
	 * time a new Bug is created.
	 */
	public static void incrementCounter() {
		counter++;
	}

	/**
	 * Getter for the notes that have been accumulated across all notes inputted
	 * throughout a bug's life cycle.
	 *
	 * @return the notes
	 */
	public ArrayList<String> getNotes() {
		return notes;
	}

	/**
	 * Setter used by the 6 inner concrete classes to transition from 1 state to
	 * another.
	 *
	 * @param state
	 *            the state to be set
	 */
	private void setState(String str) {
		if (str.equals(UNCONFIRMED_NAME)) {
			state = unconfirmedState;
		} else if (str.equals(NEW_NAME)) {
			state = newState;
		} else if (str.equals(ASSIGNED_NAME)) {
			state = assignedState;
		} else if (str.equals(RESOLVED_NAME)) {
			state = resolvedState;
		} else if (str.equals(CLOSED_NAME)) {
			state = closedState;
		} else if (str.equals(REOPEN_NAME)) {
			state = reopenState;
		} else {
			throw new IllegalArgumentException();
		}
	}

	/**
	 * Setter used by the 6 inner concrete classes to change the resolution of a
	 * bug
	 *
	 * @param r
	 *            the resolution to be set
	 */
	private void setResolution(String r) {
		if (r != null) {
			if (r.equals(Command.R_FIXED)) {
				reso = Command.Resolution.FIXED;
			} else if (r.equals(Command.R_DUPLICATE)) {
				reso = Command.Resolution.DUPLICATE;
			} else if (r.equals(Command.R_WONTFIX)) {
				reso = Command.Resolution.WONTFIX;
			} else if (r.equals(Command.R_WORKSFORME)) {
				reso = Command.Resolution.WORKSFORME;
			} else {
				throw new IllegalArgumentException("This was inputted" + r);
			}
		} else {
			reso = null;
		}
	}

	/**
	 * This is the final state of the Finite State Machine. Bugs in this state
	 * can either go back to the Reopen state or back to the confirmed state
	 * depending on whether or not the bug is confirmed at some path in the life
	 * cycle of the bug.
	 *
	 * @author Collin Marks
	 *
	 */
	public class ClosedState implements BugState {

		private ClosedState() {
		}

		@Override
		public void updateState(Command c) {
			if (c.getCommand() == Command.CommandValue.REOPEN) {
				if (isConfirmed()) {
					setState(REOPEN_NAME);
					reso = null;
				} else {
					setState(UNCONFIRMED_NAME);
					reso = null;
				}
			} else {
				throw new UnsupportedOperationException();
			}

		}

		@Override
		public String getStateName() {
			return CLOSED_NAME;
		}

	}

	/**
	 * The New State is the state in which a bug has no owner but has received
	 * enough votes or has been confirmed in the path of the bug's life-cycle.
	 *
	 * @author Collin Marks
	 *
	 */
	public class NewState implements BugState {

		private NewState() {
		}

		@Override
		public void updateState(Command c) {
			if (c.getCommand() == CommandValue.POSSESSION) {
				if (c.getDeveloperId() != null) {
					owner = c.getDeveloperId();
				} else {
					throw new UnsupportedOperationException();
				}
				if (getOwner() != null && !getOwner().isEmpty()) {
					setState(ASSIGNED_NAME);
				} else {
					throw new UnsupportedOperationException();
				}
			} else {
				throw new UnsupportedOperationException();
			}
		}

		@Override
		public String getStateName() {
			return NEW_NAME;
		}

	}

	/**
	 * The Reopen State is the state in which a bug has been confirmed but has
	 * not been applied a resolution of fixed yet. The bug can move out of this
	 * state if the owner is changed(Assigned), a resolution of fixed is
	 * applied(Resolved), or if the bug is applied a Resolution other than
	 * fixed(Closed).
	 *
	 * @author Collin Marks
	 *
	 */
	public class ReopenState implements BugState {

		private ReopenState() {
		}

		@Override
		public void updateState(Command c) {
			if (c.getCommand() == CommandValue.POSSESSION || c.getCommand() == CommandValue.RESOLVED) {
				if (c.getCommand() == CommandValue.POSSESSION) {
					if (c.getDeveloperId() != null) {
						owner = c.getDeveloperId();
						setState(ASSIGNED_NAME);
					} else {
						throw new IllegalArgumentException("Can't have Possesion Command without an owner!");
					}
				}
				if (c.getCommand() == CommandValue.RESOLVED) {
					if (c.getResolution() == Command.Resolution.FIXED) {
						reso = c.getResolution();
						setState(RESOLVED_NAME);
					} else if (c.getResolution() == Command.Resolution.DUPLICATE
							|| c.getResolution() == Command.Resolution.WONTFIX
							|| c.getResolution() == Command.Resolution.WORKSFORME) {
						setState(CLOSED_NAME);
						reso = c.getResolution();
					} else {
						throw new UnsupportedOperationException();
					}
				}
			} else {
				throw new UnsupportedOperationException();
			}
		}

		@Override
		public String getStateName() {
			return REOPEN_NAME;
		}

	}

	/**
	 * The Resolved State is the state in which a bug has a fixed resolution and
	 * needs to undergo quality assurance. Transitions out of this state can
	 * occur if: (1) The test passes (Closed). (2) The test fails leading to a
	 * reopening of a confirmed bug (Reopen). (3) The test fails leading to a
	 * reopening of a unconfirmed bug (Unconfirmed).
	 *
	 * @author Collin Marks
	 *
	 */
	public class ResolvedState implements BugState {
		// Uses command val is verified
		private ResolvedState() {
		}

		@Override
		public void updateState(Command c) {
			if (c.getCommand() == CommandValue.VERIFIED || c.getCommand() == CommandValue.REOPEN) {
				if (CommandValue.REOPEN == c.getCommand()) {
					if (isConfirmed()) {
						setState(REOPEN_NAME);
						reso = null;
					} else {
						setState(UNCONFIRMED_NAME);
						reso = null;
					}

				}
				if (c.getCommand() == CommandValue.VERIFIED) {
					setState(CLOSED_NAME);
					reso = Command.Resolution.FIXED;
				}
			} else {
				throw new UnsupportedOperationException();
			}
		}

		@Override
		public String getStateName() {
			return RESOLVED_NAME;
		}

	}

	/**
	 * Unconfirmed State is the initial state in which bugs reside. Transition
	 * out of this state can occur in two ways: (1) The Bug has 3 votes or is
	 * confirmed but has no owner (New). (2) The Bug has 3 votes or is confirmed
	 * but does have an owner.
	 *
	 * @author Collin Marks
	 *
	 */
	public class UnconfirmedState implements BugState {

		private UnconfirmedState() {
		}

		@Override
		public void updateState(Command c) {
			if (c.getCommand() == CommandValue.CONFIRM || c.getCommand() == CommandValue.VOTE) {
				if (c.getCommand() == CommandValue.VOTE) {
					votes++;
				}
				if (c.getCommand() == CommandValue.CONFIRM) {
					confirmed = true;
				}
				if (getVotes() >= 3 || isConfirmed()) {
					if (getOwner() == null) {
						setState(NEW_NAME);
					} else {
						setState(ASSIGNED_NAME);
					}
				}
			} else {
				throw new UnsupportedOperationException();
			}
		}

		@Override
		public String getStateName() {
			return UNCONFIRMED_NAME;
		}

	}

	/**
	 * A Bug in the Assigned State has an owner. Transition out of this state
	 * can occur in 2 ways: (1) A Resolution is applied and the resolution if
	 * "fixed" (Resolved) (2) A Resolution is applied other than "fixed"
	 * (Closed)
	 *
	 * @author Collin Marks
	 *
	 */
	public class AssignedState implements BugState {

		private AssignedState() {
		}

		@Override
		public void updateState(Command c) {
			if (c.getCommand() == Command.CommandValue.RESOLVED && c.getResolution() != null) {
				if (c.getResolution() == Resolution.FIXED) {
					setResolution(Command.R_FIXED);
					setState(RESOLVED_NAME);
				} else if (c.getResolution() != null && c.getResolution() != Resolution.FIXED) {
					setState(CLOSED_NAME);
					reso = c.getResolution();
				}
			} else {
				throw new UnsupportedOperationException();
			}
		}

		@Override
		public String getStateName() {
			return ASSIGNED_NAME;
		}

	}

	/**
	 * Getter for Reporter
	 *
	 * @return the reporter
	 */
	public String getReporter() {
		return reporter;
	}

	/**
	 * Getter for Owner
	 *
	 * @return the owner
	 */
	public String getOwner() {
		return owner;
	}

	/**
	 * Getter for the Bug ID number
	 *
	 * @return the bug id
	 */
	public int getBugId() {
		return bugId;
	}

	/**
	 * Getter for the current state of the Bug
	 *
	 * @return the current state of the bug
	 */
	public BugState getState() {
		return state;
	}

	/**
	 * Getter for the summary of the bug
	 *
	 * @return the summary
	 */
	public String getSummary() {
		return summary;
	}

	/**
	 * Getter for the number of votes a bug has
	 *
	 * @return the number of votes
	 */
	public int getVotes() {
		return votes;
	}

	/**
	 * Returns whether or not the bug has been confirmed.
	 *
	 * @return true if confirmed false if it is not confirmed
	 */
	public boolean isConfirmed() {
		return confirmed;
	}

	/**
	 * Getter for the current resolution of the bug
	 *
	 * @return the resolution as a Resolution
	 */
	public Resolution getResolution() {
		return reso;
	}

	/**
	 * Getter for the current resolution of the bug
	 *
	 * @return the resolution as a String
	 */
	public String getResolutionString() {
		if (reso != null) {
			if (reso == Command.Resolution.FIXED) {
				return Command.R_FIXED;
			} else if (reso == Command.Resolution.DUPLICATE) {
				return Command.R_DUPLICATE;
			} else if (reso == Command.Resolution.WONTFIX) {
				return Command.R_WONTFIX;
			} else if (reso == Command.Resolution.WORKSFORME) {
				return Command.R_WORKSFORME;
			} else {
				throw new IllegalArgumentException("Invalid Resolution command");
			}
		}
		return null;
	}

	/**
	 * Getter for the notes that were accumulated throughout the life cycle of
	 * the bug
	 *
	 * @return the notes as a string delimited by "------"
	 */
	public String getNotesString() {
		// Look at String method append if this doesn't work... look up...
		String out = "";
		for (String str : notes) {
			out += str + "\n------\n";
		}
		return out;

	}

	/**
	 * Assesor method used by TrackedBug(Bug) to set the count
	 *
	 * @param n
	 *            the number to set the count at
	 */
	public static void setCounter(int n) {
		counter = n;
	}

}
