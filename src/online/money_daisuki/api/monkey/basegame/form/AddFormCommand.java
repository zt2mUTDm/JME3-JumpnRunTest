package online.money_daisuki.api.monkey.basegame.form;

import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppStateManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

import online.money_daisuki.api.base.DataSink;
import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.monkey.basegame.SceneGraphAppState;
import online.money_daisuki.api.monkey.basegame.misc.Utils;
import online.money_daisuki.api.monkey.console.Command;

public final class AddFormCommand implements Command {
	@Override
	public void execute(final Spatial caller, final String[] cmd, final Runnable done, final SimpleApplication app) {
		Requires.notNull(caller, "caller == null");
		Requires.notNull(cmd, "cmd == null");
		Requires.containsNotNull(cmd, "cmd contains null");
		Requires.lenEqual(cmd, 12);
		Requires.notNull(app, "app == null");
		
		final AppStateManager state = app.getStateManager();
		
		final Form form = state.getState(FormLoadAppState.class).load(cmd[1]);
		final Spatial spatial = form.getPhysicsObject().getSpatial();
		
		final Vector3f location = new Vector3f(
				Float.parseFloat(cmd[2]),
				Float.parseFloat(cmd[3]),
				Float.parseFloat(cmd[4])
		);
		final Vector3f scale = new Vector3f(
				Float.parseFloat(cmd[5]),
				Float.parseFloat(cmd[6]),
				Float.parseFloat(cmd[7])
		);
		final Quaternion q = new Quaternion().fromAngles(new float[] {
				Float.parseFloat(cmd[8]) * FastMath.DEG_TO_RAD,
				Float.parseFloat(cmd[9]) * FastMath.DEG_TO_RAD,
				Float.parseFloat(cmd[10]) * FastMath.DEG_TO_RAD
		});
		
		Utils.forEachControl(spatial, RigidBodyControl.class, new DataSink<RigidBodyControl>() {
			@Override
			public void sink(final RigidBodyControl rigid) {
				rigid.setPhysicsLocation(location);
				rigid.setPhysicsScale(scale);
				rigid.setPhysicsRotation(q);
			}
		});
		
		spatial.setLocalTranslation(location);
		spatial.setLocalScale(scale);
		spatial.setLocalRotation(q);
		
		final BulletAppState bullet = state.getState(BulletAppState.class);
		
		final String targetName = String.valueOf(cmd[11]);
		
		final SceneGraphAppState graph = state.getState(SceneGraphAppState.class);
		
		final Node target = graph.getRootNode();
		app.enqueue(new Runnable() {
			@Override
			public void run() {
				target.attachChild(spatial);
				bullet.getPhysicsSpace().addAll(spatial);
				
				done.run();
			}
		});
	}
}
