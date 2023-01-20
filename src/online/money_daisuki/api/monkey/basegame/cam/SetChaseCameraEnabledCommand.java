package online.money_daisuki.api.monkey.basegame.cam;

import com.jme3.input.ChaseCamera;
import com.jme3.scene.Spatial;

import online.money_daisuki.api.base.BiConverter;
import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.monkey.console.Command;

public final class SetChaseCameraEnabledCommand implements Command {
	private final BiConverter<String, Spatial, Spatial> spatialTarget;
	
	public SetChaseCameraEnabledCommand(final BiConverter<String, Spatial, Spatial> spatialTarget) {
		this.spatialTarget = Requires.notNull(spatialTarget, "spatialTarget == null");
	}
	@Override
	public void execute(final Spatial caller, final String[] cmd, final Runnable done) {
		Requires.notNull(caller, "caller == null");
		Requires.containsNotNull(cmd, "cmd contains null");
		Requires.lenEqual(cmd, 3);
		
		final String spatialName = cmd[1];
		final Spatial spatial = spatialTarget.convert(spatialName, caller);
		
		switch(cmd[2]) {
			case("on"):
				set(spatial, true);
			break;
			case("off"):
				set(spatial, false);
			break;
			default:
				throw new IllegalArgumentException("Expect \"on\" or \"off\".");
		}
		done.run();
	}
	private void set(final Spatial spatial, final boolean b) {
		final ChaseCamera cam = spatial.getControl(ChaseCamera.class);
		if(cam == null) {
			throw new IllegalArgumentException("Spatial has no ChaseCamera");
		}
		cam.setEnabled(b);
	}
}
