package online.money_daisuki.api.monkey.basegame.spatial;

import com.jme3.app.Application;
import com.jme3.bullet.BulletAppState;
import com.jme3.scene.Spatial;

import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.monkey.console.Command;

public final class UnloadCommand implements Command {
	private final Application app;
	
	public UnloadCommand(final Application app) {
		this.app = Requires.notNull(app, "app == null");
	}
	@Override
	public void execute(final Spatial caller, final String[] cmd, final Runnable done) {
		Requires.notNull(caller, "caller == null");
		Requires.containsNotNull(cmd, "contains null");
		Requires.lenEqual(cmd, 1);
		
		final BulletAppState bullet = app.getStateManager().getState(BulletAppState.class);
		bullet.getPhysicsSpace().removeAll(caller);
		caller.removeFromParent();
		
		done.run();
	}
}
