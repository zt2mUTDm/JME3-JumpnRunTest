package online.money_daisuki.api.base.models;

import online.money_daisuki.api.base.Setable;

/**
 * Interface to extends a {@link MutableSingleValueModel} with {@link Setable} to give
 * the ability to be empty and remove the stored value.
 * 
 * @author (c) Money Daisuki Online
 *
 * @param <T> Type of the value.
 */
public interface SetableMutableSingleValueModel<T> extends SetableSingleValueModel<T>, MutableSingleValueModel<T> {

}
