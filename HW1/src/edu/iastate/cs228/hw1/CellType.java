package edu.iastate.cs228.hw1;


/**
 * @author Sam Richter
 * 
 * CellType 'wraps' the more basic {@link State} enum with additional
 * linked properties and functionality for converting between cell type representations,
 * as well as reordering the types to be more intuitive.
 */
public enum CellType {

	Casual		(State.CASUAL,		'C', 0, 1),
	Streamer	(State.STREAMER,	'S', 1, 0),
	Reseller	(State.RESELLER,	'R', 2, 0),
	Outage		(State.OUTAGE,		'O', 3, 0),
	Empty		(State.EMPTY,		'E', 4, 0);


	private final State alt;
	private final char value;
	private final int index, profit;

	private CellType(
		State s,
		char char_val,
		int idx,
		int profit
	) {
		this.alt = s;
		this.value = char_val;
		this.index = idx;
		this.profit = profit;
	}


	/**
	 * Get the {@link State} representation of this type.
	 * 
	 * @return the alternative value
	 */
	public State getAlt() { return this.alt; }
	/**
	 * Get the char representation of this type.
	 * 
	 * @return the char value
	 */
	public char getCharValue() { return this.value; }
	/**
	 * Get the integer index representation of this type.
	 * 
	 * @return the int value
	 */
	public int getIdx() { return this.index; }
	/**
	 * Get the profit correlated with this type.
	 * 
	 * @return the profit, in dollars
	 */
	public int getProfit() { return this.profit; }
	/**
	 * Count the number of this type of cell given a neighborhood census array.
	 * 
	 * @return the number of this type of cell as recorded in the census
	 */
	public int getCount(int[] type_count) {
		// check if the this type's index is within the census's length, if not return 0 by default
		return (type_count.length > this.index) ? type_count[this.index] : 0;
	}



	/**
	 * Attempt to convert an integer index to the corresponding CellType representation of the type.
	 * 
	 * @return the corresponding CellType value, or null if the index is not valid (ie. not in the range [0, # of types])
	 */
	public static CellType fromIdx(int idx) {
		final CellType[] vals = CellType.values();
		// check if the index is in bounds, if so return the value at that index of the array of all types
		return (idx >= 0 && idx < vals.length) ? vals[idx] : null;
	}
	/**
	 * Attempt to convert an integer index to the corresponding char representation of the type.
	 * 
	 * @return the corresponding char value, or a null termination character if the index is not valid
	 */
	public static char idxToChar(int idx) {
		// get the enum representation
		CellType t = fromIdx(idx);
		// extract the char if the index was valid, else return null termination char
		return t != null ? t.getCharValue() : '\0';
	}


}
