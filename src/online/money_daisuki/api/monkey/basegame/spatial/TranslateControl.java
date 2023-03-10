package online.money_daisuki.api.monkey.basegame.spatial;

import java.io.IOException;

import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.objects.PhysicsCharacter;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;
import com.jme3.util.TempVars;

import online.money_daisuki.api.monkey.basegame.character.control.CharControl;

public final class TranslateControl implements Control {
	private Spatial spatial;
	private Vector3f newLocalTranslation;
	
	public TranslateControl() {
		super();
	}
	@Override
	public void update(final float tpf) {
		if(newLocalTranslation != null) {
			doRelocate();
		}
	}
	private void doRelocate() {
		final TempVars tmp = TempVars.get();
		try {
			final Vector3f translation = spatial.getLocalTranslation();
			newLocalTranslation.subtractLocal(translation);
			
			spatial.setLocalTranslation(newLocalTranslation);
			
			final RigidBodyControl rigid = spatial.getControl(RigidBodyControl.class);
			if(rigid != null) {
				rigid.getPhysicsLocation(tmp.vect1);
				tmp.vect1.addLocal(newLocalTranslation);
				rigid.setPhysicsLocation(tmp.vect1);
			}
			
			final CharControl cc = spatial.getControl(CharControl.class);
			if(cc != null) {
				final PhysicsCharacter c = cc.getCharacter();
				c.getPhysicsLocation(tmp.vect1);
				tmp.vect1.addLocal(newLocalTranslation);
				c.setPhysicsLocation(tmp.vect1);
			}
		} finally {
			tmp.release();
		}
		newLocalTranslation = null;
	}
	
	public void relocate(final Vector3f vect, final boolean now) {
		scheduleRelocate(vect);
		if(now) {
			doRelocate();
		}
	}
	private void scheduleRelocate(final Vector3f vect) {
		this.newLocalTranslation = new Vector3f(vect);
	}
	
	@Override
	public final Control cloneForSpatial(final Spatial spatial) {
		return(new TranslateControl());
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
		throw new UnsupportedOperationException("Auto-generated method stub");
	}
	@Override
	public void write(final JmeExporter ex) throws IOException {
		throw new UnsupportedOperationException("Auto-generated method stub");
	}
}
