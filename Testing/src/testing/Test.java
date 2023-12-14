package testing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.AbstractCollection;
import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.NoSuchElementException;


public class Test {
	
	public static class LinkedList<T, N extends Test.LinkedList.LinkedNode<T>> 
		extends AbstractCollection<T>
	{
		
		public static interface LinkedNode<T> {
			
			public LinkedNode<T> next();
			public void apply(LinkedNode<T> next);
			
			public T get();
			public void set(T v);
			
		}
		public static class GenericNode<T> implements LinkedNode<T> {
			
			private LinkedNode<T> nn = null;
			private T data;
			
			public GenericNode(T data) {
				this.data = data;
			}
			
			@Override
			public LinkedNode<T> next() { return this.nn; }
			@Override
			public void apply(LinkedNode<T> next) { this.nn = next; }
			
			@Override
			public T get() { return this.data; }
			@Override
			public void set(T v) { this.data = v; }
			
		}
		public static class NodeIterator<T> implements Iterator<T> {
			
			private LinkedNode<T> node = null;
			
			public NodeIterator(LinkedNode<T> first) {
				this.node = first;
			}
			

			@Override
			public boolean hasNext() {
				return this.node != null && this.node != null;
			}
			@Override
			public T next() {
				if(this.node != null) {
					final T val = this.node.get();
					this.node = this.node.next();
					return val;
				}
				return null;
			}
			
		}
		
		
		private N
			head = null,
			tail = null;
		private int size = 0;
		
		public LinkedList(N... nodes) {
			for(final N n : nodes) {
				this.append(n);
				this.size++;
			}
		}
		
		
		public void append(N node) {
			this.insert(this.tail, node);
		}
		public void prepend(N node) {
			this.insert(null, node);
		}
		
		@Override
		public int size() {
			return this.size;
		}
		public N getN(int i) {
			if(i < this.size) {
				if(i == this.size - 1) {
					return this.tail;
				}
				N target = this.head;
				while(i > 0) {
					try {
						target = (N)target.next();
					} catch(ClassCastException e) {
						return null;
					}
					i--;
				}
				return target;
			}
			return null;
		}
		public T get(int i) {
			final N n = this.getN(i);
			return (n == null) ? null : n.get();
		}
		public void set(int i, T v) {
			final N n = this.getN(i);
			if(n != null) {
				n.set(v);
			}
		}
		
		@Override
		public Iterator<T> iterator() {
			return new NodeIterator<>(this.head);
		}
		
		
		private void insert(N a, N b) {
			if(b != null) {
				if(a != null) {
					b.apply(a.next());
					a.apply(b);
				} else {
					b.apply(this.head);
					this.head = b;
				}
				if(a == this.tail) {
					this.tail = b;
				}
				this.size++;
			}
		}
		
		
	}
	public static class GenericLinkedList<T> extends LinkedList<T, LinkedList.GenericNode<T>> {
		
		public GenericLinkedList(T... vals) {
			super();
			for(final T v : vals) {
				super.append(new GenericNode<>(v));
			}
		}
		
		public void append(T v) {
			super.append(new GenericNode<>(v));
		}
		public void prepend(T v) {
			super.prepend(new GenericNode<>(v));
		}
		
	}
	
	public static class PappasPizza {
		
		public String name;
		public double price;
		public String[] toppings;
		
		public PappasPizza(String n, double p, String[] t) {
			this.name = n;
			this.price = p;
			this.toppings = t;
		}
		
		@Override
		public boolean equals(Object other) {
			if(other == null || other.getClass() != this.getClass()) {
				return false;
			}
			PappasPizza p = (PappasPizza)other;
			if(!p.name.equals(this.name) || p.price != this.price || p.toppings.length != this.toppings.length) {
				return false;
			}
			String[] a = new String[this.toppings.length];
			String[] b = new String[this.toppings.length];
			for(int i = 0; i < a.length; i++) {
				a[i] = this.toppings[i];
				b[i] = p.toppings[i];
			}
			for(int i = 0; i < a.length; i++) {
				int min_a = i;
				int min_b = i;
				for(int j = i + 1; j < a.length; j++) {
					if(a[j].compareTo(a[min_a]) < 0) {
						min_a = j;
					}
					if(b[j].compareTo(b[min_b]) < 0) {
						min_b = j;
					}
				}
				if(!a[min_a].equals(b[min_b])) {
					return false;
				}
				String tmp = a[i];
				a[i] = a[min_a];
				a[min_a] = tmp;
				tmp = b[i];
				b[i] = b[min_b];
				b[min_b] = tmp;
			}
			return true;
		}
		
	}
	
	public static void p(Object... os) {
		for(Object o : os) {
			System.out.println(o);
		}
	}
	
	
	public static interface TestInterface<T, N extends TestInterface<T, ?>> {
		
		public N getVal();
		public T getInnerVal();
		
		default public N test() { return (N)this; }
		
	}
	public static class TestClass<T> implements TestInterface<T, TestClass<T>> {
		
		private T data;
		
		public TestClass(T data) {
			this.data = data;
		}
		

		@Override
		public TestClass<T> getVal() {
			return this;
		}
		@Override
		public T getInnerVal() {
			return this.data;
		}
		
		@Override
		public String toString() {
			return String.format("TestClass<%s>", this.data);
		}
		
	}
	public static class TestClass2<T> extends TestClass<T> {
		
		public TestClass2(T data) { super(data); }
		
		@Override
		public String toString() {
			return String.format("TestClass2<%s>", super.data);
		}
		
	}
	
	public static class Stack<E> {
		
		private static class Node<E> {
			public Node<E> next;
			public E data;
		}
		
		private Node<E> head;
		private int size = 0;
		
		public void push(E data) {
			final Node<E> n = new Node<>();
			n.data = data;
			n.next = this.head;
			this.head = n;
			this.size++;
		}
		public E peek() {
			if(this.head == null) { throw new NoSuchElementException(); }
			return this.head.data;
		}
		public E pop() {
			if(this.head == null) { throw new NoSuchElementException(); }
			final E ret = this.head.data;
			this.head = this.head.next;
			this.size--;
			return ret;
		}
		public boolean isEmpty() {
			return this.size == 0;
		}
		
	}
	
	public static boolean eval(String wf) {

		Stack<Character> stack = new Stack<>();
		for(int i = 0; i < wf.length(); i++) {
			final char c = wf.charAt(i);
			try{
				switch(c) {
					case '{': {
						try {
							final char d = stack.peek();
							if(d == '[' || d == '(') {
								return false;
							} 
						} catch(NoSuchElementException e) {}
						stack.push(c);
						break;
					}
					case '[': {
						try {
							final char d = stack.peek();
							if(d == '(') {
								return false;
							}
						} catch(NoSuchElementException e) {}
						stack.push(c);
						break;
					}
					case '(': {
						stack.push(c);
						break;
					}
					case ')': {
						if(stack.pop() != '(') {
							return false;
						}
						break;
					}
					case ']': {
						if(stack.pop() != '[') {
							return false;
						}
						break;
					}
					case '}': {
						if(stack.pop() != '{') {
							return false;
						}
						break;
					}
					default: {}
				}
			} catch(NoSuchElementException e) {
				return false;
			}
		}
		return stack.isEmpty();

	}

	public static void main(String... args) {
//		final PappasPizza
//			a = new PappasPizza("a", 0.5, new String[]{"sa", "pp", "tl", "tl"}),
//			b = new PappasPizza("a", 0.5, new String[]{"tl", "sa", "tl", "pp"});
//		System.out.println(a.equals(b));
		
//		String
//			a = "fjdksljdlk",
//			b = "jfldksjl %s %s";
//		try {
//			System.out.println(String.format(a, 1));
//			System.out.println(String.format(b, 1));
//		} catch(Exception e) {
//			System.out.println(e);
//			return;
////			throw e;
//		} finally {
//			System.out.println("called!");
//		}
		
//		p("fds", (Integer)1);
		
		
//		final GenericLinkedList<String> list = new GenericLinkedList<>("jfldksjl", "hellow", "whats up!?");
//		for(final String s : list) {
//			System.out.println(s);
//		}
//		list.prepend("first!?");
//		System.out.println("---------------");
//		for(final String s : list) {
//			System.out.println(s);
//		}
		
//		TestInterface<?, ?> inf = new TestClass2("hellow");
//		System.out.println(inf);
//		System.out.println(inf.getInnerVal());
//		System.out.println(inf.test() == inf);
		
		System.out.println(eval("{[[(dsfds)]]((f))}a"));
		
	}
	
	
	
	
	

}
