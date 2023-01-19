package online.money_daisuki.api.monkey.basegame.cam;

import com.jme3.app.SimpleApplication;
import com.jme3.scene.Spatial;

import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.monkey.console.Command;

public class FlycamMoveSpeedCommand implements Command {
	private final SimpleApplication app;
	
	public FlycamMoveSpeedCommand(final SimpleApplication app) {
		this.app = Requires.notNull(app, "app == null");
	}
	@Override
	public void execute(final Spatial a, final String[] b, final Runnable done) {
		app.getFlyByCamera().setMoveSpeed(Float.parseFloat(b[1]));
		done.run();
	}
}
