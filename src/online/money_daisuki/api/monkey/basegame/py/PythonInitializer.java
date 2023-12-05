package online.money_daisuki.api.monkey.basegame.py;

import java.io.File;

import com.jme3.app.state.AppStateManager;

import online.money_daisuki.api.base.DataSink;
import online.money_daisuki.api.monkey.basegame.ModulesApp;
import online.money_daisuki.api.monkey.basegame.form.FormDataStoreState;
import online.money_daisuki.api.monkey.basegame.physobj.PythonCollisionState;
import online.money_daisuki.api.monkey.basegame.physobj.SetCollisionEnabledCommand;
import online.money_daisuki.api.monkey.basegame.script.CommandExecutorAppState;
import online.money_daisuki.api.monkey.basegame.test.ExecScriptCommand;

public final class PythonInitializer implements DataSink<ModulesApp> {
	@Override
	public void sink(final ModulesApp app) {
		final AppStateManager state = app.getStateManager();
		
		final PythonAppState pyState = new PythonAppState(new File("Scripts/"));
		state.attach(pyState);
		
		state.attach(new FormDataStoreState());
		
		final PythonCollisionState pyColState = new PythonCollisionState();
		state.attach(pyColState);
		
		final CommandExecutorAppState console = state.getState(CommandExecutorAppState.class);
		console.addCommand("SetCollisionEnabled", new SetCollisionEnabledCommand());
		console.addCommand("ExecScript", new ExecScriptCommand());
	}
}