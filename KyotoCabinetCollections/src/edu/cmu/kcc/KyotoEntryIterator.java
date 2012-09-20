package edu.cmu.kcc;

import java.util.AbstractMap;
import java.util.Iterator;
import java.util.Map;

import kyotocabinet.Cursor;

public abstract class KyotoEntryIterator<E,F> implements Iterator<Map.Entry<E,F>> {
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
	public Map.Entry<E,F> next() {
		if (!initialized) throw new IllegalStateException("Must initialize with a cursor before iterating");
		String key   = cursor.get_key_str(false);
		String value = cursor.get_value_str(true);
		return new AbstractMap.SimpleEntry<E,F>(keyFromString(key),valueFromString(value));
	}
	protected abstract E keyFromString(String s);
	protected abstract F valueFromString(String s);
	

	protected void finalize() {
		cursor.disable();
	}
}
