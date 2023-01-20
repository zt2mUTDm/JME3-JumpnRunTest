package online.money_daisuki.api.monkey.basegame.cam;

import com.jme3.input.ChaseCamera;
import com.jme3.scene.Spatial;

import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.base.SetableDataSource;
import online.money_daisuki.api.monkey.console.Command;

public final class SetCameraTransformCommand implements Command {
	private final SetableDataSource<Spatial> player;
	
	public SetCameraTransformCommand(final SetableDataSource<Spatial> player) {
		this.player = Requires.notNull(player, "player == null");
	}
	@Override
	public void execute(final Spatial caller, final String[] cmd, final Runnable done) {
		Requires.notNull(caller, "caller == null");
		Requires.containsNotNull(cmd, "cmd contains null");
		Requires.lenEqual(cmd, 4);
		
		final Spatial spatial = player.source();
		final ChaseCamera cam = spatial.getControl(ChaseCamera.class);
		if(cam != null) {
			final float h = Float.parseFloat(cmd[1]);
			final float v = Float.parseFloat(cmd[2]);
			final float z = Float.parseFloat(cmd[3]);
			
			cam.setDefaultHorizontalRotation(h);
			cam.setDefaultVerticalRotation(v);
			cam.setDefaultDistance(z);
		}
	}
}
