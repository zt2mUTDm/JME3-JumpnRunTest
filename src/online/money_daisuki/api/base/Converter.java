package online.money_daisuki.api.base;

/**
 * <p>The Converter interface is a high generatic interface for functions that
 * take one argument and returns something of possible another type.</p> 
 *
 * <p>This template is recommend to stack some Converters behind to reduce
 * them to a single call.</p>
 * 
 * <pre>
 * public final class DoSomethingConverter<R> implements Converter&lt;First, R&gt; {
 * 	private final Converter&lt;? super Second, ? extends R&gt; parent;
 * 	
 * 	public DoSomethingConverter(final Converter&lt;? super Second, ? extends R&gt; parent) {
 * 		this.parent = Requires.notNull(parent, "parent == null");
 * 	}
 * 	&commat;Override
 * 	public R convert(final First value) {
 * 		return(parent.convert(doConvert(value)));
 * 	}
 * 	private Second doConvert(final First value) {
 * 		throw new UnsupportedOperationException("Replace with your convertion");
 * 	}
 * }
 * </pre>
 * 
 * @author (c) Money Daisuki Online
 *
 * @param <A>
 * @param <R>
 */
public interface Converter<A, R> {
	
	R convert(A value);
	
}
