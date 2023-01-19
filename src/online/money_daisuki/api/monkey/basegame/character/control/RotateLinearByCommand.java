package online.money_daisuki.api.monkey.basegame.character.control;

import com.jme3.math.FastMath;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;

import online.money_daisuki.api.base.BiConverter;
import online.money_daisuki.api.monkey.basegame.ExtendedApplication;
import online.money_daisuki.api.monkey.console.Command;

public final class RotateLinearByCommand implements Command {
	private final BiConverter<String, Spatial, Spatial> nodeConverter;
	private final ExtendedApplication app;
	
	public RotateLinearByCommand(final BiConverter<String, Spatial, Spatial> nodeConverter, final ExtendedApplication app) {
		this.nodeConverter = nodeConverter;
		this.app = app;
	}
	@Override
	public void execute(final Spatial a, final String[] b, final Runnable done) {
		// TODO
		final String node = b[1];
		final float speed = Float.parseFloat(b[2]);
		final float x = Float.parseFloat(b[3]) * FastMath.DEG_TO_RAD;
		final float y = Float.parseFloat(b[4]) * FastMath.DEG_TO_RAD;
		final float z = Float.parseFloat(b[5]) * FastMath.DEG_TO_RAD;
		
		final Spatial spatial = nodeConverter.convert(node, a);
		
		spatial.removeControl(RotateLinearControl.class);
		
		final Control control = new RotateLinearControl(new float[] { x, y, z }, speed, new Runnable() {
			@Override
			public void run() {
				spatial.removeControl(RotateLinearControl.class);
				done.run();
			}
		});
		spatial.addControl(control);
	}
}
