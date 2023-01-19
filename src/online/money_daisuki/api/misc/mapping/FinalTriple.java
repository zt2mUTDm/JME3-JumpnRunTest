package online.money_daisuki.api.misc.mapping;

/**
 * Maps three elements, immortable.
 * Equals and HashCode is not implement, Write a wrappler to implement your own equals- and hashing strategy.
 * Null is allowed.
 */
public final class FinalTriple<T, U, X> implements Triple<T, U, X> {
	private final T a;
	private final U b;
	private final X c;
	
	public FinalTriple(final T a, final U b, final X c) {
		this.a = a;
		this.b = b;
		this.c = c;
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
	public X getC() {
		return c;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append(getClass().getName());
		builder.append("[a=");
		builder.append(a);
		builder.append(",b=");
		builder.append(b);
		builder.append(",c=");
		builder.append(c);
		builder.append("]");
		return(builder.toString());
	}
}
