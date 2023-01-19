package online.money_daisuki.api.base;

/**
 * Handler for cases where some data change. Get as argument the old and the
 * new value.
 * 
 * @author (c) Money Daisuki Online
 *
 * @param <T> The type of the values.
 */
public interface ValueChangedHandler<T> {
	
	void valueChanged(T old, T nevv);
	
}
