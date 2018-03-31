package edu.ncsu.csc216.flix_2.inventory;

import java.util.Scanner;

/**
 * Handles all operations and behavior nessesary for a Movie with a stock and a
 * name to function properly.
 *
 * @author Collin Marks
 *
 */
public class Movie {
	private String name;
	private int inStock;

	/**
	 * Constructs a Movie from a String of the format
	 * <number-in-stock><whitespace><movie-title>
	 *
	 * @param entryLine
	 *            the String to parse for the stock and the name of the movie
	 */
	public Movie(String entryLine) {
		Scanner scan = new Scanner(entryLine);
		int stock;
		String title;
		try {
			stock = Integer.parseInt(scan.next());
			title = scan.nextLine().trim();
		} catch (NumberFormatException e) {
			scan.close();
			throw new NumberFormatException();
		}
		this.inStock = stock;
		this.name = title;
		scan.close();
	}

	/**
	 * Getter method for the name of the movie
	 *
	 * @return The name of the movie
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Returns just the movie title if the movie is in stock. If the movie is
	 * not in stock it will display the Movie plus "(currently unavailable").
	 *
	 * @return the display name
	 */
	public String getDisplayName() {
		if (!this.isAvailable()) {
			return this.getName() + " (currently unavailable)";
		} else {
			return this.getName();
		}
	}

	/**
	 * Will return true if there are "Movies" still left in stock
	 *
	 * @return true if movies are still available
	 */
	public boolean isAvailable() {
		return (inStock > 0);
	}

	/**
	 * Places a movie back in the inventory, increasing the stock of that movie
	 */
	public void backToInventory() {
		inStock++;
	}

	/**
	 * Removes a movie from the inventory stock. If that movie is not available
	 * then an Exception is thrown.
	 */
	public void removeOneCopyFromInventory() {
		if (isAvailable()) {
			inStock--;
		} else {
			throw new IllegalStateException("No copy of this movie currently available.");
		}
	}

	/**
	 * Compares two movies to each other for sorting purposes
	 *
	 * @param m
	 *            the movie to sort
	 * @return an int that will be used for sorting
	 */
	public int compareToByName(Movie m) {
		Scanner scanOther = new Scanner(m.getName());
		Scanner scanThis = new Scanner(this.getName());
		String firstThis = scanThis.next();
		String firstOther = scanOther.next();
		String movieName = name;
		String passedInName = m.getName();
		if (firstThis.equalsIgnoreCase("a") || firstThis.equalsIgnoreCase("the") || firstThis.equalsIgnoreCase("an")) {
			movieName = scanThis.nextLine().trim();
		}
		if (firstOther.equalsIgnoreCase("a") || firstOther.equalsIgnoreCase("the")
				|| firstOther.equalsIgnoreCase("an")) {
			passedInName = scanOther.nextLine().trim();
		}
		scanOther.close();
		scanThis.close();
		return movieName.compareToIgnoreCase(passedInName);
	}

}