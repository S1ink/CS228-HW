package edu.iastate.cs228.hw4;

import java.util.ArrayList;


public class Main {

	public static class ArrayStack<T> {

		private ArrayList<T> stack = new ArrayList<>();


		public void push(T item) {
			this.stack.add(item);
		}
		public T pop() {
			return this.stack.remove(this.top());
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

	public static class MsgTree {

		public char item = '\0';
		public MsgTree left, right;

		public MsgTree(String encodingString) {}
		public MsgTree(char item) {}
		public MsgTree() {}

		public static void printCodes(MsgTree root, String code) {}

		private boolean isTraversal() { return this.item == '\0'; }

		public static void buildTree(String format, MsgTree root) {
			if(root == null || format == null || format.isEmpty()) return;
			final char[] chars = format.toCharArray();
			int i = 0;
			if(chars[0] == '^') i++;
			ArrayStack<MsgTree> nstack = new ArrayStack<>();
			nstack.push(root);
			for(; i < chars.length; i++) {
				if(chars[i] == '^') {
					if(nstack.peek().left == null) {
						nstack.peek().left = new MsgTree();
						nstack.push(nstack.peek().left);
					} else if(nstack.peek().right == null) {
						nstack.peek().right = new MsgTree();
						nstack.push(nstack.peek().right);
					} else {

					}
				} else {
					if(nstack.peek().left == null) {
						nstack.pop().left = new MsgTree(chars[i]);
					} else if(nstack.peek().right == null) {
						nstack.pop().right = new MsgTree(chars[i]);
					} else {
						
					}
				}
			}
		}

	}

	public static void main(String... args) {
		System.out.println("Working entrypoint!?");
	}

}