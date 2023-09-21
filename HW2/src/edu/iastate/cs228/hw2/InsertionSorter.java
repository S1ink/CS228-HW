package edu.iastate.cs228.hw2;

import java.io.FileNotFoundException;
import java.lang.NumberFormatException; 
import java.lang.IllegalArgumentException; 
import java.util.InputMismatchException;


/**
 *  
 * @author
 *
 */

/**
 * 
 * This class implements insertion sort.   
 *
 */

public class InsertionSorter extends AbstractSorter {

	// Other private instance variables if you need ... 

	/**
	 * Constructor takes an array of points.  It invokes the superclass constructor, and also 
	 * set the instance variables algorithm in the superclass.
	 * 
	 * @param pts 
	 */
	public InsertionSorter(Point[] pts) throws IllegalArgumentException {
		super(pts, "InsertionSort");
	}


	/** 
	 * Perform insertion sort on the array points[] of the parent class AbstractSorter.  
	 */
	@Override 
	public void sort() {
		for(int i = 1; i < super.points.length; i++) {
			int p = i;
			while(p > 0 &&
				super.pointComparator.compare(	super.points[p],
												super.points[p - 1] ) < 0
			) {
				super.swap(p, p - 1);
				p--;
			}
		}
	}


}
