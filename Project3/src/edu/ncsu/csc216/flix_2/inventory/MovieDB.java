package edu.ncsu.csc216.flix_2.inventory;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import edu.ncsu.csc216.flix_2.list_util.MultiPurposeList;

/**
 * Represents an internal database of movies.
 *
 * @author Collin Marks
 *
 */
public class MovieDB {
	private MultiPurposeList<Movie> movies = new MultiPurposeList<Movie>();
	private Scanner scan;

	/**
	 * Constructs a database of movie from a given file name
	 *
	 * @param fileName
	 *            the name of the file of movies
	 */
	public MovieDB(String fileName) {
		try {
			readFromFile(fileName);
		} catch (FileNotFoundException e) {
			throw new IllegalArgumentException(e.getMessage());
		}
		while (scan.hasNextLine()) {
			Movie m = new Movie(scan.nextLine());
			insertInOrder(m);
		}
	}

	/**
	 * Traverses all movies
	 *
	 * @return a string representation of all movies
	 */
	public String traverse() {
		String result = "";
		for (int i = 0; i < movies.size(); i++) {
			result += movies.lookAtItemN(i).getDisplayName() + "\n";
		}
		return result;
	}

	/**
	 * Getter for a movie in the database.
	 *
	 * @param n
	 *            the index to retrieve a given movie
	 * @return the movie at the given index
	 */
	public Movie findItemAt(int n) {
		if (n < 0 || n >= movies.size()) {
			throw new IllegalArgumentException();
		} else {
			return movies.lookAtItemN(n);
		}
	}

	/**
	 * Reads a file
	 *
	 * @param fileName
	 *            the name of the file to be read
	 * @throws FileNotFoundException
	 *             if the file cannot be read properly
	 */
	private void readFromFile(String fileName) throws FileNotFoundException {
		try {
			scan = new Scanner(new File(fileName));
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException(e.getMessage());
		}
	}

	/**
	 * Inserts a movie in the right order -- "Alphanumerically".
	 * 
	 * @param m
	 *            the movie to insert properly in order
	 */
	private void insertInOrder(Movie m) {
		int pos = 0;
		movies.resetIterator();
		while (movies.hasNext()) {
			if (m.getName().compareTo(movies.next().getName()) > 0) {
				pos++;
			}
		}
		movies.addItem(pos, m);
	}
}
