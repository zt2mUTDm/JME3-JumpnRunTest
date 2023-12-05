package online.money_daisuki.api.monkey.basegame.sky;

import com.jme3.scene.Spatial;

import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.base.SetableDataSource;
import online.money_daisuki.api.monkey.console.Command;

public final class RemoveSkyCommand implements Command {
	private final SetableDataSource<? extends Spatial> skyTarget;
	
	public RemoveSkyCommand(final SetableDataSource<? extends Spatial> skyTarget) {
		this.skyTarget = Requires.notNull(skyTarget, "skyTarget == null");
	}
	@Override
	public void execute(final Spatial caller, final String[] cmd, final Runnable done) {
		Requires.notNull(caller, "caller == null");
		Requires.containsNotNull(cmd, "cmd contains null");
		Requires.lenEqual(cmd, 1);
		
		if(skyTarget.isSet()) {
			final Spatial sky = skyTarget.source();
			sky.removeFromParent();
			skyTarget.unset();
		}
		done.run();
	}
}
