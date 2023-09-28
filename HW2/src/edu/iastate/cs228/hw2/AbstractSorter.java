package edu.iastate.cs228.hw2;

import java.lang.IllegalArgumentException;
import java.util.InputMismatchException;
import java.util.Comparator;
import java.io.FileNotFoundException;


/**
 * @author Sam Richter
 */

/**
 * This abstract class is extended by SelectionSort, InsertionSort, MergeSort, and QuickSort.
 * It stores the input (later the sorted) sequence.
 */
public abstract class AbstractSorter {

	protected Point[] points;	// array of points operated on by a sorting algorithm.
								// stores ordered points after a call to sort().
	protected String algorithm = null;	// "selection sort", "insertion sort", "mergesort", or
										// "quicksort". Initialized by a subclass constructor.
	protected Comparator<Point> pointComparator = null;


	/**
	 * This constructor accepts an array of points as input. Copy the points into the array points[]. 
	 * 
	 * @param  pts - input array of points
	 * @throws IllegalArgumentException if pts == null or pts.length == 0.
	 */
	protected AbstractSorter(Point[] pts) throws IllegalArgumentException {
		if(pts == null || pts.length == 0) {
			throw new IllegalArgumentException("Points array is empty or null!");
		}
		this.points = Point.deepCopy(pts);
	}
	/**
	 * Initialize the base, including the algorithm name variable.
	 * 
	 * @param pts - input array of points
	 * @param algo - the algorithm name
	 * @throws IllegalArgumentException if pts == null or pts.length == 0
	 */
	protected AbstractSorter(Point[] pts, String algo) throws IllegalArgumentException {
		this(pts);
		this.algorithm = algo;
	}


	/**
	 * Conduct sorting on the internal array of points. Implemented by child classes.
	 */
	public abstract void sort();

	/**
	 * Generates a comparator on the fly that compares by x-coordinate if order == 0, by y-coordinate
	 * if order == 1. Assign the comparator to the variable pointComparator.
	 * 
	 * @param order - 0: by x-coordinate OR 1: by y-coordinate
	 * @throws IllegalArgumentException if order is less than 0 or greater than 1
	 */
	public void setComparator(int order) throws IllegalArgumentException {
		if(order <= 1 && order >= 0) {
			final boolean x = (order == 0);
			Point.setXorY(x);
			this.pointComparator = x ? Point::compareXY : Point::compareYX;
		} else {
			throw new IllegalArgumentException("Order must be 1 or 0");
		}
	}

	/**
	 * Obtain the point in the array points[] that has median index
	 * 
	 * @return the median valued point
	 */
	public Point getMedian() {
		return points[(points.length - 1) / 2];
	}
	/**
	 * Copys the array points[] onto the array pts[].
	 * 
	 * @param pts - an array of points with length >= that of the internal array's length
	 */
	public void getPoints(Point[] pts) {
		if(pts != null) {
			if(pts.length >= this.points.length) {
				for(int i = 0; i < this.points.length; i++) {
					pts[i] = new Point(this.points[i]);
				}
			}
		}
	}

	/**
	 * Swaps the two elements indexed at i and j respectively in the array points[].
	 * 
	 * @param i - the index of the first element to be swapped
	 * @param j - the index of the second element to be swapped
	 */
	protected void swap(int i, int j) {
		final Point _i = this.points[i];
		this.points[i] = this.points[j];
		this.points[j] = _i;
	}


	/**
	 * Get the name of the underlying algorithm.
	 * 
	 * @return the algorithm name
	 */
	@Override
	public String toString() {
		return this.algorithm;
	}


}
