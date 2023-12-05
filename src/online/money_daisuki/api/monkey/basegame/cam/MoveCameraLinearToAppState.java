package online.money_daisuki.api.monkey.basegame.cam;

import java.util.Collection;
import java.util.LinkedList;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;

import online.money_daisuki.api.base.Requires;

public final class MoveCameraLinearToAppState extends BaseAppState implements GameCamera {
	private Vector3f targetVector;
	private Quaternion targetRotation;
	private float durationMod;
	
	private Vector3f startVector;
	private Quaternion startRotation;
	
	private Vector3f interpolateVector;
	private Quaternion interpolateRotation;
	
	private float value;
	
	private boolean active;
	
	private Collection<Runnable> doneListeners;
	
	@Override
	public void update(final float tpf) {
		if(active) {
			value+= (durationMod * tpf);
			
			if(value >= 1 || (interpolateVector.equals(targetVector) && interpolateRotation.equals(targetRotation))) {
				move(1.0f);
				handleDone();
			} else {
				move(value);
			}
		}
	}
	private void move(final float f) {
		interpolateVector.interpolateLocal(startVector, targetVector, f);
		interpolateRotation.slerp(startRotation, targetRotation, f);
		
		final Camera cam = getApplication().getCamera();
		cam.setLocation(interpolateVector);
		cam.setRotation(interpolateRotation);
	}
	private void handleDone() {
		stop();
		fireDoneListeners();
	}
	
	public void stop() {
		if(!active) {
			return;
		}
		
		active = false;
		
		interpolateVector = null;
		interpolateRotation = null;
		
		startVector = null;
		startRotation = null;
		
		targetVector = null;
		targetRotation = null;
	}
	public void start(final Vector3f targetVector, final Quaternion targetRotation, final float duration) {
		this.targetVector = new Vector3f(targetVector);
		this.targetRotation = new Quaternion(targetRotation);
		this.durationMod = (1.0f / Requires.greaterThanZero(duration, "duration <= 0"));
		
		value = 0.0f;
		
		final Camera cam = getApplication().getCamera();
		startVector = new Vector3f(cam.getLocation());
		startRotation = new Quaternion(cam.getRotation());
		
		interpolateVector = new Vector3f(startVector);
		interpolateRotation = new Quaternion(startRotation);
		
		active = true;
	}
	
	public void addDoneListener(final Runnable l) {
		Requires.notNull(l, "l == null");
		if(doneListeners == null) {
			doneListeners = new LinkedList<>();
		}
		doneListeners.add(l);
	}
	public boolean removeDoneListener(final Runnable l) {
		if(doneListeners == null) {
			return(false);
		}
		final boolean b = doneListeners.remove(l);
		if(b && doneListeners.isEmpty()) {
			doneListeners = null;
		}
		return(b);
	}
	private void fireDoneListeners() {
		if(doneListeners != null) {
			for(final Runnable l:doneListeners) {
				l.run();
			}
		}
	}
	
	@Override
	protected void initialize(final Application app) {
		
	}
	
	@Override
	protected void cleanup(final Application app) {
		stop();
		
		if(doneListeners != null) {
			doneListeners.clear();
			doneListeners = null;
		}
	}
	
	@Override
	protected void onEnable() {
		
	}
	
	@Override
	protected void onDisable() {
		
	}
	
	@Override
	public void acquire() {
		
	}
	@Override
	public void dispose() {
		getApplication().getStateManager().detach(this);
	}
}
