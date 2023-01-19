package online.money_daisuki.api.monkey.basegame.variables;

import com.jme3.scene.Spatial;

import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.monkey.basegame.ExtendedApplication;
import online.money_daisuki.api.monkey.console.Command;

public final class SetVariableCommand implements Command {
	private final ExtendedApplication app;
	
	public SetVariableCommand(final ExtendedApplication app) {
		this.app = Requires.notNull(app, "app == null");
	}
	@Override
	public void execute(final Spatial caller, final String[] cmd, final Runnable done) {
		Requires.notNull(caller, "caller == null");
		Requires.containsNotNull(cmd, "contains null");
		Requires.lenEqual(cmd, 4);
		
		VariableContainer var;
		switch(cmd[3]) {
			case("true"):
			case("false"):
				var = new BooleanVariableContainer(Boolean.valueOf(cmd[3]));
			break;
			default:
				final int i = Integer.parseInt(cmd[3]);
				var = new IntegerVariableContainer(i);
			break;
		}
		
		app.setVariable(cmd[1], cmd[2], var);
		done.run();
	}
}
