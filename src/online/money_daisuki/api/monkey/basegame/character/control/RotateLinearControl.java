package online.money_daisuki.api.monkey.basegame.character.control;

import java.io.IOException;
import java.util.Arrays;

import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;
import com.jme3.util.TempVars;

import online.money_daisuki.api.base.DataSink;
import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.monkey.basegame.misc.Utils;

public final class RotateLinearControl implements Control {
	private final float[] angles;
	private final float[] toMove;
	private final float speed;
	private final Runnable doneListener;
	
	private boolean done;
	
	private Spatial spatial;
	
	public RotateLinearControl(final float[] angles, final float speed, final Runnable doneListener) {
		this.angles = Requires.lenEqual(Arrays.copyOf(angles, Requires.notNull(angles, "angles == null").length), 3);
		this.toMove = new float[] {
				Math.abs(angles[0]),
				Math.abs(angles[1]),
				Math.abs(angles[2])
		};
		this.speed = Requires.greaterThanZero(speed, "speed <= 0");
		this.doneListener = Requires.notNull(doneListener, "doneListener <= 0");
	}
	@Override
	public void update(final float tpf) {
		if(done) {
			return;
		}
		
		final float x = get(tpf, 0);
		final float y = get(tpf, 1);
		final float z = get(tpf, 2);
		
		final TempVars vars = TempVars.get();
		try {
			vars.quat1.fromAngles(x, y, z);
			spatial.rotate(vars.quat1);
			Utils.forEachControl(spatial, RigidBodyControl.class, new DataSink<RigidBodyControl>() {
				@Override
				public void sink(final RigidBodyControl value) {
					value.getPhysicsRotation(vars.quat2);
					vars.quat2.multLocal(vars.quat1);
					value.setPhysicsRotation(vars.quat2);
				}
			});
		} finally {
			vars.release();
		}
		
		if(toMove[0] == 0.0f && toMove[1] == 0.0f && toMove[2] == 0.0f) {
			doneListener.run();
			done = true;
		}
	}
	private float get(final float tpf, final int axis) {
		float distance = angles[axis] * tpf * speed;
		final float distanceAbs = Math.abs(distance);
		final float max = toMove[axis];
		if(distanceAbs > max) {
			distance = max;
			toMove[axis] = 0.0f;
		} else {
			toMove[axis] = max - distanceAbs;
		}
		return(distance);
	}
	
	@Override
	public Control cloneForSpatial(final Spatial spatial) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void setSpatial(final Spatial spatial) {
		this.spatial = spatial;
	}
	
	@Override
	public void render(final RenderManager rm, final ViewPort vp) {
		
	}
	
	@Override
	public void read(final JmeImporter im) throws IOException {
		throw new UnsupportedOperationException("Auto-generated method stub"); // TODO
	}
	@Override
	public void write(final JmeExporter ex) throws IOException {
		throw new UnsupportedOperationException("Auto-generated method stub"); // TODO
	}
}
