package online.money_daisuki.api.monkey.basegame.variables;

public interface VariablesManager {
	
	void setVariable(String type, String name, VariableContainer var);
	
	VariableContainer getVariable(String type, String name);
	
	boolean containsVariable(String type, String name);
	
	void clearVariablenType(String type);
	
	void clearVariables();
	
}
