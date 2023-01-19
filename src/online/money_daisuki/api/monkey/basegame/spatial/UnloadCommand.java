package online.money_daisuki.api.monkey.basegame.spatial;

import com.jme3.bullet.BulletAppState;
import com.jme3.scene.Spatial;

import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.monkey.console.Command;

public final class UnloadCommand implements Command {
	private final BulletAppState bullet;
	
	public UnloadCommand(final BulletAppState bullet) {
		this.bullet = Requires.notNull(bullet, "bullet == null");
	}
	@Override
	public void execute(final Spatial caller, final String[] cmd, final Runnable done) {
		Requires.notNull(caller, "caller == null");
		Requires.containsNotNull(cmd, "contains null");
		Requires.lenEqual(cmd, 1);
		
		bullet.getPhysicsSpace().removeAll(caller);
		caller.removeFromParent();
		
		done.run();
	}
}
