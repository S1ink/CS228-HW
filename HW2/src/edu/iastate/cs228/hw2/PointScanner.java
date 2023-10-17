package edu.iastate.cs228.hw2;

import java.util.InputMismatchException;
import java.util.ArrayList;
import java.util.Scanner;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.File;


/**
 * @author Sam Richter
 */

/**
 * This class sorts all the points in an array of 2D points to determine a reference point whose x and y 
 * coordinates are respectively the medians of the x and y coordinates of the original points.
 * 
 * It records the employed sorting algorithm as well as the sorting time for comparison.
 */
public class PointScanner {

	private Point[] points;
	private Point medianCoordinatePoint = new Point();
	private Algorithm sortingAlgorithm;
	private String algo_name = "N/A";

	protected long scanTime;				// execution time in nanoseconds.


	/**
	 * This constructor accepts an array of points and one of the four sorting algorithms as input. Copy 
	 * the points into the array points[].
	 * 
	 * @param pts - input array of points
	 * @param algo - the algorithm to initialize when sorting
	 * @throws IllegalArgumentException if pts == null or pts.length == 0.
	 */
	public PointScanner(Point[] pts, Algorithm algo)
		throws IllegalArgumentException
	{
		if(pts == null || pts.length == 0) {
			throw new IllegalArgumentException("Points array is empty or null!");
		}
		this.points = Point.deepCopy(pts);
		this.sortingAlgorithm = algo;
	}

	/**
	 * This constructor reads points from a file.
	 * 
	 * @param inputFileName - the file name from which to load points
	 * @param algo - the algorithm to initialize when sorting
	 * @throws FileNotFoundException 
	 * @throws InputMismatchException   if the input file contains an odd number of integers
	 */
	protected PointScanner(String inputFileName, Algorithm algo)
		throws FileNotFoundException, InputMismatchException
	{
		this.points = deserializePoints(new File(inputFileName));
		this.sortingAlgorithm = algo;
	}


	/**
	 * Carry out two rounds of sorting using the algorithm designated by sortingAlgorithm as follows:
	 *    
	 *     a) Sort points[] by the x-coordinate to get the median x-coordinate.
	 *     b) Sort points[] again by the y-coordinate to get the median y-coordinate.
	 *     c) Construct medianCoordinatePoint using the obtained median x- and y-coordinates.
	 *  
	 * Based on the value of sortingAlgorithm, create an object of SelectionSorter, InsertionSorter, MergeSorter,
	 * or QuickSorter to carry out sorting.
	 */
	public void scan() {

		AbstractSorter sorter = genSorter(this.sortingAlgorithm, this.points);
		this.algo_name = sorter.toString();
		try{
			// sort with x-primary
			sorter.setComparator(0);
			final long a = System.nanoTime();
			sorter.sort();
			final long b = System.nanoTime();
			final int x = sorter.getMedian().getX();
			// sort with y-primary
			sorter.setComparator(1);
			final long c = System.nanoTime();
			sorter.sort();
			final long d = System.nanoTime();
			final int y = sorter.getMedian().getY();
			// combine
			this.medianCoordinatePoint = new Point(x, y);
			this.scanTime = (b - a) + (d - c);
		} catch(IllegalArgumentException e) {
			// won't ever happen with current usage
		}

	}

	/**
	 * Get performance statistics in the format: <sorting algorithm>\t<size>\t<time>
	 * 
	 * @return the statistics in a formatted string
	 */
	public String stats() {
		return String.format("%s\t%d\t%d", this.algo_name, this.points.length, this.scanTime);
	}
	/**
	 * This method, called after scanning, writes point data into a file by outputFileName. The format 
	 * of data in the file is the same as printed out from toString(). The file can help you verify 
	 * the full correctness of a sorting result and debug the underlying algorithm.
	 * 
	 * @param outputFileName - the file path to write the MCP
	 * @throws FileNotFoundException
	 */
	public void writeMCPToFile(String outputFileName) throws FileNotFoundException {
		try {
			Files.write(Paths.get(outputFileName), this.toString().getBytes());
		} catch(IOException e) {
			throw new FileNotFoundException("Could not find file: " + outputFileName);
		}
	}

	/**
	 * Write MCP after a call to scan(), in the format "MCP: (x, y)" The x and y coordinates of the point are displayed on the same line with exactly one blank space
	 * in between.
	 * 
	 * @return the formatted MCP string
	 */
	@Override
	public String toString() {
		return "MCP: " + (this.medianCoordinatePoint == null ? "(0, 0)" : this.medianCoordinatePoint.toString());
	}





	/**
	 * Generate a sorting instance based on the enumeration value provided.
	 * 
	 * @param a - the algorithm enumeration
	 * @param pts - the points that will be sorted
	 * @return an AbstractSorter referencing the created algorithm instance
	 */
	private static AbstractSorter genSorter(Algorithm a, Point[] pts) {
		switch(a) {
			case SelectionSort:	return new SelectionSorter(pts);
			case InsertionSort:	return new InsertionSorter(pts);
			case BubbleSort:	return new BubbleSorter(pts);
			case MergeSort:		return new MergeSorter(pts);
			case QuickSort:		return new QuickSorter(pts);
			default:			return null;
		}
	}
	/**
	 * Deserialize points from the provided file.
	 * 
	 * @param f - file to load points from
	 * @return a Points[] containing the points read from file - may be of size 0 if a failure occurred
	 * @throws FileNotFoundException when the file cannot be found
	 * @throws InputMismatchException when the number of raw data points is odd
	 */
	public static Point[] deserializePoints(File f) throws FileNotFoundException, InputMismatchException {
		final ArrayList<Integer> buff = new ArrayList<>();
		final Scanner s = new Scanner(f);
		try {
			// keep reading numbers until no more are left
			while(s.hasNextInt()) {
				buff.add(s.nextInt());
			}
		} catch(Exception e) {} finally {
			s.close();
		}
		// odd number of parsed numbers
		if(buff.size() % 2 > 0) {
			throw new InputMismatchException("Invalid number of data points!");
		}
		// create points using parsed data
		final Point[] ret = new Point[buff.size() / 2];
		for(int i = 0; i < ret.length; i++) {
			ret[i] = new Point(
				buff.get(i * 2 + 0),
				buff.get(i * 2 + 1)
			);
		}
		return ret;
	}


}
