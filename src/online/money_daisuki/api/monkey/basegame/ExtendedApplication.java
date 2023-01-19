package online.money_daisuki.api.monkey.basegame;

import com.jme3.app.SimpleApplication;
import com.jme3.scene.Spatial;

import online.money_daisuki.api.monkey.basegame.variables.VariableContainer;

public abstract class ExtendedApplication extends SimpleApplication {
	
	public abstract void executeSimpleScript(final Spatial caller, final String s, final Runnable done);
	
	public abstract void setVariable(String type, String name, final VariableContainer var);
	
	public abstract VariableContainer getVariable(String type, String name);
	
	public abstract boolean containsVariable(String type, String name);
	
	public abstract void clearVariablenType(String type);
	
	public abstract void clearVariables();
	
	public abstract void execute(Spatial spatial, String[] cmd, Runnable done);
	
}
