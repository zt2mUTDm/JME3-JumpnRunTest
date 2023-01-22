package online.money_daisuki.api.monkey.basegame.misc;

import com.jme3.scene.Spatial;

import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.monkey.console.Command;

public class TakeScreenshotCommand implements Command {
	private final OwnScreenshotAppState state;
	
	public TakeScreenshotCommand(final OwnScreenshotAppState state) {
		this.state = Requires.notNull(state, "state == null");
	}
	@Override
	public void execute(final Spatial caller, final String[] cmd, final Runnable done) {
		Requires.notNull(caller, "caller == null");
		Requires.containsNotNull(cmd, "cmd contains null");
		Requires.lenEqual(cmd, 1);
		
		state.capture();
		done.run();
	}
}
