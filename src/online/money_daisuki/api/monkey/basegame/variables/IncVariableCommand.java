package online.money_daisuki.api.monkey.basegame.variables;

import com.jme3.scene.Spatial;

import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.monkey.basegame.ExtendedApplication;
import online.money_daisuki.api.monkey.console.Command;

public final class IncVariableCommand implements Command {
	private final ExtendedApplication app;
	
	public IncVariableCommand(final ExtendedApplication app) {
		this.app = Requires.notNull(app, "app == null");
	}
	@Override
	public void execute(final Spatial caller, final String[] cmd, final Runnable done) {
		Requires.notNull(caller, "caller == null");
		Requires.containsNotNull(cmd, "contains null");
		Requires.lenEqual(cmd, 3);
		
		final String type = cmd[1];
		final String name = cmd[2];
		
		if(app.containsVariable(type, name)) {
			final VariableContainer var = app.getVariable(type, name);
			if(var instanceof IntegerVariableContainer) {
				app.setVariable(type, name, new IntegerVariableContainer(((IntegerVariableContainer)var).asInt() + 1));
			} else {
				throw new IllegalArgumentException("Cannot increment non-integer variable");
			}
		} else {
			app.setVariable(type, name, new IntegerVariableContainer(1));
		}
		done.run();
	}
}
