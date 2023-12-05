package online.money_daisuki.api.monkey.basegame.character.control;

import com.jme3.math.FastMath;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;

import online.money_daisuki.api.base.BiConverter;
import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.monkey.console.Command;

public final class RotateLinearByCommand implements Command {
	private final BiConverter<? super String, ? super Spatial, ? extends Spatial> spatialTarget;
	
	public RotateLinearByCommand(final BiConverter<? super String, ? super Spatial, ? extends Spatial> spatialTarget) {
		this.spatialTarget = Requires.notNull(spatialTarget, "spatialTarget == null");
	}
	@Override
	public void execute(final Spatial caller, final String[] cmd, final Runnable done) {
		Requires.notNull(caller, "caller == null");
		Requires.containsNotNull(cmd, "cmd contains null");
		Requires.lenEqual(cmd, 6);
		
		final String spatialName = cmd[1];
		final Spatial spatial = spatialTarget.convert(spatialName, caller);
		
		final float speed = Float.parseFloat(cmd[2]);
		final float x = Float.parseFloat(cmd[3]) * FastMath.DEG_TO_RAD;
		final float y = Float.parseFloat(cmd[4]) * FastMath.DEG_TO_RAD;
		final float z = Float.parseFloat(cmd[5]) * FastMath.DEG_TO_RAD;
		
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
