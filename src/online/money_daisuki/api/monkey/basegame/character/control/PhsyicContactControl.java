package online.money_daisuki.api.monkey.basegame.character.control;

import java.io.IOException;

import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.collision.PhysicsCollisionObject;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;

import online.money_daisuki.api.base.Requires;

public final class PhsyicContactControl implements Control {
	private final PhysicsCollisionObject object;
	private final PhysicsCollisionListener listener;
	private final BulletAppState bullet;
	
	public PhsyicContactControl(final PhysicsCollisionObject object, final PhysicsCollisionListener listener,
			final BulletAppState bullet) {
		this.object = Requires.notNull(object, "object == null");
		this.listener = Requires.notNull(listener, "listener == null");
		this.bullet = Requires.notNull(bullet, "bullet == null");
	}
	@Override
	public void update(final float tpf) {
		bullet.getPhysicsSpace().contactTest(object, listener);
	}
	
	@Override
	public Control cloneForSpatial(final Spatial spatial) {
		return(new PhsyicContactControl(object, listener, bullet));
	}
	
	@Override
	public void setSpatial(final Spatial spatial) {
		
	}
	
	@Override
	public void render(final RenderManager rm, final ViewPort vp) {
		
	}
	
	@Override
	public void write(final JmeExporter ex) throws IOException {
		throw new UnsupportedOperationException("Implement when need");
	}
	@Override
	public void read(final JmeImporter im) throws IOException {
		throw new UnsupportedOperationException("Implement when need");
	}
}
