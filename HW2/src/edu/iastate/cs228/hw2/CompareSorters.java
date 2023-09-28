package edu.iastate.cs228.hw2;

import java.io.File;

/**
 *  
 * @author
 *
 */

/**
 * 
 * This class executes four sorting algorithms: selection sort, insertion sort, mergesort, and
 * quicksort, over randomly generated integers as well integers from a file input. It compares the 
 * execution times of these algorithms on the same input. 
 *
 */

import java.io.FileNotFoundException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;


public class CompareSorters {

	private static String iterate(Point[] dataset) {
		final Algorithm[] algos = Algorithm.values();
		final PointScanner[] scanners = new PointScanner[algos.length];
		for(int i = 0; i < scanners.length; i++) {
			try{
				scanners[i] = new PointScanner(dataset, algos[i]);
				scanners[i].scan();
			} catch(IllegalArgumentException e) {

			}
		}
		

	}

	/**
	 * Repeatedly take integer sequences either randomly generated or read from files. 
	 * Use them as coordinates to construct points.  Scan these points with respect to their 
	 * median coordinate point four times, each time using a different sorting algorithm.  
	 * 
	 * @param args
	 **/
	public static void main(String[] args) throws FileNotFoundException
	{
		// TODO 
		// 
		// Conducts multiple rounds of comparison of four sorting algorithms.  Within each round, 
		// set up scanning as follows: 
		// 
		//    a) If asked to scan random points, calls generateRandomPoints() to initialize an array 
		//       of random points. 
		// 
		//    b) Reassigns to the array scanners[] (declared below) the references to four new 
		//       PointScanner objects, which are created using four different values  
		//       of the Algorithm type:  SelectionSort, InsertionSort, MergeSort and QuickSort. 
		// 
		// 	
		PointScanner[] scanners = new PointScanner[4];
		
		// For each input of points, do the following. 
		// 
		//     a) Initialize the array scanners[].  
		//
		//     b) Iterate through the array scanners[], and have every scanner call the scan() 
		//        method in the PointScanner class.  
		//
		//     c) After all four scans are done for the input, print out the statistics table from
		//		  section 2.
		//
		// A sample scenario is given in Section 2 of the project description. 


		// String output = "";
		// for(int i = 0; i < scanners.length; i++) {
		// 	try {
		// 		scanners[i] = new PointScanner(pts, Algorithm.values()[i]);
		// 	} catch (IllegalArgumentException e) {}
		// 	scanners[i].scan();
		// 	output += scanners[i].stats();
		// }
		
		
//		final String fname = "test.txt";
//		Point[] arr = null;
//		try {
//			arr = PointScanner.deserializePoints(new File(fname));
//			System.out.println("Parsed Points:\n" + Point.formatArray(arr));
//		} catch(Exception e) {
//			System.out.println("Serialization failed: " + e.getMessage());
//		}

		final Integer[]
			vals1 = new Integer[1000],
			vals2 = new Integer[1000],
			vals3 = new Integer[1000],
			vals4 = new Integer[1000];
		Random rand = new Random();
		for(int i = 0; i < 1000; i++) {
			vals1[i] = vals2[i] = vals3[i] = vals4[i] = rand.nextInt(100);
		}
		
		// final Integer[] vals1 = new Integer[]{0, 0, 98, 8, 4, 2, -937, 3, 2, 2, 4, 6, -10001};
		// final Integer[] vals2 = new Integer[]{0, 0, 98, 8, 4, 2, -937, 3, 2, 2, 4, 6, -10001};
		// final Integer[] vals3 = new Integer[]{0, 0, 98, 8, 4, 2, -937, 3, 2, 2, 4, 6, -10001};
		// final Integer[] vals4 = new Integer[]{0, 0, 98, 8, 4, 2, -937, 3, 2, 2, 4, 6, -10001};
//		MergeSorter.mergeSort(vals, (Integer a, Integer b)->{ return b - a; });
		// for(Integer v : vals1) {
		// 	System.out.println(v);
		// }
		final Comparator<Integer> comp = (Integer a, Integer b)->{ return a - b; };
		final long a = System.nanoTime();
		Sorting.insertionSort(vals1, comp);
		final long b = System.nanoTime();
		Sorting.selectionSort(vals2, comp);
		final long c = System.nanoTime();
		Sorting.mergeSort(vals3, comp);
		final long d = System.nanoTime();
		Sorting.quickSort(vals4, comp);
		final long e = System.nanoTime();
		
		System.out.println(String.format(
			"Insertion Sort:\t%d\n"+
			"Selection Sort:\t%d\n"+
			"Merge Sort:\t\t%d\n"+
			"Quick Sort:\t\t%d\n",
			(b - a), (c - b), (d - c), (e - d)
		));
		
		try {
			Files.write( // write to file
				Paths.get("debug.txt"), // get path from file
				Collections.singleton(Arrays.toString(vals3)), // transform array to collection using singleton
				Charset.forName("UTF-8") // formatting
			);
		} catch(Exception ex) {}
		
		// System.out.println("-------------");
		// for(Integer v : vals1) {
		// 	System.out.println(v);
		// }
		// System.out.println("-------------");
		// for(Integer v : vals2) {
		// 	System.out.println(v);
		// }
		// System.out.println("-------------");
		// for(Integer v : vals3) {
		// 	System.out.println(v);
		// }
		// System.out.println("-------------");
		// for(Integer v : vals4) {
		// 	System.out.println(v);
		// }
		
	}
	
	
	/**
	 * This method generates a given number of random points.
	 * The coordinates of these points are pseudo-random numbers within the range 
	 * [-50,50] � [-50,50]. Please refer to Section 3 on how such points can be generated.
	 * 
	 * Ought to be private. Made public for testing. 
	 * 
	 * @param numPts  	number of points
	 * @param rand      Random object to allow seeding of the random number generator
	 * @throws IllegalArgumentException if numPts < 1
	 */
	public static Point[] generateRandomPoints(int numPts, Random rand) throws IllegalArgumentException
	{ 
		if(numPts < 1) {
			throw new IllegalArgumentException("Array must include at least 1 Point!");
		}
		final Point[] arr = new Point[numPts];
		for(int i = 0; i < numPts; i++) {
			arr[i] = Point.genRange(50, rand);
		}
		return arr;
	}
	
}
