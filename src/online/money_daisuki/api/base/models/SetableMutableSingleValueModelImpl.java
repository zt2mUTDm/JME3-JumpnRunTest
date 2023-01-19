package online.money_daisuki.api.base.models;

import java.util.LinkedList;
import java.util.Queue;

import online.money_daisuki.api.base.ConstantDataSource;
import online.money_daisuki.api.base.DataSinkConsumer;
import online.money_daisuki.api.base.DataSource;
import online.money_daisuki.api.base.FireValueChangedListenerSink;
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
	private final Queue<ValueChangedHandler<T>> changeHandlers;
	
	public SetableMutableSingleValueModelImpl() {
		this.changeHandlers = new LinkedList<>();
	}
	public SetableMutableSingleValueModelImpl(final T value) {
		this.value = Requires.notNull(value, "value == null");
		this.changeHandlers = new LinkedList<>();
	}
	public SetableMutableSingleValueModelImpl(final DataSource<T> src) {
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
		value = null;
	}
	@Override
	public void sink(final T t) {
		final T old = this.value;
		final T firedOld = (old != null ? old : t);
		this.value = Requires.notNull(t, "t == null");
		
		changeHandlers.forEach(new DataSinkConsumer<>(new FireValueChangedListenerSink<>(
				new ConstantDataSource<>(firedOld), new ConstantDataSource<>(t))));
	}
	
	@Override
	public Runnable addValueChangedHandler(final ValueChangedHandler<T> l) {
		changeHandlers.add(Requires.notNull(l, "l == null"));
		
		return(new OnceRunnable(new Runnable() {
			@Override
			public void run() {
				changeHandlers.remove(l);
			}
		}));
	}
	
	/*@Override
	public String toString() {
		return(getClass().getName() + "[value=" + value + ",changeHandlers=" + changeHandlers.getClass() +
				"[" + new CollectionStringFactory<>(changeHandlers, new ToStringGenerator<>(new LoopbackGenerator<>()), ",") + "]]");
	}*/
}
