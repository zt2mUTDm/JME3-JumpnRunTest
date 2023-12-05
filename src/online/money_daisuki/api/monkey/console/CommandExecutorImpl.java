package online.money_daisuki.api.monkey.console;

import java.util.HashMap;
import java.util.Map;

import com.jme3.app.SimpleApplication;
import com.jme3.scene.Spatial;

import online.money_daisuki.api.base.Requires;

public final class CommandExecutorImpl implements CommandExecutor {
	private final Map<String, Command> executors;
	
	public CommandExecutorImpl() {
		executors = new HashMap<>();
	}
	@Override
	public void addCommand(final String name, final Command command) {
		executors.put(Requires.notNull(name, "name == null"), Requires.notNull(command, "command == null"));
	}
	@Override
	public void removeCommand(final String name) {
		executors.remove(Requires.notNull(name, "name == null"));
	}
	
	@Override
	public void execute(final Spatial a, final String[] b, final Runnable done, final SimpleApplication app) {
		Requires.notNull(a, "a == null");
		Requires.containsNotNull(b, "contains null");
		Requires.sizeGreaterThan(b, 0);
		
		final Command command = executors.get(b[0]);
		if(command == null) {
			throw new IllegalArgumentException("Command not found: " + b[0]);
		}
		command.execute(a, b, done);
		command.execute(a, b, done, app);
	}

}
