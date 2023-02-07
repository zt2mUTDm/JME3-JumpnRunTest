package online.money_daisuki.api.monkey.basegame.spatial;

import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

import online.money_daisuki.api.base.BiConverter;
import online.money_daisuki.api.base.Requires;
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
		if(target == null) {
			throw new IllegalArgumentException("Spatial not found");
		}
		
		final TranslateControl c = target.getControl(TranslateControl.class);
		if(c == null) {
			throw new IllegalStateException("Spatial has no TranslateControl :(");
		}
		
		final float x = Float.parseFloat(cmd[2]);
		final float y = Float.parseFloat(cmd[3]);
		final float z = Float.parseFloat(cmd[4]);
		
		c.relocate(new Vector3f(x, y, z), true);
		
		done.run();
	}
}
