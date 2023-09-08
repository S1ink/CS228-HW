package edu.iastate.cs228.hw1;

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


/**
 * @author Sam Richter
 * 
 * The TownCellTest class tests all the generic functionality shared by all TownCell descendants.
 */
class TownCellTest {

	static Town town;
	static TownCell[] tcells;

	/**
	 * Setup the Town and TownCell[] used by multiple tests.
	 */
	@BeforeAll
	static void setup() {
		try {
			town = new Town("ISP4x4.txt");
		} catch(FileNotFoundException e) {
			town = null;
		}
		tcells = new TownCell[TownCell.NUM_CELL_TYPE];
		tcells[0] = new Reseller(town, 0, 1);
		tcells[1] = new Empty(town, 1, 0);
		tcells[2] = new Casual(town, 1, 2);
		tcells[3] = new Outage(town, 0, 0);
		tcells[4] = new Streamer(town, 2, 1);
	}

	/**
	 * Test that all constructors are valid.
	 */
	@Test
	void constructTest() {
		for(TownCell t : tcells) {
			assertTrue(t != null);
		}
	}
	/**
	 * Tests that all calls to census() produce valid results.
	 */
	@Test
	void censusTest() {
		final int[] arr = new int[TownCell.NUM_CELL_TYPE];
		for(TownCell t : tcells) {
			t.census(arr);
			assertTrue(arr != null);
			for(int x : arr) {
				assertTrue(x >= 0 && x <= 8);
			}
		}
	}
	/**
	 * Tests that all profits are in a valid range.
	 */
	@Test
	void profitTest() {
		for(TownCell t : tcells) {
			assertTrue(t.profit() >= 0);
		}
	}
	/**
	 * Tests that all character representations are at least within the range of ASCII capital letters
	 */
	@Test
	void charValueTest() {
		for(TownCell t : tcells) {
			final char c = t.charValue();
			assertTrue(c >= 'A' && c <= 'Z');
		}
	}
	/**
	 * Tests that all type indices are within a valid range.
	 */
	@Test
	void typeIndexTest() {
		for(TownCell t : tcells) {
			final int x = t.typeIndex();
			assertTrue(x >= 0 && x < TownCell.NUM_CELL_TYPE);
		}
	}
	/**
	 * Tests that all CellType representations are valid.
	 */
	@Test
	void typeTest() {
		for(TownCell t : tcells) {
			assertTrue(t.type() != null);
		}
	}
	/**
	 * Tests that all State representations are valid.
	 */
	@Test
	void whoTest() {
		for(TownCell t : tcells) {
			assertTrue(t.who() != null);
		}
	}
	/**
	 * Tests that all calls to next() produce valid results.
	 */
	@Test
	void nextTest() {
		for(TownCell t : tcells) {
			assertTrue(t.next(town) != null);
		}
	}


}
