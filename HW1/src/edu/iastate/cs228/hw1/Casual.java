package edu.iastate.cs228.hw1;


/**
 * @author Sam Richter
 * 
 * The Causal class is the TownCell extension representing a casual internet user.
 */
public final class Casual extends TownCell {

	public Casual(Town p, int r, int c) {
		super(p, r, c);
	}
	public Casual(TownCell t) {
		this(t.plain, t.row, t.col);
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
		if(Town.altRuleA_Reseller(nCensus)) {
			return new Reseller(this);
		} else if(CellType.Reseller.getCount(nCensus) >= 1) {
			return new Outage(this);
		} else if(CellType.Streamer.getCount(nCensus) >= 1) {
			return new Streamer(this);
		} else if(Town.altRuleB_Streamer(nCensus)) {
			return new Streamer(this);
		} else {
			return this;
		}
	}


}