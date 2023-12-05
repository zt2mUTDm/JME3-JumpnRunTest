package online.money_daisuki.api.monkey.basegame.shapes;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.scene.Spatial;

import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.io.json.JsonMap;

public final class CollisionShapeControlLoaderAppState extends BaseAppState implements CollisionShapeControlLoader {
	private Application app;
	
	public CollisionShapeControlLoaderAppState() {
	}
	@Override
	public ExtendsPhysicsCollisionObject convert(final JsonMap map, final Spatial spatial) {
		return(new CollisionShapeControlLoaderImpl(map, spatial, Requires.notNull(app, "not initialized")).source());
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
