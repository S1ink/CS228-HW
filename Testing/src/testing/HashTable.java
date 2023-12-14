package testing;


public class HashTable<Key, Value> {
	
	public static interface Hasher<Key> {
		public int hash(Key key);
		public default int hash(Key key, int mod) { return (this.hash(key) & 0x7FFFFFFF) % mod; }
	}

	protected static interface Bucket<Key, Value> {
		public Key key();
		public Value val();
		public void key(Key k);
		public void val(Value v);
	}
	protected static class Element<T> implements Bucket<T, T> {
		public T keyval;

		@Override public T key() { return this.keyval; }
		@Override public T val() { return this.keyval; }
		@Override public void key(T k) { this.keyval = k; }
		@Override public void val(T v) { this.keyval = v;}
	}
	protected static class Pair<Key, Value> implements Bucket<Key, Value> {
		public Key key;
		public Value val;

		@Override public Key key() { return this.key; }
		@Override public Value val() { return this.val; }
		@Override public void key(Key k) { this.key = k; }
		@Override public void val(Value v) { this.val = v; }
	}
	protected static class ChainNode<Key, Value> extends Pair<Key, Value> {

		public ChainNode<Key, Value> next = null;

		public ChainNode(Key k, Value v) {
			super.key = k;
			super.val = v;
		}
		public ChainNode(Key k, Value v, ChainNode<Key, Value> n) {
			this(k, v);
			this.next = n;
		}

		// static <Key, Value> ChainNode<Key, Value> add(Bucket<Key, Value>[] buckets, int idx) {
		// 	if(buckets[idx] == null) { return null; }
		// 	try {
		// 		ChainNode<Key, Value> n = (ChainNode<Key, Value>)buckets[idx];

		// 	} catch(Exception e) {
		// 		return null;
		// 	}
		// }

	}


	protected Bucket<Key, Value>[] buckets;
	protected Hasher<Key> hash = (Key k)->k.hashCode();
	protected int total_elements;
	protected double max_load = 0.75;

	public HashTable() {
		this.buckets = new Bucket[10];
	}

	public int buckets() { return this.buckets.length; }
	protected long _hash(Key k) { return this.hash.hash(k, this.buckets()); }
	protected ChainNode<Key, Value> bucket(int idx) { return (ChainNode<Key, Value>)this.buckets[idx]; }
	public double load() { return (double)this.total_elements / this.buckets(); }

	public void insert(Key k, Value v) {
		if((double)(this.total_elements + 1) / this.buckets() > this.max_load) {
			this.resize(this.total_elements * 2 + 7);
		}
		final int idx = (int)this._hash(k);
		this.buckets[idx] = new ChainNode<>(k, v, this.bucket(idx));
		this.total_elements++;
	}
	protected ChainNode<Key, Value> _search(Key k) {
		final int idx = (int)this._hash(k);
		ChainNode<Key, Value> n = this.bucket(idx);
		if(n == null) { return null; }
		else if(n.key.equals(k)) { return new ChainNode<>(null, null, n); }
		for(; n.next != null && !n.next.key.equals(k); n = n.next);
		return n.next == null ? null : n;
	}
	public Value search(Key k) {
		final ChainNode<Key, Value> prev = this._search(k);
		return prev != null ? prev.next.val : null;
	}
	public Value remove(Key k) {
		final ChainNode<Key, Value> prev = this._search(k);
		if(prev == null) { return null; }
		final ChainNode<Key, Value> n = prev.next;
		prev.next = n.next;
		n.next = null;
		this.total_elements--;
		return n.val;
	}

	protected void resize(int new_size) {
		final ChainNode<Key, Value>[] new_buckets = new ChainNode[new_size];
		for(int i = 0; i < this.buckets(); i++) {
			ChainNode<Key, Value> n = this.bucket(i), next;
			for(; n != null; ) {
				next = n.next;
				final int idx = (int)this.hash.hash(n.key, new_size);
				n.next = new_buckets[idx];
				new_buckets[idx] = n;
				n = next;
			}
		}
		this.buckets = new_buckets;
	}



	public static void main(String... args) {

		final HashTable<String, Double> table = new HashTable<>();
		table.insert("4.0", 4.0);
		table.insert("5.67", 5.67);
		System.out.println(table.search("4.0"));
		System.out.println(table.search("5.67"));
		System.out.println(table.search("hellow world"));

	}


}
