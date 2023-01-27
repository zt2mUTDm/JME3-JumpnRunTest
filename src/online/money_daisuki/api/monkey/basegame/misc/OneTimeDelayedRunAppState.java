package online.money_daisuki.api.monkey.basegame.misc;

import com.jme3.app.Application;
import com.jme3.app.state.AppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.renderer.RenderManager;

import online.money_daisuki.api.base.Requires;

public final class OneTimeDelayedRunAppState implements MayDoneAppState {
	private final AppState state;
	private final float delay;
	
	private float counter;
	private boolean done;
	
	public OneTimeDelayedRunAppState(final AppState state, final float delay) {
		this.state = Requires.notNull(state, "state == null");
		this.delay = Requires.positive(delay, "delay < 0");
	}
	@Override
	public void update(final float tpf) {
		if(!done) {
			counter+= tpf;
			if(counter > delay) {
				state.update(tpf);
				done = true;
			}
		}
	}
	@Override
	public boolean isDone() {
		return(done);
	}
	@Override
	public void initialize(final AppStateManager stateManager, final Application app) {
		state.initialize(stateManager, app);
	}
	@Override
	public boolean isInitialized() {
		return(state.isInitialized());
	}
	@Override
	public String getId() {
		return(state.getId());
	}
	@Override
	public void setEnabled(final boolean active) {
		state.setEnabled(active);
	}
	@Override
	public boolean isEnabled() {
		return(state.isEnabled());
	}
	@Override
	public void stateAttached(final AppStateManager stateManager) {
		state.stateAttached(stateManager);
	}
	@Override
	public void stateDetached(final AppStateManager stateManager) {
		state.stateDetached(stateManager);
	}
	@Override
	public void render(final RenderManager rm) {
		state.render(rm);
	}
	@Override
	public void postRender() {
		state.postRender();
	}
	@Override
	public void cleanup() {
		state.cleanup();
	}
}
