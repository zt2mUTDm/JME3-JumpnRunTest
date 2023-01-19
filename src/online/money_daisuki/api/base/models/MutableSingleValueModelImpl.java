package online.money_daisuki.api.base.models;

import java.util.LinkedList;
import java.util.List;

import online.money_daisuki.api.base.DataSource;
import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.base.ValueChangedHandler;

/**
 * Class that holds a single non-null element.
 * 
 * @author (c) Money Daisuki Online
 *
 * @param <T> Type of the value.
 */
public final class MutableSingleValueModelImpl<T> implements MutableSingleValueModel<T> {
	private final List<ValueChangedHandler<T>> changeHandlers;
	
	private T value;
	
	public MutableSingleValueModelImpl(final T value) {
		this.value = Requires.notNull(value, "value == null");
		this.changeHandlers = new LinkedList<>();
	}
	public MutableSingleValueModelImpl(final DataSource<T> src) {
		this(Requires.notNull(src, "src == null").source());
	}
	
	@Override
	public T source() {
		return (value);
	}	
	@Override
	public void sink(final T t) {
		final T old = this.value;
		this.value = Requires.notNull(t, "t == null");
		
		for(final ValueChangedHandler<T> h:changeHandlers) {
			h.valueChanged(old, t);
		}
	}
	
	@Override
	public Runnable addValueChangedHandler(final ValueChangedHandler<T> l) {
		changeHandlers.add(Requires.notNull(l, "l == null"));
		
		return(new Runnable() {
			@Override
			public void run() {
				changeHandlers.remove(l);
			}
		});
	}
	
	/*@Override
	public String toString() {
		return(getClass().getName() + "[value=" + value + ",changeHandlers=" + changeHandlers.getClass().getName() +
				"[" + new CollectionStringFactory<>(changeHandlers, new ToStringGenerator<>(new LoopbackGenerator<>()), ",") + "]]");
	}*/
}
