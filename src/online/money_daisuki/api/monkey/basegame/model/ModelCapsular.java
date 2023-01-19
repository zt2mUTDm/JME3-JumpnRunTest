package online.money_daisuki.api.monkey.basegame.model;

import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.scene.Spatial;

public interface ModelCapsular {
	
	Spatial getSpatial();
	
	boolean hasCharControl();
	
	CharacterControl getCharControl();
	
	boolean hasBodyControl();
	
	RigidBodyControl getBodyControl();
	
}
