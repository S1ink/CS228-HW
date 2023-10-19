package edu.iastate.cs228.hw3;

import java.util.Arrays;
import java.util.ListIterator;
import java.util.Random;


public class Runtime {
	public static void main(String... args) {

		StoutList list = new StoutList();

		list.add("A");
		list.add("B");
		list.add("C");
		list.add("D");
		System.out.println(list);
		list.add("C");
		list.add("D");
		list.add("E");
		System.out.println(list);
		list.remove(1);
		System.out.println(list);
		list.remove(2);
		list.add("M");
		list.remove(1);
		System.out.println("(Figure 3) Starting list:\n" +list.toStringInternal());
		System.out.println();

		list.add("V");
		System.out.println("(Figure 4) After adding V:\n" +list.toStringInternal());
		System.out.println();

		list.add("W");
		list.add("W");
		System.out.println("(Figure 5) After adding W:\n" +list.toStringInternal());
		System.out.println();

		list.add(2, "X");
		System.out.println("(Figure 6) After adding X at 2:\n" +list.toStringInternal());
		System.out.println();

		list.add(2, "Y");
		System.out.println("(Figure 7) After adding Y at 2:\n" +list.toStringInternal());
		System.out.println();

		list.add(2, "Z");
		System.out.println("(Figure 8) After adding Z at 2:\n" +list.toStringInternal());
		System.out.println();

		list.remove(9);
		System.out.println("(Figure 10) After removing W at 9:\n" +list.toStringInternal());
		System.out.println();

		list.remove(3);
		System.out.println("(Figure 11) After removing Y at 3:\n" +list.toStringInternal());
		System.out.println();

		list.remove(3);
		System.out.println("(Figure 12) After removing X at 3:\n" +list.toStringInternal());
		System.out.println();

		list.remove(5);
		System.out.println("(Figure 13) After removing E at 5:\n" +list.toStringInternal());
		System.out.println();

		list.remove(3);
		System.out.println("(Figure 14) After removing C at 3:\n" +list.toStringInternal());
		System.out.println();

		// StoutList<String> l = new StoutList<>(4);
		// l.add("A");
		// l.add("B");
		// l.add("C");
		// l.add("D");
		// l.add("E");
		// StoutList<String> l2 = StoutList.fromArray(new String[]{ "A", "B", null, null, "C", "D", "E" }, 4);
		// System.out.println(l2);
		// l2.add("V");
		// System.out.println(l2);
		// l2.add("W");
		// System.out.println(l2);
		// l2.add(2, "X");
		// System.out.println(l2);
		// l2.add(2, "Y");
		// System.out.println(l2);
		// l2.add(2, "Z");
		// System.out.println(l2);

		// ListIterator<String> itr = l2.listIterator();
		// System.out.println(itr);
		// while(itr.hasNext()) {
		// 	itr.add("M");
		// 	itr.next();
		// 	System.out.println("Index [" + itr.nextIndex() + "]:  " + itr);
		// }
		// itr.add("M");
		// System.out.println(itr);
		// while(itr.hasPrevious()) {
		// 	itr.previous();
		// 	itr.remove();
		// 	System.out.println("Index [" + itr.previousIndex() + "]:  " + itr);
		// }

		// l2.s2();
		// System.out.println(l2);
		// l2.s2R();
		// System.out.println(l2);


		// // final Comparable[] test = l2.toArray();
		// final String[] test = new String[9999];
		// final Random rand = new Random();
		// for(int i = 0; i < test.length; i++) {
		// 	test[i] = Integer.toString(rand.nextInt(100));
		// }

		// StoutList<String>
		// 	t1 = StoutList.fromArray(test, 4),
		// 	t2 = StoutList.fromArray(test, 4);
		// // System.out.println(t1);
		// // System.out.println(t2);
		// long a = System.nanoTime();
		// t1.s2();
		// long b = System.nanoTime();
		// t2.sort();
		// long c = System.nanoTime();

		// a = System.nanoTime();
		// t1.sortReverse();
		// b = System.nanoTime();
		// t2.s2R();
		// c = System.nanoTime();
		// System.out.println("Advantage (R): " + (float)(b - a) / (c - b));
		// // System.out.println(t1);
		// // System.out.println(t2);
		// a = System.nanoTime();
		// t1.sort();
		// b = System.nanoTime();
		// t2.s2();
		// c = System.nanoTime();
		// System.out.println("Advantage (F): " + (float)(b - a) / (c - b));
		// // System.out.println(t1);
		// // System.out.println(t2);
		// a = System.nanoTime();
		// t1.sortReverse();
		// b = System.nanoTime();
		// t2.s2R();
		// c = System.nanoTime();
		// System.out.println("Advantage (R): " + (float)(b - a) / (c - b));
		// // System.out.println(t1);
		// // System.out.println(t2);
		// a = System.nanoTime();
		// t1.sort();
		// b = System.nanoTime();
		// t2.s2();
		// c = System.nanoTime();
		// System.out.println("Advantage (F): " + (float)(b - a) / (c - b));
		// // System.out.println(t1);
		// // System.out.println(t2);

		// l2.sortReverse();
		// System.out.println(l2);
		// l2.remove(9);
		// System.out.println(l2);
		// l2.remove(3);
		// System.out.println(l2);
		// l2.remove(3);
		// System.out.println(l2);
		// l2.remove(5);
		// System.out.println(l2);
		// l2.remove(3);
		// System.out.println(l2);
		// l.add("L");
		// System.out.println(l.toStringInternal());
		// l.add("Q");
		// System.out.println(l.toStringInternal());
		// System.out.println("Size: " + l.size());

		// l.condense();
		// System.out.println(l.toStringInternal());
		// l.sort();
		// System.out.println(l.toStringInternal());
		// l.sortReverse();
		// System.out.println(l.toStringInternal());

		// System.out.println(Arrays.toString(l.toArray()));

		// for(String s : l) {
		// 	System.out.println(s);
		// }
		// ListIterator<String> it = l.listIterator(l.size());
		// while(it.hasPrevious()) {
		// 	System.out.println(it.previous());
		// }
		// while(it.hasNext()) {
		// 	// it.add("H");
		// 	it.next();
		// 	it.remove();
		// 	System.out.println(l.toStringInternal());
		// }

		// l.remove(l.size() - 1);
		// System.out.println(l.toStringInternal());
		// l.remove(3);
		// System.out.println(l.toStringInternal());
		// l.remove(3);
		// System.out.println(l.toStringInternal());
		// l.remove(3);
		// System.out.println(l.toStringInternal());
		// l.remove(3);
		// System.out.println(l.toStringInternal());
		// l.remove(0);
		// System.out.println(l.toStringInternal());
		// l.remove(3);
		// System.out.println(l.toStringInternal());
		// l.remove(3);
		// System.out.println(l.toStringInternal());
		// l.remove(3);
		// System.out.println(l.toStringInternal());
		// l.remove(0);
		// System.out.println(l.toStringInternal());
		// l.remove(0);
		// System.out.println(l.toStringInternal());
		// l.remove(0);
		// System.out.println(l.toStringInternal());

		// l.add("HELLOW WORLD!!!");
		// System.out.println(l.toStringInternal());

	}
}
