package edu.iastate.cs228.hw1;

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


/**
 * @author Sam Richter
 * 
 * The ISPBusinessTest class tests all public helper methods within the ISPBusiness class (excluding simulate() since it requires user input)
 */
class ISPBusinessTest {

	static Town town;

	/**
	 * Setup the town instance used by multiple tests.
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
	 * Tests if a plain can successfully be iterated.
	 */
	@Test
	void updatePlainTest() {
		final Town new_town = ISPBusiness.updatePlain(town);
		assertNotEquals(new_town, null);
		assertEquals(new_town.getLength(), town.getLength());
		assertEquals(new_town.getWidth(), town.getWidth());
		assertTrue(
			new_town.toString().equals(
				"E E E E \n" +
				"C C O E \n" +
				"C O E O \n" +
				"C E E E \n"
			)
		);
	}
	/**
	 * Tests if the correct profit can be calculated for the given grid.
	 */
	@Test
	void getProfitTest() {
		// the loaded plain only has a single 'C'
		assertEquals(ISPBusiness.getProfit(town), 1);
	}
	/**
	 * Tests if the maximum profit is calculated correctly.
	 */
	@Test
	void maxProfitTest() {
		assertEquals(ISPBusiness.maxProfit(town), 16);
	}


}
