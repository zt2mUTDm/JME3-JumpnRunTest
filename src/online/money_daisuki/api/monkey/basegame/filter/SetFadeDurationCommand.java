package online.money_daisuki.api.monkey.basegame.filter;

import com.jme3.app.SimpleApplication;
import com.jme3.scene.Spatial;

import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.monkey.console.Command;

public final class SetFadeDurationCommand implements Command {
	private final SimpleApplication app;
	
	public SetFadeDurationCommand(final SimpleApplication app) {
		this.app = Requires.notNull(app, "app == null");
	}
	@Override
	public void execute(final Spatial caller, final String[] cmd, final Runnable done) {
		Requires.notNull(caller, "caller == null");
		Requires.containsNotNull(cmd, "cmd contains null");
		Requires.lenEqual(cmd, 2);
		
		final FadeAppState state = Requires.notNull(app.getStateManager().getState(FadeAppState.class), "No FadeAppState registered");
		state.setDuration(Float.parseFloat(cmd[1]));
		done.run();
	}
}
