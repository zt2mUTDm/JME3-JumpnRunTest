package online.money_daisuki.api.monkey.basegame.player.control;

import java.io.IOException;
import java.util.Map;

import com.jme3.bullet.collision.PhysicsCollisionObject;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;

import online.money_daisuki.api.base.Requires;

public final class NotifyReceiveControl implements Control {
	private final NotifyReceiver recv;
	
	public NotifyReceiveControl(final NotifyReceiver recv) {
		this.recv = Requires.notNull(recv, "recv == null");
	}
	
	@Override
	public void update(final float tpf) {
		
	}
	
	public void run(final Map<String, PhysicsCollisionObject> target) {
		recv.run(target);
	}
	
	@Override
	public void setSpatial(final Spatial spatial) {
		
	}
	
	@Override
	public Control cloneForSpatial(final Spatial spatial) {
		return(new NotifyReceiveControl(recv));
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
