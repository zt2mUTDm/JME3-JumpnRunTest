package online.money_daisuki.api.base;

/**
 * {@link DataSink} that receives a ValueChangeHandler and call it with
 * old and new value taken from two {@link DataSource}s.
 * 
 * @author (c) Money Daisuki Online
 *
 * @param <T> The type of the old and new value.
 */
public final class FireValueChangedListenerSink<T> implements DataSink<ValueChangedHandler<T>> {
	private final DataSource<T> oldValueSource;
	private final DataSource<T> newValueSource;
	
	public FireValueChangedListenerSink(final DataSource<T> oldValueSource,
			final DataSource<T> newValueSource) {
		this.oldValueSource = Requires.notNull(oldValueSource, "oldValueSource == null");
		this.newValueSource = Requires.notNull(newValueSource, "newValueSource == null");
	}
	@Override
	public void sink(final ValueChangedHandler<T> value) {
		value.valueChanged(oldValueSource.source(), newValueSource.source());
	}
}
