package online.money_daisuki.api.base;

public final class ConstantDataSource<T> implements DataSource<T> {
	private final T t;
	
	public ConstantDataSource(final T t) {
		this.t = Requires.notNull(t, "t == null");
	}
	@Override
	public T source() {
		return(t);
	}
}
