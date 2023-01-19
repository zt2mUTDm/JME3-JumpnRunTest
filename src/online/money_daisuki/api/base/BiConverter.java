package online.money_daisuki.api.base;

/**
 * A type like {@link Converter}, but take two instead a single argument.
 * This make unnecessary to wrap multiple objects in one for frequenting
 * multiple calls like in a loop.
 * 
 * @see Converter
 * @author (c) Money Daisuki Online
 *
 * @param <A> First argument
 * @param <B> Second argument
 * @param <O> Return value
 */
public interface BiConverter<A, B, O> {
	
	public O convert(A a, B b);
	
}
