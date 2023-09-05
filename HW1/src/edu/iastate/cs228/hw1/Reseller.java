package edu.iastate.cs228.hw1;


/**
 * @author Sam Richter
 * 
 * 
 */
public final class Reseller extends TownCell {

	public Reseller(Town p, int r, int c) {
		super(p, r, c);
	}
	public Reseller(TownCell t) {
		this(t.plain, t.row, t.col);
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
		} else if(Town.altRuleB_Streamer(nCensus)) {
			return new Streamer(this);
		} else {
			return this;
		}
	}


}