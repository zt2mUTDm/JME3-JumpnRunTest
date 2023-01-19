package online.money_daisuki.api.base;

/**
 * High generic interface for an object, returning one instance.
 * 
 * @author (c) Money Daisuki Online
 * @see DataSink
 *
 * @param <T>
 */
public interface DataSource<T> {
	
	T source();
	
}
