package online.money_daisuki.api.monkey.basegame.character.control;

import com.jme3.bullet.control.PhysicsControl;
import com.jme3.bullet.objects.PhysicsCharacter;
import com.jme3.math.Vector3f;

import online.money_daisuki.api.monkey.basegame.spatial.HasSpatial;
import online.money_daisuki.api.monkey.basegame.spatial.Translatable;

public interface CharControl extends PhysicsControl, Cloneable, Translatable, HasSpatial {
	
	void setMoveVector(Vector3f vec);
	
	Vector3f getMoveVector(Vector3f vec);
	
	void setViewDirection(Vector3f direction);
	
	@Override
	void setTranslation(Vector3f location);
	
	void jump();
	
	boolean isOnGround();
	
	void playAnimation(String name, boolean once);
	
	void playAnimation(String name, boolean once, Runnable l);
	
	Vector3f getLinearVelocity(Vector3f vec);
	
	PhysicsCharacter getCharacter();
	
}
