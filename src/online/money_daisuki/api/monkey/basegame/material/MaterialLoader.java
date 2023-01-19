package online.money_daisuki.api.monkey.basegame.material;

import com.jme3.app.Application;
import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.WrapMode;

import online.money_daisuki.api.base.DataSource;
import online.money_daisuki.api.base.Requires;

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
		//TODO
		final AssetManager assetManager = app.getAssetManager();

		/** 1. Create terrain material and load four textures into it. */
		final Material mat = new Material(assetManager,
				"Common/MatDefs/Terrain/Terrain.j3md");

		/** 1.1) Add ALPHA map (for red-blue-green coded splat textures) */
		mat.setTexture("Alpha", assetManager.loadTexture(
				"Textures/Terrain/splat/alphamap.png"));

		/** 1.2) Add GRASS texture into the red layer (Tex1). */
		final Texture grass = assetManager.loadTexture(
				"Textures/Terrain/splat/grass.jpg");
		grass.setWrap(WrapMode.Repeat);
		mat.setTexture("Tex1", grass);
		mat.setFloat("Tex1Scale", 64f);

		/** 1.3) Add DIRT texture into the green layer (Tex2) */
		final Texture dirt = assetManager.loadTexture(
				"Textures/Terrain/splat/dirt.jpg");
		dirt.setWrap(WrapMode.Repeat);
		mat.setTexture("Tex2", dirt);
		mat.setFloat("Tex2Scale", 32f);

		/** 1.4) Add ROAD texture into the blue layer (Tex3) */
		final Texture rock = assetManager.loadTexture(
				"Textures/Terrain/splat/road.jpg");
		rock.setWrap(WrapMode.Repeat);
		mat.setTexture("Tex3", rock);
		mat.setFloat("Tex3Scale", 128f);
		return(mat);
	}
}
