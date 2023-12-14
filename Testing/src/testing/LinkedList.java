package testing;

import java.util.AbstractCollection;
import java.util.Iterator;


public class LinkedList<T, N extends LinkedList.LinkedNode<T, ? super N>>
//	extends AbstractCollection<T>
{

	public static interface LinkedNode<T, N extends LinkedNode<T, ? super N>> {

		public N next();
		public void next(N next);

		public T val();
		public void val(T val);

	}
	public static interface DoublyLinkedNode<T, N extends DoublyLinkedNode<T, ? super N>> 
		extends LinkedNode<T, N>
	{

		public N prev();
		public void prev(N prev);

	}


	private N
		head = null,
		tail = null;
	private int
		size = 0;

	public LinkedList(N... nodes) {

	}
	// public static <T> LinkedList<T, N> makeGeneric(boolean dbll, T... vals) {		// use generic wrapper

	// }


//	@Override
	public int size() {
		return this.size;
	}
	public void append(N node) {

	}
	public void append(T data) {

	}
	public void prepend(N node) {

	}
	public void prepend(T data) {
		
	}


}
