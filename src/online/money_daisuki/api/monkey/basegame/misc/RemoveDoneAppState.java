package online.money_daisuki.api.monkey.basegame.misc;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.app.state.BaseAppState;

import online.money_daisuki.api.base.Requires;

public final class RemoveDoneAppState extends BaseAppState {
	private final Collection<MayDoneAppState> states;
	
	private Application app;
	
	public RemoveDoneAppState() {
		this.states = new LinkedList<>();
	}
	
	public void addAppState(final MayDoneAppState state) {
		states.add(Requires.notNull(state, "state == null"));
	}
	public void removeAppState(final MayDoneAppState state) {
		states.remove(Requires.notNull(state, "state == null"));
	}
	
	@Override
	public void update(final float tpf) {
		final AppStateManager stateMnr = app.getStateManager();
		
		final Iterator<MayDoneAppState> it = states.iterator();
		while(it.hasNext()) {
			final MayDoneAppState state = it.next();
			if(state.isDone()) {
				stateMnr.detach(state);
				it.remove();
			}
		}
	}
	@Override
	protected void initialize(final Application app) {
		this.app = app;
	}
	@Override
	protected void cleanup(final Application app) {
		this.app = null;
	}
	@Override
	protected void onEnable() {
		
	}
	@Override
	protected void onDisable() {
		
	}
}
