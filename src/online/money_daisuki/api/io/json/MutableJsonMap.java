package online.money_daisuki.api.io.json;

public interface MutableJsonMap extends JsonMap {
	void put(String key, JsonElement value);
}
