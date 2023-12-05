package online.money_daisuki.api.monkey.basegame.variables;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import online.money_daisuki.api.base.Requires;

public final class VariablesManagerImpl implements VariablesManager {
	private final Map<String, Map<String, VariableContainer>> variables;
	
	public VariablesManagerImpl() {
		this.variables = new HashMap<>();
	}
	@Override
	public void setVariable(final String type, final String name, final VariableContainer var) {
		Map<String, VariableContainer> submap = variables.get(Requires.notNull(type, "type == null"));
		if(submap == null) {
			submap = new HashMap<>();
			variables.put(type, submap);
		}
		submap.put(Requires.notNull(name, "name == null"), Requires.notNull(var, "var == null"));
	}
	@Override
	public VariableContainer getVariable(final String type, final String name) {
		final Map<String, VariableContainer> submap = variables.get(Requires.notNull(type, "type == null"));
		if(submap == null) {
			throw new IllegalArgumentException("Variable type " + type + " don't exists");
		}
		return(submap.get(Requires.notNull(name, "name == null")));
	}
	@Override
	public boolean containsVariable(final String type, final String name) {
		final Map<String, VariableContainer> submap = variables.get(Requires.notNull(type, "type == null"));
		if(submap == null) {
			return(false);
		}
		return(submap.containsKey(Requires.notNull(name, "name == null")));
	}
	@Override
	public void clearVariablenType(final String type) {
		final Map<String, VariableContainer> submap = variables.remove(Requires.notNull(type, "type == null"));
		if(submap != null) {
			submap.clear();
		}
	}
	@Override
	public void clearVariables() {
		for(final Entry<String, Map<String, VariableContainer>> e:variables.entrySet()) {
			final Map<String, VariableContainer> submap = e.getValue();
			submap.clear();
		}
		variables.clear();
	}

}
