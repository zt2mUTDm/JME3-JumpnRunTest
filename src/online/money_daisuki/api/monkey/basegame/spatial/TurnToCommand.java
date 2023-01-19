package online.money_daisuki.api.monkey.basegame.spatial;

import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

import online.money_daisuki.api.base.BiConverter;
import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.monkey.basegame.character.control.CharControl;
import online.money_daisuki.api.monkey.console.Command;

public final class TurnToCommand implements Command {
	private final BiConverter<String, Spatial, Spatial> target;
	
	public TurnToCommand(final BiConverter<String, Spatial, Spatial> biConverter) {
		this.target = Requires.notNull(biConverter, "target == null");
	}
	@Override
	public void execute(final Spatial a, final String[] b, final Runnable done) {
		Requires.notNull(a, "a == null");
		Requires.containsNotNull(b, "contains null");
		Requires.lenEqual(b, 3);
		
		final Spatial targetSpatial = target.convert(b[1], a);
		
		final Vector3f targetVector = new Vector3f(targetSpatial.getWorldTranslation());
		targetVector.subtractLocal(a.getWorldTranslation());
		
		if(b[2].equals("true")) {
			targetVector.multLocal(1, 0, 1);
		}
		
		final CharControl cc = a.getControl(CharControl.class);
		if(cc != null) {
			cc.setViewDirection(targetVector);
		}
		done.run();
	}
}
