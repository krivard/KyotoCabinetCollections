package edu.cmu.kcc;

import java.util.Iterator;

import kyotocabinet.Cursor;


public abstract class KyotoKeyIterator<E> implements Iterator<E> {
	private Cursor cursor;
	private boolean initialized = false;
	public void init(Cursor cursor) {
		this.initialized = true;
		this.cursor = cursor;
		this.cursor.jump();
	}
	public void remove() { throw new UnsupportedOperationException("Can't remove from a Tokyo Cabinet iterator."); }
	public boolean hasNext() {
		if (!initialized) throw new IllegalStateException("Must initialize with a cursor before iterating");
		return (cursor.get_key_str(false) != null);
	}
	public E next() {
		if (!initialized) throw new IllegalStateException("Must initialize with a cursor before iterating");
		String key = cursor.get_key_str(true);
		return keyFromString(key); 
	}
	protected void finalize() {
		cursor.disable();
	}
	
	protected abstract E keyFromString(String s);
	
}
