package online.money_daisuki.api.monkey.console;

import java.util.ArrayList;

import online.money_daisuki.api.base.Requires;

/**
 * History that store changes in the history and the actual value like a classical linux shell.
 * @param <T>
 */
public final class SoftstoringHistory<T> {
	private final ArrayList<T> history;
	
	private int index;
	private boolean trimmed;
	
	private final boolean ignoreSequentalDublicates;
	private final int maxEntries;
	
	public SoftstoringHistory(final int maxEntries, final boolean ignoreSequentalDublicates) {
		this.maxEntries = Requires.greaterThanZero(maxEntries, "maxEntries <= 0") + 1;
		this.ignoreSequentalDublicates = ignoreSequentalDublicates;
		
		history = new ArrayList<>();
	}
	public T previous(final T newValue) {
		if(index >= history.size() - 1) {
			return(newValue);
		}
		
		return(updateAndGet(newValue, 1));
	}
	public T next(final T newValue) {
		if(index == 0) {
			return(newValue);
		}
		return(updateAndGet(newValue, -1));
	}
	public void submit(final T newValue) {
		final int size = history.size();
		if(size > 0) {
			if(size == maxEntries) {
				if(!trimmed) {
					history.trimToSize();
					trimmed = true;
				}
				history.remove(size - 1);
			}
			
			if(size == 1 || !ignoreSequentalDublicates || !newValue.equals(history.get(1))) {
				history.set(0, newValue);
				history.add(0, null);
			}
		} else {
			history.add(null);
			history.add(newValue);
		}
		index = 0;
	}
	private T updateAndGet(final T newValue, final int indexChange) {
		if(index == history.size()) {
			history.add(newValue);
		} else {
			history.set(index, newValue);
		}
		index+= indexChange;
		return(history.get(index));
	}
}
