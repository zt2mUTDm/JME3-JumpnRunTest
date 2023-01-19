package online.money_daisuki.api.monkey.basegame.player.control;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;

import online.money_daisuki.api.base.Requires;

public final class NamedEventTriggerControl implements Control {
	private final Map<String, EventTriggerer> triggers;
	
	public NamedEventTriggerControl(final Map<String, EventTriggerer> triggers) {
		this.triggers = Requires.containsNotNull(new HashMap<>(Requires.notNull(triggers, "triggers == null")));
	}
	
	@Override
	public void update(final float tpf) {
		
	}
	
	public boolean run(final String name, final boolean b) {
		final EventTriggerer trig = Requires.notNull(triggers.get(Requires.notNull(name)), "Trigger " + name + " not found");
		return(trig.run(b));
	}
	
	@Override
	public void setSpatial(final Spatial spatial) {
		
	}
	
	@Override
	public Control cloneForSpatial(final Spatial spatial) {
		return(new NamedEventTriggerControl(triggers));
	}
	
	@Override
	public void render(final RenderManager rm, final ViewPort vp) {
		
	}
	
	@Override
	public void write(final JmeExporter ex) throws IOException {
		throw new UnsupportedOperationException("Auto-generated method stub");
	}
	
	@Override
	public void read(final JmeImporter im) throws IOException {
		throw new UnsupportedOperationException("Auto-generated method stub");
	}
	
}
