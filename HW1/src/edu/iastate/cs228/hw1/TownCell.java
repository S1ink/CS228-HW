package edu.iastate.cs228.hw1;


/**
 * 
 * @author <<Write your name here>>
 *	Also provide appropriate comments for this class
 *
 */
public abstract class TownCell {

	protected Town plain;
	protected int rows;
	protected int cols;
	
	
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
		rows = r;
		cols = c;
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
		// zero the counts of all customers
		// nCensus[RESELLER] = 0; 
		// nCensus[EMPTY] = 0; 
		// nCensus[CASUAL] = 0; 
		// nCensus[OUTAGE] = 0; 
		// nCensus[STREAMER] = 0; 

		//TODO: Write your code here.
		Town.pollNeighborhood(this.plain, this.rows, this.cols, nCensus);

	}

	/**
	 * Get the profit of the cell.
	 */
	public int profit() {
		return this.type().getProfit();
	}
	/** 
	 * Get the single char representation of the cell's type.
	*/
	public char charValue() {
		return this.type().getCharValue();
	}
	/** 
	 * Get the cell type's index.
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












	private static boolean altRuleA_Reseller(int[] census) {
		return (CellType.Empty.getCount(census) + CellType.Outage.getCount(census) <= 1);
	}
	private static boolean altRuleB_Streamer(int[] census) {
		return (CellType.Casual.getCount(census) >= 5);
	}



	/** >> TOWNCELL IMPLEMENTATIONS >> */

	public static final class Casual extends TownCell {

		public Casual(Town p, int r, int c) {
			super(p, r, c);
		}
		public Casual(TownCell t) {
			this(t.plain, t.rows, t.cols);
		}

		@Override
		public CellType type() {
			return CellType.Casual;
		}
		@Override
		public State who() {
			return this.type().getAlt();
		}
		@Override
		public TownCell next(Town tNew) {
			this.census(TownCell.nCensus);
			this.plain = tNew;
			if(altRuleA_Reseller(nCensus)) {
				return new Reseller(this);
			} else if(CellType.Reseller.getCount(nCensus) >= 1) {
				return new Outage(this);
			} else if(CellType.Streamer.getCount(nCensus) >= 1) {
				return new Streamer(this);
			} else if(altRuleB_Streamer(nCensus)) {
				return new Streamer(this);
			} else {
				return this;
			}
		}


	}
	public static final class Streamer extends TownCell {

		public Streamer(Town p, int r, int c) {
			super(p, r, c);
		}
		public Streamer(TownCell t) {
			this(t.plain, t.rows, t.cols);
		}

		@Override
		public CellType type() {
			return CellType.Streamer;
		}
		@Override
		public State who() {
			return this.type().getAlt();
		}
		@Override
		public TownCell next(Town tNew) {
			this.census(TownCell.nCensus);
			this.plain = tNew;
			if(altRuleA_Reseller(nCensus)) {
				return new Reseller(this);
			} else if(CellType.Reseller.getCount(nCensus) >= 1) {
				return new Outage(this);
			} else if(CellType.Outage.getCount(nCensus) >= 1) {
				return new Empty(this);
			}  else if(altRuleB_Streamer(nCensus)) {
				return new Streamer(this);
			} else {
				return this;
			}
		}


	}
	public static final class Reseller extends TownCell {

		public Reseller(Town p, int r, int c) {
			super(p, r, c);
		}
		public Reseller(TownCell t) {
			this(t.plain, t.rows, t.cols);
		}

		@Override
		public CellType type() {
			return CellType.Reseller;
		}
		@Override
		public State who() {
			return this.type().getAlt();
		}
		@Override
		public TownCell next(Town tNew) {
			this.census(TownCell.nCensus);
			this.plain = tNew;
			if(CellType.Casual.getCount(nCensus) <= 3) {
				return new Empty(this);
			} else if(CellType.Empty.getCount(nCensus) >= 3) {
				return new Empty(this);
			} else if(altRuleB_Streamer(nCensus)) {
				return new Streamer(this);
			} else {
				return this;
			}
		}


	}
	public static final class Outage extends TownCell {

		public Outage(Town p, int r, int c) {
			super(p, r, c);
		}
		public Outage(TownCell t) {
			this(t.plain, t.rows, t.cols);
		}

		@Override
		public CellType type() {
			return CellType.Outage;
		}
		@Override
		public State who() {
			return this.type().getAlt();
		}
		@Override
		public TownCell next(Town tNew) {
			this.plain = tNew;
			return new Empty(this);
		}


	}
	public static final class Empty extends TownCell {

		public Empty(Town p, int r, int c) {
			super(p, r, c);
		}
		public Empty(TownCell t) {
			this(t.plain, t.rows, t.cols);
		}

		@Override
		public CellType type() {
			return CellType.Empty;
		}
		@Override
		public State who() {
			return this.type().getAlt();
		}
		@Override
		public TownCell next(Town tNew) {
			this.census(nCensus);
			this.plain = tNew;
			if(altRuleA_Reseller(nCensus)) {
				return new Reseller(this);
			} else {
				return new Casual(this);
			}
		}


	}


}
