package online.money_daisuki.api.monkey.basegame.cam;

import com.jme3.input.ChaseCamera;
import com.jme3.scene.Spatial;

import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.base.SetableDataSource;
import online.money_daisuki.api.monkey.console.Command;

public final class PrintCameraTransformCommand implements Command {
	private final SetableDataSource<Spatial> player;
	
	public PrintCameraTransformCommand(final SetableDataSource<Spatial> player) {
		this.player = Requires.notNull(player, "player == null");
	}
	
	@Override
	public void execute(final Spatial caller, final String[] cmd, final Runnable done) {
		Requires.notNull(caller, "caller == null");
		Requires.containsNotNull(cmd, "cmd contains null");
		Requires.lenEqual(cmd, 1);
		
		if(!player.isSet()) {
			return;
		}
		final Spatial spatial = player.source();
		final ChaseCamera cam = spatial.getControl(ChaseCamera.class);
		if(cam != null) {
			System.err.println(
					"H: " + cam.getHorizontalRotation()
					+ ", V: " + cam.getVerticalRotation()
					+ ", Z: " + cam.getDistanceToTarget()
			);
		}
		
		done.run();
	}
}
