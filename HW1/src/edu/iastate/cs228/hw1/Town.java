package edu.iastate.cs228.hw1;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Objects;
import java.util.Random;
import java.util.Scanner;
// import java.lang.Runnable;


/**
 *  @author <<Write your name here>>
 *
 */
public class Town {

	private int length, width;  //Row and col (first and second indices)
	public TownCell[][] grid;


	private static interface CellOperation {
		public void apply(TownCell t, int r, int c);
	}
	private static void iterateCells(TownCell[][] grid, CellOperation op) {
		for(int i = 0; i < grid.length; i++) {
			for(int j = 0; j < grid[i].length; j++) {
				op.apply(grid[i][j], i, j);
			}
		}
	}

	/** Reallocate a new grid of size l*w */
	private void reallocGrid(int l, int w) {
		this.length = l;
		this.width = w;
		this.grid = new TownCell[l][w];
	}
	/** Create a new TownCell descendant type based on the character encoding */
	private TownCell makeCellType(char id, int r, int c) {
		switch(id) {
			case 'C': return new TownCell.Casual(this, r, c);
			case 'S': return new TownCell.Streamer(this, r, c);
			case 'R': return new TownCell.Reseller(this, r, c);
			case 'E': return new TownCell.Empty(this, r, c);
			case 'O': return new TownCell.Outage(this, r, c);
			default: return null;
		}
	}
	/**  */
	private void deserializeGrid(File f) throws FileNotFoundException {
		final Scanner s = new Scanner(f);
		final int
			l = s.nextInt(),
			w = s.nextInt();
		this.reallocGrid(l, w);
		if(s.hasNextLine()) { s.nextLine(); }
		for(int i = 0; i < this.length; i++) {
			final String line = s.nextLine().replaceAll("\\s", "");
			for(int j = 0; j < this.width; j++) {
				final char c = line.charAt(j);
				this.grid[i][j] = this.makeCellType(c, i, j);
			}
		}
		s.close();
	}

	public static int[] pollNeighborhood(Town town, int r, int c, int[] type_count)
		{ return pollNeighborhood(town.grid, r, c, type_count); }
	public static int[] pollNeighborhood(TownCell[][] grid, int r, int c, int[] type_count) {
		Objects.requireNonNull(grid, "TownCell[][] grid cannot be null!");
		if(type_count == null || type_count.length < TownCell.NUM_CELL_TYPE) {
			type_count = new int[5];
		} else {
			for(int i = 0; i < TownCell.NUM_CELL_TYPE; i++) {
				type_count[i] = 0;
			}
		}
		final int
			ly = Math.max(0, r - 1),
			hy = Math.min(grid.length - 1, r + 1),
			lx = Math.max(0, c - 1),
			hx = Math.min(grid[0].length - 1, c + 1);
		for(int _r = ly; _r <= hy; _r++) {
			for(int _c = lx; _c <= hx; _c++) {
				if(_r != r || _c != c) { 
					type_count[grid[_r][_c].typeIndex()] += 1;
				}
			}
		}
		return type_count;
	}





	/** PUBLIC INTERFACE */

	/**
	 * Constructor to be used when user wants to generate grid randomly, with the given seed.
	 * This constructor does not populate each cell of the grid (but should assign a 2D array to it).
	 * @param length
	 * @param width
	 */
	public Town(int length, int width) {
		//TODO: Write your code here.
		this.reallocGrid(length, width);
	}

	/** 
	 * An additional constructor override for conveniently initializing the grid and also 
	 * seeding each cell.
	 * @param length
	 * @param width
	 * @param seed
	*/
	public Town(int length, int width, int seed) {
		this.restart(length, width, seed);
	}
	
	/**
	 * Constructor to be used when user wants to populate grid based on a file.
	 * Please see that it simple throws FileNotFoundException exception instead of catching it.
	 * Ensure that you close any resources (like file or scanner) which is opened in this function.
	 * @param inputFileName
	 * @throws FileNotFoundException
	 */
	public Town(String inputFileName) throws FileNotFoundException {
		//TODO: Write your code here.
		this.restart(inputFileName);
	}


	/**
	 * Regenerate the grid given the specified size and initialize each cell randomly from a seed.
	 * @param l
	 * @param w
	 * @param seed
	 * @return the current instance so that operations can be chained
	 */
	public Town restart(int l, int w, int seed) {
		this.reallocGrid(l, w);
		this.randomInit(seed);
		return this;
	}
	/** 
	 * Regenerate the grid from a config file.
	 * @param fname
	 * @return the current instance so that operations can be chained
	 * @throws FileNotFoundException
	 */
	public Town restart(String fname) throws FileNotFoundException {
		this.deserializeGrid(new File(fname));
		return this;
	}
	
	/**
	 * Returns width of the grid.
	 * @return
	 */
	public int getWidth() {
		//TODO: Write/update your code here.
		return this.width;
	}
	/**
	 * Returns length of the grid.
	 * @return
	 */
	public int getLength() {
		//TODO: Write/update your code here.
		return this.length;
	}
	/** 
	 * Returns the total area of the grid. Useful for finding the potential maximum profit.
	 * @return
	*/
	public int getArea() {
		return this.length * this.width;
	}

	/**
	 * Initialize the grid by randomly assigning cell with one of the following class object:
	 * Casual, Empty, Outage, Reseller OR Streamer
	 * @param seed
	 */
	public void randomInit(int seed) {
		final Random rand = new Random(seed);
		//TODO: Write your code here.
		iterateCells(this.grid, (TownCell cell, int r, int c)->{
			this.grid[r][c] = this.makeCellType( CellType.idxToValue( rand.nextInt(5) ), r, c );
		});
	}
	/**
	 * Get the total profit from all the cells in the grid.
	 * @return the sum profit, in dollars
	 */
	public int sumProfit() {
		int profit = 0;
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
	 */
	@Override
	public String toString() {
		String s = "";
		//TODO: Write your code here.
		for(int r = 0; r < this.length; r++) {
			for(int c = 0; c < this.width; c++) {
				s += this.grid[r][c].charValue() + " ";
			}
			s += "\n";
		}
		return s;
	}
}
