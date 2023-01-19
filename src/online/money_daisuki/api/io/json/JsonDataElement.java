package online.money_daisuki.api.io.json;

public interface JsonDataElement extends JsonElement {
	boolean isNull();
	Void asNull();
	
	boolean isBool();
	boolean asBool();
	
	boolean isString();
	String asString();
	
	boolean isNumber();
	
	JsonDataNumberElement asNumber();
}
