package edu.iastate.cs228.hw4;

import java.util.Stack;
import java.util.NoSuchElementException;


/**
 * @author Sam Richter
 * 
 * The MsgTree class represents a binary tree (possibly a subtree) used in decoding messages.
 * It's payload is a single char which can be a null termination character to represent that the node is traversal only.
 */
public class MsgTree {

	/** InvalidFormatException is thrown when an encoding string is ill-formed. */
	public static class InvalidFormatException extends Exception {}


	private static final char EMPTY_ITEM = '\0';

	public char item = EMPTY_ITEM;
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


	private boolean isEndpoint() { return this.item != EMPTY_ITEM; }





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
		Stack<MsgTree> nstack = new Stack<>();
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
		else if(node.isEndpoint()) {
			switch(node.item) {
				case '\n': {
					System.out.printf("[\\n]\t%s\n", code);
					break;
				}
				case '\t': {
					System.out.printf("[\\t]\t%s\n", code);
					break;
				}
				case ' ': {
					System.out.printf("[ ]\t%s\n", code);
					break;
				}
				default: {
					System.out.printf("%c\t%s\n", node.item, code);
				}
			}
		} else {
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
