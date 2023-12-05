package online.money_daisuki.api.monkey.basegame.spatial;

import com.jme3.app.state.AppStateManager;

import online.money_daisuki.api.base.DataSink;
import online.money_daisuki.api.monkey.basegame.ModulesApp;
import online.money_daisuki.api.monkey.basegame.material.MaterialLoadAppState;
import online.money_daisuki.api.monkey.basegame.model.ModelLoadAppState;
import online.money_daisuki.api.monkey.basegame.physobj.PhysicsObjectLoadAppState;

public final class SpatialLoadInitializer implements DataSink<ModulesApp> {
	@Override
	public void sink(final ModulesApp app) {
		final AppStateManager state = app.getStateManager();
		
		state.attach(new MaterialLoadAppState());
		state.attach(new ModelLoadAppState());
		state.attach(new PhysicsObjectLoadAppState());
	}
}
