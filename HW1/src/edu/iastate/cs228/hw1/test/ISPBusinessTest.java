package edu.iastate.cs228.hw1.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

import edu.iastate.cs228.hw1.*;


class ISPBusinessTest {

	@Test
	void updatePlainTest() {
		Town town;
		try {
			town = new Town("ISP4x4.txt");
		} catch(FileNotFoundException e) {
			fail();
			return;
		}
		Town new_town = ISPBusiness.updatePlain(town);
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
	@Test
	void getProfitTest() {
		fail();
	}
	@Test
	void maxProfitTest() {
		fail();
	}
	@Test
	void simulateTest() {
		fail();
	}


}
