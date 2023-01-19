package online.money_daisuki.api.monkey.basegame.misc;

import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.light.Light;
import com.jme3.light.PointLight;
import com.jme3.light.SpotLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.monkey.console.Command;

/**
 * <cmd> ambient r g b a
 * <cmd> directional r g b a x y z
 * <cmd> point r g b a r x y z
 * <cmd> spot range innerAngel outerAngel r g b a locationX locationY locationZ dirX dirY dirZ
 * @author Money Daisuki Online
 *
 */
public final class AddLightCommand implements Command {
	private final Node root;

	public AddLightCommand(final Node root) {
		this.root = Requires.notNull(root, "root == null");
	}
	@Override
	public void execute(final Spatial a, final String[] b, final Runnable done) {
		Requires.notNull(a, "a == null");
		Requires.containsNotNull(b, "contains null");
		Requires.sizeGreaterThan(b, 1);
		
		final Node target = root;
		
		switch(b[1]) {
			case("ambient"):
				target.addLight(createAmbientLight(b));
			break;
			case("directional"):
				target.addLight(createDirectionalLight(b));
			break;
			case("point"):
				target.addLight(createPointLight(b));
			break;
			case("spot"):
				target.addLight(createSpotLight(b));
			break;
			default:
				throw new IllegalArgumentException("Unknown light type: " + b[1]);
		}
		done.run();
	}
	private Light createAmbientLight(final String[] b) {
		Requires.lenEqual(b, 6);
		
		final Light light = new AmbientLight();
		
		light.setColor(new ColorRGBA(
				Float.parseFloat(b[2]),
				Float.parseFloat(b[3]),
				Float.parseFloat(b[4]),
				Float.parseFloat(b[5]))
		);
		return(light);
	}
	private Light createDirectionalLight(final String[] b) {
		Requires.lenEqual(b, 9);
		
		final DirectionalLight light = new DirectionalLight();
		
		light.setColor(new ColorRGBA(
				Float.parseFloat(b[2]),
				Float.parseFloat(b[3]),
				Float.parseFloat(b[4]),
				Float.parseFloat(b[5]))
		);
		light.setDirection(new Vector3f(
				Float.parseFloat(b[6]),
				Float.parseFloat(b[7]),
				Float.parseFloat(b[8])
		).normalizeLocal());
		return(light);
	}
	private Light createPointLight(final String[] b) {
		Requires.lenEqual(b, 10);
		
		final PointLight light = new PointLight();
		
		light.setColor(new ColorRGBA(
				Float.parseFloat(b[2]),
				Float.parseFloat(b[3]),
				Float.parseFloat(b[4]),
				Float.parseFloat(b[5]))
		);
		light.setRadius(Float.parseFloat(b[6]));
		light.setPosition(new Vector3f(
				Float.parseFloat(b[7]),
				Float.parseFloat(b[8]),
				Float.parseFloat(b[9])
		));
		return(light);
	}
	private Light createSpotLight(final String[] b) {
		Requires.lenEqual(b, 15);
		
		final SpotLight light = new SpotLight();
		
		light.setSpotRange(Float.parseFloat(b[2]));
		light.setSpotInnerAngle(Float.parseFloat(b[3]));
		light.setSpotOuterAngle(Float.parseFloat(b[4]));
		light.setColor(new ColorRGBA(
				Float.parseFloat(b[5]),
				Float.parseFloat(b[6]),
				Float.parseFloat(b[7]),
				Float.parseFloat(b[8]))
		);
		light.setPosition(new Vector3f(
				Float.parseFloat(b[9]),
				Float.parseFloat(b[10]),
				Float.parseFloat(b[11])
		));
		light.setDirection(new Vector3f(
				Float.parseFloat(b[12]),
				Float.parseFloat(b[13]),
				Float.parseFloat(b[14])
		).normalizeLocal());
		return(light);
	}
}
