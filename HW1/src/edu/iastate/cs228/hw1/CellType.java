package edu.iastate.cs228.hw1;


/**
 * @author Sam Richter
 * 
 * CellType 'wraps' the more basic {@link State} enum with additional linked properties
 * and functionality for converting between cell type representations.
 * This file also acts a container for static helpers dealing with type properties, rules, and conversion.
 */
public enum CellType {

	Reseller	(State.RESELLER,	'R', TownCell.RESELLER,	0),
	Empty		(State.EMPTY,		'E', TownCell.EMPTY,	0),
	Casual		(State.CASUAL,		'C', TownCell.CASUAL,	1),
	Outage		(State.OUTAGE,		'O', TownCell.OUTAGE,	0),
	Streamer	(State.STREAMER,	'S', TownCell.STREAMER,	0);


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
	 * @param idx the index to convert to a CellType
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
	 * @param idx the index to get the char representation of
	 * 
	 * @return the corresponding char value, or a null termination character if the index is not valid
	 */
	public static char idxToChar(int idx) {
		// get the enum representation
		CellType t = fromIdx(idx);
		// extract the char if the index was valid, else return null termination char
		return t != null ? t.getCharValue() : '\0';
	}



	/** 
	 * Test if alternate rule A applies given a neighborhood census.
	 * 
	 * @return whether or not alternate rule A applies to the given neighborhood census
	 */
	public static boolean altRuleA_Reseller(int[] census) {
		// "Any cell that (1) is not a Reseller or Outage and (2) and has (Number of Empty + Number of Outage neighbors less than or equal to 1) converts to Reseller"
		return (Math.max(CellType.Empty.getCount(census), 0) + Math.max(CellType.Outage.getCount(census), 0) <= 1);
	}
	/**
	 * Test if alternate rule B applies given a neighborhood census.
	 * 
	 * @return whether or not alternate rule B applies to the given neighborhood census
	 */
	public static boolean altRuleB_Streamer(int[] census) {
		// "If none of the above rules apply, any cell with 5 or more casual neighbors becomes a Streamer."
		return (CellType.Casual.getCount(census) >= 5);
	}


}
