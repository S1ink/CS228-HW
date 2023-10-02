package edu.iastate.cs228.hw2;

import java.lang.IllegalArgumentException;


/**
 * @author Sam Richter
 */

/**
 * This class extends the AbstractSorter interface and represents the insertion sort algorithm.
 */
public class InsertionSorter extends AbstractSorter {


	/**
	 * Constructor takes an array of points. It invokes the superclass constructor, and also 
	 * set the instance variables algorithm in the superclass.
	 * 
	 * @param pts - input array of points
	 */
	public InsertionSorter(Point[] pts) throws IllegalArgumentException {
		super(pts, "InsertionSort");
	}


	/** 
	 * Perform insertion sort on the array points[] of the parent class AbstractSorter.
	 */
	@Override 
	public void sort() {
		Sorting.insertionSort(super.points);
	}


}
