package online.money_daisuki.api.monkey.basegame;

import com.jme3.bullet.BulletAppState;

import online.money_daisuki.api.base.DataSink;

public final class PhysicsInitializer implements DataSink<ModulesApp> {
	@Override
	public void sink(final ModulesApp app) {
		app.getStateManager().attach(new BulletAppState());
	}
}
