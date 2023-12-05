package online.money_daisuki.api.monkey.basegame.debug;

import com.jme3.app.SimpleApplication;
import com.jme3.scene.Spatial;

import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.monkey.console.Command;

public final class SetStatsCommand implements Command {
	@Override
	public void execute(final Spatial a, final String[] b, final Runnable done, final SimpleApplication app) {
		Requires.lenEqual(b, 2);
		
		switch(b[1]) {
			case("on"):
				setCullHint(app, true);
			break;
			case("off"):
				setCullHint(app, false);
			break;
			default:
				throw new IllegalArgumentException("Illegal argument: " + b[1]);
		}
		done.run();
	}
	private void setCullHint(final SimpleApplication app, final boolean b) {
		app.setDisplayFps(b);
		app.setDisplayStatView(b);
	}
}
