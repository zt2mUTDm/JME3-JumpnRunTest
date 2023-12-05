package online.money_daisuki.api.monkey.basegame.character.control;

import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

import online.money_daisuki.api.base.BiConverter;
import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.monkey.console.Command;

public final class MoveLinearToCommand implements Command {
	private final BiConverter<? super String, ? super Spatial, ? extends Spatial> target;
	
	public MoveLinearToCommand(final BiConverter<? super String, ? super Spatial, ? extends Spatial> target) {
		this.target = Requires.notNull(target, "target == null");
	}
	@Override
	public void execute(final Spatial caller, final String[] cmd, final Runnable done) {
		Requires.notNull(caller, "caller == null");
		Requires.containsNotNull(cmd, "cmd contains null");
		Requires.lenEqual(cmd, 6);
		
		final String spatialName = cmd[1];
		final float speed = Float.parseFloat(cmd[2]);
		final float x = Float.parseFloat(cmd[3]);
		final float y = Float.parseFloat(cmd[4]);
		final float z = Float.parseFloat(cmd[5]);
		
		final Spatial spatial = target.convert(spatialName, caller);
		
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
