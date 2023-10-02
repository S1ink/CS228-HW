package edu.iastate.cs228.hw2;

import java.lang.IllegalArgumentException;


/**
 * @author Sam Richter
 */

/**
 * This class extends the AbstractSorter interface and represnts the quick sort algorithm.
 */
public class QuickSorter extends AbstractSorter {


	/**
	 * Constructor takes an array of points. It invokes the superclass constructor, and also 
	 * set the instance variables algorithm in the superclass.
	 * 
	 * @param pts - input array of points
	 */
	public QuickSorter(Point[] pts) throws IllegalArgumentException	{
		super(pts, "QuickSort");
	}


	/**
	 * Carry out quicksort on the points[] array in the AbstractSorter superclass.
	 */
	@Override
	public void sort() {
		Sorting.quickSort(super.points);
	}

	// /**
	//  * Operates on the subarray of points[] with indices between first and last.
	//  * 
	//  * @param first - starting index of the subarray
	//  * @param last - ending index of the subarray
	//  */
	// private void quickSortRec(int first, int last) {
	// 	Sorting.quickSort(ArrayView.range(super.points, first, last), super.pointComparator);
	// }

	// /**
	//  * Operates on the subarray of points[] with indices between first and last.
	//  * 
	//  * @param first
	//  * @param last
	//  * @return
	//  */
	// private int partition(int first, int last) {
	// 	return 0;
	// }


}
