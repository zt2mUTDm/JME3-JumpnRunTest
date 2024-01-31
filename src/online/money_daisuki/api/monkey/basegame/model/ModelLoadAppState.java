package online.money_daisuki.api.monkey.basegame.model;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.asset.ModelKey;
import com.jme3.scene.Spatial;

public final class ModelLoadAppState extends BaseAppState {
	public Spatial loadModel(final String name) {
		return(getApplication().getAssetManager().loadModel(new ModelKey(name)));
	}
	
	@Override
	protected void initialize(final Application app) {
		
	}
	@Override
	protected void cleanup(final Application app) {
		
	}
	@Override
	protected void onEnable() {
		
	}
	@Override
	protected void onDisable() {
		
	}
}
