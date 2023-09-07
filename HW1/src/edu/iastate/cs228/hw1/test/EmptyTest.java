package edu.iastate.cs228.hw1.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

import edu.iastate.cs228.hw1.*;


/**
 * The EmptyTest class tests all {@link Empty} specific public methods.
 */
class EmptyTest {

	static Town town;
	static Empty cell;
	
	/**
	 * Setup the Town and Empty instances used in multiple of the tests.
	 */
	@BeforeAll
	static void setup() {
		try {
			town = new Town("ISP4x4.txt");
		} catch(FileNotFoundException e) {
			town = null;
		}
		// use [1, 0] becuase that is 'E' in ISP4x4.txt
		cell = new Empty(town, 1, 0);
	}

	/**
	 * Evaluate if both of Empty's constructors work correctly.
	 */
	@Test
	void constructTest() {
		Empty cell2 = new Empty(cell);
		assertNotEquals(cell, null);
		assertNotEquals(cell2, null);
		assertNotEquals(cell, cell2);
	}
	/**
	 * Evaluate if the CellType returned is correct.
	 */
	@Test
	void typeTest() {
		assertEquals(cell.type(), CellType.Empty);
	}
	/**
	 * Evaluate if the State returned is correct.
	 */
	@Test
	void whoTest() {
		assertEquals(cell.who(), State.EMPTY);
	}
	/**
	 * Evaluate if the cell can correctly iterate given a known correct next cell type (since we loaded the town from file).
	 */
	@Test
	void nextTest() {
		assertTrue( cell.next(town).type() == CellType.Casual );
	}


}
