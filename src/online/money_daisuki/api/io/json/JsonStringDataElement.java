package online.money_daisuki.api.io.json;

public final class JsonStringDataElement implements JsonDataElement {
	private final String s;
	
	public JsonStringDataElement(final String s) {
		if(s == null) {
			throw new IllegalArgumentException("s == null");
		}
		this.s = s;
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
		return(false);
	}
	@Override
	public boolean asBool() {
		throw new ClassCastException("Not a boolean data: " + toString());
	}
	@Override
	public boolean isString() {
		return(true);
	}
	@Override
	public String asString() {
		return(s);
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
		return(getClass().getName() + "[s=" + s + "]");
	}
	@Override
	public String toJsonString() {
		return("\"" + s.replace("\"", "\\\"") + "\"");
	}
}
