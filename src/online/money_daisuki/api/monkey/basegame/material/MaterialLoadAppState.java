package online.money_daisuki.api.monkey.basegame.material;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.material.Material;

public final class MaterialLoadAppState extends BaseAppState {
	public Material loadMaterial(final String value) {
		return(getApplication().getAssetManager().loadMaterial(value));
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
