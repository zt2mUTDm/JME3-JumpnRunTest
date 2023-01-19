package online.money_daisuki.api.monkey.basegame.character.control;

import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.collision.PhysicsCollisionObject;
import com.jme3.bullet.collision.shapes.ConvexShape;
import com.jme3.bullet.control.GhostControl;
import com.jme3.bullet.objects.PhysicsCharacter;
import com.jme3.scene.Spatial;

import online.money_daisuki.api.base.Converter;
import online.money_daisuki.api.base.DataSource;
import online.money_daisuki.api.base.Requires;

public final class CharacterOnGroundContactTester implements DataSource<Boolean> {
	private boolean initalized;
	
	private GhostControl ghost;
	
	private final ConvexShape collisionShape;
	private final Spatial spatialToAttach;
	private final int staticCollidingObjectCount;
	private final PhysicsCollisionListener l;
	private final BulletAppState bullet;
	
	private final CollisionFilterListener listener;
	
	/**
	 * 
	 * @param collisionShape
	 * @param spatialToAttach
	 * @param staticCollidingObjectCount
	 * @param l may be null
	 * @param bullet
	 */
	public CharacterOnGroundContactTester(final ConvexShape collisionShape, final Spatial spatialToAttach,
			final int staticCollidingObjectCount, final PhysicsCollisionListener l,
			final BulletAppState bullet) {
		this.collisionShape = Requires.notNull(collisionShape, "collisionShape == null");
		this.spatialToAttach = Requires.notNull(spatialToAttach, "spatialToAttach == null");
		this.staticCollidingObjectCount = Requires.positive(staticCollidingObjectCount, "staticCollidingObjectCount < 0");
		this.l = l;
		this.bullet = Requires.notNull(bullet, "bullet == null");
		
		this.listener = new CollisionFilterListener(new Converter<PhysicsCollisionEvent, Boolean>() {
			@Override
			public Boolean convert(final PhysicsCollisionEvent event) {
				return(!(event.getObjectA() instanceof PhysicsCharacter || event.getObjectB() instanceof PhysicsCharacter));
			}
		});
	}
	@Override
	public Boolean source() {
		if(!initalized) {
			doInit();
			initalized = true;
		}
		
		listener.reset();
		bullet.getPhysicsSpace().contactTest(ghost, listener);
		return(listener.i > staticCollidingObjectCount);
	}
	public PhysicsCollisionObject getCollisionObject() {
		return (ghost);
	}
	
	public void deinitialize() {
		if(initalized) {
			bullet.getPhysicsSpace().remove(ghost);
			spatialToAttach.removeControl(ghost);
			
			ghost = null;
			
			initalized = false;
		}
	}
	private void doInit() {
		ghost = new GhostControl(collisionShape);
		ghost.setCollisionGroup(PhysicsCollisionObject.COLLISION_GROUP_16);
		ghost.setCollideWithGroups(1);
		bullet.getPhysicsSpace().add(ghost);
		spatialToAttach.addControl(ghost);
	}
	
	private final class CollisionFilterListener implements PhysicsCollisionListener {
		private int i;
		private final Converter<PhysicsCollisionEvent, Boolean> converter;
		
		public CollisionFilterListener(final Converter<PhysicsCollisionEvent, Boolean> converter) {
			this.converter = Requires.notNull(converter, "converter == null");
		}
		public void reset() {
			i = 0;
		}
		@Override
		public void collision(final PhysicsCollisionEvent event) {
			if(converter.convert(event)) {
				i++;
			}
		}
	}
}
