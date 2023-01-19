package online.money_daisuki.api.io.json;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.collections.UnmodifiableIterator;

public final class DefaultJsonList implements MutableJsonList {
	private final List<JsonElement> list;
	
	public DefaultJsonList() {
		list = new ArrayList<>();
	}
	public DefaultJsonList(final int initSize) {
		list = new ArrayList<>(initSize);
	}
	
	@Override
	public void add(final JsonElement value) {
		list.add(Requires.notNull(value, "value == null"));
	}
	@Override
	public JsonElement get(final int i) {
		if(i < 0 || i >= list.size()) {
			throw new IllegalArgumentException("i < 0 || i >= list.size()");
		}
		return(list.get(i));
	}
	@Override
	public int size() {
		return(list.size());
	}
	@Override
	public Iterator<JsonElement> iterator() {
		return(new UnmodifiableIterator<>(list.iterator()));
	}
	
	@Override
	public boolean isMap() {
		return(false);
	}
	@Override
	public JsonMap asMap() {
		throw new ClassCastException("Not a map: " + toString());
	}
	
	@Override
	public boolean isList() {
		return(true);
	}
	@Override
	public JsonList asList() {
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
		return(getClass().getName() + "[list=" + list + "]");
	}
	@Override
	public String toJsonString() {
		final StringBuilder builder = new StringBuilder("[ ");
		int i = 0;
		for(final JsonElement e:list) {
			builder.append(e.toJsonString());
			if(i < list.size() - 1) { // Some parser doesn't like a coma at end
				builder.append(", ");
				i++;
			}
		}
		builder.append(" ]");
		return(builder.toString());
	}
}
