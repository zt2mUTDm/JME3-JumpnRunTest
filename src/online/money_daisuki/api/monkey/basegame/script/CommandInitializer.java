package online.money_daisuki.api.monkey.basegame.script;

import online.money_daisuki.api.base.DataSink;
import online.money_daisuki.api.monkey.basegame.ModulesApp;
import online.money_daisuki.api.monkey.console.CommandExecutorImpl;

public final class CommandInitializer implements DataSink<ModulesApp> {
	@Override
	public void sink(final ModulesApp app) {
		final CommandExecutorAppState exe = new CommandExecutorAppState(new CommandExecutorImpl(), app.getRootNode()); // TODO change parent
		app.getStateManager().attach(exe);
	}
}
