package edu.iastate.cs228.hw1;


/**
 * @author Sam Richter
 * 
 * 
 */
public final class Empty extends TownCell {

	public Empty(Town p, int r, int c) {
		super(p, r, c);
	}
	public Empty(TownCell t) {
		this(t.plain, t.row, t.col);
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
		if(Town.altRuleA_Reseller(nCensus)) {
			return new Reseller(this);
		} else {
			return new Casual(this);
		}
	}


}