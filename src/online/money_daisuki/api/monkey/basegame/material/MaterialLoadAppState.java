package online.money_daisuki.api.monkey.basegame.material;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.material.Material;

import online.money_daisuki.api.base.Requires;

public final class MaterialLoadAppState extends BaseAppState {
	private Application app;
	
	public Material loadMaterial(final String value) {
		return(new MaterialLoader(Requires.notNull(value, "value == null"), app).source());
	}
	@Override
	protected void initialize(final Application app) {
		this.app = app;
	}
	@Override
	protected void cleanup(final Application app) {
		this.app = null;
	}
	@Override
	protected void onEnable() {
		
	}
	@Override
	protected void onDisable() {
		
	}
}
