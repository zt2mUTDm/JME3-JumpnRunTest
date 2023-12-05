package online.money_daisuki.api.monkey.basegame.cam;

import com.jme3.app.state.AppStateManager;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;

import online.money_daisuki.api.base.Requires;

public final class MoveCameraLinearToCamera implements GameCamera {
	private final AppStateManager stateManager;
	private final Vector3f location;
	private final Quaternion rotation;
	private final float duration;
	
	private OneTimeMoveCameraLinearToAppState state;
	
	public MoveCameraLinearToCamera(final AppStateManager stateManager, final Vector3f location,
			final Quaternion rotation, final float duration) {
		this.stateManager = Requires.notNull(stateManager, "stateManager == null");
		this.location = Requires.notNull(location, "location == null");
		this.rotation = Requires.notNull(rotation, "rotation == null");
		this.duration = Requires.greaterThanZero(duration, "duration == null");
	}
	@Override
	public void acquire() {
		state = new OneTimeMoveCameraLinearToAppState(location, rotation, duration);
		stateManager.attach(state);
	}
	@Override
	public void setEnabled(final boolean b) {
		state.setEnabled(b);
	}
	@Override
	public boolean isEnabled() {
		return(state.isEnabled());
	}
	@Override
	public void dispose() {
		stateManager.detach(state);
		state = null;
	}
	
	public void addListener(final Runnable l) {
		state.addDoneListener(l);
	}
	public void removeListener(final Runnable l) {
		state.removeDoneListener(l);
	}
}
