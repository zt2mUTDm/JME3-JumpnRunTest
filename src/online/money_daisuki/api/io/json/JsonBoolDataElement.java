package online.money_daisuki.api.io.json;

public final class JsonBoolDataElement implements JsonDataElement {
	private final boolean b;
	
	public JsonBoolDataElement(final boolean b) {
		this.b = b;
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
		return(false);
	}
	@Override
	public Void asNull() {
		throw new ClassCastException("Not a null data: " + toString());
	}
	@Override
	public boolean isBool() {
		return(true);
	}
	@Override
	public boolean asBool() {
		return(b);
	}
	@Override
	public boolean isString() {
		return(true);
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
	
	public boolean getValue() {
		return b;
	}
	
	@Override
	public String toString() {
		return(getClass().getName() + "[b=" + b + "]");
	}
	@Override
	public String toJsonString() {
		return(String.valueOf(b));
	}
}
