package edu.iastate.cs228.hw3;

import java.util.NoSuchElementException;
import java.util.AbstractSequentialList;
import java.util.ListIterator;
import java.util.Comparator;
import java.util.Iterator;


/**
 * @author Sam Richter
 */

/**
 * Implementation of the list interface based on linked nodes
 * that store multiple items per node. Rules for adding and removing
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
	 * @param head - the head node
	 * @param tail - the tail node
	 * @param node_dims - the per-node max buffer length
	 * @param size - the size of the external list
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
	 * @param item - the element to add
	 * 
	 * @return true if the element was successfully added to the list
	 * @throws NullPointerException if the element is null
	 * @throws IllegalArgumentException if the element is not valid
	 */
	@Override
	public boolean add(E item) throws NullPointerException {
		try {
			this.add(this.size, item);
		} catch(Exception e) {
			return false;
		}
		return true;
	}
	/**
	 * Add an element to the list at a specified position.
	 * 
	 * @param pos - the index at which to insert the element
	 * @param item - the element to add
	 * @throws NullPointerException if the element is null
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
			final Node prev = this.tail.prev;
			if(prev != this.head && !prev.full()) {
				// add to previous node
				prev.add(item);
			} else {
				// append a new node and add
				this.insertN(prev).add(item);
			}
		} else if(ei.idx == 0 && ei.node.prev != this.head && !ei.node.prev.full()) {
			// put in an available spot on a previous node
			ei.node.prev.add(item);
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
			// no special node fenagling is necessary
			ei.node.insert(ei.idx, item);
		}
		this.size++;
	}
	/**
	 * Remove an element from the list at the specified position.
	 * 
	 * @param pos - the index of the element to remove
	 * 
	 * @return the element that was just removed (possibly null)
	 * @throws IndexOutOfBoundsException if the removal index is not valid
	 */
	@Override
	public E remove(int pos) {
		if(pos < 0 || pos >= this.size) {
			throw new IndexOutOfBoundsException("Index is not valid!");
		}
		final ElemIndex ei = this.fromAbsolute(pos);
		final Node n = ei.node;
		final int m = this.node_max_elems / 2;
		// "remove element and shift as necessary"
		final E ret = n.remove(ei.idx);
		// shifting was alredy done so only need further alterment if size <= M/2
		if(n != this.tail && n.size <= m) {
			if(n.next.size > m) {
				// move back a single successing element
				n.add(n.next.remove(0));
			} else if(n.next != this.tail) {
				// move back all elements from successing node
				for(int i = 0; i < n.next.size; i++) {
					n.data[n.size + i] = n.next.data[i];
				}
				n.size += n.next.size;
				// remove successing node
				this.removeN(n.next);
			} else if(n.size <= 0) {
				// remove node if now empty
				this.removeN(n);
			}
		}
		this.size--;
		return ret;
	}


	/**
	 * Sort all elements in the stout list in the NON-DECREASING order. You may do the following. 
	 * Traverse the list and copy its elements into an array, deleting every visited node along 
	 * the way. Then, sort the array by calling the insertionSort() method (Note that sorting 
	 * efficiency is not a concern for this project.). Finally, copy all elements from the array 
	 * back to the stout list, creating new nodes for storage. After sorting, all nodes but 
	 * (possibly) the last one must be full of elements.
	 * 
	 * Comparator<E> must have been implemented for calling insertionSort().
	 */
	public void sort() {
		E[] arr = this.toArray();
		this.insertionSort(arr, (E a, E b)->a.compareTo(b));
		StoutList<E> l = StoutList.fromArray(arr, this.node_max_elems);
		this.head = l.head;
		this.tail = l.tail;
		// this.mergeSort((E a, E b)->a.compareTo(b));
	}
	public void sort2() {
		this.mergeSort((E a, E b)->a.compareTo(b));
	}
	/**
	 * Sort all elements in the stout list in the NON-INCREASING order. Call the bubbleSort()
	 * method. After sorting, all but (possibly) the last nodes must be filled with elements.
	 *  
	 * Comparable<? super E> must be implemented for calling bubbleSort().
	 */
	public void sortReverse() {
		E[] arr = this.toArray();
		this.bubbleSort(arr);
		StoutList<E> l = StoutList.fromArray(arr, this.node_max_elems);
		this.head = l.head;
		this.tail = l.tail;
		this.mergeSort((E a, E b)->b.compareTo(a));
		// this.mergeSort((E a, E b)->b.compareTo(a));
	}
	public void sortReverse2() {
		this.mergeSort((E a, E b)->b.compareTo(a));
	}


	/**
	 * Get a forward iterator for the list, which starts at the first element.
	 * 
	 * @return an iterator for the list
	 */
	@Override
	public Iterator<E> iterator() {
		return new StoutListIterator();
	}
	/**
	 * Get a bi-directional iterator for the list which starts at the first element.
	 * 
	 * @return a bi-directional iterator for the list
	 */
	@Override
	public ListIterator<E> listIterator() {
		return new StoutListIterator();
	}
	/**
	 * Get a bi-directional iterator for the list, starting at the provided index.
	 * 
	 * @param index - the beginning index from which to iterate
	 * 
	 * @return a bi-directional iterator for the list
	 * @throws IndexOutOfBoundsException if the starting index is out or range
	 */
	@Override
	public ListIterator<E> listIterator(int index) throws IndexOutOfBoundsException {
		return new StoutListIterator(index);
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
	 * Get the list's String representation
	 */
	@Override
	public String toString() {
		return this.toStringInternal();
	}





	/**
	 * Node type for this list. Each node holds a maximum
	 * of nodeSize elements in an array. Empty slots are null.
	 */
	private class Node {

		/** The outer instance */
		private final StoutList<E> list = StoutList.this;

		/** Array of actual data elements. */
		public E[] data;	// final?
		/** Links to next and previous nodes. */
		public Node next, prev;

		/** Index of the next available offset in this node, also equal to the number of elements in this node. */
		public int size;


		/**
		 * Default Node constructor. Initializes the buffer to be of the default length and size to 0.
		 */
		public Node() {
			this((E[]) new Comparable[node_max_elems], 0);	// Unchecked warning unavoidable.
		}
		/**
		 * Construct a node given a preinitialized buffer and size. Used to create dummy nodes for the list (buffers are null).
		 * 
		 * @param elems - the data array to store elements
		 * @param len - the number of active starting elements, if any
		 */
		public Node(E[] elems, int len) {
			this.data = elems;
			this.size = len;
		}


		/**
		 * Get an element at a specified index. Returns null if the index
		 * is less than 0 or greater than or equal to the buffer's length.
		 * 
		 * @param i - the index to access
		 * @return the element at this index
		 */
		public E get(int i) {
			return (i >= 0 && i < this.data.length) ? this.data[i] : null;
		}
		/**
		 * Assign an element to a specific index. Nothing will be assigned if the index is
		 * less than 0 or greater than or equal to the buffer's length.
		 * 
		 * @param i - the index of assignment
		 * @param e - the element to assign
		 */
		public void set(int i, E e) {
			if(i >= 0 && i < this.data.length) {
				this.data[i] = e;
			}
		}

		/**
		 * Get whether or not the node's buffer is full (ie., size == data.length).
		 * 
		 * @return {@code true} if the node's buffer is at maximum capacity.
		 */
		public boolean full() {
			return this.size == this.data.length;
		}


		/**
		 * Adds an item to this node at the first available offset.
		 * 
		 * @param e - the element to be added
		 * @return whether or not the node was successfully added
		 */
		public boolean add(E e) {
			if (this.size >= list.node_max_elems) {
				return false;
			}
			this.data[this.size++] = e;
			return true;
		}
		/**
		 * Inserts an item to this node at the indicated offset, shifting elements to the right as necessary.
		 * 
		 * @param offset - the array index at which to put the new element
		 * @param item - the element to be added
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
		}
		/**
		 * Deletes an element from this node at the indicated offset, shifting elements left as necessary.
		 * 
		 * @param offset - the index for which element to delete
		 * @return the deleted element, or null if the index was invalid
		 */
		public E remove(int offset) {
			if(offset < 0 || offset >= this.data.length) {
				return null;
			}
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
	 * Represents a location within the StoutList, holding a Node instance
	 * and index within that node.
	 */
	private class ElemIndex {

		public Node node;
		public int idx;

		/**
		 * Get the element that this index points to.
		 * 
		 * @return the element
		 */
		public E val() { return node.get(idx); }
		/**
		 * Reassign the element that this index points to.
		 * 
		 * @param e the new element to assign
		 */
		public void set(E e) { node.set(idx, e); }

	}
	/**
	 * Get the equivalent ElemIndex representation for a given absolute index.
	 * 
	 * @param absolute - the absolute index to convert
	 * @return the ElemIndex representation
	 */
	private ElemIndex fromAbsolute(int absolute) {
		return fromAbsolute(absolute, null);
	}
	/**
	 * Get the equivalent ElemIndex represntation for a given absolute index.
	 * This overload allows for buffer passing - a null param will generate a new ElemIndex.
	 * 
	 * @param absolute - the absolute index to convert
	 * @param buff - the ElemIndex buffer to reuse
	 * @return the ElemIndex representation for the given absolute index
	 */
	private ElemIndex fromAbsolute(int absolute, ElemIndex buff) {
		if(absolute == this.size) {
			if(buff == null) { buff = new ElemIndex(); }
			buff.node = this.tail;
			return buff;
		}
		if(absolute >= 0) {
			Node n = this.head.next;
			while(n != null) {
				if(absolute < n.size || n == this.tail) {
					if(buff == null) { buff = new ElemIndex(); }
					buff.node = n;
					buff.idx = absolute;
					return buff;
				}
				absolute -= n.size;
				n = n.next;
			}
		}
		return null;
	}
	/**
	 * Get the absolute index of a location represented by an ElemIndex.
	 * 
	 * @param ei - the ElemIndex to convert
	 * @return the absolute index, or -1 if this index is no longer valid for the current list's state
	 */
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

	/**
	 * Link two Nodes. Use linkS() if the parameters' validity has not been determined yet.
	 * 
	 * @param a - the first node (comes before the second one in the list)
	 * @param b - the second node (comes after the first on in the list)
	 */
	private void link(Node a, Node b) {
		a.next = b;
		b.prev = a;
	}
	/**
	 * Link two Nodes w/ included nullptr safety.
	 * 
	 * @param a - the first node (comes before the second one in the list)
	 * @param b - the second node (comes after the first on in the list)
	 */
	private void linkS(Node a, Node b) {
		if(a != null) a.next = b;
		if(b != null) b.prev = a;
	}
	/**
	 * Append a newly created node after the provided node.
	 * 
	 * @param pre - the base node
	 * @return the instance of the newly created node
	 */
	private Node insertN(Node pre) {
		return this.insertN(pre, new Node());
	}
	/**
	 * Insert a node after another node and resolve pre/post linking.
	 * 
	 * @param a - the base node
	 * @param n - the node to append to the base
	 * @return the instance of the newly inserted node
	 */
	private Node insertN(Node a, Node n) {
		final Node b = a.next;
		this.linkS(a, n);
		this.linkS(n, b);
		return n;
	}
	/**
	 * Dissolve all external links pointing to the given node (such that it can be garbage collected).
	 * 
	 * @param n - the node to unlink
	 * @return the instance of the node that was removed
	 */
	private Node removeN(Node n) {
		this.linkS(n.prev, n.next);
		return n;
	}
	/**
	 * Condense the list such that all nodes' buffers are at maximum capacity (except possibly the last node).
	 */
	public void condense() {
		Node n = this.head.next;
		while(n != this.tail && n.next != this.tail) {
			if(n.size == n.data.length) {
				n = n.next;
				continue;
			}
			final int
				k = n.size,
				k2 = n.next.size;
			for(int i = 0; i < k2 && i + k < n.data.length; i++) {
				n.data[i + k] = n.next.data[i];
				n.size++;
				n.next.size--;
			}
			if(n.next.size > 0) {
				final int
					gap = k2 - n.next.size,
					k3 = n.next.data.length - gap;
				for(int i = 0; i < k3; i++) {
					n.next.data[i] = n.next.data[i + gap];
					n.next.data[i + gap] = null;
				}
			} else {
				this.removeN(n.next);
			}
			if(n.size >= n.data.length) {
				n = n.next;
			}
		}
	}
	/**
	 * Get an array of elements that is analogous to the internal list.
	 * 
	 * @return the array of elements
	 */
	public E[] toArray() {
		final E[] arr = (E[]) new Comparable[this.size];
		int i = 0;
		for(Node n = this.head.next; n != this.tail; n = n.next) {
			for(int k = 0; k < n.size; k++) {
				arr[i] = n.data[k];
				i++;
			}
		}
		return arr;
	}
	/**
	 * Create a new StoutList from an array of elements. Null values are skipped and leave voids in the list.
	 * 
	 * @param arr - the array of elements to translate
	 * @param node_size - the size of each node within the list
	 * @return the newly created StoutList instace with all the elements
	 */
	public static <E extends Comparable<? super E>> StoutList<E> fromArray(E[] arr, int node_size) {

		if(arr == null) { return null; }
		if(node_size < 1) { node_size = DEFAULT_NODESIZE; }

		final StoutList<E> list = new StoutList<>(node_size);
		final StoutList<E>.Node n1 = list.new Node();
		StoutList<E>.Node n = n1;
		int p = 0;
		for(int i = 0; i < arr.length; i++) {
			if(arr[i] != null) {
				n.data[p] = arr[i];
				list.size++;
				n.size++;
			}
			p++;
			if(p >= n.data.length && i < arr.length - 1) {
				list.link(n, list.new Node());
				n = n.next;
				p = 0;
			}
		}
		list.link(list.head, n1);
		list.link(n, list.tail);
		return list;

	}




	/**
	 * A bi-directional iterator implementation for StoutList. Adheres to the ListIterator<> interface.
	 */
	private class StoutListIterator implements ListIterator<E> {

		private final StoutList<E> list = StoutList.this;
		private Node container;
		private int recent, relative, absolute;


		/**
		 * Increment the pointer(s).
		 */
		private void increment() {
			this.absolute++;
			this.relative++;
			if(this.relative >= this.container.size) {
				this.container = this.container.next;
				this.relative = 0;
			}
			this.recent = this.relative - 1;
		}
		/**
		 * Decrement the pointer(s).
		 */
		private void decrement() {
			this.absolute--;
			this.relative--;
			if(this.relative < 0) {
				this.container = this.container.prev;
				this.relative = this.container.size - 1;
			}
			this.recent = this.relative;
		}
		/**
		 * Refresh the container and relative offset after (for use after an array operation).
		 */
		private void refresh() {
			final ElemIndex ei = list.fromAbsolute(this.absolute);
			this.container = ei.node;
			this.relative = ei.idx;
		}


		/**
		 * Default constructor. Starts the iterator at index 0.
		 */
		public StoutListIterator() {
			this(0);
		}
		/**
		 * Construct an iterator that starts at the given position in the list
		 * 
		 * @param pos - the starting position
		 * @throws IndexOutOfBoundsException if the starting index is beyond the list's range
		 */
		public StoutListIterator(int pos) throws IndexOutOfBoundsException {
			if(pos < 0 || pos > list.size) {
				throw new IndexOutOfBoundsException();
			}
			this.absolute = pos;
			this.refresh();
		}



		/**
		 * Returns true if there are more accessible elements in the FORWARD direction.
		 * 
		 * @return {@code true} if there are more elements
		 */
		@Override
		public boolean hasNext() {
			return this.absolute < list.size;
		}
		/**
		 * Get the next element in the FORWARD direction.
		 * 
		 * @return the next element in the list
		 * @throws NoSuchElementException if there is no next element in the list
		 */
		@Override
		public E next() throws NoSuchElementException {
			if(this.hasNext()) {
				this.increment();
				if(this.recent < 0) {
					return this.container.prev.get(this.container.prev.size + this.recent);
				}
				return this.container.get(this.recent);
			} else {
				throw new NoSuchElementException();
			}
		}
		/**
		 * Returns true if there are more accessible elements in the BACKWARD direction.
		 * 
		 * @return {@code true} if there are more elements
		 */
		@Override
		public boolean hasPrevious() {
			return this.absolute > 0;
		}
		/**
		 * Get the next element in the BACKWARD direction.
		 * 
		 * @return the "next previous" element in the list
		 * @throws NoSuchElementException if there is not previous element in the list
		 */
		@Override
		public E previous() throws NoSuchElementException {
			if(this.hasPrevious()) {
				this.decrement();
				return this.container.get(this.recent);
			} else {
				throw new NoSuchElementException();
			}
		}
		/**
		 * Get the index of the next element in the FORWARD direction.
		 * 
		 * @return an integer index for the next element
		 */
		@Override
		public int nextIndex() {
			return this.absolute;
		}
		/**
		 * Get the index of the next element in the BACKWARD direction.
		 * 
		 * @return an integer index for the previous element
		 */
		@Override
		public int previousIndex() {
			return this.absolute - 1;
		}
		/**
		 * Removes the element from the list that was most recently accessed.
		 * 
		 * @throws IllegalStateException if the previous accessed element no longer is valid
		 */
		@Override
		public void remove() throws IllegalStateException {
			// if valid recent
			if(this.recent <= this.relative) {
				if(this.recent < this.relative) {
					list.remove(this.absolute - 1);
					this.absolute--;
				} else {
					list.remove(this.absolute);
				}
				this.refresh();		// TODO optimize refresh
				// invalidate recent
				this.recent = this.relative + 1;
			} else {
				throw new IllegalStateException();
			}
		}
		/**
		 * Replaces the element from the list that was most recently accessed.
		 * 
		 * @param e - the element for replacing
		 * @throws NullPointerException if the element being added is null
		 * @throws IllegalStateException if the previously accessed element no longer is valid
		 */
		@Override
		public void set(E e) throws NullPointerException, IllegalStateException {
			if(e == null) {
				throw new NullPointerException();
			}
			// check no add() or remove()
			if(this.recent <= this.relative) {
				if(this.recent < 0) {
					this.container.prev.set(this.container.prev.size + this.recent, e);
				} else {
					this.container.set(this.recent, e);
				}
			} else {
				throw new IllegalStateException();
			}
		}
		/**
		 * Inserts an element into the list before the current iterator's position.
		 * 
		 * @param e - the element to insert
		 * @throws NullPointerException if the element being added is null
		 */
		@Override
		public void add(E e) throws NullPointerException {
			if(e == null) {
				throw new NullPointerException();
			}
			// if valid recent
			list.add(this.absolute, e);
			this.absolute++;
			this.refresh();	// TODO optimize refresh
			// invalidate recent
			this.recent = this.relative + 1;
		}

		/**
		 * Get the iterator's String representation
		 */
		@Override
		public String toString() {
			return list.toStringInternal(this);
		}


	}



	/**
	 * Sort an array arr[] using the insertion sort algorithm in the NON-DECREASING order.
	 * 
	 * @param arr - array storing elements from the list
	 * @param comp - comparator used in sorting
	 */
	private void insertionSort(E[] arr, Comparator<? super E> comp) {
		insertionSort(arr, comp, arr.length);
	}
	/**
	 * Sort an array arr[] up to the specified length using the insertion sort algorithm.
	 * 
	 * @param arr - the array to sort
	 * @param comp - the comparator that determines the sorted order
	 * @param size - the max bound for the part of the array that will be sorted
	 */
	private void insertionSort(E[] arr, Comparator<? super E> comp, int size) {

		for(int i = 1; i < size; i++) {
			int p = i;
			while(p > 0 && comp.compare(arr[p], arr[p - 1]) < 0) {
				final E tmp = arr[p];
				arr[p] = arr[p - 1];
				arr[p - 1] = tmp;
				p--;
			}
		}

	}
	/**
	 * Sort arr[] using the bubble sort algorithm in the NON-INCREASING order. For a 
	 * description of bubble sort please refer to Section 6.1 in the project description. 
	 * You must use the compareTo() method from an implementation of the Comparable 
	 * interface by the class E or ? super E.
	 * 
	 * @param arr - array holding elements from the list
	 */
	private void bubbleSort(E[] arr) {

		int unsorted_max = arr.length - 1;
		while(unsorted_max > 1) {
			int last_sorted = 0;
			for(int i = 0; i < unsorted_max; i++) {
				if(arr[i].compareTo(arr[i + 1]) < 0) {
					final E tmp = arr[i];
					arr[i] = arr[i + 1];
					arr[i + 1] = tmp;
					last_sorted = i + 1;
				}
			}
			unsorted_max = last_sorted;
		}

	}





	/**
	 * Represents a view of a list of linked nodes.
	 */
	private class NodeView {

		public final Node first, last;

		/**
		 * Create a new NodeView using the first and last nodes provided.
		 * 
		 * @param f - the first node in the view
		 * @param l - the last node in the view
		 */
		public NodeView(Node f, Node l) {
			this.first = f;
			this.last = l;
		}

		/**
		 * Append a node to the end of the list view and return a new view including it. WARNING: The source
		 * list's state will be altered!
		 * 
		 * @param n - the node to append
		 * @return the new NodeView which includes the newly appended node
		 */
		public NodeView append(Node n) {
			linkS(this.last, n);
			return new NodeView(this.first, n);
		}
		/**
		 * Prepend a node to the beginning of the list view and return a new view including it. WARNING: The source
		 * list's state will be altered!
		 * 
		 * @param n - the node to prepend
		 * @return the new NodeView which includes the newly prepended node
		 */
		public NodeView prepend(Node n) {
			linkS(n, this.first);
			return new NodeView(n, this.last);
		}

	}
	/**
	 * Link two NodeView's and return a new view that includes both. WARNING: This method
	 * only deals with the two views and does not resolve/relink the larger source lists (if any).
	 * Any external relinking must be done manually beforehand.
	 * 
	 * @param a - the first view
	 * @param b - the second view
	 * @return the new view including both views
	 */
	private NodeView linkView(NodeView a, NodeView b) {
		this.link(a.last, b.first);
		return new NodeView(a.first, b.last);
	}
	/**
	 * Link two NodeView's and return a new view that includes both (with nullptr safety).
	 * WARNING: This method only deals with the two views and does not resolve/relink the
	 * larger source lists (if any). Any external relinking must be done manually beforehand.
	 * 
	 * @param a - the first view
	 * @param b - the second view
	 * @return the new view including both views
	 */
	private NodeView linkViewS(NodeView a, NodeView b) {
		if(a != null && b != null) {
			this.linkS(a.last, b.first);
			return new NodeView(a.first, b.last);
		}
		return null;
	}

	/**
	 * Sort the list. This method is a variant of merge sort where all node buffers are sorted first
	 * and then merged into a running list of nodes (binary merge). Once all nodes have been iterated,
	 * the list's elements are replaced.
	 * 
	 * @param comp - the comparator that determines the direction of sorting
	 */
	private void mergeSort(Comparator<? super E> comp) {
		Node n = this.head.next;
		if(n != this.tail) {
			this.condense();
			this.insertionSort(n.data, comp, n.size);
			NodeView sorted = new NodeView(n, n);
			n = n.next;
			while(n != this.tail) {
				this.insertionSort(n.data, comp, n.size);
				sorted = this.merge(sorted, new NodeView(n, n), comp);
				n = n.next;
			}
			this.link(this.head, sorted.first);
			this.link(sorted.last, this.tail);
		}
	}
	/**
	 * Merge algorithm used by mergeSort(). Returns a new NodeView with the sorted elements from both
	 * initial NodeView's.
	 * 
	 * @param a - the first nodeview
	 * @param b - the second nodeview
	 * @param comp - the comparator object
	 * @return a new NodeView for the list containing the sorted elements
	 */
	private NodeView merge(NodeView a, NodeView b, Comparator<? super E> comp) {

		final Node f = new Node();
		Node
			c1 = a.first,
			c2 = b.first,
			c = f;
		int p1 = 0, p2 = 0, p = 0;
		while(c1 != null && c2 != null) {

			final E
				e1 = c1.get(p1),
				e2 = c2.get(p2);
			// System.out.println("#1: " + e1 + "\t#2: " + e2);
			if(comp.compare(e1, e2) < 0) {
				c.set(p, e1);
				c.size++;
				p1++;
				if(p1 >= c1.size) {
					if(c1 == a.last) {
						// finish off #2
						while(c2 != null) {
							p++;
							if(p >= c.data.length) {
								p = 0;
								final Node n = new Node();
								link(c, n);
								c = n;
							}
							c.set(p, c2.get(p2));
							c.size++;
							p2++;
							if(p2 >= c2.size) {
								if(c2 == b.last) {
									break;
								}
								p2 = 0;
								c2 = c2.next;
							}
						}
						break;
					}
					p1 = 0;
					c1 = c1.next;
				}
			} else {
				c.set(p, e2);
				c.size++;
				p2++;
				if(p2 >= c2.size) {
					if(c2 == b.last) {
						// finish off #1
						while(c1 != null) {
							p++;
							if(p >= c.data.length) {
								p = 0;
								final Node n = new Node();
								link(c, n);
								c = n;
							}
							c.set(p, c1.get(p1));
							c.size++;
							p1++;
							if(p1 >= c1.size) {
								if(c1 == a.last) {
									break;
								}
								p1 = 0;
								c1 = c1.next;
							}
						}
						break;
					}
					p2 = 0;
					c2 = c2.next;
				}
			}
			p++;
			if(p >= c.data.length) {
				p = 0;
				final Node n = new Node();
				link(c, n);
				c = n;
			}

		}
		return new NodeView(f, c);

	}


}