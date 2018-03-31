package edu.ncsu.csc216.flix_2.list_util;

/**
 * A Generic list class that will support underlying list operations for the
 * necessary inventories.
 *
 * @author Collin Marks
 *
 * @param <T>
 *            the type of elements in the list
 */
public class MultiPurposeList<T> {
	Node head;
	Node iterator;

	/**
	 * Constructs an empty list
	 */
	public MultiPurposeList() {
		head = null;
		iterator = null;
	}

	/**
	 * Will add an item of Type T (generic) at the given position.
	 *
	 * @param psn
	 *            the position
	 * @param type
	 *            the type of the item
	 */
	public void addItem(int psn, T type) {
		Node traveler = head;
		if (head == null || psn <= 0) {
			head = new Node(type, head);
		} else {
			for (int i = 0; i < psn - 1 && traveler.link != null; i++) {
				traveler = traveler.link;
			}
			if (traveler != null) {
				traveler.link = new Node(type, traveler.link);
			}
		}
	}

	/**
	 * Will return true whenever the iterator points to a non-Null element in
	 * the list
	 *
	 * @return whether there is another element
	 */
	public boolean hasNext() {
		return iterator != null;
	}

	/**
	 * Will return whether or not the list is currently empty/
	 *
	 * @return whether the list is empty or not
	 */
	public boolean isEmpty() {
		return head == null;
	}

	/**
	 * Adds an element to the end of the list.
	 *
	 * @param type
	 *            the type of data to add to the rear of the list
	 */
	public void addToRear(T type) {
		if (head == null) {
			head = new Node(type, null);
		} else {
			Node pointer = head;
			while (pointer.link != null) {
				pointer = pointer.link;
			}
			pointer.link = new Node(type, null);
		}
	}

	/**
	 * Moves the element at the given position ahead one position in the list.
	 *
	 * @param psn
	 *            the position (index) in which that element is being moved
	 *            ahead one
	 */
	public void moveAheadOne(int psn) {
		if (psn > 0 && psn <= size()) {
			Node first = head;
			Node second = head.link;
			if (psn < size() && psn != 1) {
				for (int i = 0; i <= psn && second.link != null; i++) {
					first = second;
					second = second.link;
				}
				if (second != null) {
					T swap = first.data;
					first.data = second.data;
					second.data = swap;
				}
			} else {
				T swap = first.data;
				first.data = second.data;
				second.data = swap;
			}
		}

	}

	/**
	 * Returns the size of the list
	 *
	 * @return the size of the list
	 */
	public int size() {
		int size = 0;
		Node current = head;
		while (current != null) {
			size++;
			current = current.link;
		}
		return size;
	}

	/**
	 * Will return the element that iterator is pointing to and will movie the
	 * iterator such that it will point to the next element in the list.
	 *
	 * @return the next element in the list
	 */
	public T next() {
		if (iterator == null) {
			return null;
		}
		T info = iterator.data;
		iterator = iterator.link;
		return info;
	}

	/**
	 * Resets the iterator back to the beginning of the list.
	 */
	public void resetIterator() {
		iterator = head;
		// this is all I need hear correct???? I think that this is causing me
		// some problems...
	}

	/**
	 * Removes a list item at the given position
	 *
	 * @param psn
	 *            the position in which to remove the element.
	 * @return the removed element
	 */
	public T remove(int psn) {
		Node current = head;
		Node previous = null;
		while (current != null && psn > 0) {
			previous = current;
			current = current.link;
			psn--;
		}
		if (current != null) {
			if (current == head) {
				head = head.link;
			} else {
				previous.link = current.link;
			}
			return current.data;
		}
		return null; // psn out of range
	}

	/**
	 * Contains two data members head which will always point to the first
	 * element in the list. Additionally iterator will traverse through the list
	 * of elements. Iterator will always point to the next element to be
	 * visited.
	 *
	 * @author Collin Marks
	 *
	 */
	public class Node {
		/**
		 * Data for the Node constructor
		 */
		public T data;
		Node link;

		/**
		 * Simple node constructor that will set the data of type T and the link
		 * that will point to the next element.
		 *
		 * @param data
		 *            the type of the elements in the list
		 * @param link
		 *            the Node that points to the next element in the list
		 */
		public Node(T data, Node link) {
			this.data = data;
			this.link = link;
		}
	}

	/**
	 * Looks at the object at the given position. It will return that object.
	 *
	 * @param psn
	 *            the position in which to look for
	 * @return the object at that position
	 */
	public T lookAtItemN(int psn) {
		Node pointer = head;
		if (psn < 0 || psn > size()) {
			return null;
		}
		for (int i = 0; i < psn && pointer.link != null; i++) {
			pointer = pointer.link;
		}
		if (pointer != null && pointer.data != null) {
			return pointer.data;
		} else {
			throw new IllegalArgumentException("NULL!");
		}
	}
}