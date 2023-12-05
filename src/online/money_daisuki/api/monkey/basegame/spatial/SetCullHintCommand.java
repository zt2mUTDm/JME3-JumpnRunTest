package online.money_daisuki.api.monkey.basegame.spatial;

import com.jme3.scene.Spatial;
import com.jme3.scene.Spatial.CullHint;

import online.money_daisuki.api.base.BiConverter;
import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.monkey.console.Command;

public final class SetCullHintCommand implements Command {
	private final BiConverter<? super String, ? super Spatial, ? extends Spatial> spatialTarget;
	
	public SetCullHintCommand(final BiConverter<? super String, ? super Spatial, ? extends Spatial> spatialTarget) {
		this.spatialTarget = Requires.notNull(spatialTarget, "spatialTarget == null");
	}
	@Override
	public void execute(final Spatial caller, final String[] cmd, final Runnable done) {
		Requires.notNull(caller, "caller == null");
		Requires.containsNotNull(cmd, "cmd contains null");
		Requires.lenEqual(cmd, 3);
		
		final String id = cmd[1];
		
		switch(cmd[2]) {
			case("always"):
				setCullHint(caller, id, CullHint.Always);
			break;
			case("dynamic"):
				setCullHint(caller, id, CullHint.Dynamic);
			break;
			case("inherit"):
				setCullHint(caller, id, CullHint.Inherit);
			break;
			case("never"):
				setCullHint(caller, id, CullHint.Never);
			break;
			default:
				throw new IllegalStateException("Unexpected CullHint");
		}
		done.run();
	}
	private void setCullHint(final Spatial caller, final String id, final CullHint hint) {
		spatialTarget.convert(id, caller).setCullHint(hint);
	}
}
