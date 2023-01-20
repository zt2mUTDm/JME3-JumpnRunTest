package online.money_daisuki.api.monkey.basegame.text;

import com.jme3.scene.Spatial;

import online.money_daisuki.api.base.DataSink;
import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.monkey.console.Command;

public final class SetLanguageKeyCommand implements Command {
	private final DataSink<String> target;
	
	public SetLanguageKeyCommand(final DataSink<String> target) {
		this.target = Requires.notNull(target, "target == null");
	}
	@Override
	public void execute(final Spatial caller, final String[] cmd, final Runnable done) {
		Requires.notNull(caller, "caller == null");
		Requires.containsNotNull(cmd, "cmd contains null");
		Requires.lenEqual(cmd, 2);
		
		target.sink(cmd[1]);
		done.run();
	}
}
