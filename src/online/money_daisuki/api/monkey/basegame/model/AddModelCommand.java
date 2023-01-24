package online.money_daisuki.api.monkey.basegame.model;

import java.io.File;

import com.jme3.app.Application;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

import online.money_daisuki.api.base.Converter;
import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.monkey.console.Command;

/**
 * AddTerrain file x y z scaleX scaleY scaleZ
 * @author Money Daisuki Online
 *
 */
public final class AddModelCommand implements Command {
	private final Converter<? super String, ? extends Spatial> factory;
	private final Node node;
	private final Application app;
	
	public AddModelCommand(final Converter<? super String, ? extends Spatial> factory,
			final Node node, final Application app) {
		this.factory = Requires.notNull(factory, "factory == null");
		this.node = Requires.notNull(node, "node == null");
		this.app = Requires.notNull(app, "app == null");
	}
	@Override
	public void execute(final Spatial a, final String[] b, final Runnable done) {
		Requires.lenEqual(b, 8);
		final File f = new File(b[1]);
		//Requires.isTrue(Utils.isSubdirectory(f, new File("models")));
		
		final Spatial spatial = factory.convert(b[1]);
		
		spatial.setLocalTranslation(new Vector3f(
				Float.parseFloat(b[2]),
				Float.parseFloat(b[3]),
				Float.parseFloat(b[4])
		));
		
		final Vector3f scale = new Vector3f(
				Float.parseFloat(b[5]),
				Float.parseFloat(b[6]),
				Float.parseFloat(b[7])
		);
		final Quaternion q = new Quaternion().fromAngles(new float[] {
				Float.parseFloat(b[8]) * FastMath.DEG_TO_RAD,
				Float.parseFloat(b[9]) * FastMath.DEG_TO_RAD,
				Float.parseFloat(b[10]) * FastMath.DEG_TO_RAD
		});
		
		spatial.setLocalScale(scale);
		spatial.setLocalRotation(q);
		
		final RigidBodyControl rigid = spatial.getControl(RigidBodyControl.class);
		if(rigid != null) {
			rigid.setPhysicsScale(scale);
		}
		
		app.enqueue(new Runnable() {
			@Override
			public void run() {
				node.attachChild(spatial);
				done.run();
			}
		});
	}
}
