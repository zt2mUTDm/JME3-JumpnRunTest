package online.money_daisuki.api.monkey.basegame.sky;

import com.jme3.app.SimpleApplication;
import com.jme3.scene.Spatial;

import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.base.SetableDataSource;
import online.money_daisuki.api.monkey.console.Command;

public final class RemoveSkyCommand implements Command {
	private final SetableDataSource<Spatial> skyTarget;
	private final SimpleApplication app;
	
	public RemoveSkyCommand(final SetableDataSource<Spatial> skyTarget, final SimpleApplication app) {
		this.skyTarget = Requires.notNull(skyTarget, "skyTarget == null");
		this.app = Requires.notNull(app, "app == null");
	}
	@Override
	public void execute(final Spatial caller, final String[] cmd, final Runnable done) {
		Requires.notNull(caller, "caller == null");
		Requires.containsNotNull(cmd, "cmd contains null");
		Requires.lenEqual(cmd, 1);
		
		if(!skyTarget.isSet()) {
			done.run();
			return;
		}
		
		final Spatial sky = skyTarget.source();
		app.getRootNode().detachChild(sky);
		skyTarget.unset();
		done.run();
	}
}
