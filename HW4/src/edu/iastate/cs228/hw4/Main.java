package edu.iastate.cs228.hw4;

import java.util.ArrayList;
import java.util.NoSuchElementException;


public class Main {

	public static class ArrayStack<T> {

		private ArrayList<T> stack = new ArrayList<>();


		public void push(T item) {
			this.stack.add(item);
		}
		public T pop() throws NoSuchElementException {
			try {
				return this.stack.remove(this.top());
			} catch(IndexOutOfBoundsException e) {
				throw new NoSuchElementException();
			}
		}
		public T peek() {
			return this.stack.get(this.top());
		}
		public boolean empty() {
			return this.size() == 0;
		}

		public int size() { return this.stack.size(); }
		private int top() { return this.stack.size() - 1; }

	}

	public static class InvalidFormatException extends Exception {}

	public static class MsgTree {

		public char item = '\0';
		public MsgTree left, right;

		public MsgTree(String encodingString) {}
		public MsgTree(char item) { this.item = item; }
		public MsgTree() {}

		private boolean isTraversal() { return this.item == '\0'; }
		private boolean isEndpoint() { return this.item != '\0'; }
		public static int height(MsgTree t) {
			if(t == null) return 0;
			return Math.max(height(t.left), height(t.right)) + 1;
		}

		public static void buildTree(String format, MsgTree root) throws InvalidFormatException {
			if(root == null || format == null || format.isEmpty()) return;
			final char[] chars = format.toCharArray();
			int i = 0;
			if(chars[0] == '^') i++;
			ArrayStack<MsgTree> nstack = new ArrayStack<>();
			nstack.push(root);
			for(; i < chars.length; i++) {
				try {
					if(chars[i] == '^') {
						if(nstack.peek().left == null) {
							nstack.peek().left = new MsgTree();
							nstack.push(nstack.peek().left);
						} else if(nstack.peek().right == null) {
							nstack.peek().right = new MsgTree();
							nstack.push(nstack.peek().right);
						} else {
							nstack.pop();	// both subtrees full, we need to continue upward (with the same index)
							i--;
						}
					} else {
						if(nstack.peek().left == null) {
							nstack.peek().left = new MsgTree(chars[i]);
						} else if(nstack.peek().right == null) {
							nstack.pop().right = new MsgTree(chars[i]);	// only pop here since we need to go back up a level to continue
						} else {
							nstack.pop();	// both subtrees full, we need to continue upward (with the same index)
							i--;
						}
					}
				} catch(NoSuchElementException e) {
					throw new InvalidFormatException();
				}
			}
		}
		public static void printCodes(MsgTree root, String code) {
			ArrayStack<MsgTree> traversal = new ArrayStack<>();
			MsgTree node = root;
			for(;;) {
				if(node.left != null) {
					
					node = node.left;
				}
			}
		}
		public static String decode(MsgTree encoding, String code) {
			StringBuilder b = new StringBuilder();
			final char[] chars = code.toCharArray();
			MsgTree node = encoding;
			for(int i = 0; i < chars.length; i++) {
				if(chars[i] == '0') {
					node = node.left;
				} else {
					node = node.right;
				}
				if(node.isEndpoint()) {
					b.append(node.item);
					node = encoding;
				}
			}
			return b.toString();
		}

		public static String toString(MsgTree root) {
			final String[] rows = build_string(root);
			StringBuilder b = new StringBuilder(rows[0]);
			for(int i = 1; i < rows.length; i++) {
				b.append('\n').append(rows[i]);
			}
			return b.toString();
		}
		private static String[] build_string(MsgTree base) {
			if(base.left == null && base.right == null && base.isEndpoint()) {
				return new String[]{ String.format("[%c]", base.item) };		// endpoint --> no need to represent null branches
			}
			// continue branches, evaluate each side
			String[] left, right;
			if(base.left != null) {
				left = build_string(base.left);
			} else {
				left = new String[]{ "[null]" };
			}
			if(base.right != null) {
				right = build_string(base.right);
			} else {
				right = new String[]{ "[null]" };
			}
			// combine sides for each String row, insert rootpoint
			int node_left = 0, node_right = 0;
			final char[] ltop = left[0].toCharArray(), rtop = right[0].toCharArray();
			while(ltop[ltop.length - 1 - node_left] != ']') {
				node_left++;
			}
			while(rtop[node_right] != '[') {
				node_right++;
			}
			final String[] rows = new String[Math.max(left.length + node_left + 1, right.length + node_right + 1) + 1];
			rows[0] = String.format(String.format("%%%ds[*]%%%ds", ltop.length, rtop.length), "", "");		// "%Ls[*]%Rs"
			for(int i = 1; i < rows.length; i++) {
				final int
					rel_left = (i - node_left - 2),		// i - 1 - (node_left + 1)
					rel_right = (i - node_right - 2),
					off_left = (node_left + 1) + rel_left,
					off_right = (node_right + 1) - rel_right;
				rows[i] = String.format("%s   %s",
					(rel_left < 0) ? gen_left_vertex(ltop.length - off_left, off_left) :
						((rel_left < left.length) ? left[rel_left] : String.format(String.format("%%%ds", ltop.length), "")),
					(rel_right < 0) ? gen_right_vertex(off_right, rtop.length - off_right) :
						((rel_right < right.length) ? right[rel_right] : String.format(String.format("%%%ds", rtop.length), ""))
				);
			}
			return rows;
		}
		private static String gen_left_vertex(int len, int off) {
			if(off == 0) {
				return String.format(String.format("%%%ds", len), "/");
			} else {
				return String.format(String.format("%%%ds%%-%ds", len - off, off), "", "/");
			}
		}
		private static String gen_right_vertex(int len, int off) {
			if(off == 0) {
				return String.format(String.format("%%-%ds", len), "\\");
			} else {
				return String.format(String.format("%%%ds%%%ds", off, len - off), "\\", "");
			}
		}

	}

	public static void main(String... args) {
		MsgTree root = new MsgTree();
		try{ MsgTree.buildTree("^^^^r^c^^S^P^^^D2^^^5)O1jb^^d^,ps^^ine^^^ao^^^m^^^I^^WE^B^^(^^'^87Y3^U;wl^^u^^^^NR^q^J^G^4L-^^x^Hk^TC^^vg\n^ ^t^h^^^^^^X:V^^^^F^^0\"^^K96MzA.yf", root); } catch(Exception e) { System.out.println("Invalid format!"); }
		System.out.println(MsgTree.toString(root));
		System.out.println(MsgTree.decode(root, ""));
		System.out.println("Height: " + MsgTree.height(root));
	}

}