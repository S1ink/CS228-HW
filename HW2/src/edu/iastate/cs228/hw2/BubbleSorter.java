package edu.iastate.cs228.hw2;

import java.lang.IllegalArgumentException;


/**
 * @author Sam Richter
 */

/**
 * This class extends the AbstractSorter interface and represents the bubble sort algorithm.
 */
public class BubbleSorter extends AbstractSorter {


	/**
	 * Constructor takes an array of points. It invokes the superclass constructor, and also 
	 * set the instance variables algorithm in the superclass.
	 * 
	 * @param pts - input array of points
	 */
	public BubbleSorter(Point[] pts) throws IllegalArgumentException {
		super(pts, "BubbleSort");
	}


	/** 
	 * Perform bubble sort on the array points[] of the parent class AbstractSorter.
	 */
	@Override 
	public void sort() {
		Sorting.bubbleSort(super.points);
	}


}
