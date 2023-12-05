package online.money_daisuki.api.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

import online.money_daisuki.api.base.DataSink;
import online.money_daisuki.api.base.Requires;

public final class DataListenerContainer<T> {
	private final Collection<DataSink<? super T>> listeners;
	
	public DataListenerContainer() {
		this.listeners = new LinkedList<>();
	}
	
	public void addListener(final DataSink<? super T> l) {
		listeners.add(Requires.notNull(l, "l == null"));
	}
	public Collection<DataSink< ? super T>> getListeners() {
		return(new ArrayList<>(listeners));
	}
	public boolean removeListener(final DataSink<? super T> l) {
		return(listeners.remove(Requires.notNull(l, "l == null")));
	}
	public void clearListeners() {
		listeners.clear();
	}
	
	public void fireListeners(final T argument) {
		for(final DataSink<? super T> l:listeners) {
			l.sink(argument);
		}
	}
}
