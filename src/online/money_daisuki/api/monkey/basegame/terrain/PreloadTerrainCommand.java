package online.money_daisuki.api.monkey.basegame.terrain;

import java.io.File;

import com.jme3.scene.Spatial;
import com.jme3.terrain.geomipmap.TerrainQuad;

import online.money_daisuki.api.base.Converter;
import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.monkey.basegame.misc.Utils;
import online.money_daisuki.api.monkey.console.Command;

/**
 * PreloadTerrain file
 * @author Money Daisuki Online
 *
 */
public final class PreloadTerrainCommand implements Command {
	private final Converter<? super String, ? extends TerrainQuad> factory;
	
	public PreloadTerrainCommand(final Converter<? super String, ? extends TerrainQuad> factory) {
		this.factory = Requires.notNull(factory, "factory == null");
	}
	@Override
	public void execute(final Spatial a, final String[] b, final Runnable done) {
		Requires.lenEqual(b, 2);
		final File f = new File(b[1]);
		Requires.isTrue(Utils.isDataSubdirectory(f));
		
		factory.convert(b[1]);
		
		done.run();
	}
}
