package online.money_daisuki.api.monkey.basegame.script;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Collection;

import com.jme3.app.Application;
import com.jme3.scene.Spatial;

import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.monkey.console.Command;

public final class ExecCommand implements Command {
	private final CommandExecutorAppState exe;
	
	public ExecCommand(final Application app) {
		this.exe = Requires.notNull(app, "app == null").getStateManager().getState(CommandExecutorAppState.class);
	}
	@Override
	public void execute(final Spatial caller, final String[] cmd, final Runnable done) {
		Requires.notNull(caller, "caller == null");
		Requires.containsNotNull(cmd, "cmd contains null");
		Requires.lenEqual(cmd, 2);
		
		try(final Reader in = new FileReader(cmd[1])) {
			final Collection<String[]> commands = new ScriptFileLoader(in).source();
			if(!commands.isEmpty()) {
				exe.executeSimpleScript(caller, commands, done, false); // TODO
			} else {
				done.run();
			}
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
	}
}
