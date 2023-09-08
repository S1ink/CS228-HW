package edu.iastate.cs228.hw1;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;


/**
 * @author Sam Richter
 * 
 * The StreamerTest class tests all {@link Streamer} specific public methods.
 */
class StreamerTest {

	static Town town;
	static Streamer cell;
	
	/**
	 * Setup the Town and Streamer instances used in multiple of the tests.
	 */
	@BeforeAll
	static void setup() {
		try {
			town = new Town("ISP4x4.txt");
		} catch(FileNotFoundException e) {
			town = null;
		}
		// use [2, 1] becuase that is 'S' in ISP4x4.txt
		cell = new Streamer(town, 2, 1);
	}

	/**
	 * Evaluate if both of Streamer's constructors work correctly.
	 */
	@Test
	void constructTest() {
		Streamer cell2 = new Streamer(cell);
		assertNotEquals(cell, null);
		assertNotEquals(cell2, null);
		assertNotEquals(cell, cell2);
	}
	/**
	 * Evaluate if the CellType returned is correct.
	 */
	@Test
	void typeTest() {
		assertEquals(cell.type(), CellType.Streamer);
	}
	/**
	 * Evaluate if the State returned is correct.
	 */
	@Test
	void whoTest() {
		assertEquals(cell.who(), State.STREAMER);
	}
	/**
	 * Evaluate if the cell can correctly iterate given a known correct next cell type (since we loaded the town from file).
	 */
	@Test
	void nextTest() {
		assertTrue( cell.next(town).type() == CellType.Outage );
	}


}
