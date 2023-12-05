package online.money_daisuki.api.monkey.basegame.cam;

import com.jme3.app.Application;
import com.jme3.input.ChaseCamera;
import com.jme3.scene.Spatial;

import online.money_daisuki.api.base.BiConverter;
import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.monkey.console.Command;

public final class AddChaseCameraCommand implements Command {
	private final BiConverter<String, Spatial, Spatial> spatialTarget;
	private final Application app;
	
	public AddChaseCameraCommand(final BiConverter<String, Spatial, Spatial> spatialTarget, final Application app) {
		this.spatialTarget = Requires.notNull(spatialTarget, "spatialTarget == null");
		this.app = Requires.notNull(app, "app == null");
	}
	@Override
	public void execute(final Spatial caller, final String[] cmd, final Runnable done) {
		Requires.notNull(caller, "caller == null");
		Requires.containsNotNull(cmd, "cmd contains null");
		Requires.lenEqual(cmd, 2);
		
		final String spatialName = cmd[1];
		final Spatial spatial = spatialTarget.convert(spatialName, caller);
		
		if(spatial.getControl(ChaseCamera.class) != null) {
			throw new IllegalArgumentException("Spatial has already a ChaseCamera attached");
		}
		
		final ChaseCamera cam = new ChaseCamera(app.getCamera(), spatial, app.getInputManager());
		cam.setEnabled(false);
		
		done.run();
	}
}
