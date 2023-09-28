package edu.iastate.cs228.hw2;


/**
 * @author Sam Richter
 */

/**
 * ArrayView wraps a generically typed primitive array and allows mapping of indices to a specific range.
 */
public class ArrayView<T> {

	private T[] buff;
	private final int beg, end;

	/**
	 * Create an array view. The view window will range from the first index up to and including the last index.
	 * 
	 * @param buffer - the array to be wrapped
	 * @param first - the first index in the window (inclusive)
	 * @param last - the last index in the window (inclusive)
	 */
	private ArrayView(T[] buffer, int first, int last) {
		this.buff = buffer;
		this.beg = Math.max(0, first);
		this.end = last;
	}

	/**
	 * Create a new ArrayView that maps the entire source array.
	 * 
	 * @param <T> - the array's generic type
	 * @param buff - the source array
	 * @return the newly created ArrayView
	 */
	public static <T> ArrayView<T> window(T[] buff) {
		if(buff == null) { return null; }
		return new ArrayView<T>(buff, 0, lastIdx(buff));
	}
	/**
	 * Create a new ArrayView that maps a range beginning at the source array's first index and ending at the last
	 * index provided (inclusive)
	 * 
	 * @param <T> - the array's generic type
	 * @param buff - the source array
	 * @param last - the last index to include in the window
	 * @return the newly created ArrayView
	 */
	public static <T> ArrayView<T> endAt(T[] buff, int last) {
		if(buff == null) { return null; }
		return new ArrayView<T>(buff, 0, Math.min(last, lastIdx(buff)));
	}
	/**
	 * Create a new ArrayView that maps a range beginning at the specified index (inclusive) and ending at the source array's
	 * last index.
	 * 
	 * @param <T> - the array's generic type
	 * @param buff - the source array
	 * @param first - the first index to include in the window
	 * @return the newly created ArrayView
	 */
	public static <T> ArrayView<T> startingAt(T[] buff, int first) {
		if(buff == null) { return null; }
		final int limit = lastIdx(buff);
		return new ArrayView<T>(buff, Math.min(first, limit), limit);
	}
	/**
	 * Create a new ArrayView for any subset range within the source array. Note that invalid
	 * index bounds will be clamped to a valid range.
	 * 
	 * @param <T> - the array's generic type
	 * @param buff - the source array
	 * @param first - the first index to include in the window (inclusive)
	 * @param last - the last index to include in the window (inclusive)
	 * @return the newly created ArrayView
	 */
	public static <T> ArrayView<T> range(T[] buff, int first, int last) {
		if(buff == null) { return null; }
		final int
			limit = lastIdx(buff),
			beg = Math.min(first, limit),
			end = Math.min(last, limit);
		return new ArrayView<T>(buff, beg, end);
	}


	/**
	 * Get the number of elements visible in the view
	 * 
	 * @return the size of the window
	 */
	public int size() {
		return this.last() + 1;
	}
	/**
	 * Get the last valid index (relative) for the view
	 * 
	 * @return the last valid index
	 */
	public int last() {
		return (this.end - this.beg);
	}

	/**
	 * Access an element within the range using relative indices.
	 * 
	 * @param i - the index of the element to be accessed
	 * @return null if the index is outside of the valid range, otherwise the element's value
	 */
	public T get(int i) {
		if(checkBounds(i)) {
			return this.buff[this.beg + i];
		}
		return null;
	}
	/**
	 * Set an element's value within the range using relative indices.
	 * 
	 * @param i - the index of the element to access
	 * @param val - the new value to set
	 * @return the current instance so calls can be chained
	 */
	public ArrayView<T> set(int i, T val) {
		if(checkBounds(i)) {
			this.buff[this.beg + i] = val;
		}
		return this;
	}
	/**
	 * Swap the elements at 2 different indices
	 * 
	 * @param i - the index of the first element to swap
	 * @param j - the index of the second element to swap
	 * @return the current instance so calls can be chained
	 */
	public ArrayView<T> refSwap(int i, int j) {
		if(i != j && checkBounds(i) && checkBounds(j)) {
			final T tmp = this.get(i);
			this.set(i, this.get(j));
			this.set(j, tmp);
		}
		return this;
	}

	/**
	 * Get a new view into the current window using the specified range
	 * 
	 * @param first - the first index (relative, inclusive) to include in the subview
	 * @param last - the last index (relative, inclusive) to include in the subview
	 * @return the new ArrayView instance
	 */
	public ArrayView<T> range(int first, int last) {
		return ArrayView.range(this.buff, this.beg + first, this.beg + last);
	}
	/**
	 * Get a new view into the current window, from the first valid index up to the provided index
	 * 
	 * @param last - the last index (relative, inclusive) to be included in the subview
	 * @return the new ArrayView instance
	 */
	public ArrayView<T> endAt(int last) {
		return this.range(0, last);
	}
	/**
	 * Get a new view into the current window, beginning at the provided index and ending at the last valid index
	 * 
	 * @param first - the first index (relative, inclusive) to be included in the subview
	 * @return the new ArrayView instance
	 */
	public ArrayView<T> startingAt(int first) {
		return this.range(first, this.last());
	}



	/**
	 * Check if a provided index is within the bounds of this instance's viewable range
	 * 
	 * @param i - the index (relative)
	 * @return whether or not the index is valid for this window
	 */
	private boolean checkBounds(int i) {
		return i >= 0 && i <= this.last();
	}

	/**
	 * Get the last valid index for the array
	 * 
	 * @param <T> - the array type
	 * @param arr - the array
	 * @return the index of the last element in the array (AKA, length - 1)
	 */
	public static <T> int lastIdx(T[] arr) {
		return arr.length - 1;
	}


}
