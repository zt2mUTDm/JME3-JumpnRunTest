package online.money_daisuki.api.monkey.basegame.light;

import com.jme3.light.SpotLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

import online.money_daisuki.api.base.BiConverter;
import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.monkey.console.Command;

public final class AddSpotLightCommand implements Command {
	private final BiConverter<String, Spatial, Spatial> spatialTarget;
	
	public AddSpotLightCommand (final BiConverter<String, Spatial, Spatial> spatialTarget) {
		this.spatialTarget = Requires.notNull(spatialTarget, "spatialTarget == null");
	}
	@Override
	public void execute(final Spatial a, final String[] b, final Runnable done) {
		Requires.notNull(a, "a == null");
		Requires.containsNotNull(b, "contains null");
		Requires.lenEqual(b, 10);
		
		final Spatial target = spatialTarget.convert(b[1], a);
		
		final float re = Float.parseFloat(b[2]);
		final float gr = Float.parseFloat(b[3]);
		final float bl = Float.parseFloat(b[4]);
		final float al = Float.parseFloat(b[5]);
		
		final float lx = Float.parseFloat(b[6]);
		final float ly = Float.parseFloat(b[7]);
		final float lz = Float.parseFloat(b[8]);
		
		final float dx = Float.parseFloat(b[9]);
		final float dy = Float.parseFloat(b[10]);
		final float dz = Float.parseFloat(b[11]);
		
		final float r = Float.parseFloat(b[12]);
		
		final float ia = Float.parseFloat(b[13]);
		final float oa = Float.parseFloat(b[14]);
		
		final SpotLight light = new SpotLight(
				new Vector3f(lx, ly, lz),
				new Vector3f(dx, dy, dz),
				r,
				new ColorRGBA(re, gr, bl, al),
				ia,
				oa
		);
		target.addLight(light);
		done.run();
	}
}
