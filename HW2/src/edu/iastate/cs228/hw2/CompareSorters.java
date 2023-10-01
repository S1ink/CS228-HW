package edu.iastate.cs228.hw2;

import java.io.File;

//import java.io.File;

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

		final Scanner s = new Scanner(System.in);
		final Random r = new Random();
		for(int t = 1;; t++) {

			System.out.print("Options: 1) Random generation, 2) Load from file, 3) Exit\n-->");
			int op = 0;
			Point[] dataset = null;
			try{
				op = s.nextInt();
				s.nextLine();
			} catch(Exception e) {
				System.out.println(e.getClass());
				s.nextLine();
//				continue;
			}
			switch(op) {
				case 1: {
					System.out.print("Enter the number of points to generate:\n-->");
					try {
						final int num = s.nextInt();
						dataset = generateRandomPoints(num, r);
					} catch(Exception e) {
						System.out.println(e.getClass());
					}
					break;
				}
				case 2: {
					System.out.print("Enter the file path to load:\n-->");
					try {
						final String fname = s.nextLine();
						dataset = PointScanner.deserializePoints(new File(fname));
					} catch(Exception e) {
						System.out.println(e.getClass());
					}
					break;
				}
				case 3: {
					s.close();
					return;
				}
				default: {
					System.out.println("Invalid operation code.");
					continue;
				}
			}
			if(dataset != null) {
				System.out.println(String.format("\nTrial %s:", t));
				final String[] stats = iterate(dataset);
				System.out.println(formatStats(stats));
				System.out.println("\n");
			} else {
				System.out.println("Sorting failed due to a dataset error.");
			}

		}
//		s.close();
		
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
	
	private static String[] iterate(Point[] dataset) {
		final Algorithm[] algos = Algorithm.values();
		final PointScanner[] scanners = new PointScanner[algos.length];
		final String[] outputs = new String[algos.length];
		for(int i = 0; i < scanners.length; i++) {
			try{
				scanners[i] = new PointScanner(dataset, algos[i]);
				scanners[i].scan();
				outputs[i] = scanners[i].stats();
			} catch(IllegalArgumentException e) {

			}
		}
		return outputs;
	}

	public static String formatStats(String[] stats) {

		final String[][] components = new String[stats.length][];
		int max_desc = 9, max_size = 4, max_time = 9;
		for(int i = 0; i < stats.length; i++) {
			components[i] = stats[i].split("\t", 3);
			final int
				desc = components[i][0].length(),
				size = components[i][1].length(),
				time = components[i][2].length();
			if(desc > max_desc) { max_desc = desc; }
			if(size > max_size) { max_size = size; }
			if(time > max_time) { max_time = time; }
		}
		String line = "\n";
		for(int i = 0; i < (max_desc + max_size + max_time + 6); i++) {
			line += "-";
		}
		String ret = String.format("%-" + max_desc + "s\t%-" + max_size + "s\t%s", "Algorithm", "Size", "Time (ns)");
		ret += line;
		for(String[] s : components) {
			ret += String.format("\n%-" + max_desc + "s\t%-" + max_size + "s\t%s", s[0], s[1], s[2]);
		}
		return ret + line;

	}


}
