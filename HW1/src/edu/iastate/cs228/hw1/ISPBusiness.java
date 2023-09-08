package edu.iastate.cs228.hw1;

import java.io.FileNotFoundException;
import java.util.Scanner;


/**
 * @author Sam Richter
 *
 * The ISPBusiness class performs simulation over a grid 
 * plain with cells occupied by different TownCell types.
 */
public class ISPBusiness {
	
	/**
	 * Returns a new Town object with updated grid value for next billing cycle.
	 * 
	 * @param tOld: old/current Town object.
	 * @return: New town object.
	 */
	public static Town updatePlain(Town tOld) {
		// create a new town of the same size
		Town tNew = new Town(tOld.getLength(), tOld.getWidth());
		// loop over each cell
		for(int r = 0; r < tOld.getLength(); r++) {
			for(int c = 0; c < tOld.getWidth(); c++) {
				// get each cell in the new grid from the current cell's/grid's state
				tNew.grid[r][c] = tOld.grid[r][c].next(tNew);
			}
		}
		return tNew;
	}
	/**
	 * Returns the profit for the current state in the town grid.
	 * 
	 * @param town the town instance on which to sum the profits
	 * @return the total profit of all cells in the town, in dollars
	 */
	public static int getProfit(Town town) {
		return town.sumProfit();
	}
	/**
	 * Return the maximum potential profit of the town in a single billing period based on its grid size.
	 * 
	 * @param town the town instance on which to find the maximum profit
	 * @return the maximum potential profit of the town 
	 */
	public static int maxProfit(Town town) {
		return town.getArea();
	}



	/**
	 * Run a single iteration of the simulation. This is seperated out of the main method so that the 
	 * application could easily be converted to be loopable.
	 * 
	 * @param input a scanner connected to System.in
	 * @param town the town to simulate
	 * @param debug whether or not to print debug info to System.out (ie. the grid state during each iteration)
	 */
	public static void simulate(Scanner input, Town town, boolean debug) {

		System.out.print("Please select grid generation method:\n\t1. From file\n\t2. Randomly seeded\n--> ");

		int sel = 0;
		try {
			sel = input.nextInt();
			// if(input.hasNextLine()) { input.nextLine(); }
		} catch(Exception e) {
			sel = -1;
		}

		// handle file parse errors for when the program is being used by humans --> ex. a PDF file gets passed in as the file to load...
		try {
			// option 1: load from file diologue
			if(sel == 1) {
				System.out.print("Enter the path to file:\n--> ");
				// get the file path from input
				final String f = input.nextLine();
				try {
					// if the town instance is empty, create a new one using the file, else regenerate using the file
					town = (town == null) ? new Town(f) : town.restart(f);
				} catch(FileNotFoundException e) {
					System.out.println("Error opening file path: " + e.getMessage());
					return;
				}
			// option 2: random generation diologue
			} else if(sel == 2) {
				System.out.print("Enter the # of rows, # of columns, and a seed:\n--> ");
				// get the rows, columns, and seed from input
				final int
					r = input.nextInt(),
					c = input.nextInt(),
					x = input.nextInt();
				// if the town instance is empty, generate a new one using the parameters, else regenerate using the parameters
				town = (town == null) ? new Town(r, c, x) : town.restart(r, c, x);
			// handle invalid inputs
			} else {
				System.out.println("Invalid option.");
				return;
			}
		} catch(Exception e) {
			System.out.println("Grid generation failed due to an exception. >> " + e.getMessage());
			return;
		}

		int max = 0, sum = 0;
		Town previous = town;
		if(debug) { System.out.println("\n+------ Simulating ------>\n"); }
		// iterate the grid over 12 months and keep track of the profit and max potential profit
		for(int m = 0; m < 12; m++) {
			if(debug) { System.out.println("Before billing period #" + (m + 1) + ":\n" + previous); }
			max += ISPBusiness.maxProfit(previous);
			previous = ISPBusiness.updatePlain(previous);
			sum += ISPBusiness.getProfit(previous);
		}
		if(debug) { System.out.println("End:\n" + previous); }
		// get the overall utilization as a percent
		double utilization = (double)sum / max * 100.0;
		// "this should be the only thing printed"
		System.out.println(String.format("%,.2f%%", utilization));

	}





	/**
	 *  Main method. Interact with the user and ask if user wants to specify elements of grid
	 *  via an input file (option: 1) or wants to generate it randomly (option: 2).
	 *  
	 *  Depending on the user choice, create the Town object using respective constructor and
	 *  if user choice is to populate it randomly, then populate the grid here.
	 *  
	 *  Finally: For 12 billing cycle calculate the profit and update town object (for each cycle).
	 *  Print the final profit in terms of %. You should print the profit percentage
	 *  with two digits after the decimal point:  Example if profit is 35.5600004, your output
	 *  should be:
	 *
	 *	35.56%
	 *  
	 * Note that this method does not throw any exception, so you need to handle all the exceptions
	 * in it.
	 * 
	 * @param args the command line arguments for this program
	 * 
	 */
	public static void main(String []args) {

		final Scanner s = new Scanner(System.in);
		Town town = null;
		
		if(RUN_VERBOSE) {
			for(;;) {
				simulate(s, town, true);
				System.out.print("\nEnter 1 to restart, otherwise enter any number to exit:\n--> ");
				if(s.nextInt() != 1) {
					break;
				}
				System.out.println("\n");
			}
		} else {
			simulate(s, town, false);
		}
		
		s.close();

	}
	
	private static final boolean RUN_VERBOSE = false;


}
