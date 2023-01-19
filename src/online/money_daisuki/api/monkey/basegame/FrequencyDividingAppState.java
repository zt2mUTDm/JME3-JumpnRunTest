package online.money_daisuki.api.monkey.basegame;

import com.jme3.app.Application;
import com.jme3.app.state.AppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.renderer.RenderManager;

import online.money_daisuki.api.base.Requires;

public final class FrequencyDividingAppState implements AppState {
	private final AppState state;
	
	private final float speed;
	
	private float counter;
	
	public FrequencyDividingAppState(final AppState state, final float speed) {
		this.state = Requires.notNull(state, "state == null");
		this.speed = Requires.greaterThanZero(speed, "speed <= 0");
	}
	
	@Override
	public void update(final float tpf) {
		counter+= tpf;
		if(counter >= speed) {
			state.update(tpf);
			counter-= speed;
		}
	}
	
	@Override
	public void initialize(final AppStateManager stateManager, final Application app) {
		state.initialize(stateManager, app);
	}
	@Override
	public final boolean isInitialized() {
		return(state.isInitialized());
	}
	@Override
	public final String getId() {
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
