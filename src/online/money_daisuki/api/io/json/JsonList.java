package online.money_daisuki.api.io.json;

public interface JsonList extends JsonElement, Iterable<JsonElement> {
	JsonElement get(int i);
	int size();
}
