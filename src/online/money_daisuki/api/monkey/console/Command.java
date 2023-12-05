package online.money_daisuki.api.monkey.console;

import com.jme3.app.SimpleApplication;
import com.jme3.scene.Spatial;

public interface Command {
	
	// TODO
	
	default void execute(final Spatial caller, final String[] cmd, final Runnable done) {
		
	}
	
	default void execute(final Spatial caller, final String[] cmd, final Runnable done, final SimpleApplication app) {
		
	}
	
}
