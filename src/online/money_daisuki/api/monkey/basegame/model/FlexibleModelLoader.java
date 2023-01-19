package online.money_daisuki.api.monkey.basegame.model;

import com.jme3.app.Application;
import com.jme3.material.Material;
import com.jme3.scene.Spatial;

import online.money_daisuki.api.base.Converter;
import online.money_daisuki.api.base.DataSource;
import online.money_daisuki.api.base.Requires;

public final class FlexibleModelLoader implements Converter<String, Spatial> {
	private final Converter<String, Spatial> cache;
	
	public FlexibleModelLoader(final Converter<String, Material> matLoader, final Application app) {
		/*this.cache = new WeakHashMapRefresher<>(new Converter<String, Spatial>() {
			@Override
			public Spatial convert(final String value) {
				final DataSource<Spatial> ldr = new ModelLoader(Requires.notNull(value, "value == null"), matLoader,
						app);
				return(ldr.source());
			}
		});*/
		this.cache = new Converter<String, Spatial>() {
			@Override
			public Spatial convert(final String value) {
				final DataSource<Spatial> ldr = new ModelLoader(Requires.notNull(value, "value == null"), matLoader,
						app);
				return(ldr.source());
			}
		};
	}
	@Override
	public Spatial convert(final String value) {
		return(cache.convert(value));
	}
}
