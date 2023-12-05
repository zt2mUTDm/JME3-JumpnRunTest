package online.money_daisuki.api.monkey.basegame.form;

import com.jme3.app.state.AppStateManager;

import online.money_daisuki.api.base.DataSink;
import online.money_daisuki.api.monkey.basegame.ModulesApp;

public final class FormInitializer implements DataSink<ModulesApp> {
	@Override
	public void sink(final ModulesApp app) {
		final AppStateManager state = app.getStateManager();
		state.attach(new FormLoadAppState());
	}
}
