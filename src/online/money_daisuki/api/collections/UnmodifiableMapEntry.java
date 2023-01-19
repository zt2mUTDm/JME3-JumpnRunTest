package online.money_daisuki.api.collections;

import java.util.Map.Entry;

public final class UnmodifiableMapEntry<K, V> implements Entry<K, V> {
	private final Entry<K, V> parent;
	
	public UnmodifiableMapEntry(final Entry<K, V> parent) {
		if(parent == null) {
			throw new IllegalArgumentException("parent == null");
		}
		this.parent = parent;
	}
	@Override
	public K getKey() {
		return(parent.getKey());
	}
	@Override
	public V getValue() {
		return(parent.getValue());
	}
	@Override
	public V setValue(V value) {
		throw new UnsupportedOperationException("Unmodifable Map-Entry!");
	}
}

