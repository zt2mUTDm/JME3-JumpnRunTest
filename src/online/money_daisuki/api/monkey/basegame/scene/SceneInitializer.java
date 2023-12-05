package online.money_daisuki.api.monkey.basegame.scene;

import com.jme3.app.state.AppStateManager;

import online.money_daisuki.api.base.DataSink;
import online.money_daisuki.api.monkey.basegame.ModulesApp;
import online.money_daisuki.api.monkey.basegame.SceneGraphAppState;
import online.money_daisuki.api.monkey.basegame.script.CommandExecutorAppState;

public final class SceneInitializer implements DataSink<ModulesApp> {
	@Override
	public void sink(final ModulesApp app) {
		final AppStateManager state = app.getStateManager();
		
		state.attach(new SceneGraphAppState());
		state.attach(new SceneLoadAppState());
		
		final CommandExecutorAppState exec = state.getState(CommandExecutorAppState.class);
		exec.addCommand("AddScene", new AddSceneCommand());
	}
}
