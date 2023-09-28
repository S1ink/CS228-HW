package edu.iastate.cs228.hw2;

import java.lang.IllegalArgumentException;
import java.lang.NumberFormatException;
import java.util.InputMismatchException;
import java.util.Comparator;
import java.io.FileNotFoundException;


/**
 * @author Sam Richter
 */

/**
 * This class extends the AbstractSorter interface and represents the selection sort algorithm.
 */
public class SelectionSorter extends AbstractSorter {


	/**
	 * Constructor takes an array of points. It invokes the superclass constructor, and also 
	 * set the instance variables algorithm in the superclass.
	 * 
	 * @param pts - input array of points
	 */
	public SelectionSorter(Point[] pts) throws IllegalArgumentException {
		super(pts, "SelectionSort");
	}


	/** 
	 * Apply selection sort on the array points[] of the parent class AbstractSorter.
	 */
	@Override
	public void sort() {
		Sorting.selectionSort(super.points);
	}


}
