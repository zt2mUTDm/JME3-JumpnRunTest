package online.money_daisuki.api.io.json;

public interface JsonElement {
	
	public boolean isMap();
	public JsonMap asMap();
	
	public boolean isList();
	public JsonList asList();
	
	public boolean isData();
	public JsonDataElement asData();
	
	public String toJsonString();
}
