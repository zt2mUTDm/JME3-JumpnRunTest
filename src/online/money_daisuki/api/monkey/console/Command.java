package online.money_daisuki.api.monkey.console;

import com.jme3.scene.Spatial;

public interface Command {
	
	void execute(Spatial a, String[] b, Runnable done);
	
}
