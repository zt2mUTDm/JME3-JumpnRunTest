package online.money_daisuki.api.monkey.basegame.material;

import com.jme3.app.Application;
import com.jme3.material.Material;

import online.money_daisuki.api.base.Converter;
import online.money_daisuki.api.base.Requires;

public final class FlexibleMaterialLoader implements Converter<String, Material> {
	private final Application app;
	
	public FlexibleMaterialLoader(final Application app) {
		this.app = Requires.notNull(app, "app == null");
	}
	@Override
	public Material convert(final String value) {
		return(new MaterialLoader(Requires.notNull(value, "value == null"), app).source());
	}
}
