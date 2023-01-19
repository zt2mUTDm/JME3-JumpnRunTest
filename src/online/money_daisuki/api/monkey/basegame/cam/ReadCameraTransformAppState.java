package online.money_daisuki.api.monkey.basegame.cam;

import com.jme3.app.Application;
import com.jme3.app.state.AppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.ChaseCamera;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Spatial;

import online.money_daisuki.api.base.DataSink;
import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.base.SetableDataSource;
import online.money_daisuki.api.misc.mapping.FinalTriple;
import online.money_daisuki.api.misc.mapping.Triple;

public final class ReadCameraTransformAppState implements AppState {
	private final DataSink<Triple<Float, Float, Float>> target;
	private final SetableDataSource<Spatial> player;
	
	private boolean initialized;
	private boolean enabled;
	
	public ReadCameraTransformAppState(final DataSink<Triple<Float, Float, Float>> target, final SetableDataSource<Spatial> player) {
		this.target = Requires.notNull(target, "target == null");
		this.player = Requires.notNull(player, "player == null");
		
		this.enabled = true;
	}
	
	@Override
	public void update(final float tpf) {
		if(!player.isSet()) {
			return;
		}
		final Spatial spatial = player.source();
		final ChaseCamera cam = spatial.getControl(ChaseCamera.class);
		if(cam != null) {
			target.sink(new FinalTriple<Float, Float, Float>(
					cam.getHorizontalRotation(),
					cam.getVerticalRotation(),
					cam.getDistanceToTarget()
			));
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
