package online.money_daisuki.api.misc.mapping;

public interface Triple<T, U, X> extends Mapping<T, U> {

	T getA();

	U getB();
	
	X getC();

}