package edu.iastate.cs228.hw1;


/**
 * @author Sam Richter
 * 
 * The Streamer class
 */
public final class Streamer extends TownCell {

	public Streamer(Town p, int r, int c) {
		super(p, r, c);
	}
	public Streamer(TownCell t) {
		this(t.plain, t.row, t.col);
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
		if(Town.altRuleA_Reseller(nCensus)) {
			return new Reseller(this);
		} else if(CellType.Reseller.getCount(nCensus) >= 1) {
			return new Outage(this);
		} else if(CellType.Outage.getCount(nCensus) >= 1) {
			return new Empty(this);
		}  else if(Town.altRuleB_Streamer(nCensus)) {
			return new Streamer(this);
		} else {
			return this;
		}
	}


}