package edu.ncsu.csc216.flix_2.customer;

/**
 * Maintains a list of customers.
 *
 * @author Collin Marks
 *
 */
public class CustomerDB {
	/** Constant for the maximum size of a customer database */
	public static final int MAX_SIZE = 20;
	private int size;
	private Customer[] list;

	/**
	 * Initializes a list of type customer. There will be a maximum of 20
	 * customers in the database at a time
	 */
	public CustomerDB() {
		list = new Customer[MAX_SIZE];
	}

	/**
	 * Will verify that the customer exists in the data base
	 *
	 * @param id
	 *            id of the customer to be verfied
	 * @param password
	 *            password of the customer to be verified
	 * @return the verified customer
	 */
	public Customer verifyCustomer(String id, String password) {
		if (id != null && password != null) {
			for (Customer c : list) {
				if (c != null && id.equalsIgnoreCase(c.getId()) && c.verifyPassword(password)) {
					return c;
				}
			}
			throw new IllegalArgumentException("The account doesn't exist.");
		} else {
			throw new IllegalArgumentException("The account doesn't exist.");
		}
	}

	/**
	 * A String representation of all customers in the database
	 *
	 * @return the string representation of all customers in the database.
	 */
	public String listAccounts() {
		String result = "";
		for (Customer c : list) {
			if (c != null) {
				result += c.getId() + "\n";
			}
		}
		return result;
	}

	/**
	 * Will add a new customer into the database given that space is available
	 *
	 * @param id
	 *            the id of the new customer
	 * @param password
	 *            the password of the new customer
	 * @param maxMovies
	 *            the maximum movies of the new customer
	 */
	public void addNewCustomer(String id, String password, int maxMovies) {
		if (!containsWhiteSpace(id) && !containsWhiteSpace(password) && !id.isEmpty() && !password.isEmpty()
				&& !customerExistsInList(id)) {
			if (size < MAX_SIZE) {
				list[size] = new Customer(id, password, maxMovies);
				size++;
				sort(list);
			} else {
				throw new IllegalStateException("Customer Database too large!");
			}
		} else {
			throw new IllegalArgumentException("Invalid Customer!");
		}

	}

	/**
	 * Will remove a Customer from the database
	 *
	 * @param id
	 *            the id of the customer to be removed
	 */
	public void cancelAccount(String id) {
		if (customerExistsInList(id)) {
			list = removeElement(list, id);
			sort(list);
		} else {
			throw new IllegalArgumentException();
		}
	}

	/**
	 * Private method to confirm whether or not a string contains whitespace.
	 *
	 * @param testCode
	 *            the string to test
	 * @return whether or not the string contains whitespace
	 */
	private static boolean containsWhiteSpace(final String testCode) {
		if (testCode != null) {
			for (int i = 0; i < testCode.length(); i++) {
				if (Character.isWhitespace(testCode.charAt(i))) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Will tell if a customer exists in the database or not.
	 *
	 * @param id
	 *            the id of the customer to test
	 * @return if the customer is in the database or not
	 */
	private boolean customerExistsInList(String id) {
		for (Customer c : list) {
			if (c != null && id.equalsIgnoreCase(c.getId())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Will remove a customer by its id in the list.
	 *
	 * @param list
	 *            the list in which a customer is being removed
	 * @param id
	 *            the id of the customer being removed
	 * @return the updated list with the customer removed
	 */
	private Customer[] removeElement(Customer[] list, String id) {
		for (int i = 0; i < list.length; i++) {
			if (list[i].getId() == id) {
				Customer[] copy = new Customer[list.length - 1];
				System.arraycopy(list, 0, copy, 0, i);
				System.arraycopy(list, i + 1, copy, i, list.length - i - 1);
				return copy;
			}
		}
		return list;
	}

	/**
	 * Will sort the list alphabetically by user id.
	 *
	 * @param c
	 *            the list to sort
	 */
	private void sort(Customer[] c) {
		Customer swap;
		for (int i = 0; i < c.length - 1; i++) {
			for (int j = 0; j < c.length - 1 - i; j++) {
				if (list[j] != null && list[j + 1] != null
						&& list[j].getId().compareToIgnoreCase(list[j + 1].getId()) > 0) {
					swap = list[j];
					list[j] = list[j + 1];
					list[j + 1] = swap;
				}
			}
		}
	}

}
