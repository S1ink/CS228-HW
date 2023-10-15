package edu.iastate.cs228.hw3;

import java.util.AbstractSequentialList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;


/**
 * @author Sam Richter
 */

/**
 * Implementation of the list interface based on linked nodes
 * that store multiple items per node.  Rules for adding and removing
 * elements ensure that each node (except possibly the last one)
 * is at least half full.
 */
public class StoutList<E extends Comparable<? super E>> extends AbstractSequentialList<E> {

	/** Default number of elements that may be stored in each node. */
	private static final int DEFAULT_NODESIZE = 4;

	/** Number of elements that can be stored in each node. */
	private final int node_max_elems;
	/**
	 * Dummy node for head and tail. It should be private but set to public here only 
	 * for grading purpose. In practice, you should always make the head of a 
	 * linked list a private instance variable.
	 */
	public Node head, tail;
	/** Number of elements in the list. */
	private int size = 0;



	private class ElemIndex {
		public Node node;
		public int idx;

		public E val() { return node.get(idx); }
	}
	private ElemIndex fromAbsolute(int absolute)
		{ return fromAbsolute(absolute, null); }
	private ElemIndex fromAbsolute(int absolute, ElemIndex buff) {
		// static bindings for head/tail?
		if(absolute >= 0) {
			Node n = this.head.next;
			while(n != null) {
				if(absolute < n.size || n == this.tail) {
					if(buff == null) { buff = new ElemIndex(); }
					buff.node = n;
					buff.idx = absolute;	// ensure correct
					return buff;
				}
				absolute -= n.size;
				n = n.next;
			}
		}
		return null;
	}
	private int toAbsolute(ElemIndex ei) {
		// static bindings for head/tail?
		int i = 0;
		Node n = this.head.next;
		while(n != null && n != this.tail && n != ei.node) {
			i += n.size;
			n = n.next;
		}
		if(n == ei.node) {
			return i + ei.idx;
		} else {
			return -1;	// input index not valid
		}
	}

	private void link(Node a, Node b) {
		a.next = b;
		b.prev = a;
	}
	private void linkS(Node a, Node b) {	// link w/ "safetys"
		if(a != null) a.next = b;
		if(b != null) b.prev = a;
	}
	private Node insertN(Node pre) {
		return this.insertN(pre, new Node());
	}
	private Node insertN(Node a, Node n) {
		final Node b = a.next;
		this.linkS(a, n);
		this.linkS(n, b);
		return n;
	}
	private Node removeN(Node n) {
		this.linkS(n.prev, n.next);
		return n;
	}


	/**
	 * Constructs an empty list with the default node size.
	 */
	public StoutList() {
		this(DEFAULT_NODESIZE);
	}
	/**
	 * Constructs an empty list with the provided node size.
	 * 
	 * @param node_dims number of elements that may be stored in each node, must be an even number
	 * 
	 * @throws IllegalArgumentException if the element size is less that 0 or not even
	 */
	public StoutList(int node_dims) throws IllegalArgumentException {
		if (node_dims <= 0 || node_dims % 2 != 0) {
			throw new IllegalArgumentException("Node dimensionality is not even!");
		}
		this.node_max_elems = node_dims;

		this.head = new Node(null, 0);
		this.tail = new Node(null, 0);
		this.link(this.head, this.tail);
	}
	/**
	 * Constructor for grading only. Fully implemented.
	 * 
	 * @param head
	 * @param tail
	 * @param node_dims
	 * @param size
	 */
	public StoutList(Node head, Node tail, int node_dims, int size) {
		this.node_max_elems = node_dims;
		this.head = head;
		this.tail = tail;
		this.size = size;
	}



	/**
	 * Get the total number of elements in the list.
	 * 
	 * @return an integer representing the number of elements in the list
	 */
	@Override
	public int size() {
		return this.size;
	}

	/**
	 * Add an element to the end of the list.
	 * 
	 * @param item the element to add
	 * 
	 * @return true if the element was successfully added to the list
	 * @throws ClassCastExcpetion if the element cannot be correctly cast to the internal type
	 * @throws NullPointerException if the element is null
	 * @throws IllegalArgumentException if the element is not valid
	 */
	@Override
	public boolean add(E item) throws NullPointerException, IllegalArgumentException {
		this.add(this.size, item);
		return true;
	}
	/**
	 * Add an element to the list at a specified position.
	 * 
	 * @param pos the index at which to insert the element
	 * @param item the element to add
	 * @throws ClassCastExcpetion if the element cannot be correctly cast to the internal type
	 * @throws NullPointerException if the element is null
	 * @throws IllegalArgumentException if the element is not valid
	 * @throws IndexOutOfBoundsException if the insertion index is not valid
	 */
	@Override
	public void add(int pos, E item) throws NullPointerException, IndexOutOfBoundsException {
		if(item == null)
			{ throw new NullPointerException("Cannot add null element!"); }
		if(pos < 0 || pos > this.size)
			{ throw new IndexOutOfBoundsException("Index is not valid!"); }

		final ElemIndex ei = this.fromAbsolute(pos);
		if(ei.node == this.tail) {
			// adding to end
			final Node n = this.tail.prev;
			if(n != this.head && !n.full()) {
				n.add(item);
				this.size++;
				return;
			}
			// append a new node
			this.insertN(n).add(item);
		} else if(ei.node.full()) {
			// split operation
			final Node
				n = ei.node,
				n2 = this.insertN(n);
			final int m = this.node_max_elems / 2;
			// copy
			for(int i = 0; i < m; i++) {
				n2.data[i] = n.data[m + i];
				n.data[m + i] = null;
			}
			n.size = n2.size = m;
			// item goes to different node at different index depending on ei.idx
			if(ei.idx <= m) {
				n.insert(ei.idx, item);
			} else {
				n2.insert(ei.idx - m, item);
			}
		} else {
			// no node fenagling is necessary
			ei.node.insert(ei.idx, item);
		}
		this.size++;
	}
	/**
	 * Remove an element from the list at the specified position.
	 * 
	 * @param pos the index of the element to remove
	 * 
	 * @return the element that was just removed (possibly null)
	 * @throws IndexOutOfBoundsException if the removal index is not valid
	 */
	@Override
	public E remove(int pos) {
		if(pos < 0 || pos >= this.size)
			{ throw new IndexOutOfBoundsException("Index is not valid!"); }

		final ElemIndex ei = this.fromAbsolute(pos);
		final Node n = ei.node;
		final int m = this.node_max_elems / 2;
		final E ret = n.remove(ei.idx);

		if(n != this.tail && n.size <= m) {
			if(n.next.size > m) {
				n.add(n.next.remove(0));
			} else if(n.next != this.tail) {
				for(int i = 0; i < n.next.size; i++) {
					n.data[n.size + i] = n.next.data[i];
				}
				n.size += n.next.size;
				this.removeN(n.next);
			} else if(n.size <= 0) {
				this.removeN(n);
			}
		}
		this.size--;
		return ret;
	}


	/**
	 * Sort all elements in the stout list in the NON-DECREASING order. You may do the following. 
	 * Traverse the list and copy its elements into an array, deleting every visited node along 
	 * the way.  Then, sort the array by calling the insertionSort() method.  (Note that sorting 
	 * efficiency is not a concern for this project.)  Finally, copy all elements from the array 
	 * back to the stout list, creating new nodes for storage. After sorting, all nodes but 
	 * (possibly) the last one must be full of elements.  
	 *  
	 * Comparator<E> must have been implemented for calling insertionSort().    
	 */
	public void sort() {
		// TODO
	}
	/**
	 * Sort all elements in the stout list in the NON-INCREASING order. Call the bubbleSort()
	 * method.  After sorting, all but (possibly) the last nodes must be filled with elements.  
	 *  
	 * Comparable<? super E> must be implemented for calling bubbleSort(). 
	 */
	public void sortReverse() {
		// TODO
	}


	/**
	 * Get a forward iterator for the list, which starts at the first element.
	 * 
	 * @return an iterator for the list
	 */
	@Override
	public Iterator<E> iterator() {
		// return new StoutListIterator();
		return null;
	}
	/**
	 * Get a bi-directional iterator for the list which starts at the first element.
	 * 
	 * @return a bi-directional iterator for the list
	 */
	@Override
	public ListIterator<E> listIterator() {
		// return new StoutListIterator();
		return null;
	}
	/**
	 * Get a bi-directional iterator for the list, starting at the provided index.
	 * 
	 * @param index the beginning index from which to iterate
	 * 
	 * @return a bi-directional iterator for the list
	 * @throws IndexOutOfBoundsException if the starting index is out or range
	 */
	@Override
	public ListIterator<E> listIterator(int index) throws IndexOutOfBoundsException {
		// return new StoutListIterator(index);
		return null;
	}



	/**
	 * Returns a string representation of this list showing
	 * the internal structure of the nodes.
	 */
	public String toStringInternal() {
		return toStringInternal(null);
	}
	/**
	 * Returns a string representation of this list showing the internal
	 * structure of the nodes and the position of the iterator.
	 *
	 * @param iter - an iterator for this list
	 */
	public String toStringInternal(ListIterator<E> iter) {
		int count = 0;
		int position = -1;
		if (iter != null) {
			position = iter.nextIndex();
		}

		StringBuilder sb = new StringBuilder();
		sb.append('[');
		Node current = head.next;
		while (current != tail) {
			sb.append('(');
			E data = current.data[0];
			if (data == null) {
				sb.append("-");
			} else {
				if (position == count) {
					sb.append("| ");
					position = -1;
				}
				sb.append(data.toString());
				++count;
			}

			for (int i = 1; i < node_max_elems; ++i) {
				sb.append(", ");
				data = current.data[i];
				if (data == null) {
					sb.append("-");
				} else {
					if (position == count) {
						sb.append("| ");
						position = -1;
					}
					sb.append(data.toString());
					++count;

					// iterator at end
					if (position == size && count == size) {
						sb.append(" |");
						position = -1;
					}
				}
			}
			sb.append(')');
			current = current.next;
			if (current != tail) {
				sb.append(", ");
			}
		}
		sb.append("]");
		return sb.toString();
	}





	/**
	 * Node type for this list.  Each node holds a maximum
	 * of nodeSize elements in an array.  Empty slots
	 * are null.
	 */
	private class Node {

		/** The outer instance */
		private final StoutList<E> list = StoutList.this;

		/** * Array of actual data elements. */
		public E[] data;
		/** Links to next and previous nodes. */
		public Node next, prev;

		/** Index of the next available offset in this node, also equal to the number of elements in this node. */
		public int size;


		public Node() {
			this((E[]) new Comparable[node_max_elems], 0);	// Unchecked warning unavoidable.
		}
		public Node(E[] elems, int len) {
			this.data = elems;
			this.size = len;
		}


		/**
		 * 
		 * 
		 * @return
		 */
		public boolean full() {
			return this.size == this.data.length;
		}

		/**
		 * 
		 * 
		 * @param i
		 * @return
		 */
		public E get(int i) {
			return (i >= 0 && i < this.size) ? this.data[i] : null;
		}

		/**
		 * Adds an item to this node at the first available offset.
		 * Precondition: count < nodeSize
		 * 
		 * @param item element to be added
		 */
		public boolean add(E item) {
			if (this.size >= list.node_max_elems) {
				return false;
			}
			this.data[this.size++] = item;
			return true;
			// System.out.println("Added " + item.toString() + " at index " + count + " to node "  + Arrays.toString(data));
		}
		/**
		 * Adds an item to this node at the indicated offset, shifting elements to the right as necessary.
		 * Precondition: count < nodeSize
		 * 
		 * @param offset array index at which to put the new element
		 * @param item element to be added
		 */
		public void insert(int offset, E item) {
			if(offset < 0 || offset >= this.data.length || this.size >= this.data.length) {
				return;
			}
			for (int i = this.size - 1; i >= offset; i--) {
				this.data[i + 1] = this.data[i];
			}
			this.data[offset] = item;
			this.size++;
			// System.out.println("Added " + item.toString() + " at index " + offset + " to node: "  + Arrays.toString(data));
		}
		/**
		 * Deletes an element from this node at the indicated offset, shifting elements left as necessary.
		 * Precondition: 0 <= offset < count
		 * 
		 * @param offset
		 */
		public E remove(int offset) {
			// CHECK BOUNDS
			E item = data[offset];
			for (int i = offset + 1; i < this.size; i++) {
				this.data[i - 1] = this.data[i];
			}
			this.data[this.size - 1] = null;
			this.size--;
			return item;
		}


	}



	/**
	 * 
	 */
	// private class StoutListIterator implements ListIterator<E> {

	// 	/**
	// 	 * Default constructor
	// 	 */
	// 	public StoutListIterator() {}
	// 	/**
	// 	 * Construct an iterator that starts at the given position in the list
	// 	 * 
	// 	 * @param pos - the starting position
	// 	 * @throws IndexOutOfBoundsException if the starting index is beyond the list's range
	// 	 */
	// 	public StoutListIterator(int pos) throws IndexOutOfBoundsException {}



	// 	/**
	// 	 * Returns true if there are more accessible elements in the FORWARD direction.
	// 	 * 
	// 	 * @return true if there are more elements
	// 	 */
	// 	@Override
	// 	public boolean hasNext() {}
	// 	/**
	// 	 * Get the next element in the FORWARD direction.
	// 	 * 
	// 	 * @return the next element
	// 	 * @throws NoSuchElementException if there is no next element in the list
	// 	 */
	// 	@Override
	// 	public E next() throws NoSuchElementException {}
	// 	/**
	// 	 * Returns true if there are more accessible elements in the BACKWARD direction.
	// 	 * 
	// 	 * @return true if there are more elements
	// 	 */
	// 	@Override
	// 	public boolean hasPrevious() {}
	// 	/**
	// 	 * Get the next element in the BACKWARD direction.
	// 	 * 
	// 	 * @return the next element
	// 	 * @throws NoSuchElementException if there is not previous element in the list
	// 	 */
	// 	@Override
	// 	public E previous() throws NoSuchElementException {}
	// 	/**
	// 	 * Get the index of the next element in the FORWARD direction.
	// 	 * 
	// 	 * @return an integer index for the next element
	// 	 */
	// 	@Override
	// 	public int nextIndex() {}
	// 	/**
	// 	 * Get the index of the next element in the BACKWARD direction.
	// 	 * 
	// 	 * @return an integer index for the next element
	// 	 */
	// 	@Override
	// 	public int previousIndex() {}
	// 	/**
	// 	 * Removes the element from the list that was most recently accessed.
	// 	 * 
	// 	 * @throws IllegalStateException if the previous accessed element no longer is valid
	// 	 */
	// 	@Override
	// 	public void remove() throws IllegalStateException {}
	// 	/**
	// 	 * Replaces the element from the list that was most recently accessed.
	// 	 * 
	// 	 * @param e the element for replacing
	// 	 * @throws ClassCastException if the element being added is of the wrong class
	// 	 * @throws IllegalArgumentException if the element being added is invalid
	// 	 * @throws IllegalStateException if the previously accessed element no longer is valid
	// 	 */
	// 	@Override
	// 	public void set(E e) throws ClassCastException, IllegalArgumentException, IllegalStateException {}
	// 	/**
	// 	 * Inserts an element into the list before the current iterator's position.
	// 	 * 
	// 	 * @param e the element to insert
	// 	 * @throws ClassCastException if the element being added is of the wrong class
	// 	 * @throws IllegalArugmentException if the element being added is invalid
	// 	 */
	// 	@Override
	// 	public void add(E e) throws ClassCastException, IllegalArgumentException {}


	// }


	/**
	 * Sort an array arr[] using the insertion sort algorithm in the NON-DECREASING order.
	 * 
	 * @param arr array storing elements from the list
	 * @param comp comparator used in sorting
	 */
	private void insertionSort(E[] arr, Comparator<? super E> comp) {}
	/**
	 * Sort arr[] using the bubble sort algorithm in the NON-INCREASING order. For a 
	 * description of bubble sort please refer to Section 6.1 in the project description. 
	 * You must use the compareTo() method from an implementation of the Comparable 
	 * interface by the class E or ? super E.
	 * 
	 * @param arr array holding elements from the list
	 */
	private void bubbleSort(E[] arr) {}


}