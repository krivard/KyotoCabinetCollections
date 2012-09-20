package edu.cmu.kcc;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import kyotocabinet.DB;


public abstract class KyotoCabinetMap<K,V> implements Map<K, V> {
	
	protected DB db;
	protected KyotoCabinetPersistance persistance;
	public KyotoCabinetMap(String filename, char mode) throws IOException {
		persistance = new KyotoCabinetPersistance();
		if (!filename.endsWith(".kch")) { throw new IllegalArgumentException("Kyoto Cabinet files must end in .kch"); }
		db = persistance.initDB(filename, mode);
	}
	
	public abstract K unmakeKey(String key);
	public abstract String makeKey(Object key);
	
	@Override
	public boolean containsKey(Object key) {
		return db.get(makeKey(key)) != null;
	}

	@Override
	public Set<java.util.Map.Entry<K, V>> entrySet() {
		return new TokyoEntrySet();
	}

	public abstract V unmakeValue(String value);
	public abstract String makeValue(V value);
	@Override
	public V get(Object key) {
		return unmakeValue(db.get(makeKey(key)));
	}

	@Override
	public boolean isEmpty() {
		return db.count() == 0;
	}

	@Override
	public Set<K> keySet() {
		return new TokyoKeySet();
	}

	@Override
	public V put(K key, V value) {
		String skey = makeKey(key);
		V oldvalue = unmakeValue(db.get(skey));
		if (db.set(makeKey(key), makeValue(value))) return oldvalue;
		else throw new KyotoCabinetException("Couldn't put "+skey,db);
	}

	@Override
	public V remove(Object key) {
		String skey = makeKey(key);
		V oldvalue = unmakeValue(db.get(skey));
		if(db.remove(skey)) return oldvalue;
		else throw new KyotoCabinetException("Couldn't remove "+skey,db);
	}

	@Override
	public int size() {
		return (int) db.count();
	}

	@Override
	public Collection<V> values() {
		return new TokyoValueSet();
	}

	protected void finalize() {
		persistance.close(db);
	}
	
	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		throw new UnsupportedOperationException("Not yet implemented");
	}
	@Override
	public boolean containsValue(Object value) {
		throw new UnsupportedOperationException("Not yet implemented");
	}
	@Override
	public void clear() {
		throw new UnsupportedOperationException("Not yet implemented");
	}
	
	
	/************************************ Utility Classes ********************************************/
	
	protected class TokyoKeySet implements Set<K> {

		@Override
		public Iterator<K> iterator() {
			KyotoKeyIterator<K> it = new KyotoKeyIterator<K>() {
				protected K keyFromString(String key) {
					return unmakeKey(key);
				}
			};
			it.init(db.cursor());
			return it;
		}
		
		@Override
		public boolean add(K arg0) { throw new UnsupportedOperationException(); }

		@Override
		public boolean addAll(Collection<? extends K> arg0) { throw new UnsupportedOperationException(); }

		@Override
		public void clear() { throw new UnsupportedOperationException(); }

		@Override
		public boolean contains(Object arg0) { throw new UnsupportedOperationException(); }

		@Override
		public boolean containsAll(Collection<?> arg0) { throw new UnsupportedOperationException(); }

		@Override
		public boolean isEmpty() { throw new UnsupportedOperationException(); }

		@Override
		public boolean remove(Object arg0) { throw new UnsupportedOperationException(); }

		@Override
		public boolean removeAll(Collection<?> arg0) { throw new UnsupportedOperationException(); }

		@Override
		public boolean retainAll(Collection<?> arg0) { throw new UnsupportedOperationException(); }

		@Override
		public int size() { throw new UnsupportedOperationException(); }

		@Override
		public Object[] toArray() { throw new UnsupportedOperationException(); }

		@Override
		public <T> T[] toArray(T[] arg0) { throw new UnsupportedOperationException(); }
	}

	protected class TokyoValueSet implements Collection<V> {

		@Override
		public Iterator<V> iterator() {
			KyotoValueIterator<V> it = new KyotoValueIterator<V>() {
				protected V valueFromString(String value) {
					return unmakeValue(value);
				}
			};
			it.init(db.cursor());
			return it;
		}
		@Override
		public boolean add(V arg0) { throw new UnsupportedOperationException(); }

		@Override
		public boolean addAll(Collection<? extends V> arg0) { throw new UnsupportedOperationException(); }

		@Override
		public void clear() { throw new UnsupportedOperationException(); }

		@Override
		public boolean contains(Object arg0) { throw new UnsupportedOperationException(); }

		@Override
		public boolean containsAll(Collection<?> arg0) { throw new UnsupportedOperationException(); }

		@Override
		public boolean isEmpty() { throw new UnsupportedOperationException(); }

		@Override
		public boolean remove(Object arg0) { throw new UnsupportedOperationException(); }

		@Override
		public boolean removeAll(Collection<?> arg0) { throw new UnsupportedOperationException(); }

		@Override
		public boolean retainAll(Collection<?> arg0) { throw new UnsupportedOperationException(); }

		@Override
		public int size() { throw new UnsupportedOperationException(); }

		@Override
		public Object[] toArray() { throw new UnsupportedOperationException(); }

		@Override
		public <T> T[] toArray(T[] arg0) { throw new UnsupportedOperationException(); }
	}
	
	protected class TokyoEntrySet implements Set<Map.Entry<K,V>> {

		@Override
		public Iterator<java.util.Map.Entry<K, V>> iterator() {
			KyotoEntryIterator<K,V> it = new KyotoEntryIterator<K,V>() {
				protected K keyFromString(String key) { return unmakeKey(key); }
				protected V valueFromString(String value) { return unmakeValue(value); }
			};
			it.init(db.cursor());
			return it;
		}
		
		@Override
		public boolean add(java.util.Map.Entry<K, V> e) { throw new UnsupportedOperationException(); }

		@Override
		public boolean addAll(Collection<? extends java.util.Map.Entry<K, V>> c) { throw new UnsupportedOperationException(); }

		@Override
		public void clear() { throw new UnsupportedOperationException(); }

		@Override
		public boolean contains(Object o) { throw new UnsupportedOperationException(); }

		@Override
		public boolean containsAll(Collection<?> c) { throw new UnsupportedOperationException(); }

		@Override
		public boolean isEmpty() { throw new UnsupportedOperationException(); }


		@Override
		public boolean remove(Object o) { throw new UnsupportedOperationException(); }

		@Override
		public boolean removeAll(Collection<?> c) { throw new UnsupportedOperationException(); }

		@Override
		public boolean retainAll(Collection<?> c) { throw new UnsupportedOperationException(); }

		@Override
		public int size() { throw new UnsupportedOperationException(); }

		@Override
		public Object[] toArray() { throw new UnsupportedOperationException(); }

		@Override
		public <T> T[] toArray(T[] a) { throw new UnsupportedOperationException(); }
	}
}
