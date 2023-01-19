package online.money_daisuki.api.monkey.basegame.light;

import com.jme3.light.DirectionalLight;
import com.jme3.light.Light;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

import online.money_daisuki.api.base.BiConverter;
import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.monkey.console.Command;

public final class AddDirectionalLightCommand implements Command {
	private final BiConverter<String, Spatial, Spatial> spatialTarget;
	
	public AddDirectionalLightCommand (final BiConverter<String, Spatial, Spatial> spatialTarget) {
		this.spatialTarget = Requires.notNull(spatialTarget, "spatialTarget == null");
	}
	@Override
	public void execute(final Spatial a, final String[] b, final Runnable done) {
		Requires.notNull(a, "a == null");
		Requires.containsNotNull(b, "contains null");
		Requires.lenEqual(b, 9);
		
		final Spatial target = spatialTarget.convert(b[1], a);
		
		final float re = Float.parseFloat(b[2]);
		final float gr = Float.parseFloat(b[3]);
		final float bl = Float.parseFloat(b[4]);
		final float al = Float.parseFloat(b[5]);
		
		final float x = Float.parseFloat(b[6]);
		final float y = Float.parseFloat(b[7]);
		final float z = Float.parseFloat(b[8]);
		
		final Light light = new DirectionalLight(
				new Vector3f(x, y, z),
				new ColorRGBA(re, gr, bl, al)
		);
		target.addLight(light);
		done.run();
	}
}
