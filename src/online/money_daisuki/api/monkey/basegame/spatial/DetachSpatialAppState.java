package online.money_daisuki.api.monkey.basegame.spatial;

import com.jme3.app.Application;
import com.jme3.app.state.AppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Spatial;

import online.money_daisuki.api.base.Requires;

public final class DetachSpatialAppState implements AppState {
	private final Spatial spatial;
	
	private boolean initialized;
	private boolean active;
	
	public DetachSpatialAppState(final Spatial spatial) {
		this.spatial = Requires.notNull(spatial, "spatial == null");
		this.active = true;
	}
	@Override
	public void update(final float tpf) {
		if(spatial.getParent() != null) {
			spatial.removeFromParent();
		}
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
		this.active = active;
	}
	@Override
	public boolean isEnabled() {
		return(active);
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
