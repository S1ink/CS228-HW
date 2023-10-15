package edu.iastate.cs228.hw3;


public class Runtime {
	public static void main(String... args) {

		StoutList<String> l = new StoutList<>(4);
		l.add("A");
		l.add("B");
		l.add("C");
		l.add("D");
		l.add("E");
		System.out.println(l.toStringInternal());
		l.add("V");
		System.out.println(l.toStringInternal());
		l.add("W");
		System.out.println(l.toStringInternal());
		l.add(2, "X");
		System.out.println(l.toStringInternal());
		l.add(2, "Y");
		System.out.println(l.toStringInternal());
		l.add(2, "Z");
		System.out.println(l.toStringInternal());
		l.add("L");
		System.out.println(l.toStringInternal());
		l.add("Q");
		System.out.println(l.toStringInternal());

		l.remove(l.size() - 1);
		System.out.println(l.toStringInternal());
		l.remove(3);
		System.out.println(l.toStringInternal());
		l.remove(3);
		System.out.println(l.toStringInternal());
		l.remove(3);
		System.out.println(l.toStringInternal());
		l.remove(3);
		System.out.println(l.toStringInternal());
		l.remove(0);
		System.out.println(l.toStringInternal());
		l.remove(3);
		System.out.println(l.toStringInternal());
		l.remove(3);
		System.out.println(l.toStringInternal());
		l.remove(3);
		System.out.println(l.toStringInternal());
		l.remove(0);
		System.out.println(l.toStringInternal());
		l.remove(0);
		System.out.println(l.toStringInternal());
		l.remove(0);
		System.out.println(l.toStringInternal());

		l.add("HELLOW WORLD!!!");
		System.out.println(l.toStringInternal());

	}
}
