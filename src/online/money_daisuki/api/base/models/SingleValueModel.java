package online.money_daisuki.api.base.models;

import online.money_daisuki.api.base.DataSource;
import online.money_daisuki.api.base.ValueChangedHandler;

/**
 * Interface for objects that hold a single value and return them as {@link DataSource}.
 * There is also a way to add a {@link ValueChangedHandler}.
 * 
 * @author (c) Money Daisuki Online
 *
 * @param <T> Type of the value.
 */
public interface SingleValueModel<T> extends DataSource<T> {
	
	Runnable addValueChangedHandler(ValueChangedHandler<? super T> l);
	
	void removeValueChangedHandler(ValueChangedHandler<? super T> l);
	
}
