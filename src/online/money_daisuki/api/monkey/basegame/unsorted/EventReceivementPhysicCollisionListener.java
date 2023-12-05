package online.money_daisuki.api.monkey.basegame.unsorted;

import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.collision.PhysicsCollisionObject;

public final class EventReceivementPhysicCollisionListener implements PhysicsCollisionListener {
	@Override
	public void collision(final PhysicsCollisionEvent event) {
		final PhysicsCollisionObject a = event.getObjectA();
		final PhysicsCollisionObject b = event.getObjectB();
		collision(a, b);
		collision(b, a);
	}
	private void collision(final PhysicsCollisionObject a, final PhysicsCollisionObject b) {
		if(a instanceof EventReceivement) {
			((EventReceivement) a).fireEvent(b);
		}
	}
}
