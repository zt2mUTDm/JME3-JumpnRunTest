package online.money_daisuki.api.monkey.basegame.debug;

import com.jme3.bullet.BulletAppState;
import com.jme3.scene.Spatial;

import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.monkey.console.Command;

public final class DebugBulletCommand implements Command {
	private final BulletAppState bullet;
	
	public DebugBulletCommand(final BulletAppState bullet) {
		this.bullet = Requires.notNull(bullet, "bullet == null");
	}
	@Override
	public void execute(final Spatial a, final String[] b, final Runnable done) {
		switch(b[1]) {
			case("on"):
				setDebug(true);
			break;
			case("off"):
				setDebug(false);
			break;
			default:
				throw new IllegalArgumentException( );
		}
		done.run();
	}
	private void setDebug(final boolean b) {
		bullet.setDebugEnabled(b);
	}
}
