package online.money_daisuki.api.monkey.basegame.terrain;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import com.jme3.app.Application;
import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.terrain.geomipmap.TerrainQuad;
import com.jme3.terrain.heightmap.AbstractHeightMap;
import com.jme3.terrain.heightmap.ImageBasedHeightMap;
import com.jme3.texture.Texture;

import online.money_daisuki.api.base.Converter;
import online.money_daisuki.api.base.DataSource;
import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.io.json.JsonDecoder;
import online.money_daisuki.api.io.json.JsonMap;

public final class TerrainLoader implements DataSource<TerrainQuad> {
	private final String s;
	private final Application app;
	private final Converter<String, Material> matLoader;
	
	public TerrainLoader(final String s, final Converter<String, Material> matLoader, final Application app) {
		this.s = Requires.notNull(s, "s == null");
		//Requires.isTrue(Utils.isDataSubdirectory(new File(s)));
		this.app = Requires.notNull(app, "app == null");
		this.matLoader = Requires.notNull(matLoader, "matLoader == null");
	}
	@Override
	public TerrainQuad source() {
		final AssetManager assetManager = app.getAssetManager();
		
		try(final Reader r = new FileReader(new File(s))) {
			final JsonMap main = new JsonDecoder(r).decode().asMap();
			
			final String name = main.get("name").asData().asString();
			final String image = main.get("image").asData().asString();
			final int size = main.get("size").asData().asNumber().asBigInteger().intValueExact();
			final int patchSize = main.get("patchSize").asData().asNumber().asBigInteger().intValueExact();
			final String material = main.get("image").asData().asString();
			
			final Material mat = matLoader.convert(material);
			
			final Texture heightMapImage = assetManager.loadTexture(image);
			final AbstractHeightMap heightmap = new ImageBasedHeightMap(heightMapImage.getImage());
			
			heightmap.load();
			
			final TerrainQuad terrain = new TerrainQuad(name, patchSize, size, heightmap.getHeightMap());
			terrain.setMaterial(mat);
			return(terrain);
		} catch(final IOException e) {
			throw new IllegalArgumentException(e);
		}
	}
}
