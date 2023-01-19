package online.money_daisuki.api.base;

import java.util.function.Consumer;

/**
 * {@link Consumer} adapter class to call a {@link DataSink}.
 * 
 * @author (c) Money Daisuki Online
 *
 * @param <T> Argument type.
 */
public final class DataSinkConsumer<T> implements Consumer<T> {
	private final DataSink<T> parent;
	
	public DataSinkConsumer(final DataSink<T> parent) {
		this.parent = Requires.notNull(parent, "parent == null");
	}
	@Override
	public void accept(final T t) {
		parent.sink(t);
	}
}
