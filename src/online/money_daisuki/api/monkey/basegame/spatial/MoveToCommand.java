package online.money_daisuki.api.monkey.basegame.spatial;

import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

import online.money_daisuki.api.base.BiConverter;
import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.monkey.basegame.character.control.CharControl;
import online.money_daisuki.api.monkey.console.Command;

public final class MoveToCommand implements Command {
	private final BiConverter<? super String, ? super Spatial, ? extends Spatial> target;
	
	public MoveToCommand(final BiConverter<? super String, ? super Spatial, ? extends Spatial> target) {
		this.target = Requires.notNull(target, "target == null");
	}
	@Override
	public void execute(final Spatial caller, final String[] cmd, final Runnable done) {
		Requires.notNull(caller, "caller == null");
		Requires.containsNotNull(cmd, "cmd contains null");
		Requires.lenEqual(cmd, 4);
		
		final Spatial targetSpatial = target.convert(cmd[1], caller);
		final float speed = Float.parseFloat(cmd[2]);
		
		final Vector3f targetVector = new Vector3f(targetSpatial.getWorldTranslation());
		targetVector.subtractLocal(caller.getWorldTranslation());
		
		if(cmd[3].equals("true")) {
			targetVector.multLocal(1, 0, 1);
		}
		targetVector.normalizeLocal();
		targetVector.multLocal(speed);
		
		final CharControl cc = caller.getControl(CharControl.class);
		if(cc != null) {
			cc.setMoveVector(targetVector);
		}
		done.run();
	}
}
