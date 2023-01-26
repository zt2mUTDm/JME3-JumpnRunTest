package online.money_daisuki.api.monkey.basegame.player.control;

import java.util.Collection;

import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.collision.PhysicsCollisionObject;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.GhostControl;

import online.money_daisuki.api.base.Requires;

public final class NotifyReceiver extends GhostControl {
	private final BulletAppState bullet;
	
	public NotifyReceiver(final CollisionShape shape, final BulletAppState bullet) {
		super(shape);
		this.bullet = Requires.notNull(bullet, "bullet == null");
	}
	public void run(final Collection<String> target) {
		bullet.getPhysicsSpace().contactTest(this, new PhysicsCollisionListener() {
			@Override
			public void collision(final PhysicsCollisionEvent event) {
				test(event.getObjectA());
				test(event.getObjectB());
			}
			private void test(final PhysicsCollisionObject ob) {
				if(ob == NotifyReceiver.this) {
					return;
				}
				
				if(ob instanceof NotifySender) {
					final NotifySender cast = ((NotifySender)ob);
					target.add(cast.getDescription());
				}
			}
		});
	}
}
