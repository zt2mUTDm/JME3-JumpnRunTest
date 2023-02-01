package online.money_daisuki.api.monkey.basegame.spatial;

import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import com.jme3.util.TempVars;

import online.money_daisuki.api.base.BiConverter;
import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.monkey.basegame.character.control.CharControl;
import online.money_daisuki.api.monkey.console.Command;

public final class SetSpatialTranslationCommand  implements Command {
	private final BiConverter<? super String, ? super Spatial, ? extends Spatial> source;
	
	public SetSpatialTranslationCommand(final BiConverter<? super String, ? super Spatial, ? extends Spatial> source) {
		this.source = Requires.notNull(source, "source == null");
	}
	@Override
	public void execute(final Spatial caller, final String[] cmd, final Runnable done) {
		Requires.notNull(caller, "caller == null");
		Requires.containsNotNull(cmd, "contains null");
		Requires.lenEqual(cmd, 5);
		
		final Spatial target = source.convert(cmd[1], caller);
		if(target != null) {
			final TempVars tmp = TempVars.get();
			try {
				final float x = Float.parseFloat(cmd[2]);
				final float y = Float.parseFloat(cmd[3]);
				final float z = Float.parseFloat(cmd[4]);
				
				tmp.vect1.set(x, y, z);
				
				target.setLocalTranslation(tmp.vect1);
				
				final Vector3f worldTrans = target.getWorldTranslation();
				
				final RigidBodyControl rigid = target.getControl(RigidBodyControl.class);
				if(rigid != null) {
					rigid.setPhysicsLocation(worldTrans);
				}
				
				final CharControl cc = target.getControl(CharControl.class);
				if(cc != null) {
					cc.setViewDirection(worldTrans);
				}
			} finally {
				tmp.release();
			}
		}
		done.run();
	}
}
