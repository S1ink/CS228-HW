package edu.iastate.cs228.hw1.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

import edu.iastate.cs228.hw1.*;


/**
 * The CasualTest class tests all {@link Casual} specific public methods.
 */
class CasualTest {
	
	static Town town;
	static Casual cell;
	
	/**
	 * Setup the Town and Casual instances used in multiple of the tests.
	 */
	@BeforeAll
	static void setup() {
		try {
			town = new Town("ISP4x4.txt");
		} catch(FileNotFoundException e) {
			town = null;
		}
		// use [1, 2] becuase that is 'C' in ISP4x4.txt
		cell = new Casual(town, 1, 2);
	}

	/**
	 * Evaluate if both of Casual's constructors work correctly.
	 */
	@Test
	void constructTest() {
		Casual cell2 = new Casual(cell);
		assertNotEquals(cell, null);
		assertNotEquals(cell2, null);
		assertNotEquals(cell, cell2);
	}
	/**
	 * Evaluate if the CellType returned is correct.
	 */
	@Test
	void typeTest() {
		assertEquals(cell.type(), CellType.Casual);
	}
	/**
	 * Evaluate if the State returned is correct.
	 */
	@Test
	void whoTest() {
		assertEquals(cell.who(), State.CASUAL);
	}
	/**
	 * Evaluate if the cell can correctly iterate given a known correct next cell type (since we loaded the town from file).
	 */
	@Test
	void nextTest() {
		assertTrue( cell.next(town).type() == CellType.Outage );
	}


}
