package online.money_daisuki.api.monkey.basegame.cam;

import com.jme3.app.LegacyApplication;
import com.jme3.input.ChaseCamera;
import com.jme3.scene.Spatial;

import online.money_daisuki.api.base.BiConverter;
import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.monkey.console.Command;

public final class RemoveChaseCameraCommand implements Command {
	private final BiConverter<String, Spatial, Spatial> spatialTarget;
	private final LegacyApplication app;
	
	public RemoveChaseCameraCommand(final BiConverter<String, Spatial, Spatial> spatialTarget, final LegacyApplication app) {
		this.spatialTarget = Requires.notNull(spatialTarget, "spatialTarget == null");
		this.app = Requires.notNull(app, "app == null");
	}
	@Override
	public void execute(final Spatial caller, final String[] cmd, final Runnable done) {
		Requires.notNull(caller, "caller == null");
		Requires.containsNotNull(cmd, "cmd contains null");
		Requires.lenEqual(cmd, 1);
		
		final String spatialName = cmd[0];
		final Spatial spatial = spatialTarget.convert(spatialName, caller);
		
		final ChaseCamera cam = spatial.getControl(ChaseCamera.class);
		if(cam == null) {
			throw new IllegalArgumentException("ChaseCamera not found");
		}
		cam.cleanupWithInput(app.getInputManager());
		
		spatial.removeControl(cam);
		
		done.run();
	}
}
