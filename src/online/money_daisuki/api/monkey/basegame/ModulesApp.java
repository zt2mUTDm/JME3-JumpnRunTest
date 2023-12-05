package online.money_daisuki.api.monkey.basegame;

import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppState;

import online.money_daisuki.api.base.DataSink;
import online.money_daisuki.api.base.Requires;

public final class ModulesApp extends SimpleApplication {
	private AppState initAppState;
	
	public ModulesApp(final DataSink<? super ModulesApp> callback) {
		initAppState = new InitializeAppModulesState(Requires.notNull(callback, "callback == null"));
	}
	@Override
	public void simpleInitApp() {
		getFlyByCamera().setEnabled(false);
		
		setDisplayFps(false);
		setDisplayStatView(false);
		
		getStateManager().attach(initAppState);
		initAppState = null;
	}
}
