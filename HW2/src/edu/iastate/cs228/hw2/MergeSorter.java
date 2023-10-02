package edu.iastate.cs228.hw2;

import java.lang.IllegalArgumentException;


/**
 * @author Sam Richter
 */

/**
 * This class extends the AbstractSorter interface and represents the merge sort algorithm.
 */
public class MergeSorter extends AbstractSorter {


	/**
	 * Constructor takes an array of points. It invokes the superclass constructor, and also
	 * set the instance variables algorithm in the superclass.
	 * 
	 * @param pts - input array of points
	 */
	public MergeSorter(Point[] pts) throws IllegalArgumentException {
		super(pts, "MergeSort");
	}


	/**
	 * Perform merge sort on the array points[] of the parent class AbstractSorter.
	 */
	@Override
	public void sort() {
		Sorting.mergeSort(super.points);
	}

	// /**
	//  * This is a recursive method that carries out mergesort on an array pts[] of points. One 
	//  * way is to make copies of the two halves of pts[], recursively call mergeSort on them, 
	//  * and merge the two sorted subarrays into pts[].
	//  * 
	//  * @param pts - the array of points to sort
	//  */
	// private void mergeSortRec(Point[] pts) {
	// 	Sorting.mergeSort(pts);
	// }


}
