package edu.iastate.cs228.hw1;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;


/**
 * @author Sam Richter
 * 
 * The OutageTest class tests all {@link Outage} specific public methods.
 */
class OutageTest {

	static Town town;
	static Outage cell;
	
	/**
	 * Setup the Town and Outage instances used in multiple of the tests.
	 */
	@BeforeAll
	static void setup() {
		try {
			town = new Town("ISP4x4.txt");
		} catch(FileNotFoundException e) {
			town = null;
		}
		// use [0, 0] becuase that is 'O' in ISP4x4.txt
		cell = new Outage(town, 0, 0);
	}

	/**
	 * Evaluate if both of Outage's constructors work correctly.
	 */
	@Test
	void constructTest() {
		Outage cell2 = new Outage(cell);
		assertNotEquals(cell, null);
		assertNotEquals(cell2, null);
		assertNotEquals(cell, cell2);
	}
	/**
	 * Evaluate if the CellType returned is correct.
	 */
	@Test
	void typeTest() {
		assertEquals(cell.type(), CellType.Outage);
	}
	/**
	 * Evaluate if the State returned is correct.
	 */
	@Test
	void whoTest() {
		assertEquals(cell.who(), State.OUTAGE);
	}
	/**
	 * Evaluate if the cell can correctly iterate given a known correct next cell type (since we loaded the town from file).
	 */
	@Test
	void nextTest() {
		assertTrue( cell.next(town).type() == CellType.Empty );
	}


}
