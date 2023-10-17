package edu.iastate.cs228.hw2;

import java.util.Scanner;
import java.util.Random;
import java.io.FileNotFoundException;
import java.io.File;


/**
 * @author Sam Richter
 */

/**
 * This class executes four sorting algorithms: selection sort, insertion sort, mergesort, and
 * quicksort, over randomly generated integers as well integers from a file input. It compares the 
 * execution times of these algorithms on the same input.
 */
public class CompareSorters {

	/**
	 * Repeatedly take integer sequences either randomly generated or read from files.
	 * Use them as coordinates to construct points. Scan these points with respect to their
	 * median coordinate point four times, each time using a different sorting algorithm.
	 * 
	 * @param args
	 **/
	public static void main(String[] args) throws FileNotFoundException {

		runIO(
			null,
			"Options: 1 - Random generation, 2 - Load from file, 3 - Exit\n--> ",
			"Enter the number of points to generate:\n--> ",
			"Enter the file path to load:\n--> ",
			"\nTrial %d:\n"
		);
		// runIO(
		// 	"Keys: 1 (random integers)  2 (file input)  3 (exit)\n",
		// 	"Trial %d: ",
		// 	"Enter number of random points: ",
		// 	"File name: ",
		// 	"\n"
		// );

	}



	/**
	 * Loop an indefinite number of trials based on user IO
	 * 
	 * @param init - whatever should be printed on startup
	 * @param trial - whatever should be printed before an option is expected
	 * @param rgen - whatever should be printed before a number of points is expected
	 * @param fload - whatever should be printed before a file path is expected
	 * @param inter - whatever should be printed before stats are outputed
	 * @throws FileNotFoundException when a file to read cannot be found
	 */
	private static void runIO(
		String init, String trial, String rgen, String fload, String inter
	) throws FileNotFoundException {
		
		if(init != null) { System.out.print(init); }
		final Scanner s = new Scanner(System.in);
		final Random r = new Random();
		// handle any and all funny business
		try {
			// loop indefinitely and keep track of the trial number
			for(int t = 1;; t++) {

				try { System.out.print(String.format(trial, t)); } catch(Exception e) {}
				int op = 0;
				Point[] dataset = null;
				// attempt to get the selected option
				try{
					op = s.nextInt();
				} catch(Exception e) {
					System.out.println("Failed to parse input :(\n>> " + e.toString() + "\n\n");
					continue;
				} finally {
					s.nextLine();
				}
				// fill the Points[] based on selected option
				switch(op) {
					// random generation
					case 1: {
						System.out.print(rgen);
						try {
							final int num = s.nextInt();
							dataset = generateRandomPoints(num, r);
						} catch(Exception e) {
							System.out.println("Failed to generate points :(\n>> " + e.toString() + "\n\n");
							continue;
						} finally {
							s.nextLine();
						}
						break;
					}
					// file input
					case 2: {
						System.out.print(fload);
						try {
							final String fname = s.nextLine();
							dataset = PointScanner.deserializePoints(new File(fname));
						} catch(FileNotFoundException e) {
							throw e;
						} catch(Exception e) {
							System.out.println("Failed to deserialize points :(\n>> " + e.toString() + "\n\n");
							continue;
						}
						break;
					}
					// exit
					case 3: {
						return;
					}
					// invalid option -- skip this round
					default: {
						System.out.println("Invalid operation code. Please try again.\n\n");
						continue;
					}
				}
				// run the scanners
				if(dataset != null) {
					try { System.out.print(String.format(inter, t)); } catch(Exception e) {}
					final String[] stats = iterate(dataset);
					System.out.println(formatStats(stats) + "\n\n");
				} else {
					System.out.println("Sorting failed due to a dataset error.\n\n");
				}

			}
		} catch(FileNotFoundException e) {
			throw e;
		} catch(Exception e) {
			System.out.println("Unrecoverable error occurred :(\n>> " + e.toString());
		} finally {
			s.close();
		}

	}

	/**
	 * This method generates a given number of random points.
	 * The coordinates of these points are pseudo-random numbers within the range 
	 * [-50,50] � [-50,50]. Please refer to Section 3 on how such points can be generated.
	 * 
	 * @param numPts - number of points
	 * @param rand - Random object to allow seeding of the random number generator
	 * @return the randomly generated points
	 * @throws IllegalArgumentException if numPts < 1
	 */
	private static Point[] generateRandomPoints(int numPts, Random rand) throws IllegalArgumentException {
		if(numPts < 1) {
			throw new IllegalArgumentException("Generated points array must include at least 1 point!");
		}
		final Point[] arr = new Point[numPts];
		for(int i = 0; i < numPts; i++) {
			arr[i] = Point.genRange(50, rand);
		}
		return arr;
	}

	/**
	 * Run all sorting algorithms on a set of points and provide the output of each.
	 * 
	 * @param dataset - the points to sort
	 * @return the outputs of each algorithm run
	 */
	private static String[] iterate(Point[] dataset) {
		final Algorithm[] algos = Algorithm.values();
		final PointScanner[] scanners = new PointScanner[algos.length];
		final String[] outputs = new String[algos.length];
		// generate a scanner for each algo, scan, and store stats
		for(int i = 0; i < scanners.length; i++) {
			try{
				scanners[i] = new PointScanner(dataset, algos[i]);
				scanners[i].scan();
				outputs[i] = scanners[i].stats();
//				System.out.println(scanners[i]);	// debug all sorting methods are the same
			} catch(IllegalArgumentException e) {
				// not that big of a deal - just continue
			}
		}
		return outputs;
	}

	/**
	 * Format multiple stats outputs to be aligned vertically (and append headings).
	 * 
	 * @param stats - an array of statistics outputs as formatted by PointScanner
	 * @return the fully formatted output
	 */
	public static String formatStats(String[] stats) {

		final String[][] components = new String[stats.length][];
		int max_desc = 9, max_size = 4, max_time = 9;
		// find the maximum length for each of the 3 sections
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
		// generate correctly sized dividers (assume tab size of 3 which seems to be a good middle ground)
		String line = "\n";
		for(int i = 0; i < (max_desc + max_size + max_time + 6); i++) {
			line += "-";
		}
		// generate headings
		String ret = String.format("%-" + max_desc + "s\t%-" + max_size + "s\t%s", "Algorithm", "Size", "Time (ns)");
		ret += line;
		// append each correctly formatted line
		for(String[] s : components) {
			ret += String.format("\n%-" + max_desc + "s\t%-" + max_size + "s\t%s", s[0], s[1], s[2]);
		}
		return ret + line;

	}


}
