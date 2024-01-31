package online.money_daisuki.api.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

import online.money_daisuki.api.base.Requires;

/**
 * Template:<br/>
 * 
	public void add[Template:Uppercase]Listener(final Runnable l) {
		[Template:Lowercase]Listeners.addListener(Requires.notNull(l, "l == null"));
	}
	public Collection<Runnable> get[Template:Uppercase]Listeners() {
		return([Template:Lowercase]Listeners.getListeners());
	}
	public boolean remove[Template:Uppercase]Listener(final Runnable l) {
		return([Template:Lowercase]Listeners.removeListener(Requires.notNull(l, "l == null")));
	}
	public void clear[Template:Uppercase]Listeners() {
		[Template:Lowercase]Listeners.clearListeners();
	}
	
	private void fire[Template:Uppercase]Listeners() {
		[Template:Lowercase]Listeners.fireListeners();
	}
 * 
 * @author Money Daisuki Online
 *
 */
public final class NoDataListenerContainer {
	private final Collection<Runnable> listeners;
	
	public NoDataListenerContainer() {
		this.listeners = new LinkedList<>();
	}
	
	public void addListener(final Runnable l) {
		listeners.add(Requires.notNull(l, "l == null"));
	}
	public Collection<Runnable> getListeners() {
		return(new ArrayList<>(listeners));
	}
	public boolean removeListener(final Runnable l) {
		return(listeners.remove(Requires.notNull(l, "l == null")));
	}
	public void clearListeners() {
		listeners.clear();
	}
	
	public void fireListeners() {
		for(final Runnable l:listeners) {
			l.run();
		}
	}
}
