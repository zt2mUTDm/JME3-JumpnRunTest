package online.money_daisuki.api.monkey.basegame.test;

import com.jme3.app.SimpleApplication;
import com.jme3.scene.Spatial;

import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.monkey.basegame.py.PythonAppState;
import online.money_daisuki.api.monkey.console.Command;

public final class ExecScriptCommand implements Command {
	@Override
	public void execute(final Spatial caller, final String[] cmd, final Runnable done, final SimpleApplication app) {
		Requires.notNull(caller, "caller == null");
		Requires.notNull(cmd, "cmd == null");
		Requires.lenEqual(cmd, 3);
		Requires.containsNotNull(cmd, "cmd contains null");
		Requires.notNull(done, "done == null");
		Requires.notNull(app, "app == null");
		
		final String module = cmd[1];
		final String name = cmd[2];
		
		final PythonAppState py = app.getStateManager().getState(PythonAppState.class);
		py.addSingleScript(module, name);
		
		done.run();
	}
}
