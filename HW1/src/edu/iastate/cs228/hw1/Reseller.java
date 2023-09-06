package edu.iastate.cs228.hw1;


/**
 * @author Sam Richter
 * 
 * The Reseller class is the TownCell implementation representing a reseller of internet service.
 */
public final class Reseller extends TownCell {

	/**
	 * Create a Reseller internet user provided the town they are a part of and their location.
	 * 
	 * @param p the town plain that the cell is or will be apart of
	 * @param r the row component of the cell's location
	 * @param c the column component of the cell's location
	 */
	public Reseller(Town p, int r, int c) {
		super(p, r, c);
	}
	/**
	 * Create a Reseller internet user by copying the town and location members from another cell.
	 * 
	 * @param t the TownCell which will be replaced
	 */
	public Reseller(TownCell t) {
		this(t.plain, t.row, t.col);
	}


	/**
	 * Get the type of this cell as a {@link CellType}.
	 * 
	 * @return the enum value of {@link CellType.Reseller}
	 */
	@Override
	public CellType type() {
		return CellType.Reseller;
	}
	/**
	 * Get the type of this cell as a {@link State}.
	 * 
	 * @return the enum value of {@link State.RESELLER}
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
		// "If there are 3 or fewer casual users in the neighborhood, then Reseller finds it unprofitable to maintain the business and leaves, making the cell Empty."
		if(CellType.Casual.getCount(TownCell.nCensus) <= 3) {	// could OR this together with the next statement but keeping them separate shows that they are separate rules
			return new Empty(this);
		// "Also, if there are 3 or more empty cells in the neighborhood, then the Reseller leaves, making the cell Empty. Resellers do not like unpopulated regions."
		} else if(CellType.Empty.getCount(TownCell.nCensus) >= 3) {
			return new Empty(this);
		// "If none of the above rules apply, any cell with 5 or more casual neighbors becomes a Streamer."
		} else if(CellType.altRuleB_Streamer(TownCell.nCensus)) {
			return new Streamer(this);
		// "If none of the rules apply, then the cell state remains unchanged for the next iteration."
		} else {
			return this;
		}
	}


}