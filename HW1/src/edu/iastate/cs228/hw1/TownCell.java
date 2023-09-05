package edu.iastate.cs228.hw1;


/**
 * @author Sam Richter
 * 
 * The TownCell abstract class defines all base behavior necessary for
 * simulation of the Town grid.
 */
public abstract class TownCell {

	protected Town plain;
	protected int row;
	protected int col;
	
	
	// constants to be used as indices.
	protected static final int RESELLER = 0;
	protected static final int EMPTY = 1;
	protected static final int CASUAL = 2;
	protected static final int OUTAGE = 3;
	protected static final int STREAMER = 4;
	
	public static final int NUM_CELL_TYPE = 5;
	
	//Use this static array to take census.
	public static final int[] nCensus = new int[NUM_CELL_TYPE];

	/** Create a TownCell in the plain p, at location (r, c) */
	public TownCell(Town p, int r, int c) {
		plain = p;
		row = r;
		col = c;
	}
	
	/**
	 * Checks all neighborhood cell types in the neighborhood.
	 * Refer to homework pdf for neighbor definitions (all adjacent
	 * neighbors excluding the center cell).
	 * Use who() method to get who is present in the neighborhood
	 *  
	 * @param nCensus counts of all customers
	 */
	public void census(int nCensus[]) {
		Town.pollNeighborhood(this.plain, this.row, this.col, nCensus);
	}

	/**
	 * Find the profit of the cell. By default this is dependant on
	 * the CellType enum but can be overloaded if necessary.
	 * 
	 * @return the profit in dollars
	 */
	public int profit() {
		return this.type().getProfit();
	}
	/** 
	 * Get the char representation of the cell type.
	 * 
	 * @return the char representation
	*/
	public char charValue() {
		return this.type().getCharValue();
	}
	/** 
	 * Get the index corresponding to the cell's type.
	 * 
	 * @return the integer index corresponding to the cell's type
	*/
	public int typeIndex() {
		return this.type().getIdx();
	}


	/**
	 * Gets the identity of the cell as a CellType.
	 * 
	 * @return the CellType representation of the cell's classification
	 */
	public abstract CellType type();
	/**
	 * Gets the identity of the cell.
	 * 
	 * @return State
	 */
	public abstract State who();

	/**
	 * Determines the cell type in the next cycle. Generates a new cell object and returns
	 * that if appropriate, otherwise returns itself while updating the town context.
	 * 
	 * @param tNew: town of the next cycle
	 * @return TownCell
	 */
	public abstract TownCell next(Town tNew);


}
