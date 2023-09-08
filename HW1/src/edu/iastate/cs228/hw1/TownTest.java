package edu.iastate.cs228.hw1;

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


/**
 * @author Sam Richter
 * 
 * Tests all public instance methods and static helpers in the Town class.
 */
class TownTest {

	static Town town;

	/**
	 * Setup the Town instance used by multiple tests.
	 */
	@BeforeAll
	static void setup() {
		try {
			town = new Town("ISP4x4.txt");
		} catch(FileNotFoundException e) {
			town = null;
		}
	}

	/**
	 * Test if all the constructors can correctly generate a Town instance.
	 */
	@Test
	void constructTest() {
		Town
			t1 = new Town(4, 4),
			t2 = new Town(4, 4, 10),
			t3;
		try {
			t3 = new Town("ISP5x5.txt");
		} catch(FileNotFoundException e) {
			t3 = null;
		}
		assertTrue(t1 != null);
		assertTrue(t2 != null);
		assertTrue(t3 != null);
		assertTrue(t1.getLength() == 4);
		assertTrue(t1.getWidth() == 4);
		assertTrue(t2.getLength() == 4);
		assertTrue(t2.getWidth() == 4);
		assertTrue(t3.getLength() == 5);
		assertTrue(t3.getWidth() == 5);
		assertTrue(t1.grid != null);
		assertTrue(t2.grid != null);
		assertTrue(t3.grid != null);
		assertTrue(t1.grid.length == 4);
		assertTrue(t1.grid[0].length == 4);
		assertTrue(t2.grid.length == 4);
		assertTrue(t2.grid[0].length == 4);
		assertTrue(t3.grid.length == 5);
		assertTrue(t3.grid[0].length == 5);
	}
	/**
	 * Test if the grid can be regenerated randomly to valid state.
	 */
	@Test
	void restartRandomTest() {
		final Town t1 = new Town(1, 1).restart(4, 4, 10);
		assertTrue(t1.getLength() == 4);
		assertTrue(t1.getWidth() == 4);
		assertTrue(t1.grid.length == 4);
		assertTrue(t1.grid[0].length == 4);
		for(TownCell[] a : t1.grid) {
			for(TownCell b : a) {
				assertTrue(b != null);
			}
		}
	}
	/**
	 * Test if the grid can be correctly regenerated from a config file.
	*/
	@Test
	void restartFileTest() {
		final Town t1 = new Town(1, 1);
		try {
			t1.restart("ISP4x4.txt");
		} catch(FileNotFoundException e) {
			fail();
		}
		assertTrue(t1.getLength() == 4);
		assertTrue(t1.getWidth() == 4);
		assertTrue(t1.grid.length == 4);
		assertTrue(t1.grid[0].length == 4);
		assertTrue(
			town.toString().equals(
				"O R O R \n" +
				"E E C O \n" +
				"E S O S \n" +
				"E O R R \n"
			)
		);
	}
	/**
	 * Test if the returned length is correct.
	 */
	@Test
	void getLengthTest() {
		assertTrue(town.getLength() == 4);
	}
	/**
	 * Test if the returned width is correct.
	 */
	@Test
	void getWidthTest() {
		assertTrue(town.getWidth() == 4);
	}
	/**
	 * Test if the returned area is correct.
	 */
	@Test
	void getAreaTest() {
		assertTrue(town.getArea() == 16);
	}
	/**
	 * Test if all cells can be randomly initialized to a valid state.
	 */
	@Test
	void randomInitTest() {
		final Town t1 = new Town(3, 3);
		t1.randomInit(8);
		for(TownCell[] a : t1.grid) {
			for(TownCell b : a) {
				assertTrue(b != null);
			}
		}
	}
	/**
	 * Test if the profit can be correctly summed given a known grid state.
	 */
	@Test
	void sumProfitTest() {
		// there is only a single 'C' in the 4x4 file
		assertTrue(town.sumProfit() == 1);
	}
	/**
	 * Test if the string conversion works correctly.
	 */
	@Test
	void toStringTest() {
		// should match the loaded file exactly
		assertTrue(
			town.toString().equals(
				"O R O R \n" +
				"E E C O \n" +
				"E S O S \n" +
				"E O R R \n"
			)
		);
	}
	/**
	 * Test if the census helper method works correctly in an array of scenarios.
	 */
	@Test
	void pollNeighborhoodTest() {
		final int[]
			// at [1, 1], the neighborhood census should be [1, 2, 1, 3, 1]
			correct = new int[]{1, 2, 1, 3, 1},
			arr1 = new int[TownCell.NUM_CELL_TYPE],
			arr2 = new int[TownCell.NUM_CELL_TYPE - 1],
			arr3 = Town.pollNeighborhood(town, 1, 1, arr1),
			arr4 = Town.pollNeighborhood(town, 1, 1, arr2),
			arr5 = Town.pollNeighborhood(town, 1, 1, null),
			arr6 = Town.pollNeighborhood(town.grid, 1, 1, null);
		try {
			Town.pollNeighborhood((Town)null, 1, 1, arr1);
			// if working correctly, this shouldn't be reached.
			fail();
		} catch(NullPointerException e) {
			// pass
		}
		assertTrue(arr3 != null && arr3 == arr1);
		assertTrue(arr4 != null && arr4 != arr2);
		assertTrue(arr5 != null);
		assertTrue(arr6 != null);
		for(int i = 0; i < TownCell.NUM_CELL_TYPE; i++) {
			// test all the results of a certain index at the same time
			assertTrue(arr3[i] == correct[i]);
			assertTrue(arr4[i] == correct[i]);
			assertTrue(arr5[i] == correct[i]);
			assertTrue(arr6[i] == correct[i]);
		}
	}


}
