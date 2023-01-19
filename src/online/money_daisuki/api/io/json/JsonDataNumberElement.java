package online.money_daisuki.api.io.json;

import java.math.BigDecimal;
import java.math.BigInteger;

public interface JsonDataNumberElement extends JsonDataElement {
	boolean isFloat();
	double asFloat();
	
	boolean isInt();
	long asInt();
	
	boolean isBigInteger();
	BigInteger asBigInteger();
	
	boolean isBigDecimal();
	BigDecimal asBigDecimal();
	
	BigInteger castToInt();
	BigDecimal castToFloat();
}
