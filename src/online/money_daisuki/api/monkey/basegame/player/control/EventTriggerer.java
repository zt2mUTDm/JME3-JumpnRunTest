package online.money_daisuki.api.monkey.basegame.player.control;

import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.collision.PhysicsCollisionObject;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.GhostControl;

import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.base.models.MutableSingleValueModel;
import online.money_daisuki.api.base.models.MutableSingleValueModelImpl;
import online.money_daisuki.api.monkey.basegame.player.control.EventReceiver.TriggerType;

public final class EventTriggerer extends GhostControl {
	private final BulletAppState bullet;
	
	private final MutableSingleValueModel<Boolean> triggered;
	
	public EventTriggerer(final CollisionShape shape, final BulletAppState bullet) {
		super(shape);
		this.bullet = Requires.notNull(bullet, "bullet == null");
		this.triggered = new MutableSingleValueModelImpl<>(Boolean.FALSE);
	}
	public boolean run(final boolean b) {
		triggered.sink(Boolean.FALSE);
		bullet.getPhysicsSpace().contactTest(this, new PhysicsCollisionListener() {
			@Override
			public void collision(final PhysicsCollisionEvent event) {
				test(event.getObjectA());
				test(event.getObjectB());
			}
			private void test(final PhysicsCollisionObject ob) {
				if(ob == EventTriggerer.this) {
					return;
				}
				
				if(ob instanceof EventReceiver) {
					final EventReceiver cast = ((EventReceiver)ob);
					
					if(cast.getTriggerType() == TriggerType.TOUCH || b) {
						cast.trigger();
						triggered.sink(Boolean.TRUE);
					}
				} else if(ob instanceof OnOffEventReceiver) {
					final OnOffEventReceiver cast = ((OnOffEventReceiver)ob);
					cast.trigger();
				} else {
					triggered.sink(Boolean.TRUE);
				}
			}
		});
		return(triggered.source().booleanValue());
	}
}
