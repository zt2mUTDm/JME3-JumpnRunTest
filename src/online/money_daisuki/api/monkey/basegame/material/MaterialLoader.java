package online.money_daisuki.api.monkey.basegame.material;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import com.jme3.app.Application;
import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.texture.Texture;

import online.money_daisuki.api.base.DataSource;
import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.io.json.JsonDecoder;
import online.money_daisuki.api.io.json.JsonElement;
import online.money_daisuki.api.io.json.JsonList;
import online.money_daisuki.api.io.json.JsonMap;

public final class MaterialLoader implements DataSource<Material> {
	private final String s;
	private final Application app;
	
	public MaterialLoader(final String s, final Application app) {
		this.s = Requires.notNull(s, "path == null");
		//Requires.isTrue(Utils.isDataSubdirectory(new File(s)));
		this.app = Requires.notNull(app, "app == null");
	}
	@Override
	public Material source() {
		try(final Reader r = new FileReader(new File(s))) {
			final JsonMap main = new JsonDecoder(r).decode().asMap();
			return(load(main));
		} catch(final IOException e) {
			throw new IllegalArgumentException(e);
		}
	}
	private Material load(final JsonMap main) {
		final AssetManager assetManager = app.getAssetManager();
		
		final String url = main.get("url").asData().asString();
		
		final Material mat = new Material(assetManager, url);
		
		if(main.containsKey("textures")) {
			final JsonList texList = main.get("textures").asList();
			for(final JsonElement e:texList) {
				final JsonMap texMap = e.asMap();
				final Texture texture = assetManager.loadTexture(texMap.get("url").asData().asString());
				mat.setTexture(texMap.get("name").asData().asString(), texture);
			}
		}
		return(mat);
	}
}
