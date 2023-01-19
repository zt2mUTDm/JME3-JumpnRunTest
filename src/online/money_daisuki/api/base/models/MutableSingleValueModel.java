package online.money_daisuki.api.base.models;

import online.money_daisuki.api.base.DataSink;

/**
 * {@link SingleValueModel} extended by a {@link DataSink} to set the stored
 * value.
 * 
 * @author (c) Money Daisuki Online
 *
 * @param <T> Type of the value.
 */
public interface MutableSingleValueModel<T> extends SingleValueModel<T>, DataSink<T> {
	
}
