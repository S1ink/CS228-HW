package edu.iastate.cs228.hw3;

import java.util.Arrays;
import java.util.ListIterator;
import java.util.Random;


public class Runtime {

	public static void main(String... args) {

		final Random r = new Random();

		for(int t = r.nextInt(50) + 50; t > 0; t--) {
			try{
				final StoutList<String> list = new StoutList<>((r.nextInt(5) + 1) * 2);
				System.out.println(">>>>>>>>>>>>>>>>>>>> Running Test <<<<<<<<<<<<<<<<<<<<<<<<");
				test(list, r);
				System.out.println(">>>>>>>>>>>>>>>>>>>> Test Completed <<<<<<<<<<<<<<<<<<<<<<<<");
			} catch(Exception e) {
				throw e;
			}

		}

		// for(char c = 'A'; c <= 'Z'; c++) {
		// 	list.add(Character.toString(c));
		// }
		// System.out.println(list);
		// list.add(18, "fjd");
		// list.add(20, "fjhkdl");
		// System.out.println(list);
		// list.sortReverse();
		// System.out.println(list);
		// final ListIterator<String> itr = list.listIterator();
		// while(itr.hasNext()) {
		// 	itr.next();
		// 	itr.remove();
		// 	itr.add("M");
		// }
		// System.out.println(list);

	}


	private static String rstring(Random r, int mlen) {

		int len = r.nextInt(mlen) + 1;
		StringBuilder b = new StringBuilder();
		for(;len > 0; len--) {
			final int s = r.nextInt(52);
			b.append(Character.toString((s > 25 ? 'A' + s - 26 : 'a' + s)));
		}
		return b.toString();

	}

	private static boolean test(StoutList<String> list, Random r) {

		try {

			// add()
			int len = r.nextInt(20) + 10;
			for(;len > 0; len--) {
				list.add(rstring(r, 10));
			}
			System.out.println(list + " >> " + list.size());
			// add(loc)
			len = r.nextInt(15) + 5;
			for(;len > 0; len--) {
				list.add(r.nextInt(list.size()), rstring(r, 5));
			}
			System.out.println(list + " >> " + list.size());
			// remove(loc)
			len = r.nextInt(list.size() - 5) + 5;
			for(;len > 0; len--) {
				list.remove(r.nextInt(list.size()));
			}
			System.out.println(list + " >> " + list.size());
			// sort()
			list.sort();
			System.out.println(list + " >> " + list.size());
			// sortReverse()
			list.sortReverse();
			System.out.println(list + " >> " + list.size());
			// iterator()
			final ListIterator<String> itr = list.listIterator();
			String l = null;
			while(itr.hasNext()) {
				l = itr.next();
			}
			System.out.println("Asymmetry Test: " + (l == itr.previous()));
			while(itr.hasPrevious()) {
				itr.previous();
			}
			// System.out.println(itr + " >> " + itr.nextIndex());
			len = r.nextInt(list.size() / 2 + 1);
			for(;len > 0; len--) {
				final ListIterator<String> itrr = list.listIterator(r.nextInt(list.size()));
				l = null;
				while(itrr.hasNext()) {
					l = itrr.next();
				}
				System.out.println("Asymmetry Test: " + (l == itrr.previous()));
				while(itrr.hasPrevious()) {
					itrr.previous();
				}
				System.out.println(itrr + " >> " + itr.nextIndex());
			}
			len = r.nextInt(15) + 5;
			boolean rev = false;
			for(;len > 0; len--) {
				for(int i = r.nextInt(4) + 1; i > 0; i--) {
					if(rev) {
						itr.previous();
						if(!itr.hasPrevious()) { rev = false; }
					} else {
						itr.next();
						if(!itr.hasNext()) { rev = true; }
					}
				}
				itr.add(rstring(r, 10));
			}
			System.out.println(itr + " >> " + list.size());
			len = r.nextInt(10);
			rev = !itr.hasNext();
			for(;len > 0; len--) {
				for(int i = r.nextInt(4) + 1; i > 0; i--) {
					if(rev) {
						itr.previous();
						if(!itr.hasPrevious()) { rev = false; }
					} else {
						itr.next();
						if(!itr.hasNext()) { rev = true; }
					}
				}
				itr.set(rstring(r, 2));
			}
			len = r.nextInt(list.size());
			rev = itr.hasPrevious();
			for(;len > 0; len--) {
				String m = null;
				for(int i = r.nextInt(2) + 1; i > 0; i--) {
					System.out.print(itr.nextIndex() + " >> ");
					if(rev) {
						m = itr.previous();
						if(!itr.hasPrevious()) { rev = false; }
					} else {
						m = itr.next();
						if(!itr.hasNext()) { rev = true; }
					}
					System.out.println(itr + " >> " + itr.nextIndex());
				}
				itr.remove();
				System.out.println(m);
			}
			System.out.println(itr + " >> " + list.size());

			list.sort();
			System.out.println(list);

			return true;
		} catch(Exception e) {
			System.out.println("Error occured while testing StoutList\n>> " + e.toString());
			throw e;
			// return false;
		}

	}


}
