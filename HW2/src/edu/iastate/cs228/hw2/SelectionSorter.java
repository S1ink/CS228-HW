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
 * This class implements selection sort.   
 *
 */

public class SelectionSorter extends AbstractSorter {

	// Other private instance variables if you need ... 
	
	/**
	 * Constructor takes an array of points.  It invokes the superclass constructor, and also 
	 * set the instance variables algorithm in the superclass.
	 *  
	 * @param pts  
	 */
	public SelectionSorter(Point[] pts) throws IllegalArgumentException {
		super(pts, "SelectionSort");
	}


	/** 
	 * Apply selection sort on the array points[] of the parent class AbstractSorter.  
	 * 
	 */
	@Override 
	public void sort() {

		for(int i = 0; i < super.points.length - 1; i++) {

			int mi = i;
			for(int p = i + 1; p < super.points.length; p++) {
				if(super.pointComparator.compare(
					super.points[p],
					super.points[mi]
				) < 0) {
					mi = p;
				}
			}
			if(mi != i) {
				super.swap(i, mi);
			}

		}

	}


}
