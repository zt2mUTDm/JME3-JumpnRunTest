package online.money_daisuki.api.monkey.basegame.debug;

import com.jme3.app.SimpleApplication;
import com.jme3.scene.Spatial;

import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.monkey.console.Command;

public final class SetStatsCommand implements Command {
	private final SimpleApplication app;
	
	public SetStatsCommand(final SimpleApplication app) {
		this.app = Requires.notNull(app, "app == null");
	}
	@Override
	public void execute(final Spatial a, final String[] b, final Runnable done) {
		Requires.lenEqual(b, 2);
		
		switch(b[1]) {
			case("on"):
				setCullHint(true);
			break;
			case("off"):
				setCullHint(false);
			break;
			default:
				throw new IllegalArgumentException("Illegal argument: " + b[1]);
		}
		done.run();
	}
	private void setCullHint(final boolean b) {
		app.setDisplayFps(b);
		app.setDisplayStatView(b);
	}
}
