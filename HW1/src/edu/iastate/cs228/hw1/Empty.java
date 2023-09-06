package edu.iastate.cs228.hw1;


/**
 * @author Sam Richter
 * 
 * The Empty class is the TownCell implementation representing an empty grid cell.
 */
public final class Empty extends TownCell {

	/**
	 * Create a Empty cell provided the town it is a part of and their location.
	 * 
	 * @param p the town plain that the cell is or will be apart of
	 * @param r the row component of the cell's location
	 * @param c the column component of the cell's location
	 */
	public Empty(Town p, int r, int c) {
		super(p, r, c);
	}
	/**
	 * Create a Empty cell by copying the town and location members from another cell.
	 * 
	 * @param t the TownCell which will be replaced
	 */
	public Empty(TownCell t) {
		this(t.plain, t.row, t.col);
	}


	/**
	 * Get the type of this cell as a {@link CellType}.
	 * 
	 * @return the enum value of {@link CellType.Empty}
	 */
	@Override
	public CellType type() {
		return CellType.Empty;
	}
	/**
	 * Get the type of this cell as a {@link State}.
	 * 
	 * @return the enum value of {@link State.EMPTY}
	 */
	@Override
	public State who() {
		return this.type().getAlt();
	}

	/**
	 * Perform iteration of this cell's location and get the updated cell that holds the same location.
	 * 
	 * @param tNew the next town plain
	 * 
	 * @return the TownCell that replaces this one in the next iteration's town plain
	 */
	@Override
	public TownCell next(Town tNew) {
		this.census(TownCell.nCensus);
		this.plain = tNew;
		// "Any cell that (1) is not a Reseller or Outage and (2) and has (Number of Empty + Number of Outage neighbors less than or equal to 1) converts to Reseller."
		if(CellType.altRuleA_Reseller(TownCell.nCensus)) {
			return new Reseller(this);
		// "If the cell was empty, then a Casual user takes it and it becomes a C."
		} else {
			return new Casual(this);
		}
	}


}