package edu.iastate.cs228.hw2;


public class ArrayView<T> {

	private T[] buff;
	private final int beg, end;

	public ArrayView(T[] buffer, int first, int last) {
		this.buff = buffer;
		this.beg = first;
		this.end = last;
	}


	public int size() {
		return (this.end - this.beg) + 1;
	}

	public T get(int i) {
		final int abs = this.beg + i;
		return abs <= this.end ? this.buff[abs] : null;
	}


}
