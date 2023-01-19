package online.money_daisuki.api.collections;

import java.util.Iterator;

public final class UnmodifiableIterator<T> implements Iterator<T> {
	private final Iterator<T> parent;

	public UnmodifiableIterator(final Iterator<T> parent) {
		if(parent == null) {
			throw new IllegalArgumentException("parent == null");
		}
		this.parent = parent;
	}
	@Override
	public boolean hasNext() {
		return(parent.hasNext());
	}
	@Override
	public T next() {
		return(parent.next());
	}
	@Override
	public void remove() {
		throw new UnsupportedOperationException("UnmodifiableIterator");
	}
}