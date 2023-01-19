package online.money_daisuki.api.monkey.basegame;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import com.jme3.app.Application;
import com.jme3.app.state.AppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.renderer.RenderManager;

import online.money_daisuki.api.base.Requires;

public final class RemoveDoneAppState implements AppState {
	private final Collection<MayDoneAppState> states;
	private final AppStateManager stateManager;
	
	private boolean initialized;
	private boolean enabled;
	
	public RemoveDoneAppState(final AppStateManager stateManager) {
		this.stateManager = Requires.notNull(stateManager, "stateManager == null");
		this.states = new LinkedList<>();
		this.enabled = true;
	}
	
	public void addAppState(final MayDoneAppState state) {
		states.add(Requires.notNull(state, "state == null"));
	}
	public void removeAppState(final MayDoneAppState state) {
		states.remove(Requires.notNull(state, "state == null"));
	}
	
	@Override
	public void update(final float tpf) {
		final Iterator<MayDoneAppState> it = states.iterator();
		while(it.hasNext()) {
			final MayDoneAppState state = it.next();
			if(state.isDone()) {
				stateManager.detach(state);
				it.remove();
			}
		}
	}
	
	@Override
	public void initialize(final AppStateManager stateManager, final Application app) {
		this.initialized = true;
	}
	@Override
	public final boolean isInitialized() {
		return(initialized);
	}
	@Override
	public final String getId() {
		return(getClass().getName());
	}
	@Override
	public void setEnabled(final boolean active) {
		this.enabled = active;
	}
	@Override
	public final boolean isEnabled() {
		return(enabled);
	}

	@Override
	public void stateAttached(final AppStateManager stateManager) {
		
	}
	@Override
	public void stateDetached(final AppStateManager stateManager) {
	
	}
	
	@Override
	public void render(final RenderManager rm) {
	
	}
	
	@Override
	public void postRender() {
	
	}
	
	@Override
	public void cleanup() {
	
	}
}
