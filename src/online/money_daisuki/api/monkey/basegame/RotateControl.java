package online.money_daisuki.api.monkey.basegame;

import java.io.IOException;
import java.util.Arrays;

import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.math.Quaternion;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;

import online.money_daisuki.api.base.Requires;

public final class RotateControl implements Control {
	private Spatial spatial;
	
	private final float[] axises;
	
	public RotateControl(final float[] axises) {
		this.axises = Arrays.copyOf(Requires.lenEqual(Requires.notNull(axises, "axises == null"), 3), axises.length);
	}
	
	@Override
	public void update(final float tpf) {
		spatial.rotate(axises[0] * tpf, axises[1] * tpf, axises[2] * tpf);
		final Quaternion q = new Quaternion().fromAngles(axises[0] * tpf, axises[1] * tpf, axises[2] * tpf);
		final RigidBodyControl c = spatial.getControl(RigidBodyControl.class);
		if(c != null) {
			//c.setPhysicsRotation(c.getPhysicsRotation().mult(q));
		}
	}
	
	@Override
	public void render(final RenderManager rm, final ViewPort vp) {
		
	}
	@Override
	public void write(final JmeExporter ex) throws IOException {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void read(final JmeImporter im) throws IOException {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public Control cloneForSpatial(final Spatial spatial) {
		throw new UnsupportedOperationException("Auto-generated method stub"); // TODO
	}
	
	@Override
	public void setSpatial(final Spatial spatial) {
		this.spatial = spatial;
	}
}
