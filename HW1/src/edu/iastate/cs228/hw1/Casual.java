package edu.iastate.cs228.hw1;


/**
 * @author Sam Richter
 * 
 * The Causal class is the TownCell implementation representing a casual internet user.
 */
public final class Casual extends TownCell {

	/**
	 * Create a Causal internet user provided the town they are a part of and their location.
	 * 
	 * @param p the town plain that the cell is or will be apart of
	 * @param r the row component of the cell's location
	 * @param c the column component of the cell's location
	 */
	public Casual(Town p, int r, int c) {
		super(p, r, c);
	}
	/**
	 * Create a Causal internet user by copying the town and location members from another cell.
	 * 
	 * @param t the TownCell which will be replaced
	 */
	public Casual(TownCell t) {
		this(t.plain, t.row, t.col);
	}


	/**
	 * Get the type of this cell as a {@link CellType}.
	 * 
	 * @return the enum value of {@link CellType.Casual}
	 */
	@Override
	public CellType type() {
		return CellType.Casual;
	}
	/**
	 * Get the type of this cell as a {@link State}.
	 * 
	 * @return the enum value of {@link State.CASUAL}
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
		// "Any cell that (1) is not a Reseller or Outage and (2) and has (Number of Empty + Number of Outage neighbors less than or equal to 1) converts to Reseller"
		if(CellType.altRuleA_Reseller(TownCell.nCensus)) {
			return new Reseller(this);
		// "If there is any reseller in its neighborhood, then the reseller causes outage in the casual user cell. Thus, the state of the cell changes from C (Casual) to O (Outage)."
		} else if(CellType.Reseller.getCount(TownCell.nCensus) >= 1) {
			return new Outage(this);
		// "Otherwise, if there is any neighbor who is a streamer, then the casual user also becomes a streamer, in the hopes of making it big on the internet."
		} else if(CellType.Streamer.getCount(TownCell.nCensus) >= 1) {
			return new Streamer(this);
		// "If none of the above rules apply, any cell with 5 or more casual neighbors becomes a Streamer."
		} else if(CellType.altRuleB_Streamer(TownCell.nCensus)) {
			return new Streamer(this);
		// "If none of the rules apply, then the cell state remains unchanged for the next iteration."
		} else {
			return this;
		}
	}


}