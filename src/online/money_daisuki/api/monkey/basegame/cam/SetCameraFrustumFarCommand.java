package online.money_daisuki.api.monkey.basegame.cam;

import com.jme3.renderer.Camera;
import com.jme3.scene.Spatial;

import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.monkey.console.Command;

public final class SetCameraFrustumFarCommand implements Command {
	private final Camera cam;
	
	public SetCameraFrustumFarCommand(final Camera cam) {
		this.cam = Requires.notNull(cam, "cam == null");
	}
	@Override
	public void execute(final Spatial a, final String[] b, final Runnable done) {
		Requires.lenEqual(b, 2);
		
		cam.setFrustumFar(Float.parseFloat(b[1]));
		done.run();
	}
}
