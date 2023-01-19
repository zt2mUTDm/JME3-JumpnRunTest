package online.money_daisuki.api.monkey.basegame;

import java.io.IOException;

import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;

public final class ControlTemplate implements Control {
	private Spatial spatial;
	
	public ControlTemplate() {
		
	}
	@Override
	public void update(final float tpf) {
		throw new UnsupportedOperationException("Auto-generated method stub"); // TODO
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
