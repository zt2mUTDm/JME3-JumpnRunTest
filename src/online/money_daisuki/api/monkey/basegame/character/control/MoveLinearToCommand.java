package online.money_daisuki.api.monkey.basegame.character.control;

import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

import online.money_daisuki.api.base.BiConverter;
import online.money_daisuki.api.monkey.basegame.ExtendedApplication;
import online.money_daisuki.api.monkey.console.Command;

public final class MoveLinearToCommand implements Command {
	private final BiConverter<String, Spatial, Spatial> nodeConverter;
	private final ExtendedApplication app;
	
	public MoveLinearToCommand(final BiConverter<String, Spatial, Spatial> nodeConverter, final ExtendedApplication app) {
		this.nodeConverter = nodeConverter;
		this.app = app;
	}
	@Override
	public void execute(final Spatial a, final String[] b, final Runnable done) {
		// TODO
		final String node = b[1];
		final float speed = Float.parseFloat(b[2]);
		final float x = Float.parseFloat(b[3]);
		final float y = Float.parseFloat(b[4]);
		final float z = Float.parseFloat(b[5]);
		
		final Spatial spatial = nodeConverter.convert(node, a);
		
		spatial.removeControl(MoveInDirectLineControl.class);
		
		final MoveInDirectLineControl control = new MoveInDirectLineControl(new Vector3f(x, y, z), speed, new Runnable() {
			@Override
			public void run() {
				spatial.removeControl(MoveInDirectLineControl.class);
				done.run();
			}
		});
		spatial.addControl(control);
	}
}
