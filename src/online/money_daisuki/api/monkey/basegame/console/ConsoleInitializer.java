package online.money_daisuki.api.monkey.basegame.console;

import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;

import online.money_daisuki.api.base.DataSink;
import online.money_daisuki.api.monkey.basegame.ModulesApp;
import online.money_daisuki.api.monkey.basegame.script.CommandExecutorAppState;
import online.money_daisuki.api.monkey.console.CommandExecutor;
import online.money_daisuki.api.monkey.console.CommandStringDataSink;
import online.money_daisuki.api.monkey.console.ConsoleAppState;
import online.money_daisuki.api.monkey.console.ConsoleGui;
import online.money_daisuki.api.monkey.console.ConsoleNodeBuilder;

public final class ConsoleInitializer implements DataSink<ModulesApp> {
	@Override
	public void sink(final ModulesApp app) {
		final CommandExecutor exe = app.getStateManager().getState(CommandExecutorAppState.class);
		
		final ViewPort vp = app.getViewPort();
		final ConsoleNodeBuilder cnb = new ConsoleNodeBuilder(app.getAssetManager(), vp.getCamera().getWidth(), vp.getCamera().getHeight(), app.getGuiNode());
		final ConsoleGui console = cnb.source();
		console.setVisible(false);
		app.getStateManager().attach(new ConsoleAppState(console, new CommandStringDataSink(exe, new Node("ConsoleDummyNode"))));
		console.attach();
	}
}
