package online.money_daisuki.api.monkey.basegame.unsorted;

import online.money_daisuki.api.base.DataSink;
import online.money_daisuki.api.monkey.basegame.ModulesApp;
import online.money_daisuki.api.monkey.basegame.misc.OwnScreenshotAppState;
import online.money_daisuki.api.monkey.basegame.misc.TakeScreenshotCommand;
import online.money_daisuki.api.monkey.basegame.script.CommandExecutorAppState;
import online.money_daisuki.api.monkey.console.CommandExecutor;

public final class ScreenshotConsoleInitializer implements DataSink<ModulesApp> {
	@Override
	public void sink(final ModulesApp app) {
		final CommandExecutor exe = app.getStateManager().getState(CommandExecutorAppState.class);
		
		//exe.addCommand("SetScreenshotDirectory", new SetScreenshotDirectoryCommand(screenshotDirectory));
		exe.addCommand("TakeScreenshot", new TakeScreenshotCommand(app.getStateManager().getState(OwnScreenshotAppState.class)));
	}
}
