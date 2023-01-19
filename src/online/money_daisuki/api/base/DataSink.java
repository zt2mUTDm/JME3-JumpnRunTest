package online.money_daisuki.api.base;

import java.util.function.Consumer;

/**
 * High generic interface for an object, receiving one instance.
 * It is basicly the same as {@link Consumer} but is better for the
 * opposite type {@link DataSource}.
 * 
 * @author (c) Money Daisuki Online
 * @see DataSource
 *
 * @param <T> The argument of the generic method.
 */
public interface DataSink<T> {
	
	public void sink(T value);
	
}
