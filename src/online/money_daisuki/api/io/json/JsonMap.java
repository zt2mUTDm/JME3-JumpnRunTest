package online.money_daisuki.api.io.json;

import java.util.Map.Entry;
import java.util.Set;

import online.money_daisuki.api.base.DataSink;
import online.money_daisuki.api.misc.mapping.Mapping;

public interface JsonMap extends JsonElement {
	JsonElement get(String key);
	Set<Entry<String, JsonElement>> entrySet();
	int size();
	boolean containsKey(final String key);
	void forEach(final DataSink<Mapping<String, JsonElement>> target);
}
