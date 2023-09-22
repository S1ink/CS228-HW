package edu.iastate.cs228.hw2;


public class ArrayView<T> {

	private T[] buff;
	private final int beg, end;

	/**
	 * 
	 * Create an array view. The view window will range from the first index up to and including the last index.
	 * 
	 * @param buffer
	 * @param first
	 * @param last
	 */
	private ArrayView(T[] buffer, int first, int last) {
		this.buff = buffer;
		this.beg = Math.max(0, first);
		this.end = last;
	}

	public static <T> ArrayView<T> window(T[] buff) {
		if(buff == null) { return null; }
		return new ArrayView<T>(buff, 0, lastIdx(buff));
	}
	public static <T> ArrayView<T> endAt(T[] buff, int last) {
		if(buff == null) { return null; }
		return new ArrayView<T>(buff, 0, Math.min(last, lastIdx(buff)));
	}
	public static <T> ArrayView<T> startingAt(T[] buff, int first) {
		if(buff == null) { return null; }
		final int limit = lastIdx(buff);
		return new ArrayView<T>(buff, Math.min(first, limit), limit);
	}
	public static <T> ArrayView<T> range(T[] buff, int first, int last) {
		if(buff == null) { return null; }
		final int
			limit = lastIdx(buff),
			beg = Math.min(first, limit),
			end = Math.min(last, limit);
		return new ArrayView<T>(buff, beg, end);
	}


	public int size() {
		return this.last() + 1;
	}
	public int last() {
		return (this.end - this.beg);
	}

	public T get(int i) {
		if(checkBounds(i)) {
			return this.buff[this.beg + i];
		}
		return null;
	}
	public ArrayView<T> set(int i, T val) {
		if(checkBounds(i)) {
			this.buff[this.beg + i] = val;
		}
		return this;
	}
	public ArrayView<T> refSwap(int i, int j) {
		if(checkBounds(i) && checkBounds(j)) {
			final T tmp = this.get(i);
			this.set(i, this.get(j));
			this.set(j, tmp);
		}
		return this;
	}

	public ArrayView<T> range(int first, int last) {
		return ArrayView.range(this.buff, this.beg + first, this.beg + last);
	}
	public ArrayView<T> endAt(int last) {
		return this.range(0, last);
	}
	public ArrayView<T> startingAt(int first) {
		return this.range(first, this.last());
	}



	private boolean checkBounds(int i) {
		return i >= 0 && i <= this.last();
	}





	public static <T> int lastIdx(T[] arr) {
		return arr.length - 1;
	}


}
