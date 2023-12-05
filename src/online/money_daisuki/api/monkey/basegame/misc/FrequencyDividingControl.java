package online.money_daisuki.api.monkey.basegame.misc;

import java.io.IOException;

import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;

import online.money_daisuki.api.base.Requires;

public final class FrequencyDividingControl implements Control {
	private final Control parent;
	private final float speed;
	
	private float counter;
	
	public FrequencyDividingControl(final Control parent, final float speed) {
		this.parent = Requires.notNull(parent, "parent == null");
		this.speed = Requires.greaterThanZero(speed, "speed <= 0");
	}
	
	@Override
	public void update(final float tpf) {
		counter+= tpf;
		while(counter >= speed) {
			parent.update(tpf);
			counter-= speed;
		}
	}
	
	@Override
	public void render(final RenderManager rm, final ViewPort vp) {
		parent.render(rm, vp);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public Control cloneForSpatial(final Spatial spatial) {
		return(new FrequencyDividingControl(parent.cloneForSpatial(spatial), speed));
	}
	
	@Override
	public void read(final JmeImporter im) throws IOException {
		throw new UnsupportedOperationException("Auto-generated method stub"); // TODO
	}
	@Override
	public void write(final JmeExporter ex) throws IOException {
		throw new UnsupportedOperationException("Auto-generated method stub"); // TODO
	}
	@Override
	public void setSpatial(final Spatial spatial) {
		parent.setSpatial(spatial);
	}
}
