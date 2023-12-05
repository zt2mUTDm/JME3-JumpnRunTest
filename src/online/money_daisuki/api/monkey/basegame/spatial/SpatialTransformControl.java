package online.money_daisuki.api.monkey.basegame.spatial;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.control.Control;

public interface SpatialTransformControl extends Control {
	
	public void setTranslation(Vector3f vec);
	
	public void setVelocity(Vector3f vec);
	
	void setViewDirection(Quaternion quat);
	
}
