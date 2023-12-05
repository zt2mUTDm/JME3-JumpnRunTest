package online.money_daisuki.api.monkey.basegame.cam;

import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;

import online.money_daisuki.api.base.Requires;

public final class FixedLocationLookAtPlayerCamera extends AbstractControl implements GameCamera {
	private final Camera cam;
	private final Vector3f worldUpVector;
	
	private Spatial target;
	
	public FixedLocationLookAtPlayerCamera(final Camera cam) {
		this.cam = Requires.notNull(cam, "cam == null");
		this.worldUpVector = new Vector3f(Vector3f.UNIT_Y);
	}
	public FixedLocationLookAtPlayerCamera(final Camera cam, final Vector3f worldUpVector) {
		this.cam = Requires.notNull(cam, "cam == null");
		this.worldUpVector = new Vector3f(Requires.notNull(worldUpVector, "worldUpVector == null"));
	}
	public FixedLocationLookAtPlayerCamera(final Camera cam, final Spatial target, final Vector3f worldUpVector) {
		this.cam = Requires.notNull(cam, "cam == null");
		this.target = Requires.notNull(target, "target == null");
		this.worldUpVector = new Vector3f(Requires.notNull(worldUpVector, "worldUpVector == null"));
	}
	@Override
	protected void controlUpdate(final float tpf) {
		cam.lookAt(getSpatial().getWorldTranslation(), worldUpVector);
	}
	@Override
	protected void controlRender(final RenderManager rm, final ViewPort vp) {
		
	}
	
	@Override
	public void acquire() {
		if(target != null) {
			target.addControl(this);
		}
	}
	@Override
	public void dispose() {
		if(target != null) {
			target.removeControl(this);
		}
	}
}
