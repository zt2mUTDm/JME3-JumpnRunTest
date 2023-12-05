package online.money_daisuki.api.monkey.basegame.spatial;

import com.jme3.bullet.control.CharacterControl;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.util.TempVars;

import online.money_daisuki.api.base.Requires;

public final class CharacterControlTransformControl extends AbstractControl implements SpatialTransformControl {
	private CharacterControl cc;
	
	
	@Override
	public void setSpatial(final Spatial spatial) {
		cc = Requires.notNull(spatial.getControl(CharacterControl.class), "Can only add to an Spatial containing CharacterControl");
		super.setSpatial(spatial);
	}
	
	@Override
	protected void controlUpdate(final float tpf) {
		
	}
	
	@Override
	public void setTranslation(final Vector3f vec) {
		cc.setPhysicsLocation(vec);
	}
	
	@Override
	public void setVelocity(final Vector3f vec) {
		cc.setWalkDirection(vec);
	}
	
	@Override
	public void setViewDirection(final Quaternion quat) {
		final TempVars tmp = TempVars.get();
		try {
			tmp.vect1.set(Vector3f.UNIT_Z);
			quat.multLocal(tmp.vect1);
			cc.setViewDirection(tmp.vect1);
		} finally {
			tmp.release();
		}
	}
	
	@Override
	protected void controlRender(final RenderManager rm, final ViewPort vp) {
		
	}
}
