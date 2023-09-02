package edu.iastate.cs228.hw1;


/**
 * 
 * @author <<Write your name here>>
 *	Also provide appropriate comments for this class
 *
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
	 * @param counts of all customers
	 */
	public void census(int nCensus[]) {
		// zero the counts of all customers
		nCensus[RESELLER] = 0; 
		nCensus[EMPTY] = 0; 
		nCensus[CASUAL] = 0; 
		nCensus[OUTAGE] = 0; 
		nCensus[STREAMER] = 0; 

		//TODO: Write your code here.

	}

	/**
	 * Gets the identity of the cell.
	 * 
	 * @return State
	 */
	public abstract State who();

	/**
	 * Determines the cell type in the next cycle.
	 * 
	 * @param tNew: town of the next cycle
	 * @return TownCell
	 */
	public abstract TownCell next(Town tNew);

	public int profit() {
		return this.who() == State.CASUAL ? 1 : 0;
	}
	public abstract char getCharID();
	public abstract int getIDX();












	/** >> TOWNCELL IMPLEMENTATIONS >> */

	public static class Casual extends TownCell {

		public Casual(Town p, int r, int c) {
			super(p, r, c);
		}

		@Override
		public State who() {
			return State.CASUAL;
		}
		@Override
		public TownCell next(Town tNew) {

		}


	}
	public static class Streamer extends TownCell {

		public Streamer(Town p, int r, int c) {
			super(p, r, c);
		}

		@Override
		public State who() {
			return State.STREAMER;
		}
		@Override
		public TownCell next(Town tNew) {

		}


	}
	public static class Reseller extends TownCell {

		public Reseller(Town p, int r, int c) {
			super(p, r, c);
		}

		@Override
		public State who() {
			return State.RESELLER;
		}
		@Override
		public TownCell next(Town tNew) {

		}


	}
	public static class Empty extends TownCell {

		public Empty(Town p, int r, int c) {
			super(p, r, c);
		}

		@Override
		public State who() {
			return State.EMPTY;
		}
		@Override
		public TownCell next(Town tNew) {

		}


	}
	public static class Outage extends TownCell {

		public Outage(Town p, int r, int c) {
			super(p, r, c);
		}

		@Override
		public State who() {
			return State.OUTAGE;
		}
		@Override
		public TownCell next(Town tNew) {

		}


	}


}
