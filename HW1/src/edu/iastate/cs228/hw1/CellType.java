package edu.iastate.cs228.hw1;


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


	public State getAlt() { return this.alt; }
	public char getCharValue() { return this.value; }
	public int getIdx() { return this.index; }
	public int getProfit() { return this.profit; }
	public int getCount(int[] type_count) {
		return (type_count.length > this.index) ? type_count[this.index] : 0;
	}

	public static CellType fromIdx(int idx) {
		final CellType[] vals = CellType.values();
		return (idx >= 0 && idx < vals.length) ? vals[idx] : null;
	}
	public static char idxToValue(int idx) {
		CellType t = fromIdx(idx);
		return t != null ? t.getCharValue() : '\0';
	}


}
