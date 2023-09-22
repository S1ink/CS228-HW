package edu.iastate.cs228.hw2;

import java.io.FileNotFoundException;
import java.lang.NumberFormatException; 
import java.lang.IllegalArgumentException;
import java.util.Comparator;
import java.util.InputMismatchException;

/**
 *  
 * @author
 *
 */

/**
 * 
 * This class implements the mergesort algorithm.
 *
 */

public class MergeSorter extends AbstractSorter {

	// Other private instance variables if needed
	
	/** 
	 * Constructor takes an array of points.  It invokes the superclass constructor, and also 
	 * set the instance variables algorithm in the superclass.
	 *  
	 * @param pts   input array of integers
	 */
	public MergeSorter(Point[] pts) throws IllegalArgumentException {
		super(pts, "MergeSort");
	}


	/**
	 * Perform mergesort on the array points[] of the parent class AbstractSorter. 
	 * 
	 */
	@Override 
	public void sort() {
		this.mergeSortRec(super.points);
	}

	
	/**
	 * This is a recursive method that carries out mergesort on an array pts[] of points. One 
	 * way is to make copies of the two halves of pts[], recursively call mergeSort on them, 
	 * and merge the two sorted subarrays into pts[].
	 * 
	 * @param pts	point array 
	 */
	private void mergeSortRec(Point[] pts) {

		mergeSort(pts, this.pointComparator);

	}

	// Other private methods if needed ...





	public static <T> void mergeSort(T[] arr, Comparator<T> comp) {

		return mergeSort(ArrayView.window(arr), comp);

	}
	private static <T> void mergeSort(ArrayView<T> arr, Comparator<T> comp) {

		final int len = arr.size();

		// base cases
		if(len <= 1) {
			return;
		}
		if(len == 2) {
			if(comp.compare(arr.get(0), arr.get(1)) < 0) {
				arr.refSwap(0, 1);
			}
			return;
		}

		final int split = len / 2;
		final ArrayView<T>
			a = arr.endAt(split),
			b = arr.startingAt(split + 1);
		mergeSort(a, comp);
		mergeSort(b, comp);

		// merge code

	}


}
