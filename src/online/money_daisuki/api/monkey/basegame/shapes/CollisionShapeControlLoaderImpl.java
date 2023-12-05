package online.money_daisuki.api.monkey.basegame.shapes;

import com.jme3.app.Application;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.scene.Spatial;

import online.money_daisuki.api.base.DataSource;
import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.io.json.JsonMap;

public final class CollisionShapeControlLoaderImpl implements DataSource<ExtendsPhysicsCollisionObject> {
	private final JsonMap map;
	private final Spatial spatial;
	private final Application app;
	
	public CollisionShapeControlLoaderImpl(final JsonMap map, final Spatial spatial, final Application app) {
		this.map = Requires.notNull(map, "map == null");
		this.spatial = Requires.notNull(spatial, "spatial == null");
		this.app = Requires.notNull(app, "app == null");
	}
	@Override
	public ExtendsPhysicsCollisionObject source() {
		final CollisionShape shape = app.getStateManager().getState(CollisionShapeLoaderAppState.class).convert(map, spatial);
		
		final ExtendsPhysicsCollisionObject c = parseControl(map, spatial, shape);
		loadPhysicalCollisionGroup(map, c);
		return(c);
	}
	private ExtendsPhysicsCollisionObject parseControl(final JsonMap map, final Spatial spatial, final CollisionShape shape) {
		final String purpose = map.get("purpose").asData().asString();
		switch(purpose) {
			case("rigid"):
				return(loadRigidPurpose(map, shape));
			case("ghost"):
				return(loadGhostPurpose(map, shape));
			default:
				throw new IllegalArgumentException("Unknown purpose: " + purpose);
		}
	}
	private ExtendsPhysicsCollisionObject loadGhostPurpose(final JsonMap map, final CollisionShape shape) {
		final ExtendedGhostControl c = new ExtendedGhostControl(shape);
		return(c);
	}
	private ExtendsPhysicsCollisionObject loadRigidPurpose(final JsonMap map, final CollisionShape shape) {
		final float mass = map.get("mass").asData().asNumber().asBigDecimal().floatValue();
		
		final ExtendedRigidBodyControl c = new ExtendedRigidBodyControl(shape, mass);
		return(c);
	}
	private void loadPhysicalCollisionGroup(final JsonMap map, final ExtendsPhysicsCollisionObject obj) {
		if(map.containsKey("collisionGroup")) {
			final int collisionGroupShift = map.get("collisionGroup").asData().asNumber().asBigInteger().intValueExact();
			final int collisionGroupMask = (1 << collisionGroupShift);
			obj.setCollisionGroup(collisionGroupMask);
			obj.setCollideWithGroups(collisionGroupMask);
		}
	}
}
