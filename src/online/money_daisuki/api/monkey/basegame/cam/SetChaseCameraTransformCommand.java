package online.money_daisuki.api.monkey.basegame.cam;

import com.jme3.input.ChaseCamera;
import com.jme3.scene.Spatial;

import online.money_daisuki.api.base.BiConverter;
import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.monkey.console.Command;

public final class SetChaseCameraTransformCommand implements Command {
	private final BiConverter<String, Spatial, Spatial> spatialTarget;
	
	public SetChaseCameraTransformCommand(final BiConverter<String, Spatial, Spatial> spatialTarget) {
		this.spatialTarget = Requires.notNull(spatialTarget, "spatialTarget == null");
	}
	@Override
	public void execute(final Spatial caller, final String[] cmd, final Runnable done) {
		Requires.notNull(caller, "caller == null");
		Requires.containsNotNull(cmd, "cmd contains null");
		Requires.lenEqual(cmd, 5);
		
		final String spatialName = cmd[1];
		final Spatial spatial = spatialTarget.convert(spatialName, caller);
		
		final float h = Float.parseFloat(cmd[2]);
		final float v = Float.parseFloat(cmd[3]);
		final float z = Float.parseFloat(cmd[4]);
		
		final ChaseCamera cam = spatial.getControl(ChaseCamera.class);
		if(cam == null) {
			throw new IllegalArgumentException("Spatial has no ChaseCamera");
		}
		
		cam.setDefaultHorizontalRotation(h);
		cam.setDefaultVerticalRotation(v);
		cam.setDefaultDistance(z);
		
		done.run();
	}
}
