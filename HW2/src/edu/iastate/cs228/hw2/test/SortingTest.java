package edu.iastate.cs228.hw2.test;

import static org.junit.Assert.fail;

import java.util.Comparator;
import java.util.Random;

import org.junit.jupiter.api.Test;

import edu.iastate.cs228.hw2.Sorting;


public class SortingTest {

	public static final int
		NUM_TESTS = 10,
		TEST_SIZE = 1000,
		INT_RANGE = 1000;


	public static <T> boolean checkAscending(T[] arr, Comparator<? super T> comp) {
		for(int i = 0; i < arr.length - 1; i++) {
			if(comp.compare(arr[i], arr[i + 1]) > 0) {
				return false;
			}
		}
		return true;
	}
	public static Integer[] genData(int size, int abs_range, Random r) {
		final Integer[] arr = new Integer[size];
		for(int i = 0; i < size; i++) {
			arr[i] = r.nextInt(abs_range * 2 + 1) - abs_range;
		}
		return arr;
	}
	public static Integer[][] genTest(int size, int abs_range, int num_tests, Random r) {
		final Integer[][] tests = new Integer[num_tests][];
		for(int i = 0; i < num_tests; i++) {
			tests[i] = genData(size, abs_range, r);
		}
		return tests;
	}

	public static boolean testSorter(Sorting.Sorter<Integer> s, Comparator<Integer> comp, Random r) {
		final Integer[][] tests = genTest(TEST_SIZE, INT_RANGE, NUM_TESTS, r);
		for(Integer[] t : tests) {
			s.sort(t, comp);
			if(!checkAscending(t, comp)) {
				return false;
			}
		}
		return true;
	}



	private static final Random rand = new Random();
	private static final Comparator<Integer>
		comp = (Integer a, Integer b)->{ return a - b; };

	@Test
	void testSelection() {
		if(!testSorter(Sorting::selectionSort, comp, rand)) {
			fail("Selection Sort order is incorrect");
		}
	}
	@Test
	void testInsertion() {
		if(!testSorter(Sorting::insertionSort, comp, rand)) {
			fail("Insertion Sort order is incorrect");
		}
	}
	@Test
	void testMerge() {
		if(!testSorter(Sorting::mergeSort, comp, rand)) {
			fail("Merge Sort order is incorrect");
		}
	}
	@Test
	void testQuick() {
		if(!testSorter(Sorting::quickSort, comp, rand)) {
			fail("Quick Sort order is incorrect");
		}
	}


}
