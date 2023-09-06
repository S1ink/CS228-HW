package edu.iastate.cs228.hw1;


/**
 * @author Sam Richter
 * 
 * The Outage class is the TownCell implementation representing a cell that currently has an internet outage.
 */
public final class Outage extends TownCell {

	/**
	 * Create an Outage cell provided the town they are a part of and their location.
	 * 
	 * @param p the town plain that the cell is or will be apart of
	 * @param r the row component of the cell's location
	 * @param c the column component of the cell's location
	 */
	public Outage(Town p, int r, int c) {
		super(p, r, c);
	}
	/**
	 * Create an Outage cell by copying the town and location members from another cell.
	 * 
	 * @param t the TownCell which will be replaced
	 */
	public Outage(TownCell t) {
		this(t.plain, t.row, t.col);
	}


	/**
	 * Get the type of this cell as a {@link CellType}.
	 * 
	 * @return the enum value of {@link CellType.Outage}
	 */
	@Override
	public CellType type() {
		return CellType.Outage;
	}
	/**
	 * Get the type of this cell as a {@link State}.
	 * 
	 * @return the enum value of {@link State.OUTAGE}
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
		this.plain = tNew;
		// "An Outage cell becomes an Empty cell, meaning internet access is restored on the billing cycle after an outage."
		return new Empty(this);
	}


}