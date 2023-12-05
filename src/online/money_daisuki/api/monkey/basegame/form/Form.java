package online.money_daisuki.api.monkey.basegame.form;

import online.money_daisuki.api.monkey.basegame.physobj.PhysicsObject;

public interface Form {
	
	long getId();
	
	PhysicsObject getPhysicsObject();
	
	String getScriptModuleName();
	
	String getScriptClassName();
	
}
