package online.money_daisuki.api.io.json;

import java.math.BigDecimal;
import java.math.BigInteger;

import online.money_daisuki.api.base.Requires;

public final class JsonFloatDataElement implements JsonDataNumberElement {
	private final BigDecimal d;
	
	public JsonFloatDataElement(final double d) {
		this(new BigDecimal(d));
	}
	public JsonFloatDataElement(final BigDecimal d) {
		this.d = Requires.notNull(d, "d == null");
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
		return(true);
	}
	@Override
	public double asFloat() {
		return(d.doubleValue());
	}
	@Override
	public boolean isInt() {
		return(false);
	}
	@Override
	public long asInt() {
		throw new ClassCastException("Not an int number data: " + toString());
	}
	@Override
	public boolean isBigInteger() {
		return(false);
	}
	@Override
	public BigInteger asBigInteger() {
		throw new ClassCastException("Not an big integer number data: " + toString());
	}
	
	@Override
	public boolean isBigDecimal() {
		return(true);
	}
	@Override
	public BigDecimal asBigDecimal() {
		return(d);
	}	
	
	@Override
	public BigInteger castToInt() {
		return(d.toBigIntegerExact());
	}
	@Override
	public BigDecimal castToFloat() {
		return(d);
	}
	
	@Override
	public String toString() {
		return(getClass().getName() + "[d=" + d + "]");
	}
	@Override
	public String toJsonString() {
		return(d.toPlainString());
	}
}
