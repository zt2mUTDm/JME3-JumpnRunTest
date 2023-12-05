package online.money_daisuki.api.monkey.basegame.cam;

import java.io.PrintStream;

import com.jme3.app.SimpleApplication;
import com.jme3.renderer.Camera;
import com.jme3.scene.Spatial;

import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.monkey.console.Command;

public final class PrintCameraTransformCommand implements Command {
	private final PrintStream out;
	
	public PrintCameraTransformCommand(final PrintStream out) {
		this.out = Requires.notNull(out, "out == null");
	}
	@Override
	public void execute(final Spatial caller, final String[] cmd, final Runnable done, final SimpleApplication app) {
		Requires.notNull(caller, "caller == null");
		Requires.containsNotNull(cmd, "cmd contains null");
		Requires.lenEqual(cmd, 1);
		
		final Camera cam = app.getCamera();
		out.println(
				"(" + cam.getLocation() + ", "
				+ cam.getRotation()
				+ ")"
		);
		
		done.run();
	}
}
