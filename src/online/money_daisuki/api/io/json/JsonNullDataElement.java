package online.money_daisuki.api.io.json;

public final class JsonNullDataElement implements JsonDataElement {
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
		return(false);
	}
	@Override
	public JsonList asList() {
		throw new ClassCastException("Not a list: " + toString());
	}
	
	@Override
	public boolean isData() {
		return(true);
	}
	@Override
	public JsonDataElement asData() {
		return(this);
	}
	
	@Override
	public boolean isNull() {
		return(true);
	}
	@Override
	public Void asNull() {
		return(null);
	}
	@Override
	public boolean isBool() {
		return(false);
	}
	@Override
	public boolean asBool() {
		throw new ClassCastException("Not a boolean data: " + toString());
	}
	@Override
	public boolean isString() {
		return(false);
	}
	@Override
	public String asString() {
		throw new ClassCastException("Not a string data: " + toString());
	}
	@Override
	public boolean isNumber() {
		return(false);
	}
	@Override
	public JsonDataNumberElement asNumber() {
		throw new ClassCastException("Not a number data: " + toString());
	}
	
	@Override
	public String toString() {
		return(getClass().getName());
	}
	@Override
	public String toJsonString() {
		return(null);
	}
}
