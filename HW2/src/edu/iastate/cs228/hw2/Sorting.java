package edu.iastate.cs228.hw2;

import java.util.Comparator;


/**
 * A collection of genericly typed and variable comparator array sorting algorithms
 */
public final class Sorting {

	/**
	 * 
	 * @param <T>
	 * @param arr
	 * @param comp
	 */
	public static <T> void selectionSort(T[] arr, Comparator<T> comp) {

		for(int i = 0; i < arr.length - 1; i++) {

			int mi = i;
			for(int p = i + 1; p < arr.length; p++) {
				if(comp.compare(arr[p], arr[mi]) < 0) {
					mi = p;
				}
			}
			if(mi != i) {
				final T tmp = arr[i];
				arr[i] = arr[mi];
				arr[mi] = tmp;
			}

		}

	}

	/**
	 * 
	 * @param <T>
	 * @param arr
	 * @param comp
	 */
	public static <T> void insertionSort(T[] arr, Comparator<T> comp) {

		for(int i = 1; i < arr.length; i++) {

			int p = i;
			while(p > 0 && comp.compare(arr[p], arr[p - 1]) < 0) {
				final T tmp = arr[p];
				arr[p] = arr[p - 1];
				arr[p - 1] = tmp;
				p--;
			}

		}

	}

	/**
	 * 
	 * @param <T>
	 * @param arr
	 * @param comp
	 */
	public static <T> void mergeSort(T[] arr, Comparator<T> comp) {

		mergeSort(ArrayView.window(arr), comp);

	}
	/**
	 * 
	 * @param <T>
	 * @param arr
	 * @param comp
	 */
	public static <T> void mergeSort(ArrayView<T> arr, Comparator<T> comp) {

		final int
			len = arr.size(),
			last = arr.last();

		// base cases - normal mergesort only has one base case but there is no need to spawn another layer when the array view is just 2 elems
		if(len <= 1) {
			return;
		}
		if(len == 2) {
			if(comp.compare(arr.get(0), arr.get(1)) > 0) {
				arr.refSwap(0, 1);
			}
			return;
		}

		final int split = last / 2;
		mergeSort(arr.endAt(split), comp);
		mergeSort(arr.startingAt(split + 1), comp);

		final Object[] tmp = new Object[len];

		int s1 = 0, s2 = split + 1, ptr = 0;
		while(s1 <= split && s2 <= last) {

			final T a = arr.get(s1), b = arr.get(s2);
			if(comp.compare(a, b) > 0) {
				tmp[ptr] = b;
				s2++;
			} else {
				tmp[ptr] = a;
				s1++;
			}
			ptr++;

		}
		while(s1 <= split) {
			tmp[ptr] = arr.get(s1);
			s1++;
			ptr++;
		}
		while(s2 <= last) {
			tmp[ptr] = arr.get(s2);
			s2++;
			ptr++;
		}

		for(int i = 0; i < len; i++) {
			arr.set(i, (T)tmp[i]);
		}

		// merge
		// {
		// 	int ptr = 0,
		// 		s1 = ptr,
		// 		s2 = split + 1;
		// 	while(s1 != s2 && s2 < len && ptr < len) {

		// 		if(comp.compare(arr.get(s1), arr.get(s2)) > 0) {	// s2 goes to ptr
		// 			arr.refSwap(s2, ptr);
		// 			if(s1 == ptr) {			// if s1 was at ptr, s1 is now starting at prev s2
		// 				s1 = s2;
		// 			}
		// 			s2++;
		// 		} else {	// s1 goes to ptr
		// 			if(s1 != ptr) {		// swap if not already the same location, s1 stays the same since the swapped value "was" s1 + 1
		// 				arr.refSwap(s1, ptr);
		// 			} else {		// same location, increment s1 along with ptr
		// 				s1++;
		// 				if(s1 == s2) {
		// 					s1 = ptr + 1;
		// 				}
		// 			}
		// 		}
		// 		ptr++;

		// 	}
		// 	while(s1 != ptr && s1 < len && ptr < len) {		// reorder section 1 if it is got segmented

		// 		if(comp.compare(arr.get(ptr), arr.get(s1)) > 0) {
		// 			arr.refSwap(ptr, s1);
		// 		}
		// 		ptr++;
		// 		s1++;

		// 	}
		// }

	}

	public static <T> void quickSort(T[] arr, Comparator<T> comp) {

		quickSort(ArrayView.window(arr), comp);

	}
	public static <T> void quickSort(ArrayView<T> arr, Comparator<T> comp) {

		if(arr.size() <= 1) {
			return;
		}

		final T pivot = arr.get(arr.last() / 2);

		int low = 0, high = arr.last();
		for(;;) {

			while(comp.compare(arr.get(low), pivot) < 0) {
				low++;
			}
			while(comp.compare(pivot, arr.get(high)) < 0) {
				high--;
			}
			if(low >= high) {
				break;
			}
			arr.refSwap(low, high);
			low++;
			high--;

		}

		quickSort(arr.endAt(high), comp);
		quickSort(arr.startingAt(high + 1), comp);

	}


}
