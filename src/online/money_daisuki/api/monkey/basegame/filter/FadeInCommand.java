package online.money_daisuki.api.monkey.basegame.filter;

import com.jme3.app.SimpleApplication;
import com.jme3.scene.Spatial;

import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.monkey.console.Command;

public final class FadeInCommand implements Command {
	@Override
	public void execute(final Spatial caller, final String[] cmd, final Runnable done, final SimpleApplication app) {
		Requires.notNull(caller, "caller == null");
		Requires.notNull(cmd, "cmd == null");
		Requires.containsNotNull(cmd, "cmd contains null");
		Requires.lenEqual(cmd, 1);
		Requires.notNull(app, "app contains null");
		
		final FadeAppState state = Requires.notNull(app.getStateManager().getState(FadeAppState.class), "No FadeAppState registered");
		state.fadeIn();
		done.run();
	}
}
