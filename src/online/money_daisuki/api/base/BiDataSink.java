package online.money_daisuki.api.base;

public interface BiDataSink<A, B> {
	
	public void sink(A a, B b);
	
}
