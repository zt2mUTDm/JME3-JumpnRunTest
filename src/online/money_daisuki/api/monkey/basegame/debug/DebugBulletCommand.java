package online.money_daisuki.api.monkey.basegame.debug;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.scene.Spatial;

import online.money_daisuki.api.monkey.console.Command;

public final class DebugBulletCommand implements Command {
	@Override
	public void execute(final Spatial a, final String[] b, final Runnable done, final SimpleApplication app) {
		final BulletAppState bullet = app.getStateManager().getState(BulletAppState.class);
		switch(b[1]) {
			case("on"):
				setDebug(bullet, true);
			break;
			case("off"):
				setDebug(bullet, false);
			break;
			default:
				throw new IllegalArgumentException();
		}
		done.run();
	}
	private void setDebug(final BulletAppState bullet, final boolean b) {
		bullet.setDebugEnabled(b);
	}
}
