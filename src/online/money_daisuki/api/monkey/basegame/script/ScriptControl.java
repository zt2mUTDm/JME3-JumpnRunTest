package online.money_daisuki.api.monkey.basegame.script;

import java.io.IOException;

import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;

import online.money_daisuki.api.base.Requires;

public final class ScriptControl implements Control {
	private final ScriptLineExecutor exec;
	private final boolean looped;
	
	private final float speed;
	
	private float counter;
	
	public ScriptControl(final ScriptLineExecutor exec, final float speed, final boolean looped) {
		this.exec = Requires.notNull(exec, "exec == null");
		this.speed = Requires.greaterThanZero(speed, "speed <= 0.0f");
		this.looped = looped;
	}
	@Override
	public void update(final float tpf) {
		counter+=tpf;
		while(counter > speed) {
			exec.sink(tpf);
			
			if(exec.isDone() && looped) {
				exec.reset();
			}
			counter-= speed;
		}
	}
	
	@Override
	public Control cloneForSpatial(final Spatial spatial) {
		return(new ScriptControl(exec, speed, looped));
	}
	
	@Override
	public void setSpatial(final Spatial spatial) {
		//this.spatial = spatial;
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
