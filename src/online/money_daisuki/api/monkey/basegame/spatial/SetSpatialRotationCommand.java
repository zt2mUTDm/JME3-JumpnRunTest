package online.money_daisuki.api.monkey.basegame.spatial;

import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.math.FastMath;
import com.jme3.scene.Spatial;
import com.jme3.util.TempVars;

import online.money_daisuki.api.base.BiConverter;
import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.monkey.basegame.character.control.CharControl;
import online.money_daisuki.api.monkey.console.Command;

public final class SetSpatialRotationCommand  implements Command {
	private final BiConverter<? super String, ? super Spatial, ? extends Spatial> source;

	public SetSpatialRotationCommand(final BiConverter<? super String, ? super Spatial, ? extends Spatial> source) {
		this.source = Requires.notNull(source, "source == null");
	}
	@Override
	public void execute(final Spatial a, final String[] b, final Runnable done) {
		Requires.notNull(a, "a == null");
		Requires.containsNotNull(b, "contains null");
		Requires.lenEqual(b, 5);
		
		final Spatial target = source.convert(b[1], a);
		if(target != null) {
			final TempVars tmp = TempVars.get();
			try {
				final float x = Float.parseFloat(b[2]) * FastMath.DEG_TO_RAD;
				final float y = Float.parseFloat(b[3]) * FastMath.DEG_TO_RAD;
				final float z = Float.parseFloat(b[4]) * FastMath.DEG_TO_RAD;
				
				tmp.quat1.fromAngles(new float[] {
						x, y, z
				});
				
				target.setLocalRotation(tmp.quat1);
				
				final RigidBodyControl rigid = target.getControl(RigidBodyControl.class);
				if(rigid != null) {
					rigid.setPhysicsRotation(tmp.quat1);
				}
				
				tmp.vect1.set(0, 0, 1);
				
				final CharControl cc = target.getControl(CharControl.class);
				if(cc != null) {
					cc.setViewDirection(tmp.quat1.mult(tmp.vect1));
				}
			} finally {
				tmp.release();
			}
		}
		done.run();
	}
}
