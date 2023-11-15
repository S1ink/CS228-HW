package edu.iastate.cs228.hw4;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.NoSuchElementException;


/**
 * @author Sam Richter
 */
public class Main {

	public static class ArrayStack<T> {

		private ArrayList<T> stack = new ArrayList<>();


		public void push(T item) {
			this.stack.add(item);
		}
		public T pop() throws NoSuchElementException {
			try { return this.stack.remove(this.top()); }
			catch(IndexOutOfBoundsException e) { throw new NoSuchElementException(); }
		}
		public T peek() throws NoSuchElementException {
			try { return this.stack.get(this.top()); }
			catch(IndexOutOfBoundsException e) { throw new NoSuchElementException(); }
		}
		public boolean empty() {
			return this.size() == 0;
		}

		public int size() { return this.stack.size(); }
		private int top() { return this.stack.size() - 1; }

	}
	public static class CharArrayStack {

		public static final int
			DEFAULT_SIZE = 16,
			REALLOC_SCALE = 2;
		private char[] stack;
		private int top = 0;

		public CharArrayStack()
			{ this.stack = new char[DEFAULT_SIZE]; }
		public CharArrayStack(int size_init)
			{ this.stack = new char[size_init]; }
		/**
		 * Be advised that this constructor will not make a deep copy if a preexisting array reference is passed!
		 * 
		 * @param init
		 */
		public CharArrayStack(char... init) {
			this.stack = init;
			this.top = this.stack.length;
		}


		public void push(char c) {
			this.ensureCapacity(this.size() + 1);
			this.stack[this.top] = c;
			this.top++;
		}
		public char pop() throws NoSuchElementException {
			if(this.empty()) { throw new NoSuchElementException(); }
			this.top--;
			final char val = this.stack[this.top];
			this.stack[this.top] = '\0';
			return val;
		}
		public char peek() throws NoSuchElementException {
			if(this.empty()) { throw new NoSuchElementException(); }
			return this.stack[this.top];
		}
		public boolean empty() {
			return this.top == 0;
		}

		public int size() { return this.top; }
		public int capacity() { return this.stack.length; }

		private void ensureCapacity(int cap) {
			if(cap > this.capacity()) {
				this.stack = Arrays.copyOf(this.stack, this.stack.length * REALLOC_SCALE);
			}
		}


		public String toString() {
			return new String(this.stack, 0, this.size());
		}


	}

	public static class InvalidFormatException extends Exception {}

	/**
	 * 
	 */
	public static class MsgTree {

		public char item = '\0';
		public MsgTree left, right;

		/**
		 * 
		 */
		public MsgTree() {}
		/**
		 * 
		 * @param item
		 */
		public MsgTree(char item) { this.item = item; }
		/**
		 * 
		 * @param encodingString
		 * @throws InvalidFormatException
		 */
		public MsgTree(String encodingString) throws InvalidFormatException { MsgTree.buildTree(this, encodingString); }

		private boolean isTraversal() { return this.item == '\0'; }
		private boolean isEndpoint() { return this.item != '\0'; }





		/**
		 * 
		 * @param t
		 * @return
		 */
		public static int height(MsgTree t) {
			if(t == null) return 0;
			return Math.max( MsgTree.height(t.left), MsgTree.height(t.right) ) + 1;
		}

		/**
		 * 
		 * @param root
		 * @param format
		 * @throws InvalidFormatException
		 */
		public static void buildTree(MsgTree root, String format) throws InvalidFormatException {
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
					// the stack was unbalanced --> encoding was ill-formed
					throw new InvalidFormatException();
				}
			}
		}

		/**
		 * 
		 * @param root
		 */
		public static void printCodes(MsgTree root) {
			System.out.println("Char\tCode\n----------------------");
			MsgTree.printCodes(root, "");
		}
		/** Internal recursive code printing worker */
		private static void printCodes(MsgTree node, String code) {
			if(node == null) return;
			else if(node.isEndpoint()) System.out.printf("%c\t%s\n", node.item, code);
			else {
				MsgTree.printCodes(node.left, code + '0');
				MsgTree.printCodes(node.right, code + '1');
			}
		}

		/**
		 * 
		 * @param enc_root
		 * @param code
		 * @return
		 */
		public static String decode(MsgTree enc_root, String code) {
			StringBuilder b = new StringBuilder();
			final char[] chars = code.toCharArray();
			MsgTree node = enc_root;
			for(int i = 0; i < chars.length; i++) {
				node = (chars[i] == '0') ? node.left : node.right;
				if(node.isEndpoint()) {
					b.append(node.item);
					node = enc_root;		// restart from the top
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





	/**
	 * 
	 * @param file
	 * @return
	 * @throws FileNotFoundException
	 */
	private static String[] parseArch(File file) throws FileNotFoundException {
		final String[] lines = new String[2];
		String ext = "";
		final Scanner s = new Scanner(file);
		try {
			for(int l = 0; s.hasNextLine() && l < lines.length; l++) {
				lines[l] = s.nextLine();
			}
			if(s.hasNextLine()) {
				ext = s.nextLine();
			}
		} catch(Exception e) {} finally {
			s.close();
		}
		if(!ext.isEmpty()) {
			lines[0] += '\n' + lines[1];	// need to add back the newline
			lines[1] = ext;
		}
		return lines;
	}

	/**
	 * 
	 * @param root
	 */
	private static void runIO(MsgTree root) {

		final Scanner s = new Scanner(System.in);
		try {

			System.out.print("Please enter filename to decode: ");
			String fname = s.nextLine();
			System.out.println();
			// extract encoding string and code
			final String[] lines = parseArch(new File(fname));
			// build the tree
			MsgTree.buildTree(root, lines[0]);
			// print the codes
			MsgTree.printCodes(root);
			// decode and print
			System.out.println("\nMESSAGE:");
			System.out.println(MsgTree.decode(root, lines[1]));

		} catch(FileNotFoundException e) {
			System.out.printf("File not found :(\n>> %s\n\n", e.toString());
		} catch(InvalidFormatException e) {
			System.out.println("Invalid or ill-formed encoding encountered :(\n\n");
		} catch(Exception e) {
			System.out.printf("Failed to parse input filename :(\n>> %s\n\n", e.toString());
		} finally {
			s.close();
		}

	}

	public static void main(String... args) {

		MsgTree root = new MsgTree();
		runIO(root);

	}

}