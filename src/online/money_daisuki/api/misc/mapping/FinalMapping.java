package online.money_daisuki.api.misc.mapping;

import java.util.Objects;

import online.money_daisuki.api.base.Requires;

/**
 * Maps two elements, immutable.
 * @param <T>
 * @param <U>
 */
public final class FinalMapping<T, U> implements Mapping<T, U> {
	private final T a;
	private final U b;
	
	public FinalMapping(final T a, final U b) {
		this.a = Requires.notNull(a, "a == null");
		this.b = Requires.notNull(b, "b == null");;
	}
	public FinalMapping(final Mapping<T, U> src) {
		this(Requires.notNull(src, "src == null").getA(), src.getB());
	}
	
	@Override
	public boolean equals(final Object obj) {
		if(obj == null || getClass() != obj.getClass()) {
			return(false);
		}
		
		@SuppressWarnings("unchecked")
		final FinalMapping<T, U> cast = (FinalMapping<T, U>) obj;
		return(this == obj || (a.equals(cast.a) && b.equals(cast.b)));
	}
	@Override
	public int hashCode() {
		return(Objects.hash(a, b));
	}
	
	@Override
	public T getA() {
		return a;
	}
	@Override
	public U getB() {
		return b;
	}
	
	@Override
	public String toString() {
		return(getClass().getName() + "[a=" + a + ",b=" + b + "]");
	}
}
