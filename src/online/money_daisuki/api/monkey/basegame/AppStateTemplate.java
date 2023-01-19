package online.money_daisuki.api.monkey.basegame;

import com.jme3.app.Application;
import com.jme3.app.state.AppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.renderer.RenderManager;

public final class AppStateTemplate implements AppState {
	private boolean initialized;
	private boolean enabled;
	
	public AppStateTemplate() {
		this.enabled = true;
	}
	
	@Override
	public void update(final float tpf) {
		throw new UnsupportedOperationException("Auto-generated method stub"); // TODO
	}
	
	@Override
	public void initialize(final AppStateManager stateManager, final Application app) {
		initialized = true;
	}
	@Override
	public boolean isInitialized() {
		return(initialized);
	}
	@Override
	public String getId() {
		return(null);
	}
	@Override
	public void setEnabled(final boolean active) {
		this.enabled = active;
	}
	@Override
	public boolean isEnabled() {
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
