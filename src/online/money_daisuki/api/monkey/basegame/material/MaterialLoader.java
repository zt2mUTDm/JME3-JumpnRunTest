package online.money_daisuki.api.monkey.basegame.material;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import com.jme3.asset.AssetInfo;
import com.jme3.asset.AssetLoader;
import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.texture.Texture;

import online.money_daisuki.api.io.json.JsonDecoder;
import online.money_daisuki.api.io.json.JsonElement;
import online.money_daisuki.api.io.json.JsonList;
import online.money_daisuki.api.io.json.JsonMap;

public final class MaterialLoader implements AssetLoader {
	@Override
	public Material load(final AssetInfo assetInfo) {
		try(final Reader r = new InputStreamReader(assetInfo.openStream())) {
			final JsonMap main = new JsonDecoder(r).decode().asMap();
			return(load(assetInfo, main));
		} catch(final IOException e) {
			throw new IllegalArgumentException(e);
		}
	}
	private Material load(final AssetInfo assetInfo, final JsonMap main) {
		final AssetManager assetManager = assetInfo.getManager();
		
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
