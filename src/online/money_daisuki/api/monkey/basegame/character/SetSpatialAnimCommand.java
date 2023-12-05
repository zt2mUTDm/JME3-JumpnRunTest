package online.money_daisuki.api.monkey.basegame.character;

import com.jme3.scene.Spatial;

import online.money_daisuki.api.base.BiConverter;
import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.monkey.basegame.character.anim.AnimControl;
import online.money_daisuki.api.monkey.console.Command;

public final class SetSpatialAnimCommand implements Command {
	private final BiConverter<String, Spatial, Spatial> target;
	
	public SetSpatialAnimCommand(final BiConverter<String, Spatial, Spatial> biConverter) {
		this.target = Requires.notNull(biConverter, "target == null");
	}
	@Override
	public void execute(final Spatial caller, final String[] cmd, final Runnable done) {
		Requires.notNull(caller, "caller == null");
		Requires.containsNotNull(cmd, "contains null");
		Requires.lenEqual(cmd, 4);
		
		final Spatial targetSpatial = target.convert(cmd[1], caller);
		final String animName = cmd[2];
		
		boolean b;
		switch(cmd[3]) {
			case("true"):
				b = true;
			break;
			case("false"):
				b = false;
			break;
			default:
				throw new IllegalArgumentException();
		}
		
		final AnimControl c = targetSpatial.getControl(AnimControl.class);
		if(b) {
			c.play(animName, true);
			done.run();
		} else {
			//c.play(animName, false, done); //TODO
		}
	}
}
