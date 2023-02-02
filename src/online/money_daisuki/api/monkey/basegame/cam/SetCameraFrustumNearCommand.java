package online.money_daisuki.api.monkey.basegame.cam;

import com.jme3.renderer.Camera;
import com.jme3.scene.Spatial;

import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.monkey.console.Command;

public final class SetCameraFrustumNearCommand implements Command {
	private final Camera cam;
	
	public SetCameraFrustumNearCommand(final Camera cam) {
		this.cam = Requires.notNull(cam, "cam == null");
	}
	@Override
	public void execute(final Spatial caller, final String[] cmd, final Runnable done) {
		Requires.notNull(caller, "caller == null");
		Requires.containsNotNull(cmd, "cmd contains null");
		Requires.lenEqual(cmd, 2);
		
		cam.setFrustumNear(Float.parseFloat(cmd[1]));
		done.run();
	}
}
