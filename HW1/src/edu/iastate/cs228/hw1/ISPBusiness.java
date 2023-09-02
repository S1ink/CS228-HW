package edu.iastate.cs228.hw1;

import java.io.FileNotFoundException;
import java.util.Scanner;


/**
 * @author <<Write your name here>>
 *
 * The ISPBusiness class performs simulation over a grid 
 * plain with cells occupied by different TownCell types.
 *
 */
public class ISPBusiness {
	
	/**
	 * Returns a new Town object with updated grid value for next billing cycle.
	 * @param tOld: old/current Town object.
	 * @return: New town object.
	 */
	public static Town updatePlain(Town tOld) {
		Town tNew = new Town(tOld.getLength(), tOld.getWidth());
		//TODO: Write your code here.
		for(int r = 0; r < tOld.getLength(); r++) {
			for(int c = 0; c < tOld.getWidth(); c++) {
				tNew.grid[r][c] = tOld.grid[r][c].next(tNew);
			}
		}
		return tNew;
	}
	
	/**
	 * Returns the profit for the current state in the town grid.
	 * @param town
	 * @return
	 */
	public static int getProfit(Town town) {	// this should really be a Town member function
		//TODO: Write/update your code here.
		return town.sumProfit();
	}
	public static int maxProfit(Town town) {
		return town.getArea();
	}



	public static void simulate(Scanner input, Town town) {

		System.out.print("Please select grid generation method:\n\t1. From file\n\t2. Randomly seeded\n--> ");

		final int sel = input.nextInt();
		if(input.hasNextLine()) { input.nextLine(); }

		if(sel == 1) {
			System.out.print("Enter the path to file:\n--> ");
			final String f = input.nextLine();
			try {
				town = (town == null) ? new Town(f) : town.restart(f);
			} catch(FileNotFoundException e) {
				System.out.println("Error opening file path: " + e.getMessage());
				return;
			}
		} else if(sel == 2) {
			System.out.print("Enter the # of rows, # of columns, and a seed:\n--> ");
			final int
				r = input.nextInt(),
				c = input.nextInt(),
				x = input.nextInt();
			town = (town == null) ? new Town(r, c, x) : town.restart(r, c, x);
		} else {
			System.out.println("Invalid Option.");
			return;
		}

		// iterate the grid, calc the profit.
		int max = 0, sum = 0;
		Town previous = town;
		System.out.println("\n+------ Simulating ------>");
		for(int m = 0; m < 12; m++) {
			System.out.println("Before billing period #" + (m + 1) + ":");
			System.out.println(previous);
			max += ISPBusiness.maxProfit(previous);
			previous = ISPBusiness.updatePlain(previous);
			sum += ISPBusiness.getProfit(previous);
		}
		double utilization = (double)sum / max * 100.0;
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
	 * @param args
	 * 
	 */
	public static void main(String []args) {
		//TODO: Write your code here.
		final Scanner s = new Scanner(System.in);
		Town town = null;
		
		for(;;) {
			// clear scanner?
			simulate(s, town);
			System.out.print("\nEnter 1 to restart, otherwise press any key to exit:\n--> ");
			if(s.nextInt() != 1) {
				break;
			}
			System.out.println("\n");
		}
		
		s.close();

	}


}
