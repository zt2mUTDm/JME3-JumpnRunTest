package online.money_daisuki.api.monkey.basegame.terrain;

import java.io.File;

import com.jme3.app.Application;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.terrain.geomipmap.TerrainQuad;

import online.money_daisuki.api.base.Converter;
import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.monkey.console.Command;

/**
 * AddTerrain file x y z scaleX scaleY scaleZ
 * @author Money Daisuki Online
 *
 */
public final class AddTerrainCommand implements Command {
	private final Converter<? super String, ? extends TerrainQuad> factory;
	private final Node node;
	private final Application app;
	
	public AddTerrainCommand(final Converter<? super String, ? extends TerrainQuad> factory,
			final Node node, final Application app) {
		this.factory = Requires.notNull(factory, "factory == null");
		this.node = Requires.notNull(node, "node == null");
		this.app = Requires.notNull(app, "app == null");
	}
	@Override
	public void execute(final Spatial a, final String[] b, final Runnable done) {
		Requires.lenEqual(b, 8);
		final File f = new File(b[1]);
		//Requires.isTrue(Utils.isSubdirectory(f, new File("data")));
		
		final TerrainQuad terrain = factory.convert(b[1]);
		terrain.setLocalTranslation(
				Float.parseFloat(b[2]),
				Float.parseFloat(b[3]),
				Float.parseFloat(b[4])
		);
		terrain.setLocalScale(
				Float.parseFloat(b[5]),
				Float.parseFloat(b[6]),
				Float.parseFloat(b[7])
		);
		
		terrain.addControl(new RigidBodyControl(0));
		
		final BulletAppState bullet = app.getStateManager().getState(BulletAppState.class);
		bullet.getPhysicsSpace().add(terrain);
		
		node.attachChild(terrain);
		
		done.run();
	}
}
