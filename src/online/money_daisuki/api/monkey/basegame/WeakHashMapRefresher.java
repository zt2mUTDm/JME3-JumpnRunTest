package online.money_daisuki.api.monkey.basegame;

import java.util.Map;
import java.util.WeakHashMap;

import online.money_daisuki.api.base.Converter;
import online.money_daisuki.api.base.Requires;

public final class WeakHashMapRefresher<K, V> implements Converter<K, V> {
	private final Map<K, V> map;
	private final Converter<K, V> loader;
	
	public WeakHashMapRefresher(final Converter<K, V> loader) {
		this.map = new WeakHashMap<>();
		this.loader = Requires.notNull(loader, "loader == null");
	}
	@Override
	public V convert(final K key) {
		V value = map.get(Requires.notNull(key, "key == null"));
		if(value == null) {
			value = Requires.notNull(loader.convert(key));
			map.put(key, value);
		}
		return(value);
	}
	
	public V get(final K key) {
		return(convert(key));
	}
	public V remove(final K key) {
		return(map.remove(Requires.notNull(key, "key == null")));
	}
	public void clear() {
		map.clear();
	}
}
