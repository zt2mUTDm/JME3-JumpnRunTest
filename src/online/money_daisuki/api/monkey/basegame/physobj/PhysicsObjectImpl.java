package online.money_daisuki.api.monkey.basegame.physobj;

import java.util.HashMap;
import java.util.Map;

import com.jme3.bullet.control.PhysicsControl;
import com.jme3.scene.Spatial;

import online.money_daisuki.api.base.Requires;

public final class PhysicsObjectImpl implements PhysicsObject {
	private final Spatial spatial;
	private final Map<String, PhysicsControl> collisions;
	
	public PhysicsObjectImpl(final Spatial spatial, final Map<String, PhysicsControl> collisions) {
		this.spatial = Requires.notNull(spatial, "spatial == null");
		this.collisions = Requires.containsNotNull(new HashMap<>(Requires.notNull(collisions, "collisions == null")));
	}
	
	@Override
	public Spatial getSpatial() {
		return (spatial);
	}
	public PhysicsControl getCollision(final String name) {
		return (collisions.get(name));
	}
}
