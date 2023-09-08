package edu.iastate.cs228.hw1;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;


/**
 * @author Sam Richter
 * 
 * The ResellerTest class tests all {@link Reseller} specific public methods.
 */
class ResellerTest {

	static Town town;
	static Reseller cell;
	
	/**
	 * @author Sam Richter
	 * 
	 * Setup the Town and Reseller instances used in multiple of the tests.
	 */
	@BeforeAll
	static void setup() {
		try {
			town = new Town("ISP4x4.txt");
		} catch(FileNotFoundException e) {
			town = null;
		}
		// use [0, 1] becuase that is 'R' in ISP4x4.txt
		cell = new Reseller(town, 0, 1);
	}

	/**
	 * Evaluate if both of Reseller's constructors work correctly.
	 */
	@Test
	void constructTest() {
		Reseller cell2 = new Reseller(cell);
		assertNotEquals(cell, null);
		assertNotEquals(cell2, null);
		assertNotEquals(cell, cell2);
	}
	/**
	 * Evaluate if the CellType returned is correct.
	 */
	@Test
	void typeTest() {
		assertEquals(cell.type(), CellType.Reseller);
	}
	/**
	 * Evaluate if the State returned is correct.
	 */
	@Test
	void whoTest() {
		assertEquals(cell.who(), State.RESELLER);
	}
	/**
	 * Evaluate if the cell can correctly iterate given a known correct next cell type (since we loaded the town from file).
	 */
	@Test
	void nextTest() {
		assertTrue( cell.next(town).type() == CellType.Empty );
	}


}
