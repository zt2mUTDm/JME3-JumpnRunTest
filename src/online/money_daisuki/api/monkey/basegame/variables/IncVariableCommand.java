package online.money_daisuki.api.monkey.basegame.variables;

import com.jme3.scene.Spatial;

import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.monkey.console.Command;

public final class IncVariableCommand implements Command {
	private final VariablesManager var;
	
	public IncVariableCommand(final VariablesManager var) {
		this.var = Requires.notNull(var, "var == null");
	}
	@Override
	public void execute(final Spatial caller, final String[] cmd, final Runnable done) {
		Requires.notNull(caller, "caller == null");
		Requires.containsNotNull(cmd, "contains null");
		Requires.lenEqual(cmd, 3);
		
		final String type = cmd[1];
		final String name = cmd[2];
		
		if(var.containsVariable(type, name)) {
			final VariableContainer v = var.getVariable(type, name);
			if(v instanceof IntegerVariableContainer) {
				var.setVariable(type, name, new IntegerVariableContainer(((IntegerVariableContainer)v).asInt() + 1));
			} else {
				throw new IllegalArgumentException("Cannot increment non-integer variable");
			}
		} else {
			var.setVariable(type, name, new IntegerVariableContainer(1));
		}
		done.run();
	}
}
