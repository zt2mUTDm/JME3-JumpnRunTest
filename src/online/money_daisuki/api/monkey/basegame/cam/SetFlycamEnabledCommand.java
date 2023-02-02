package online.money_daisuki.api.monkey.basegame.cam;

import com.jme3.input.FlyByCamera;
import com.jme3.scene.Spatial;

import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.monkey.console.Command;

public final class SetFlycamEnabledCommand implements Command {
	private final FlyByCamera cam;
	
	public SetFlycamEnabledCommand(final FlyByCamera cam) {
		this.cam = Requires.notNull(cam, "cam == null");
	}
	@Override
	public void execute(final Spatial caller, final String[] cmd, final Runnable done) {
		Requires.notNull(caller, "caller == null");
		Requires.containsNotNull(cmd, "cmd contains null");
		Requires.lenEqual(cmd, 2);
		
		final boolean b = Boolean.parseBoolean(cmd[1]);
		cam.setEnabled(b);
		done.run();
	}
}
