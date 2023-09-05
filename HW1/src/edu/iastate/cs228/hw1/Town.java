package edu.iastate.cs228.hw1;

import java.io.FileNotFoundException;
import java.io.File;
import java.util.Objects;
import java.util.Scanner;
import java.util.Random;


/**
 * @author Sam Richter
 *
 * Town contains a grid of TownCells and functionality for generating the grid,
 * taking a neighborhood census, and summing the grid's profit.
 */
public class Town {

	private int length, width;  //Row and col (first and second indices)
	public TownCell[][] grid;


	/**
	 * Constructor to be used when user wants to generate grid randomly, with the given seed.
	 * This constructor does not populate each cell of the grid (but should assign a 2D array to it).
	 * 
	 * @param length the number of rows in the grid
	 * @param width the number of columns in the grid
	 */
	public Town(int length, int width) {
		// reallocate a new grid of size [length * width]
		this.reallocGrid(length, width);
	}
	/** 
	 * An additional constructor override for conveniently initializing the grid and also 
	 * seeding each cell.
	 * 
	 * @param length the number of rows in the grid
	 * @param width the number of columns in the grid
	 * @param seed the seed to randomly populate the grid
	*/
	public Town(int length, int width, int seed) {
		// reallocate the grid then populate each cell randomly based on the seed
		this.restart(length, width, seed);
	}
	/**
	 * Constructor to be used when user wants to populate grid based on a file.
	 * Please see that it simple throws FileNotFoundException exception instead of catching it.
	 * Ensure that you close any resources (like file or scanner) which is opened in this function.
	 * 
	 * @param inputFileName the file to load the grid from
	 * @throws FileNotFoundException
	 */
	public Town(String inputFileName) throws FileNotFoundException {
		// attempt to read the file, then reallocate a new grid and fill each cell based on the encoded configuration
		this.restart(inputFileName);
	}


	/**
	 * Regenerate the grid given the specified size and populate it randomly using the provided seed.
	 * 
	 * @param l the number of rows in the grid
	 * @param w the number of columns in the grid
	 * @param seed the seed to randomly populate the grid
	 * 
	 * @return the Town current instance so that operations can be chained
	 */
	public Town restart(int l, int w, int seed) {
		// first, reallocate the grid's buffer
		this.reallocGrid(l, w);
		// second, randomly populate each cell
		this.randomInit(seed);
		// return the instance
		return this;
	}
	/** 
	 * Regenerate the grid from a config file.
	 * 
	 * @param fname the file path for the target grid configuration
	 * 
	 * @return the current instance so that operations can be chained
	 * @throws FileNotFoundException
	 */
	public Town restart(String fname) throws FileNotFoundException {
		// deserialize the file, reallocate the grid, and populate it
		this.deserializeGrid(new File(fname));
		// return the instance
		return this;
	}
	
	/**
	 * Returns width of the grid.
	 * 
	 * @return the number of cells across the top of the grid
	 */
	public int getWidth() {
		return this.width;
	}
	/**
	 * Returns length of the grid.
	 * 
	 * @return the number of cells down the side of the grid
	 */
	public int getLength() {
		return this.length;
	}
	/** 
	 * Returns the total area of the grid. Useful for finding the potential maximum profit.
	 * 
	 * @return the total number of cells in the grid
	*/
	public int getArea() {
		return this.length * this.width;
	}

	/**
	 * Initialize the grid by randomly assigning cell with one of the following class object:
	 * Casual, Empty, Outage, Reseller OR Streamer
	 * 
	 * @param seed the seed for randomly populating the grid
	 */
	public void randomInit(int seed) {
		final Random rand = new Random(seed);
		// loop over each cell and assign a new TownCell object
		for(int r = 0; r < this.length; r++) {
			for(int c = 0; c < this.width; c++) {
				// generate a random index, convert to char representation, and use the switch/case mapping method to generate the correct type
				this.grid[r][c] = this.makeCellType( CellType.idxToChar( rand.nextInt(5) ), r, c );
			}
		}
	}
	/**
	 * Get the total profit from all the cells in the grid.
	 * 
	 * @return the sum profit, in dollars
	 */
	public int sumProfit() {
		int profit = 0;
		// loop over each cell and add it's profit to the total
		for(TownCell[] ta : this.grid) {
			for(TownCell t : ta) {
				profit += t.profit();
			}
		}
		return profit;
	}
	
	/**
	 * Output the town grid. For each square, output the first letter of the cell type.
	 * Each letter should be separated either by a single space or a tab.
	 * And each row should be in a new line. There should not be any extra line between 
	 * the rows.
	 * 
	 * @return the String representation of the Town's grid
	 */
	@Override
	public String toString() {
		String s = "";
		// loop over each cell in the grid
		for(int r = 0; r < this.length; r++) {
			for(int c = 0; c < this.width; c++) {
				// get the char value and append
				s += this.grid[r][c].charValue() + " ";
			}
			// add a newline after each row
			s += "\n";
		}
		return s;
	}




	/**
	 * Reallocate this town's grid to be of size [l * w]
	 * 
	 * @param l the number of rows in the grid
	 * @param w the number of columns in the grid
	 */
	private void reallocGrid(int l, int w) {
		this.length = l;
		this.width = w;
		this.grid = new TownCell[l][w];
	}
	/**
	 * Create a new TownCell of the type represented by the provided character, at location [r, c]
	 * 
	 * @param id the char representation of the type to be created
	 * @param r the row of the location that the cell will be put
	 * @param c the column of the location that the cell will be put
	 * 
	 * @return the newly created cell, upcast back to the TownCell abstract type, or null if the char value is not valid
	 */
	private TownCell makeCellType(char id, int r, int c) {
		// use a switch/case to map each character representation to the constructor of each type
		switch(id) {
			case 'C': return new Casual(this, r, c);
			case 'S': return new Streamer(this, r, c);
			case 'R': return new Reseller(this, r, c);
			case 'E': return new Empty(this, r, c);
			case 'O': return new Outage(this, r, c);
			default: return null;
		}
	}
	/**
	 * Reallocate and populate the grid based on a configuration file.
	 * 
	 * @param f the file path to the configuration
	 * 
	 * @throws FileNotFoundException
	 */
	private void deserializeGrid(File f) throws FileNotFoundException {
		final Scanner s = new Scanner(f);
		// the first 2 integers will be the size of the grid
		final int
			l = s.nextInt(),
			w = s.nextInt();
		// reallocate the grid to be the correct size
		this.reallocGrid(l, w);
		// clear any additional characters if present
		if(s.hasNextLine()) { s.nextLine(); }
		// loop through each line based on the previously read row count of the grid
		for(int i = 0; i < this.length; i++) {
			// extract the line, get rid of all whitespace
			final String line = s.nextLine().replaceAll("\\s", "");
			// loop through each cell in the row based on the previously read column count of the grid
			for(int j = 0; j < this.width; j++) {
				// extract the char for each cell
				final char c = line.charAt(j);
				// create a new cell of the correct type using the switch/case mapping
				this.grid[i][j] = this.makeCellType(c, i, j);
			}
		}
		// close the file scanner
		s.close();
	}



	/**
	 * Find the population of each of the different cell types within the(maximum size of) 3x3 area
	 * around the provided location, not counting the "callee's" location cell type. This method overloads
	 * and wraps the method of the same name and covers the more general case of a basic TownCell[][] grid
	 * representation.
	 * 
	 * @param town the town to apply the search within
	 * @param r the row of the callee's location
	 * @param c the column of the callee's location
	 * @param type_count the array to which the census results will be copied
	 * 
	 * @return the census results array - if type_count was not the correct size, then this will be a newly allocated array of the correct length
	 */
	public static int[] pollNeighborhood(Town town, int r, int c, int[] type_count)
		{ return pollNeighborhood(town.grid, r, c, type_count); }
	/**
	 * Find the population of each of the different cell types within the(maximum size of) 3x3 area
	 * around the provided location, not counting the "callee's" location cell type.
	 * 
	 * @param grid the grid to apply the search within
	 * @param r the row of the callee's location
	 * @param c the column of the callee's location
	 * @param type_count the array to which the census results will be copied
	 * 
	 * @return the census results array - if type_count was not the correct size, then this will be a newly allocated array of the correct length
	 */
	public static int[] pollNeighborhood(TownCell[][] grid, int r, int c, int[] type_count) {
		// we cannot do anything if the grid is null
		Objects.requireNonNull(grid, "TownCell[][] grid cannot be null!");
		// make sure that type_count is valid and (at least) the minimum required length
		if(type_count == null || type_count.length < TownCell.NUM_CELL_TYPE) {
			type_count = new int[5];
		} else {
			// if the census array was not freshly allocated, zero the elements up to the total number of types
			for(int i = 0; i < TownCell.NUM_CELL_TYPE; i++) {
				type_count[i] = 0;
			}
		}
		// find the minimum and maximum bounds for the neighborhood seach area
		final int
			ly = Math.max(0, r - 1),
			hy = Math.min(grid.length - 1, r + 1),
			lx = Math.max(0, c - 1),
			hx = Math.min(grid[0].length - 1, c + 1);
		// loop over each cell in the neighborhood
		for(int _r = ly; _r <= hy; _r++) {
			for(int _c = lx; _c <= hx; _c++) {
				// if the cell is not the 'center' cell
				if(_r != r || _c != c) {
					// get the type of the cell and update the count of that type
					type_count[grid[_r][_c].typeIndex()] += 1;
				}
			}
		}
		// return the census array
		return type_count;
	}

	/** 
	 * Test if alternate rule A applies given a neighborhood census.
	 * 
	 * @return whether or not alternate rule A applies to the given neighborhood census
	 */
	public static boolean altRuleA_Reseller(int[] census) {
		return (CellType.Empty.getCount(census) + CellType.Outage.getCount(census) <= 1);
	}
	/**
	 * Test if alternate rule B applies given a neighborhood census.
	 * 
	 * @return whether or not alternate rule B applies to the given neighborhood census
	 */
	public static boolean altRuleB_Streamer(int[] census) {
		return (CellType.Casual.getCount(census) >= 5);
	}


}
