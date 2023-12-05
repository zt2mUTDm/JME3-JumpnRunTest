package online.money_daisuki.api.monkey.basegame.unsorted;

import java.util.Collection;

import com.jme3.bullet.collision.PhysicsCollisionObject;

import online.money_daisuki.api.base.DataSink;

public interface EventReceivement {
	
	void addEventListener(DataSink<? super PhysicsCollisionObject> l);
	
	Collection<? super PhysicsCollisionObject> getEventListeners();
	
	boolean removeEventListener(DataSink<? super PhysicsCollisionObject> l);
	
	void fireEvent(PhysicsCollisionObject other);
	
}
