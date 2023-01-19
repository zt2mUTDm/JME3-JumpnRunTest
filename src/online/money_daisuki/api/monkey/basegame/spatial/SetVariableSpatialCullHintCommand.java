package online.money_daisuki.api.monkey.basegame.spatial;

import com.jme3.scene.Spatial;
import com.jme3.scene.Spatial.CullHint;

import online.money_daisuki.api.base.Converter;
import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.monkey.console.Command;

public final class SetVariableSpatialCullHintCommand implements Command {
	private final Converter<? super String, ? extends HasSpatial> source;
	
	public SetVariableSpatialCullHintCommand(final Converter<? super String, ? extends HasSpatial> source) {
		this.source = Requires.notNull(source, "source == null");
	}
	@Override
	public void execute(final Spatial a, final String[] b, final Runnable done) {
		Requires.notNull(a, "a == null");
		Requires.containsNotNull(b, "contains null");
		Requires.lenEqual(b, 3);
		
		final String id = b[1];
		
		switch(b[2]) {
			case("always"):
				setCullHint(id, CullHint.Always);
			break;
			case("dynamic"):
				setCullHint(id, CullHint.Dynamic);
			break;
			case("inherit"):
				setCullHint(id, CullHint.Inherit);
			break;
			case("never"):
				setCullHint(id, CullHint.Never);
			break;
			default:
				throw new IllegalStateException("Unexpected CullHint");
		}
		done.run();
	}
	private void setCullHint(final String id, final CullHint hint) {
		source.convert(id).getSpatial().setCullHint(hint);
	}
}
