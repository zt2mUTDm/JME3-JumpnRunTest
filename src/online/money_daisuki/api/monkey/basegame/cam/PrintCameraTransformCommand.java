package online.money_daisuki.api.monkey.basegame.cam;

import java.io.PrintStream;

import com.jme3.input.ChaseCamera;
import com.jme3.scene.Spatial;

import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.base.SetableDataSource;
import online.money_daisuki.api.monkey.console.Command;

public final class PrintCameraTransformCommand implements Command {
	private final SetableDataSource<Spatial> player;
	private final PrintStream out;
	
	public PrintCameraTransformCommand(final SetableDataSource<Spatial> player,
			final PrintStream out) {
		this.player = Requires.notNull(player, "player == null");
		this.out = Requires.notNull(out, "out == null");
	}
	@Override
	public void execute(final Spatial caller, final String[] cmd, final Runnable done) {
		Requires.notNull(caller, "caller == null");
		Requires.containsNotNull(cmd, "cmd contains null");
		Requires.lenEqual(cmd, 1);
		
		// TODO Use any spatial
		if(!player.isSet()) {
			return;
		}
		
		final Spatial spatial = player.source();
		final ChaseCamera cam = spatial.getControl(ChaseCamera.class);
		if(cam != null) {
			out.println(
					"(" + cam.getHorizontalRotation() + ", "
					+ cam.getVerticalRotation() + ", "
					+ cam.getDistanceToTarget()
					+ ")"
			);
		}
		
		done.run();
	}
}
