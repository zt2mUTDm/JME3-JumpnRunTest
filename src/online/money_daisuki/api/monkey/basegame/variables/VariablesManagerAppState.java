package online.money_daisuki.api.monkey.basegame.variables;

import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.monkey.basegame.state.AppStateAdapter;

public final class VariablesManagerAppState extends AppStateAdapter implements VariablesManager {
	private final VariablesManager vars;
	
	public VariablesManagerAppState(final VariablesManager vars) {
		this.vars = Requires.notNull(vars, "vars == null");
	}
	@Override
	public void setVariable(final String type, final String name, final VariableContainer var) {
		vars.setVariable(type, name, var);
	}
	@Override
	public VariableContainer getVariable(final String type, final String name) {
		return(vars.getVariable(type, name));
	}
	@Override
	public boolean containsVariable(final String type, final String name) {
		return(vars.containsVariable(type, name));
	}
	@Override
	public void clearVariablenType(final String type) {
		vars.clearVariablenType(type);
	}
	@Override
	public void clearVariables() {
		vars.clearVariables();
	}
}
