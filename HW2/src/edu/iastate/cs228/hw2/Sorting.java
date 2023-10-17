package edu.iastate.cs228.hw2;

import java.util.Comparator;


/**
 * @author Sam Richter
 */

/**
 * A container class for generically typed sorting algorithms and other helpers.
 */
public final class Sorting {

	/**
	 * Any sorting function that sorts using a generically typed comparator.
	 */
	public static interface Sorter<T> {
		public void sort(T[] arr, Comparator<? super T> comp);
	}
	/**
	 * Any sorting fuction that sorts comparable types.
	 */
	public static interface ComparableSorter<T extends Comparable<? super T>> {
		public void sort(T[] arr);
	}

	/**
	 * Get a sorting function (typed as a {@link Sorter}) for the supplied algorithm enumeration
	 * 
	 * @param <T> - the type for which the sorter sorts
	 * @param a - the algorithm enumeration determining the returned function
	 * @return - the sorter function
	 */
	public static <T> Sorter<T> getSorter(Algorithm a) {
		switch(a) {
			case SelectionSort: return Sorting::selectionSort;
			case InsertionSort: return Sorting::insertionSort;
			case MergeSort: return Sorting::mergeSort;
			case QuickSort: return Sorting::quickSort;
			default: return null;
		}
	}
	/**
	 * Get a sorting function (typed as a {@link ComarableSorter}) for the supplied algorithm enumeration
	 * 
	 * @param <T> - the type for which the sorter sorts - must be comparable
	 * @param a - the algorithm enumeration determining the returned function
	 * @return - the sorter function
	 */
	public static <T extends Comparable<? super T>> ComparableSorter<T> getComparableSorter(Algorithm a) {
		switch(a) {
			case SelectionSort: return Sorting::selectionSort;
			case InsertionSort: return Sorting::insertionSort;
			case MergeSort: return Sorting::mergeSort;
			case QuickSort: return Sorting::quickSort;
			default: return null;
		}
	}





	/**
	 * Selection sort implemented for use with a generically typed comparator.
	 * 
	 * @param <T> - the type being sorted
	 * @param arr - the array to sort
	 * @param comp - the comparator
	 */
	public static <T> void selectionSort(T[] arr, Comparator<? super T> comp) {

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
	 * Selection sort (wrapper) for use with comparable types
	 * 
	 * @param <T> - the comparable type being sorted
	 * @param arr - the array to sort
	 */
	public static <T extends Comparable<? super T>> void selectionSort(T[] arr) {
		selectionSort(arr, (T a, T b)->a.compareTo(b));
	}





	/**
	 * Insertion sort implemented for use with a generically typed comparator.
	 * 
	 * @param <T> - the type being sorted
	 * @param arr - the array to sort
	 * @param comp - the comparator
	 */
	public static <T> void insertionSort(T[] arr, Comparator<? super T> comp) {

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
	 * Insertion sort (wrapper) for use with comparable types
	 * 
	 * @param <T> - the comparable type being sorted
	 * @param arr - the array to sort
	 */
	public static <T extends Comparable<? super T>> void insertionSort(T[] arr) {
		insertionSort(arr, (T a, T b)->a.compareTo(b));
	}





	/**
	 * Merge sort implemented for use with a generically typed comparator.
	 * 
	 * @param <T> - the type being sorted
	 * @param arr - the array to sort
	 * @param comp - the comparator
	 */
	public static <T> void mergeSort(T[] arr, Comparator<? super T> comp) {
		mergeSort(ArrayView.window(arr), comp);
	}
	/**
	 * Merge sort (wrapper) for use with comparable types
	 * 
	 * @param <T> - the comparable type being sorted
	 * @param arr - the array to sort
	 */
	public static <T extends Comparable<? super T>> void mergeSort(T[] arr) {
		mergeSort(arr, (T a, T b)->a.compareTo(b));
	}
	/**
	 * Merge sort recursive implementation using an array view of the data being sorted
	 * 
	 * @param <T> - the type being sorted
	 * @param arr - the array view of the data to sort
	 * @param comp - the comparator
	 */
	public static <T> void mergeSort(ArrayView<T> arr, Comparator<? super T> comp) {

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

		// merge
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

		// merge - attempted in-place implementation
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





	/**
	 * Quick sort implemented for use with a generically typed comparator.
	 * 
	 * @param <T> - the type being sorted
	 * @param arr - the array to sort
	 * @param comp - the comparator
	 */
	public static <T> void quickSort(T[] arr, Comparator<? super T> comp) {
		quickSort(ArrayView.window(arr), comp);
	}
	/**
	 * Quick sort (wrapper) for use with comparable types
	 * 
	 * @param <T> - the comparable type being sorted
	 * @param arr - the array to sort
	 */
	public static <T extends Comparable<? super T>> void quickSort(T[] arr) {
		quickSort(arr, (T a, T b)->a.compareTo(b));
	}
	/**
	 * Quick sort recursive implementation using an array view of the data being sorted
	 * 
	 * @param <T> - the type being sorted
	 * @param arr - the array to sort
	 * @param comp - the comparator
	 */
	public static <T> void quickSort(ArrayView<T> arr, Comparator<? super T> comp) {

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





	/**
	 * Generic bubble sort for comparable types
	 * 
	 * @param <T> the comparable type
	 * @param arr the array
	 */
	public static <T extends Comparable<? super T>> void bubbleSort(T[] arr) {
		bubbleSort(arr, (T a, T b)->a.compareTo(b));
	}
	/**
	 * Generic bubble sort
	 * 
	 * @param <T> the type
	 * @param arr the array
	 * @param comp the comparator
	 */
	public static <T> void bubbleSort(T[] arr, Comparator<? super T> comp) {

		int unsorted_max = arr.length - 1;
		while(unsorted_max > 1) {
			int last_sorted = 0;
			for(int i = 0; i < unsorted_max; i++) {
				if(comp.compare(arr[i], arr[i + 1]) > 0) {
					final T tmp = arr[i];
					arr[i] = arr[i + 1];
					arr[i + 1] = tmp;
					last_sorted = i + 1;
				}
			}
			unsorted_max = last_sorted;
		}

	}


}
