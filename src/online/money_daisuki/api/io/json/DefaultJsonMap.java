package online.money_daisuki.api.io.json;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Set;

import online.money_daisuki.api.base.DataSink;
import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.collections.UnmodifiableMapEntry;
import online.money_daisuki.api.misc.mapping.FinalMapping;
import online.money_daisuki.api.misc.mapping.Mapping;

public final class DefaultJsonMap implements MutableJsonMap {
	private final Map<String, JsonElement> map;
	
	public DefaultJsonMap() {
		map = new HashMap<>();
	}
	public DefaultJsonMap(final Map<String, JsonElement> parent) {
		if(parent == null) {
			throw new IllegalArgumentException("parent == null");
		}
		map = new HashMap<>(parent);
	}
	public DefaultJsonMap(final JsonMap parent) {
		map = new HashMap<>();
		
		for(final Entry<String, JsonElement> e:Requires.notNull(parent, "parent == null").entrySet()) {
			map.put(e.getKey(), e.getValue());
		}
	}
	
	@Override
	public void put(final String key, final JsonElement value) {
		if(key == null) {
			throw new IllegalArgumentException("key == null");
		}
		map.put(key, value);
	}
	@Override
	public JsonElement get(final String key) {
		if(key == null) {
			return(null);
		} else if(!map.containsKey(key)) {
			throw new NoSuchElementException("JSON map key " + key + " doesn't exist.");
		}
		return(map.get(key));
	}
	@Override
	public int size() {
		return(map.size());
	}
	@Override
	public boolean containsKey(final String key) {
		if(key == null) {
			throw new IllegalArgumentException("key == null");
		}
		return(map.containsKey(key));
	}
	@Override
	public Set<Entry<String, JsonElement>> entrySet() {
		final Set<Entry<String, JsonElement>> set = new HashSet<>();
		for(final Entry<String, JsonElement> entry:map.entrySet()) {
			set.add(new UnmodifiableMapEntry<>(entry));
		}
		return(set);
	}
	
	@Override
	public boolean isList() {
		return(false);
	}
	@Override
	public JsonList asList() {
		throw new ClassCastException("Not a list: " + toString());
	}
	
	@Override
	public boolean isMap() {
		return(true);
	}
	@Override
	public JsonMap asMap() {
		return(this);
	}
	
	@Override
	public boolean isData() {
		return(false);
	}
	@Override
	public JsonDataElement asData() {
		throw new ClassCastException("Not a data: " + toString());
	}
	
	@Override
	public String toString() {
		return(getClass().getName() + "[map=" + map + "]");
	}
	@Override
	public String toJsonString() {
		final StringBuilder builder = new StringBuilder("{ ");
		final Set<Entry<String, JsonElement>> set = map.entrySet();
		int i = 0;
		for(final Entry<String, JsonElement> entry:set) {
			builder.append("\"");
			builder.append(entry.getKey());
			builder.append("\"");
			builder.append(": ");
			builder.append(entry.getValue().toJsonString());
			
			if(i < set.size() - 1) {  // Some parser doesn't like a coma at end
				builder.append(", ");
				i++;
			}
		}
		builder.append(" }");
		return(builder.toString());
	}
	
	@Override
	public void forEach(final DataSink<Mapping<String, JsonElement>> target) {
		for(final Entry<String, JsonElement> e:entrySet()) {
			target.sink(new FinalMapping<>(e.getKey(), e.getValue()));
		}
	}
}
