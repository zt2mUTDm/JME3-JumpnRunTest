package online.money_daisuki.api.monkey.basegame.physobj;

import org.python.core.PyBoolean;
import org.python.core.PyObject;

import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;

import online.money_daisuki.api.base.Requires;

public final class MoveKinematicControl extends AbstractControl {
	private final Vector3f target;
	private final float speed;
	private final PyObject owner;
	
	private boolean done;
	
	private Vector3f moveDistance;
	private Vector3f moveDirection;
	
	public MoveKinematicControl(final Vector3f target, final float speed, final PyObject owner) {
		this.target = Requires.notNull(target, "target == null");
		this.speed = Requires.positive(speed, "speed < 0");
		this.owner = Requires.notNull(owner, "owner < 0");
	}
	@Override
	protected void controlUpdate(final float tpf) {
		if(done) {
			return;
		} else if(moveDistance == null) {
			moveDistance = target.subtract(spatial.getLocalTranslation());
			moveDirection = moveDistance.normalize();
			
			moveDistance.x = Math.abs(moveDistance.x);
			moveDistance.y = Math.abs(moveDistance.y);
			moveDistance.z = Math.abs(moveDistance.z);
		}
		
		final float x = get(tpf, 0);
		final float y = get(tpf, 1);
		final float z = get(tpf, 2);
		
		if(x == 0 && y == 0 && z == 0) {
			owner.invoke("signalMovingDone", new PyBoolean(false));
			done = true;
		} else {
			spatial.move(x, y, z);
		}
	}
	private float get(final float tpf, final int axis) {
		float distance = tpf * speed * moveDirection.get(axis);
		final float distanceAbs = Math.abs(distance);
		final float max = moveDistance.get(axis);
		if(distanceAbs > max) {
			distance = max;
			moveDistance.set(axis, 0);
		} else {
			moveDistance.set(axis, max - distanceAbs);
		}
		return(distance);
	}
	@Override
	protected void controlRender(final RenderManager rm, final ViewPort vp) {
		
	}
}
