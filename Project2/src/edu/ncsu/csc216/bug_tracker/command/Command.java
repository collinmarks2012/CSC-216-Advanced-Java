package edu.ncsu.csc216.bug_tracker.command;

/**
 * The Command class handles all user actions that the BugTrackerGUI will use to
 * indicate what will happen with a given bug.
 *
 * @author Collin Marks
 *
 */
public class Command {
	/** Constant for the name of the Fixed Resolution */
	public static final String R_FIXED = "Fixed";
	/** Constant for the name of the Duplicate Resolution */
	public static final String R_DUPLICATE = "Duplicate";
	/** Constant for the name of the WontFix Resolution */
	public static final String R_WONTFIX = "WontFix";
	/** Constant for the name of the WorksForMe Resolution */
	public static final String R_WORKSFORME = "WorksForMe";
	private String developerId;
	private String note;

	/**
	 * CommandValue holds the constants for Commands that can induce a
	 * transition in the Finite State Machine
	 *
	 */
	public static enum CommandValue {
		/**
		 * Vote Command
		 */
		VOTE,

		/**
		 * Possesion Command
		 */
		POSSESSION,

		/**
		 * Resolved Command
		 */
		RESOLVED,

		/**
		 * Verified Command
		 */
		VERIFIED,

		/**
		 * Reopen Command
		 */
		REOPEN,

		/**
		 * Confirm Command
		 */
		CONFIRM
	};

	/**
	 * Resolution holds the constant for Resolutions that a Resolved bug can
	 * have
	 *
	 */
	public static enum Resolution {
		/**
		 * Fixed Resolution
		 */
		FIXED,

		/**
		 * Duplicate Resolution
		 */
		DUPLICATE,

		/**
		 * Won't Fix Resolution
		 */
		WONTFIX,

		/**
		 * Works For Me Resolution
		 */
		WORKSFORME
	};

	private CommandValue comVal;
	private Resolution resol;

	/**
	 * The Command constructor is what will force an action to occur on a bug.
	 *
	 * @param c
	 *            the Command Value being passed in
	 * @param developerId
	 *            the one who is issuing the action
	 * @param reso
	 *            the Resolution being passed in
	 * @param note
	 *            the note included with the action
	 */
	public Command(CommandValue c, String developerId, Resolution reso, String note) {
		if ((c == null) || (c == CommandValue.RESOLVED && reso == null)
				|| (c == CommandValue.POSSESSION && developerId == null)
				|| (c == CommandValue.POSSESSION && developerId.equals(""))) {
			throw new IllegalArgumentException();
		} else {
			this.developerId = developerId;
			this.note = note;
			comVal = c;
			this.resol = reso;
		}
	}

	/**
	 * Getter for the developer ID.
	 *
	 * @return the developerID
	 */
	public String getDeveloperId() {
		return developerId;
	}

	/**
	 * Getter for the note.
	 *
	 * @return the note
	 */
	public String getNote() {
		return note;
	}

	/**
	 * Getter for the resolution.
	 *
	 * @return the resolution
	 */
	public Resolution getResolution() {
		return resol;
	}

	/**
	 * Getter for the command value being passed in.
	 *
	 * @return the command
	 */
	public CommandValue getCommand() {
		return comVal;
	}

}
