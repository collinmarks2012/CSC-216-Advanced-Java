package edu.ncsu.csc216.flix_2.rental_system;

import edu.ncsu.csc216.flix_2.customer.Customer;
import edu.ncsu.csc216.flix_2.inventory.MovieDB;

/**
 * Interface for a rental system where the items for rent are stored in an
 * inventory and where there are different customers. Items can be reserved,
 * checked out for home, and returned to the inventory. Items in the the
 * inventory, reserves, and at home can be located by position.
 *
 * @author Jo Perry
 * @author Collin Marks
 */
public class DVDRentalSystem implements RentalManager {
	private MovieDB inventory;
	private Customer currentCustomer;

	/**
	 * Constructs a inventory of items (movies) from a file.
	 *
	 * @param fileName
	 *            the file name of the listing of movies
	 */
	public DVDRentalSystem(String fileName) {
		inventory = new MovieDB(fileName);
	}

	/**
	 * Traverse all items in the inventory.
	 *
	 * @return the string representing the items in the inventory
	 */
	@Override
	public String showInventory() {
		return inventory.traverse();
	}

	/**
	 * Set the customer for the current context to a given value.
	 *
	 * @param c
	 *            the new current customer
	 */
	@Override
	public void setCustomer(Customer c) {
		currentCustomer = c;
		if (c != null) {
			c.login();
		}
	}

	/**
	 * Reserve the selected item for the reserve queue.
	 *
	 * @param position
	 *            position of the selected item in the inventory
	 * @throws IllegalStateException
	 *             if no customer is logged in
	 * @throws IllegalArgumentException
	 *             if position is out of bounds
	 */
	@Override
	public void addToCustomerQueue(int position) {
		if (currentCustomer != null) {
			currentCustomer.reserve(inventory.findItemAt(position));
		} else {
			throw new IllegalStateException("No customer is logged in.");
		}
	}

	/**
	 * Move the item in the given position up 1 in the reserve queue.
	 *
	 * @param position
	 *            current position of item to move up one
	 * @throws IllegalStateException
	 *             if no customer is logged in
	 */
	@Override
	public void reserveMoveAheadOne(int position) {
		if (currentCustomer != null) {
			currentCustomer.moveAheadOneInReserves(position);
		} else {
			throw new IllegalStateException("No customer is logged in.");
		}
	}

	/**
	 * Remove the item in the given position from the reserve queue.
	 *
	 * @param position
	 *            position of the item in the queue
	 * @throws IllegalStateException
	 *             if no customer is logged in
	 * @throws IllegalArgumentException
	 *             if position is out of bounds
	 */
	@Override
	public void removeSelectedFromReserves(int position) {
		if (currentCustomer != null) {
			currentCustomer.unReserve(position);
		} else {
			throw new IllegalStateException("No customer is logged in.");
		}
	}

	/**
	 * Traverse all items in the reserve queue.
	 *
	 * @return string representation of items in the queue
	 * @throws IllegalStateException
	 *             if no customer is logged in
	 */
	@Override
	public String traverseReserveQueue() {
		if (currentCustomer != null) {
			return currentCustomer.traverseReserveQueue();
		} else {
			throw new IllegalStateException("No customer is logged in.");
		}

	}

	/**
	 * Traverse all items in the reserve queue.
	 *
	 * @return string representation of items at home
	 * @throws IllegalStateException
	 *             if no customer is logged in
	 */
	@Override
	public String traverseAtHomeQueue() {
		if (currentCustomer != null) {
			return currentCustomer.traverseAtHomeQueue();
		} else {
			throw new IllegalStateException("No customer is logged in.");
		}
	}

	/**
	 * Return the selected item to the inventory.
	 *
	 * @param position
	 *            location in the list of items at home of the item to return
	 * @throws IllegalStateException
	 *             if no customer is logged in
	 * @throws IllegalArgumentException
	 *             if position is out of bounds
	 */
	@Override
	public void returnItemToInventory(int position) {
		if (currentCustomer != null) {
			currentCustomer.returnDVD(position);
			currentCustomer.login();
		} else {
			throw new IllegalStateException("No customer is logged in.");
		}
	}

}
