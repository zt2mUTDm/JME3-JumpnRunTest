package online.money_daisuki.api.monkey.basegame.physobj;

import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;
import com.jme3.util.SafeArrayList;

import online.money_daisuki.api.base.Requires;

public final class MoveKinematicControl extends AbstractControl {
	private final Vector3f targetLocation;
	private final float speedMod;
	
	private final Vector3f curLocation;
	
	private float counter;
	private Vector3f startLocation;
	
	private SafeArrayList<Runnable> listeners;
	
	public MoveKinematicControl(final Vector3f target, final float speed) {
		this.targetLocation = new Vector3f(Requires.notNull(target, "target == null"));
		this.speedMod = (1.0f / Requires.positive(speed, "speed < 0"));
		
		this.curLocation = new Vector3f();
	}
	@Override
	protected void controlUpdate(final float tpf) {
		if(startLocation == null) {
			this.startLocation = new Vector3f(getSpatial().getLocalTranslation());
		}
		counter+= (tpf * speedMod);
		
		if(counter >= 1.0f) {
			getSpatial().setLocalTranslation(targetLocation);
			
			if(listeners != null) {
				for(final Runnable l:listeners.getArray()) {
					l.run();
				}
			}
		} else {
			curLocation.interpolateLocal(startLocation, targetLocation, counter);
			getSpatial().setLocalTranslation(curLocation);
		}
	}
	
	public void addListener(final Runnable listener) {
		assert listener != null : "listener == null";
		if(listeners == null) {
			listeners = new SafeArrayList<>(Runnable.class);
		}
		listeners.add(listener);
	}
	public void removeListener(final Runnable listener) {
		assert listener != null : "listener == null";
		
		if(listeners.remove(listener) && listeners.isEmpty()) {
			listeners = null;
		}
	}
	
	@Override
	protected void controlRender(final RenderManager rm, final ViewPort vp) {
		
	}
}
