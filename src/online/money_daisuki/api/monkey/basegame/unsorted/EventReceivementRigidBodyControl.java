package online.money_daisuki.api.monkey.basegame.unsorted;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

import com.jme3.bullet.collision.PhysicsCollisionObject;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;

import online.money_daisuki.api.base.DataSink;
import online.money_daisuki.api.base.Requires;

public final class EventReceivementRigidBodyControl extends RigidBodyControl implements EventReceivement {
	private final Collection<DataSink<? super PhysicsCollisionObject>> listeners;
	
	public EventReceivementRigidBodyControl(final CollisionShape shape) {
		super(shape);
		listeners = new LinkedList<>();
	}
	public EventReceivementRigidBodyControl(final float mass) {
		super(mass);
		listeners = new LinkedList<>();
	}
	public EventReceivementRigidBodyControl(final CollisionShape shape, final float mass) {
		super(shape, mass);
		listeners = new LinkedList<>();
	}
	
	@Override
	public void addEventListener(final DataSink<? super PhysicsCollisionObject> l) {
		listeners.add(Requires.notNull(l, "l == null"));
	}
	@Override
	public Collection<? super PhysicsCollisionObject> getEventListeners() {
		return(new ArrayList<>(listeners));
	}
	@Override
	public boolean removeEventListener(final DataSink<? super PhysicsCollisionObject> l) {
		return(listeners.remove(Requires.notNull(l, "l == null")));
	}
	
	@Override
	public void fireEvent(final PhysicsCollisionObject other) {
		for(final DataSink<? super PhysicsCollisionObject> l:listeners) {
			l.sink(other);
		}
	}
}
