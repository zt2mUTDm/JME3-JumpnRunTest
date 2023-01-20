package online.money_daisuki.api.monkey.basegame.character.control;

import com.jme3.math.FastMath;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;

import online.money_daisuki.api.base.BiConverter;
import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.monkey.console.Command;

public final class RotateLinearByCommand implements Command {
	private final BiConverter<String, Spatial, Spatial> spatialConverter;
	
	public RotateLinearByCommand(final BiConverter<String, Spatial, Spatial> spatialConverter) {
		this.spatialConverter = Requires.notNull(spatialConverter, "spatialConverter == null");
	}
	@Override
	public void execute(final Spatial a, final String[] b, final Runnable done) {
		Requires.notNull(a, "a == null");
		Requires.containsNotNull(b, "contains null");
		Requires.lenEqual(b, 6);
		
		final String spatialName = b[1];
		final Spatial spatial = spatialConverter.convert(spatialName, a);
		
		final float speed = Float.parseFloat(b[2]);
		final float x = Float.parseFloat(b[3]) * FastMath.DEG_TO_RAD;
		final float y = Float.parseFloat(b[4]) * FastMath.DEG_TO_RAD;
		final float z = Float.parseFloat(b[5]) * FastMath.DEG_TO_RAD;
		
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
