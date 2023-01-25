package online.money_daisuki.api.monkey.basegame.particles;

import com.jme3.app.Application;
import com.jme3.effect.ParticleEmitter;
import com.jme3.material.Material;

import online.money_daisuki.api.base.Converter;
import online.money_daisuki.api.base.Requires;

public final class FlexibleParticleEmitterLoader implements Converter<String, ParticleEmitter> {
	private final Converter<String, Material> materialLoader;
	private final Application app;
	
	public FlexibleParticleEmitterLoader(final Converter<String, Material> materialLoader, final Application app) {
		this.materialLoader = Requires.notNull(materialLoader, "materialLoader == null");
		this.app = Requires.notNull(app, "app == null");
	}
	@Override
	public ParticleEmitter convert(final String value) {
		return(new ParticleEmitterLoader(Requires.notNull(value, "value == null"), materialLoader, app).source());
	}
}
