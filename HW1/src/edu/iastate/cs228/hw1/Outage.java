package edu.iastate.cs228.hw1;


/**
 * @author Sam Richter
 * 
 * 
 */
public final class Outage extends TownCell {

	public Outage(Town p, int r, int c) {
		super(p, r, c);
	}
	public Outage(TownCell t) {
		this(t.plain, t.row, t.col);
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