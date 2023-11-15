package edu.iastate.cs228.hw4;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.NoSuchElementException;


/**
 * @author Sam Richter
 */
public class Main {

	/**
	 * ArrayStack wraps an ArrayList for the given generic type and provides stack-specific accessors.
	 */
	public static class ArrayStack<T> {

		private ArrayList<T> stack = new ArrayList<>();


		/**
		 * Push an item onto the stack.
		 * 
		 * @param item - the item to push
		 */
		public void push(T item) {
			this.stack.add(item);
		}
		/**
		 * Pop an item off the top of the stack.
		 * 
		 * @return the top value
		 * @throws NoSuchElementException if the stack was empty -- see empty()
		 */
		public T pop() throws NoSuchElementException {
			try { return this.stack.remove(this.top()); }
			catch(IndexOutOfBoundsException e) { throw new NoSuchElementException(); }
		}
		/**
		 * Check the element on the top of the stack without removing it.
		 * 
		 * @return the item at the top of the stack
		 * @throws NoSuchElementException if the stack was empty -- see empty()
		 */
		public T peek() throws NoSuchElementException {
			try { return this.stack.get(this.top()); }
			catch(IndexOutOfBoundsException e) { throw new NoSuchElementException(); }
		}
		/**
		 * Check if the stack is empty, ie. it has no elements.
		 * 
		 * @return true if the stack is empty
		 */
		public boolean empty() {
			return this.size() == 0;
		}

		/**
		 * Get the current size of the stack.
		 * 
		 * @return the size of the stack
		 */
		public int size() { return this.stack.size(); }
		private int top() { return this.stack.size() - 1; }


	}

	/** InvalidFormatException is thrown when an encoding string is ill-formed. */
	public static class InvalidFormatException extends Exception {}

	/**
	 * The MsgTree class represents a binary tree (possibly a subtree) used in decoding messages.
	 * It's payload is a single char which can be a null termination character to represent that the node is traversal only.
	 */
	public static class MsgTree {

		public char item = '\0';
		public MsgTree left, right;

		/**
		 * Create a blank MsgTree node.
		 */
		public MsgTree() {}
		/**
		 * Create a MsgTree node with the provided payload.
		 * 
		 * @param item - the char to store
		 */
		public MsgTree(char item) { this.item = item; }
		/**
		 * Build a tree from the provided encoding string. The node will be the tree's root.
		 * 
		 * @param encodingString - the string from which to build the tree
		 * @throws InvalidFormatException if the encoding string is ill-formed
		 */
		public MsgTree(String encodingString) throws InvalidFormatException { MsgTree.buildTree(this, encodingString); }


		private boolean isEndpoint() { return this.item != '\0'; }





		/**
		 * Get the height of the tree. This is a recursive call.
		 * 
		 * @param t - the tree for which to find the height
		 * @return the height of the tree
		 */
		public static int height(MsgTree t) {
			if(t == null) return 0;
			return Math.max( MsgTree.height(t.left), MsgTree.height(t.right) ) + 1;
		}

		/**
		 * Build the decoding tree from the provided encoding format string. This method implements an iterative solution.
		 * 
		 * @param root - the root node to build the tree from
		 * @param format - the encoding string
		 * @throws InvalidFormatException if the format is ill-formed
		 */
		public static void buildTree(MsgTree root, String format) throws InvalidFormatException {
			if(root == null || format == null || format.isEmpty()) return;
			final char[] chars = format.toCharArray();
			int i = 0;
			if(chars[0] == '^') i++;
			ArrayStack<MsgTree> nstack = new ArrayStack<>();
			nstack.push(root);
			// for each char in the encoding format...
			for(; i < chars.length; i++) {
				try {
					if(chars[i] == '^') {
						// add a new traversal node to the next possible child, if none are available, pop back to the parent and keep the same char index
						if(nstack.peek().left == null) {
							nstack.peek().left = new MsgTree();
							nstack.push(nstack.peek().left);
						} else if(nstack.peek().right == null) {
							nstack.peek().right = new MsgTree();
							nstack.push(nstack.peek().right);
						} else {
							// both subtrees full, we need to continue upward (and with the same index)
							nstack.pop();
							i--;
						}
					} else {
						// add the char at the next possible child, if none are available, pop back to the parent and keep the same char index
						if(nstack.peek().left == null) {
							nstack.peek().left = new MsgTree(chars[i]);
						} else if(nstack.peek().right == null) {
							// only pop here since we need to go back up a level to continue
							nstack.pop().right = new MsgTree(chars[i]);
						} else {
							// both subtrees full, we need to continue upward (and with the same index)
							nstack.pop();
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
		 * Print each character in the tree along with it's bit code.
		 * 
		 * @param root - the tree for which to print codes
		 */
		public static void printCodes(MsgTree root) {
			System.out.println("Char\tCode\n----------------------");
			MsgTree.printCodes(root, "");
		}
		/** Internal recursive worker for print codes */
		private static void printCodes(MsgTree node, String code) {
			if(node == null) return;
			else if(node.isEndpoint()) System.out.printf("%c\t%s\n", node.item, code);
			else {
				MsgTree.printCodes(node.left, code + '0');
				MsgTree.printCodes(node.right, code + '1');
			}
		}

		/**
		 * Decode a string of binary characters using the provided decoding tree.
		 * 
		 * @param enc_root - the root node of the decoding tree
		 * @param code - the encoded message
		 * @return the decoded message
		 */
		public static String decode(MsgTree enc_root, String code) {
			StringBuilder b = new StringBuilder();
			final char[] chars = code.toCharArray();
			MsgTree node = enc_root;
			// for each bit in the encoded message...
			for(int i = 0; i < chars.length; i++) {
				// traverse the tree based on the binary value
				node = (chars[i] == '0') ? node.left : node.right;
				// if we reach an node, append the character and restart
				if(node.isEndpoint()) {
					b.append(node.item);
					node = enc_root;
				}
			}
			return b.toString();
		}


	}





	/**
	 * Parse the archive file into an encoding string and encoded message string.
	 * 
	 * @param file - the file to parse
	 * @return a String[] of length 2 -- the first element is the encoding string and the second is the message string
	 * @throws FileNotFoundException if the filename is invalid
	 */
	private static String[] parseArch(File file) throws FileNotFoundException {
		final String[] lines = new String[2];
		String ext = "";
		final Scanner s = new Scanner(file);
		try {
			// always at least 2 lines
			for(int l = 0; s.hasNextLine() && l < lines.length; l++) {
				lines[l] = s.nextLine();
			}
			// handle possible 3rd line
			if(s.hasNextLine()) {
				ext = s.nextLine();
			}
		} catch(Exception e) {} finally {
			s.close();
		}
		if(!ext.isEmpty()) {
			// need to add back the newline
			lines[0] += '\n' + lines[1];
			lines[1] = ext;
		}
		return lines;
	}

	/**
	 * Run the program's user IO.
	 * 
	 * @param root - the root MsgTree node to act upon
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
