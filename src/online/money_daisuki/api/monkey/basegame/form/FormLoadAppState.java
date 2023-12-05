package online.money_daisuki.api.monkey.basegame.form;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;

import online.money_daisuki.api.io.json.JsonDecoder;
import online.money_daisuki.api.io.json.JsonMap;
import online.money_daisuki.api.monkey.basegame.physobj.PhysicsObject;
import online.money_daisuki.api.monkey.basegame.physobj.PhysicsObjectLoadAppState;

public final class FormLoadAppState extends BaseAppState {
	private Application app;
	
	public Form load(final String name) {
		try(final Reader r = new FileReader(new File(name))) {
			final JsonMap map = new JsonDecoder(r).decode().asMap();
			return(load(map));
		} catch(final IOException e) {
			throw new IllegalArgumentException(e);
		}
	}
	private Form load(final JsonMap map) {
		final String modelUrl = map.get("model").asData().asString();
		final PhysicsObjectLoadAppState pol = app.getStateManager().getState(PhysicsObjectLoadAppState.class);
		final PhysicsObject physicsObject = pol.load(modelUrl);
		
		final long id = map.get("id").asData().asNumber().asBigDecimal().longValueExact();
		final JsonMap scriptMap = map.get("scripts").asMap();
		final String scriptModuleName = scriptMap.get("module").asData().asString();
		final String scriptClassName = scriptMap.get("class").asData().asString();
		
		final Form form = new FormImpl(id, physicsObject, scriptModuleName, scriptClassName);
		return(form);
	}
	
	/*private boolean inViewArea(Object viewer, Object target) {
		final double viewDir = getViewDirection(viewer);
		final double objectDir = getAngelBetween(viewer, target);
		
		double d = viewDir - objectDir;
		if(objectDir < viewDir) {
			d-= Math.PI * 2;
		} else if(d < 0) {
			d = Math.abs(Math.PI * 2);
		}
		return(d <= Math.PI / 2);
	}*/
	
	@Override
	protected void initialize(final Application app) {
		this.app = app;
	}
	@Override
	protected void cleanup(Application app) {
		app = null;
	}
	@Override
	protected void onEnable() {
		
	}
	@Override
	protected void onDisable() {
		
	}
}
