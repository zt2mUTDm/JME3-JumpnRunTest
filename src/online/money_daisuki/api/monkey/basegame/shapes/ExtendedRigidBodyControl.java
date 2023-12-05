package online.money_daisuki.api.monkey.basegame.shapes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

import com.jme3.bullet.collision.PhysicsCollisionObject;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;

import online.money_daisuki.api.base.BiDataSink;
import online.money_daisuki.api.base.Requires;

public final class ExtendedRigidBodyControl extends RigidBodyControl implements ExtendsPhysicsCollisionObject {
	private final Collection<BiDataSink<? super PhysicsCollisionObject, ? super PhysicsCollisionObject>> listeners;
	
	private String name;
	
	public ExtendedRigidBodyControl(final CollisionShape shape) {
		super(shape);
		listeners = new LinkedList<>();
	}
	public ExtendedRigidBodyControl(final float mass) {
		super(mass);
		listeners = new LinkedList<>();
	}
	public ExtendedRigidBodyControl(final CollisionShape shape, final float mass) {
		super(shape, mass);
		listeners = new LinkedList<>();
	}
	
	@Override
	public void setName(final String name) {
		this.name = Requires.notNull(name, "name == null");
	}
	@Override
	public String getName() {
		return (name);
	}
	
	@Override
	public void addEventListener(final BiDataSink<? super PhysicsCollisionObject,
			? super PhysicsCollisionObject> l) {
		listeners.add(Requires.notNull(l, "l == null"));
	}
	@Override
	public Collection<? super PhysicsCollisionObject> getEventListeners() {
		return(new ArrayList<>(listeners));
	}
	@Override
	public boolean removeEventListener(final BiDataSink<? super PhysicsCollisionObject, ? super PhysicsCollisionObject> l) {
		return(listeners.remove(Requires.notNull(l, "l == null")));
	}
	
	@Override
	public void fireEvent(final PhysicsCollisionObject other) {
		for(final BiDataSink<? super PhysicsCollisionObject, ? super PhysicsCollisionObject> l:listeners) {
			l.sink(this, other);
		}
	}
}
