package online.money_daisuki.api.monkey.basegame.character.control;

import java.io.IOException;

import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;
import com.jme3.util.TempVars;

import online.money_daisuki.api.base.DataSink;
import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.monkey.basegame.ControlTemplate;
import online.money_daisuki.api.monkey.basegame.misc.Utils;

public final class MoveInDirectLineControl implements Control {
	private final Vector3f target;
	private final float speed;
	private final Runnable doneListener;
	
	private boolean done;
	
	private Spatial spatial;
	
	private Vector3f moveDistance;
	private Vector3f moveDirection;
	
	public MoveInDirectLineControl(final Vector3f target, final float speed, final Runnable doneListener) {
		this.target = new Vector3f(Requires.notNull(target, "target == null"));
		this.speed = Requires.greaterThanZero(speed, "speed <= 0");
		this.doneListener = Requires.notNull(doneListener, "doneListener <= 0");
	}
	@Override
	public void update(final float tpf) {
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
		
		spatial.move(x, y, z);
		
		final TempVars vars = TempVars.get();
		try {			
			Utils.forEachControl(spatial, RigidBodyControl.class, new DataSink<RigidBodyControl>() {
				@Override
				public void sink(final RigidBodyControl value) {
					value.getPhysicsLocation(vars.vect1);
					vars.vect1.addLocal(x, y, z);
					value.setPhysicsLocation(vars.vect1);
				}
			});
		} finally {
			vars.release();
		}
		
		if(moveDistance.x == 0 && moveDistance.y == 0 && moveDistance.z == 0) {
			doneListener.run();
			done = true;
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
	public Control cloneForSpatial(final Spatial spatial) {
		return(new ControlTemplate());
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
