package online.money_daisuki.api.monkey.basegame.spatial;

import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

import online.money_daisuki.api.base.Converter;
import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.monkey.console.Command;

public final class SetSpatialTranslationCommand  implements Command {
	private final Converter<? super String, ? extends Translatable> source;

	public SetSpatialTranslationCommand(final Converter<? super String, ? extends Translatable> source) {
		this.source = Requires.notNull(source, "source == null");
	}
	@Override
	public void execute(final Spatial a, final String[] b, final Runnable done) {
		Requires.notNull(a, "a == null");
		Requires.containsNotNull(b, "contains null");
		Requires.lenEqual(b, 5);
		
		final Translatable target = source.convert(b[1]);
		if(target != null) {
			target.setTranslation(
					new Vector3f(
							Float.parseFloat(b[2]),
							Float.parseFloat(b[3]),
							Float.parseFloat(b[4])
					)
			);
		}
		done.run();
	}
}
