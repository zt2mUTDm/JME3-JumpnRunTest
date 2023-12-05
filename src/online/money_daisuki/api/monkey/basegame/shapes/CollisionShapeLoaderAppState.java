package online.money_daisuki.api.monkey.basegame.shapes;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.scene.Spatial;

import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.io.json.JsonMap;

public final class CollisionShapeLoaderAppState extends BaseAppState implements CollisionShapeLoader {
	private final CollisionShapeLoader parent;
	
	public CollisionShapeLoaderAppState(final CollisionShapeLoader parent) {
		this.parent = Requires.notNull(parent, "parent == null");
	}
	@Override
	public CollisionShape convert(final JsonMap map, final Spatial spatial) {
		return(parent.convert(map, spatial));
	}
	
	@Override
	protected void initialize(final Application app) {
		
	}
	@Override
	protected void cleanup(final Application app) {
		
	}
	@Override
	protected void onEnable() {
		
	}
	@Override
	protected void onDisable() {
		
	}
}
