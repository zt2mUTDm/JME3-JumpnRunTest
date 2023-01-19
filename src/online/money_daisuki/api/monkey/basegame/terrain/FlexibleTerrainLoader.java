package online.money_daisuki.api.monkey.basegame.terrain;

import com.jme3.app.Application;
import com.jme3.material.Material;
import com.jme3.terrain.geomipmap.TerrainQuad;

import online.money_daisuki.api.base.Converter;
import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.monkey.basegame.WeakHashMapRefresher;

public final class FlexibleTerrainLoader implements Converter<String, TerrainQuad> {
	private final WeakHashMapRefresher<String, TerrainQuad> cache;
	
	public FlexibleTerrainLoader(final Converter<String, Material> matLoader,
			final Application app) {
		this.cache = new WeakHashMapRefresher<>(new Converter<String, TerrainQuad>() {
			@Override
			public TerrainQuad convert(final String value) {
				return(new TerrainLoader(Requires.notNull(value, "value == null"), matLoader, app).source());
			}
		});
	}
	@Override
	public TerrainQuad convert(final String value) {
		return(cache.get(value));
	}
}
