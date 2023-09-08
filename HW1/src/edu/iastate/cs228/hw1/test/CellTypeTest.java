package edu.iastate.cs228.hw1.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import edu.iastate.cs228.hw1.*;


/**
 * @author Sam Richter
 * 
 * The CellTypeTest class tests all the properties of all the values in the CellType enum.
 */
class CellTypeTest {

	/**
	 * Test that the correct alternate value is returned for each type.
	 */
	@Test
	void getAltTest() {
		assertTrue(CellType.Reseller.getAlt() == State.RESELLER);
		assertTrue(CellType.Empty.getAlt() == State.EMPTY);
		assertTrue(CellType.Casual.getAlt() == State.CASUAL);
		assertTrue(CellType.Outage.getAlt() == State.OUTAGE);
		assertTrue(CellType.Streamer.getAlt() == State.STREAMER);
	}
	/**
	 * Test that the correct character representation is returned for each type.
	 */
	@Test
	void getCharValueTest() {
		assertTrue(CellType.Reseller.getCharValue() == 'R');
		assertTrue(CellType.Empty.getCharValue() == 'E');
		assertTrue(CellType.Casual.getCharValue() == 'C');
		assertTrue(CellType.Outage.getCharValue() == 'O');
		assertTrue(CellType.Streamer.getCharValue() == 'S');
	}
	/**
	 * Test that the correct index representation is return for each type.
	 */
	@Test
	void getIdxTest() {
		assertTrue(CellType.Reseller.getIdx() == 0);
		assertTrue(CellType.Empty.getIdx() == 1);
		assertTrue(CellType.Casual.getIdx() == 2);
		assertTrue(CellType.Outage.getIdx() == 3);
		assertTrue(CellType.Streamer.getIdx() == 4);
	}
	/**
	 * Test that the profit associated with each type is correct.
	 */
	@Test
	void getProfitTest() {
		assertTrue(CellType.Reseller.getProfit() == 0);
		assertTrue(CellType.Empty.getProfit() == 0);
		assertTrue(CellType.Casual.getProfit() == 1);
		assertTrue(CellType.Outage.getProfit() == 0);
		assertTrue(CellType.Streamer.getProfit() == 0);
	}
	/**
	 * Test each type's ability to count it's population given an census array.
	 */
	@Test
	void getCountTest() {
		final int[] test = new int[]{7, 0, 9, 11, 2};
		assertTrue(CellType.Reseller.getCount(test) == 7);
		assertTrue(CellType.Empty.getCount(test) == 0);
		assertTrue(CellType.Casual.getCount(test) == 9);
		assertTrue(CellType.Outage.getCount(test) == 11);
		assertTrue(CellType.Streamer.getCount(test) == 2);
	}
	/**
	 * Test that conversions from index to CellType are all correct.
	 */
	@Test
	void fromIdxTest() {
		assertTrue(CellType.fromIdx(0) == CellType.Reseller);
		assertTrue(CellType.fromIdx(1) == CellType.Empty);
		assertTrue(CellType.fromIdx(2) == CellType.Casual);
		assertTrue(CellType.fromIdx(3) == CellType.Outage);
		assertTrue(CellType.fromIdx(4) == CellType.Streamer);
		assertTrue(CellType.fromIdx(5) == null && CellType.fromIdx(-1) == null);
	}
	/**
	 * Test that conversions from index to character are all correct.
	 */
	@Test
	void idxToCharTest() {
		assertTrue(CellType.idxToChar(0) == 'R');
		assertTrue(CellType.idxToChar(1) == 'E');
		assertTrue(CellType.idxToChar(2) == 'C');
		assertTrue(CellType.idxToChar(3) == 'O');
		assertTrue(CellType.idxToChar(4) == 'S');
	}
	/**
	 * Test that alt rule A produces correct results in a variety of census scenarios.
	 */
	@Test
	void altRuleATest() {
		final int[]
			test1 = new int[]{0, 0, 5, 0, 3},
			test2 = new int[]{0, 1, 0, 2, 0},
			test3 = new int[]{0, -3, 0, 2, 0};
		assertTrue(CellType.altRuleA_Reseller(test1) == true);
		assertTrue(CellType.altRuleA_Reseller(test2) == false);
		assertTrue(CellType.altRuleA_Reseller(test3) == true);
	}
	/**
	 * Test that alt rule B produces correct results in a variety of census scenarios.
	 */
	@Test
	void altRuleBTest() {
		final int[]
			test1 = new int[]{0, 0, 5, 0, 3},
			test2 = new int[]{0, 1, 0, 2, 0};
		assertTrue(CellType.altRuleB_Streamer(test1) == true);
		assertTrue(CellType.altRuleB_Streamer(test2) == false);
	}


}
