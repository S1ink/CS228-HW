package testing;

import java.util.*;


public class Heap<T extends Comparable<? super T> > {

	public enum HeapDirection {
		MAX		(+1),
		MIN		(-1);

		private int scalar;
		private HeapDirection(int s) { this.scalar = s; }

		public <T> boolean higher(T a, T b, Comparator<? super T> comp) {
			return (comp.compare(a, b) * this.scalar) > 0;
		}
		public <T> boolean lower(T a, T b, Comparator<? super T> comp) {
			return (comp.compare(a, b) * this.scalar) < 0;
		}

	}

	public final HeapDirection ver;
	public T[] heap = null;
	private int entities = 0;

	public Heap(HeapDirection dir, T... entities) {
		this.ver = dir;
	}
	public Heap(HeapDirection dir, int arr_init) {
		this.ver = dir;
		this.heap = (T[])new Comparable[arr_init];
		this.entities = arr_init;
	}
	public Heap(HeapDirection dir) {
		this.ver = dir;
	}

	public int size() {
		return this.entities;
	}
	protected int capacity() {
		return this.heap.length;
	}

	public int height() {
		return 0;
	}




	public static int left(int parent) {
		return parent*2 + 1;
	}
	public static int right(int parent) {
		return parent*2 + 2;
	}
	public static int parent(int child) {
		return (child - 1) / 2;		// technically floor((child - 1) / 2) but integer division mimics this
	}

	public static <T> T[] ensureCapacity(T[] arr, int desired_size) {
		if(desired_size > arr.length) {
			return Arrays.copyOf(arr, desired_size);
		}
		return arr;
	}
	public static <T> T[] ensureCapacityScaled(T[] arr, int desired_size, int scale_factor) {
		if(desired_size > arr.length) {
			return Arrays.copyOf(arr, arr.length * scale_factor);
		}
		return arr;
	}

	/** Capacity must be ensured as a prerequisite */
	public static <T> void add(T[] arr, int size, T elem, Comparator<? super T> comp, HeapDirection dir) {
		arr[size] = elem;
		percolateUp(arr, size, comp, dir);
	}
	public static <T> T peek(T[] arr) {
		return arr == null ? null : arr[0];
	}
	public static <T> T remove(T[] arr, int size, Comparator<? super T> comp, HeapDirection dir) {
		final T v = arr[0];
		arr[0] = arr[size - 1];
		percolateDown(arr, size - 1, 0, comp, dir);
		return v;
	}
	protected static <T> void percolateUp(T[] arr, int idx, Comparator<? super T> comp, HeapDirection dir) {
		int parent = parent(idx);
		final T v = arr[idx];	// save the value, since percolating a hole is more efficient
		while(idx > 0 && dir.higher(v, arr[parent], comp)) {		// while the base value should be higher than this ancestor, bump the ancestor down
			arr[idx] = arr[parent];
			idx = parent;
			parent = parent(idx);
		}
		arr[parent] = v;
	}
	protected static <T> void percolateDown(T[] arr, int size, int idx, Comparator<? super T> comp, HeapDirection dir) {
		int child = left(idx);	// left index is always lower in value, so we test it first
		final T v = arr[idx];
		while(child < size) {
			if(child + 1 < size && dir.lower(arr[child], arr[child + 1], comp)) {	// reassign child if right child exists and is lower
				child = child + 1;
			}
			if(dir.lower(v, arr[child], comp)) {	// if value is less than lowest child, bump up the child (percolate down the hole)
				arr[idx] = arr[child];
			} else break;	// otherwise we done here
			idx = child;
			child = left(idx);
		}
		arr[idx] = v;
	}
	public static <T> void heapify(T[] arr, int size, Comparator<? super T> comp, HeapDirection dir) {
		for(int i = parent(size - 1); i >= 0; i--) {
			percolateDown(arr, size, i, comp, dir);
		}
	}

	public static <T> void heapSort(T[] arr, int size, Comparator<? super T> comp, HeapDirection dir) {		// the sorted array will be in opposite order of the heap direction
		heapify(arr, size, comp, dir);
		for(; size > 0; size--) {
			arr[size - 1] = remove(arr, size, comp, dir);
		}
	}



	public static void main(String... args) {

		final String[] strings = new String[]{ "A", "C", "Z", "L", "B", "Y", "R", "D", "K" };
		heapSort(strings, strings.length, (String a, String b)->b.compareTo(a), HeapDirection.MIN);
		for(String s : strings) {
			System.out.println(s);
		}
	}

}