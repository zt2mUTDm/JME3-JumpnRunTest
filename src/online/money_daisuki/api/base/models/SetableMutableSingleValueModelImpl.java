package online.money_daisuki.api.base.models;

import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;

import online.money_daisuki.api.base.DataSource;
import online.money_daisuki.api.base.OnceRunnable;
import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.base.ValueChangedHandler;

/**
 * Class that holds a single non-null element. The element can be unset, in this
 * case it is stored as null, an access throws an {@link IllegalStateException}.
 * 
 * @author (c) Money Daisuki Online
 *
 * @param <T> Type of the value.
 */
public final class SetableMutableSingleValueModelImpl<T> implements SetableMutableSingleValueModel<T> {
	private T value;
	private final Queue<ValueChangedHandler<? super T>> changeHandlers;
	
	public SetableMutableSingleValueModelImpl() {
		this.changeHandlers = new LinkedList<>();
	}
	public SetableMutableSingleValueModelImpl(final T value) {
		this.value = Requires.notNull(value, "value == null");
		this.changeHandlers = new LinkedList<>();
	}
	public SetableMutableSingleValueModelImpl(final DataSource<? extends T> src) {
		this(Requires.notNull(src, "src == null").source());
	}
	
	@Override
	public T source() {
		Requires.isTrue(isSet(), new IllegalStateException("Is unset"));
		return (value);
	}
	@Override
	public boolean isSet() {
		return(value != null);
	}
	@Override
	public void unset() {
		final T oldValue = value;
		value = null;
		
		if(oldValue != null) {
			fireValueChangedEvents(oldValue, null);
		}
	}
	@Override
	public void sink(final T t) {
		final T old = this.value;
		this.value = Requires.notNull(t, "t == null");
		
		if(!Objects.equals(old, value)) {
			fireValueChangedEvents(old, value);
		}
	}
	
	private void fireValueChangedEvents(final T oldValue, final T newValue) {
		if(Objects.equals(oldValue, newValue)) {
			return;
		}
			
		for(final ValueChangedHandler<? super T> h:changeHandlers) {
			h.valueChanged(oldValue, newValue);
		}
	}
	
	@Override
	public Runnable addValueChangedHandler(final ValueChangedHandler<? super T> l) {
		changeHandlers.add(Requires.notNull(l, "l == null"));
		
		return(new OnceRunnable(new Runnable() {
			@Override
			public void run() {
				removeValueChangedHandler(l);
			}
		}));
	}
	@Override
	public void removeValueChangedHandler(final ValueChangedHandler<? super T> l) {
		changeHandlers.remove(Requires.notNull(l, "l == null"));
	}
	
	/*@Override
	public String toString() {
		return(getClass().getName() + "[value=" + value + ",changeHandlers=" + changeHandlers.getClass() +
				"[" + new CollectionStringFactory<>(changeHandlers, new ToStringGenerator<>(new LoopbackGenerator<>()), ",") + "]]");
	}*/
}
