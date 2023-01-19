package online.money_daisuki.api.io.json;

import java.math.BigDecimal;
import java.math.BigInteger;

public final class JsonIntDataElement implements JsonDataNumberElement {
	private final BigInteger i;
	
	public JsonIntDataElement(final long l) {
		this(String.valueOf(l));
	}
	public JsonIntDataElement(final String s) {
		this(new BigInteger(s));
	}
	public JsonIntDataElement(final BigInteger i) {
		if(i == null) {
			throw new IllegalArgumentException("i == null");
		}
		this.i = i;
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
		return(false);
	}
	@Override
	public String asString() {
		throw new ClassCastException("Not a string data: " + toString());
	}
	@Override
	public boolean isNumber() {
		return(true);
	}
	@Override
	public JsonDataNumberElement asNumber() {
		return(this);
	}
	
	@Override
	public boolean isFloat() {
		return(false);
	}
	@Override
	public double asFloat() {
		throw new ClassCastException("Not a float number data: " + toString());
	}
	@Override
	public boolean isInt() {
		return(true);
	}
	@Override
	public long asInt() {
		return(i.longValue());
	}
	@Override
	public boolean isBigInteger() {
		return(true);
	}
	@Override
	public BigInteger asBigInteger() {
		return(i); // Imutable
	}
	
	@Override
	public boolean isBigDecimal() {
		return(true);
	}
	@Override
	public BigDecimal asBigDecimal() {
		return(new BigDecimal(i));
	}
	
	@Override
	public BigInteger castToInt() {
		return(asBigInteger());
	}
	@Override
	public BigDecimal castToFloat() {
		return(new BigDecimal(i));
	}
	
	@Override
	public String toString() {
		return(getClass().getName() + "[i=" + i + "]");
	}
	@Override
	public String toJsonString() {
		return(String.valueOf(i.toString(10)));
	}
}
