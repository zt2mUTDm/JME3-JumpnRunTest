package online.money_daisuki.api.monkey.basegame.particles;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import com.jme3.app.Application;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh.Type;
import com.jme3.material.Material;

import online.money_daisuki.api.base.Converter;
import online.money_daisuki.api.base.DataSource;
import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.io.json.JsonDecoder;
import online.money_daisuki.api.io.json.JsonMap;

public final class ParticleEmitterLoader implements DataSource<ParticleEmitter> {
	private final String s;
	private final Converter<String, Material> materialLoader;
	private final Application app;
	
	public ParticleEmitterLoader(final String s, final Converter<String, Material> materialLoader, final Application app) {
		this.s = Requires.notNull(s, "path == null");
		//Requires.isTrue(Utils.isDataSubdirectory(new File(s)));
		this.app = Requires.notNull(app, "app == null");
		this.materialLoader = Requires.notNull(materialLoader, "materialLoader == null");
	}
	@Override
	public ParticleEmitter source() {
		try(final Reader r = new FileReader(new File(s))) {
			final JsonMap main = new JsonDecoder(r).decode().asMap();
			return(load(main));
		} catch(final IOException e) {
			throw new IllegalArgumentException(e);
		}
	}
	private ParticleEmitter load(final JsonMap main) {
		Type type;
		switch(main.get("type").asData().asString()) {
			case("triangle"):
				type = Type.Triangle;
			break;
			case("point"):
				type = Type.Point;
			break;
			default:
				throw new IllegalArgumentException("Expect either triangle or point, not " + main.get("type").asData().asString());
		}
		
		final ParticleEmitter emitter = new ParticleEmitter(
				"",
				type,
				main.get("particleCount").asData().asNumber().asBigInteger().intValueExact()
		);
		emitter.setMaterial(materialLoader.convert(main.get("material").asData().asString()));
		
		if(main.containsKey("imageX")) {
			emitter.setImagesX(main.get("imageX").asData().asNumber().asBigInteger().intValueExact());
		}
		if(main.containsKey("imageY")) {
			emitter.setImagesY(main.get("imageY").asData().asNumber().asBigInteger().intValueExact());
		}
		if(main.containsKey("selectRandomImage")) {
			emitter.setSelectRandomImage(main.get("selectRandomImage").asData().asBool());
		}
		
		return(emitter);
	}
}
