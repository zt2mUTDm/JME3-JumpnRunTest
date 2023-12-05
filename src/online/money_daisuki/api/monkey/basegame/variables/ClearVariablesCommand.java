package online.money_daisuki.api.monkey.basegame.variables;

import com.jme3.scene.Spatial;

import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.monkey.console.Command;

public final class ClearVariablesCommand implements Command {
	private final VariablesManager var;
	
	public ClearVariablesCommand(final VariablesManager var) {
		this.var = Requires.notNull(var, "var == null");
	}
	@Override
	public void execute(final Spatial caller, final String[] cmd, final Runnable done) {
		Requires.notNull(caller, "caller == null");
		Requires.containsNotNull(cmd, "contains null");
		Requires.lenEqual(cmd, 1);
		
		var.clearVariables();
		done.run();
	}
}
