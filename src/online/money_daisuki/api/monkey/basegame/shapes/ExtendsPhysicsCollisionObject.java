package online.money_daisuki.api.monkey.basegame.shapes;

import java.util.Collection;

import com.jme3.bullet.collision.PhysicsCollisionObject;

import online.money_daisuki.api.base.BiDataSink;

public interface ExtendsPhysicsCollisionObject {
	
	void setName(String name);
	
	String getName();
	
	void addEventListener(BiDataSink<? super PhysicsCollisionObject, ? super PhysicsCollisionObject> l);
	
	Collection<? super PhysicsCollisionObject> getEventListeners();
	
	boolean removeEventListener(BiDataSink<? super PhysicsCollisionObject, ? super PhysicsCollisionObject> l);
	
	void fireEvent(PhysicsCollisionObject other);
	
	// Inherit from PhysicsCollisionObject
	
	void setCollisionGroup(int collisionGroupMask);
	
	void setCollideWithGroups(int collisionGroupMask);
	
}
