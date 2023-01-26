package online.money_daisuki.api.monkey.basegame.script;

import java.util.Collection;

import com.jme3.app.state.AppStateManager;
import com.jme3.scene.Spatial;

import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.monkey.basegame.ExtendedApplication;
import online.money_daisuki.api.monkey.basegame.misc.RemoveDoneAppState;
import online.money_daisuki.api.monkey.console.Command;

public final class ExecCommand implements Command {
	private final AppStateManager state;
	private final ExtendedApplication app;
	private final RemoveDoneAppState removeState;
	
	public ExecCommand(final AppStateManager state, final ExtendedApplication app, final RemoveDoneAppState removeState) {
		this.state = Requires.notNull(state, "state == null");
		this.app = Requires.notNull(app, "app == null");
		this.removeState = Requires.notNull(removeState, "removeState == null");
	}
	@Override
	public void execute(final Spatial a, final String[] b, final Runnable done) {
		Requires.lenEqual(b, 2);
		
		final Collection<String[]> commands = new ScriptFileLoader(b[1]).source();
		if(!commands.isEmpty()) {
			final ExecAppState s = new ExecAppState(new ScriptLineExecutorImpl(commands, a, app), 0.1f, false, done);
			state.attach(s);
			removeState.addAppState(s);
		}
	}
}
