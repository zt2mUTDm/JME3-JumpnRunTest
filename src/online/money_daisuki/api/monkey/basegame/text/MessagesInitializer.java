package online.money_daisuki.api.monkey.basegame.text;

import online.money_daisuki.api.base.DataSink;
import online.money_daisuki.api.monkey.basegame.ModulesApp;

public final class MessagesInitializer implements DataSink<ModulesApp> {
	@Override
	public void sink(final ModulesApp app) {
		final MessageBoxAppState exe = new MessageBoxAppState();
		app.getStateManager().attach(exe);
	}
}
