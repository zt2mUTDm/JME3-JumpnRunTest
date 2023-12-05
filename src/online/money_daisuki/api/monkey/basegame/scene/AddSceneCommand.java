package online.money_daisuki.api.monkey.basegame.scene;

import com.jme3.app.SimpleApplication;
import com.jme3.scene.Spatial;

import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.monkey.console.Command;

public final class AddSceneCommand implements Command {
	@Override
	public void execute(final Spatial caller, final String[] cmd, final Runnable done, final SimpleApplication app) {
		Requires.notNull(caller, "caller == null");
		Requires.notNull(cmd, "cmd == null");
		Requires.lenEqual(cmd, 2);
		Requires.containsNotNull(cmd, "cmd contains null");
		Requires.notNull(done, "done == null");
		Requires.notNull(app, "app == null");
		
		app.getStateManager().getState(SceneLoadAppState.class).load(cmd[1]);
		done.run();
	}
}
