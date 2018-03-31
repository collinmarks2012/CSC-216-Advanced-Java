package edu.ncsu.csc216.flix_2.customer;

import edu.ncsu.csc216.flix_2.rental_system.RentalManager;

/**
 * Describes behaviors of a customer management systems that permits user login.
 * The management system has an administrator. Additionally the management
 * system is for a Movie Rental System.
 *
 * @author Jo Perry
 * @author Collin Marks
 */
public class MovieCustomerAccountSystem implements CustomerAccountManager {
	private boolean adminLoggedIn;
	private boolean customerLoggedIn;
	private static final String ADMIN = "Admin";
	private CustomerDB customerList;
	private RentalManager inventorySystem;

	/**
	 * Will construct the rental manager associated with the system.
	 *
	 * @param rm
	 *            the rental manager being constructed.
	 */
	public MovieCustomerAccountSystem(RentalManager rm) {
		inventorySystem = rm;
		customerList = new CustomerDB();
	}

	/**
	 * Logs a user into the system.
	 *
	 * @param username
	 *            id/username of the user
	 * @param password
	 *            user's password
	 * @throws IllegalStateException
	 *             if a customer or the administrator is already logged in
	 * @throws IllegalArgumentException
	 *             if the customer account does not exist
	 */
	@Override
	public void login(String username, String password) {
		if (!isAdminLoggedIn() && !isCustomerLoggedIn()) {
			if (username.equalsIgnoreCase(ADMIN) && password.equalsIgnoreCase(ADMIN)) {
				adminLoggedIn = true;
				customerLoggedIn = false;
				return;
			}
			if (customerList.verifyCustomer(username, password).verifyPassword(password)
					&& customerList.verifyCustomer(username, password) != null) {
				customerLoggedIn = true;
				adminLoggedIn = false;
				if (inventorySystem != null) {
					inventorySystem.setCustomer(customerList.verifyCustomer(username, password));
				}
			} else {
				throw new IllegalArgumentException("The customer does not exist.");
			}
		} else {
			throw new IllegalStateException("Current customer or admin must first log out.");
		}
	}

	/**
	 * Logs the current customer or administrator out of the system.
	 */
	@Override
	public void logout() {
		adminLoggedIn = false;
		customerLoggedIn = false;
	}

	/**
	 * Is an administrator logged into the system?
	 *
	 * @return true if yes, false if no
	 */
	@Override
	public boolean isAdminLoggedIn() {
		return adminLoggedIn;
	}

	/**
	 * Is a customer logged into the system?
	 *
	 * @return true if yes, false if no
	 */
	@Override
	public boolean isCustomerLoggedIn() {
		return customerLoggedIn;
	}

	/**
	 * Add a new customer to the customer database. The administrator must be
	 * logged in.
	 *
	 * @param id
	 *            id/email for new customer
	 * @param password
	 *            new customer's password
	 * @param num
	 *            number associated with this customer
	 * @throws IllegalStateException
	 *             if the database is full or the administrator is not logged in
	 * @throws IllegalArgumentException
	 *             if customer with given id is already in the database
	 */
	@Override
	public void addNewCustomer(String id, String password, int num) {
		if (isAdminLoggedIn() && !isCustomerLoggedIn()) {
			customerList.addNewCustomer(id, password, num);
		} else {
			throw new IllegalStateException("Access denied.");
		}
	}

	/**
	 * Cancel a customer account.
	 *
	 * @param id
	 *            id/username of the customer to cancel
	 * @throws IllegalStateException
	 *             if the administrator is not logged in
	 * @throws IllegalArgumentException
	 *             if no matching account is found
	 */
	@Override
	public void cancelAccount(String id) {
		if (isAdminLoggedIn() && !isCustomerLoggedIn()) {
			customerList.cancelAccount(id);
		} else {
			throw new IllegalStateException("Access denied.");
		}
	}

	/**
	 * List all customer accounts.
	 *
	 * @return string of customer usernames separated by newlines
	 */
	@Override
	public String listAccounts() {
		return customerList.listAccounts();
	}

}
