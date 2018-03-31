package edu.ncsu.csc216.flix_2.customer;

import edu.ncsu.csc216.flix_2.inventory.Movie;
import edu.ncsu.csc216.flix_2.list_util.MultiPurposeList;

/**
 * Maintains all nessesary information about a Customer seeking to reserve and
 * return DVD's
 *
 * @author Collin Marks
 *
 */
public class Customer {
	private String id;
	private String password;
	private int maxAtHome;
	private int nowAtHome;
	private MultiPurposeList<Movie> atHomeQueue;
	private MultiPurposeList<Movie> reserveQueue;

	/**
	 * Constructs a Customer based on id, password, and the maximum number of
	 * movies the customer is allowed at home.
	 *
	 * @param id
	 *            the username
	 * @param password
	 *            the password
	 * @param maxAtHome
	 *            the maximum number of movies at home
	 */
	public Customer(String id, String password, int maxAtHome) {
		if (id != null && password != null && id.length() != 0 && password.length() != 0 && !id.trim().isEmpty()
				&& !password.trim().isEmpty()) {
			id = id.trim();
			password = password.trim();
			this.id = id;
			this.password = password;
			if (maxAtHome > 0 && maxAtHome <= 5) {
				this.maxAtHome = maxAtHome;
			} else if (maxAtHome > 5) {
				this.maxAtHome = 5;
			} else {
				this.maxAtHome = 0;
			}
			nowAtHome = 0;
			atHomeQueue = new MultiPurposeList<Movie>();
			reserveQueue = new MultiPurposeList<Movie>();
		} else {
			throw new IllegalArgumentException("Invalid Customer");
		}
	}

	/**
	 * Verifies that the password parameter matches the password of the customer
	 *
	 * @param pw
	 *            the password to test
	 * @return whether or not the pass word is verified
	 */
	public boolean verifyPassword(String pw) {
		return this.password.equals(pw.trim());
	}

	/**
	 * Returns the ID of the customer
	 *
	 * @return the id of the customer
	 */
	public String getId() {
		return id.trim();
	}

	/**
	 * Compare a customers name to this customers id for sorting purposes
	 *
	 * @param c
	 *            the customer to test by
	 * @return the comparing integer
	 */
	public int compareToByName(Customer c) {
		return this.getId().compareToIgnoreCase(c.getId());
	}

	/**
	 * Logs in a customer
	 */
	public void login() {
		int i = 0;
		while (nowAtHome < maxAtHome && reserveQueue.size() > 0 && i <= reserveQueue.size()) {
			if (reserveQueue.lookAtItemN(i).isAvailable() && atHomeQueue.size() < this.maxAtHome) {
				atHomeQueue.addToRear(reserveQueue.lookAtItemN(i));
				unReserve(i);
				atHomeQueue.resetIterator();
				reserveQueue.resetIterator();
			}
			i++;
		}
	}

	/**
	 * Returns a string representation of the reserve queue
	 *
	 * @return a string representation of the reserve queue
	 */
	public String traverseReserveQueue() {
		return traverseQueue(reserveQueue);
	}

	/**
	 * Returns a string representation of the at home queue
	 *
	 * @return a string representation of the at home queue
	 */
	public String traverseAtHomeQueue() {
		return traverseQueue(atHomeQueue);
	}

	/**
	 * Closes the customers account.
	 */
	public void closeAccount() {
		if (atHomeQueue != null && atHomeQueue.size() > 0) {
			for (int i = 0; i <= atHomeQueue.size() + 1; i++) {
				if (atHomeQueue != null && atHomeQueue.size() > 0 && atHomeQueue.lookAtItemN(0) != null) {
					atHomeQueue.lookAtItemN(0).backToInventory();
					atHomeQueue.remove(0);
					atHomeQueue.resetIterator();
					nowAtHome--;
				}
			}
		}
	}

	/**
	 * Returns a DVD from the at home inventory
	 *
	 * @param n
	 *            the index of the DVD to return
	 */
	public void returnDVD(int n) {
		if (n <= atHomeQueue.size() && n >= 0 && atHomeQueue.lookAtItemN(n) != null) {
			atHomeQueue.lookAtItemN(n).backToInventory();
			atHomeQueue.remove(n);
			atHomeQueue.resetIterator();
			nowAtHome--;
			login();
		} else {
			throw new IllegalArgumentException("Index Out Of Bounds!");
		}
	}

	/**
	 * Moves a movie at integer n ahead one by swapping with the movie above it
	 *
	 * @param n
	 *            the index of the movie to move ahead one in the list
	 */
	public void moveAheadOneInReserves(int n) {
		if (reserveQueue != null && n < reserveQueue.size()) {
			reserveQueue.moveAheadOne(n);
		} else {
			throw new IllegalArgumentException("Reserve size is less than n");
		}
	}

	/**
	 * Removes a movie from the reserves list
	 *
	 * @param n
	 *            the index of the movie to remove from the reserve list
	 */
	public void unReserve(int n) {
		if (reserveQueue != null) {
			if (n > reserveQueue.size() || n < 0) {
				throw new IllegalArgumentException("No movie selected.");
			} else {
				reserveQueue.remove(n);
			}
		}
	}

	/**
	 * Adds a movie to the reserves list
	 *
	 * @param m
	 *            the movie to add to the list
	 */
	public void reserve(Movie m) {
		if (nowAtHome < maxAtHome) {
			if (m != null) {
				if (m.isAvailable()) {
					atHomeQueue.addToRear(m);
					m.removeOneCopyFromInventory();
					nowAtHome++;
				} else {
					reserveQueue.addToRear(m);
				}
			} else {
				throw new IllegalArgumentException("Movie not specified.");
			}
		} else {
			if (m != null) {
				reserveQueue.addToRear(m);
			} else {
				throw new IllegalArgumentException("Movie not specified.");
			}
		}

	}

	/**
	 * Will return a string representation of movie objects in a given list
	 *
	 * @param list
	 *            the list to iterate through
	 * @return a string representation of movie objects in a given list
	 */
	private String traverseQueue(MultiPurposeList<Movie> list) {
		String result = "";
		if (list != null) {
			if (list.size() == 0) {
				return "";
			}

			for (int i = 0; i < list.size(); i++) {
				result += list.lookAtItemN(i).getName() + "\n";
			}
			return result;
		} else {
			return "";
		}
	}
}