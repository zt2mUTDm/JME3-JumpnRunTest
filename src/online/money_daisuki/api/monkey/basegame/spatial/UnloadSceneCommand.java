package online.money_daisuki.api.monkey.basegame.spatial;

import com.jme3.app.Application;
import com.jme3.bullet.BulletAppState;
import com.jme3.light.Light;
import com.jme3.light.LightList;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.Spatial.CullHint;

import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.base.Setable;
import online.money_daisuki.api.base.SetableDataSource;
import online.money_daisuki.api.monkey.console.Command;

public final class UnloadSceneCommand implements Command {
	private final Node root;
	private final SetableDataSource<Spatial> player;
	private final Setable sky;
	private final Application app;

	public UnloadSceneCommand(final Node root, final SetableDataSource<Spatial> player,
			final Setable sky, final Application app) {
		this.root = Requires.notNull(root, "root == null");
		this.player = Requires.notNull(player, "player == null");
		this.sky = Requires.notNull(sky, "sky == null");
		this.app = Requires.notNull(app, "app == null");
	}
	@Override
	public void execute(final Spatial a, final String[] b, final Runnable done) {
		Requires.notNull(a, "a == null");
		Requires.containsNotNull(b, "contains null");
		Requires.lenEqual(b, 1);
		
		final BulletAppState bullet = app.getStateManager().getState(BulletAppState.class);
		
		bullet.getPhysicsSpace().removeAll(root);
		root.detachAllChildren();
		
		final LightList lights = root.getLocalLightList();
		for(int i = lights.size() - 1; i >= 0; i--) {
			final Light light = lights.get(i);
			root.removeLight(light);
		}
		
		if(player.isSet()) {
			final Spatial playerSpatial = player.source();
			
			bullet.getPhysicsSpace().add(playerSpatial);
			root.attachChild(playerSpatial);
			
			playerSpatial.setCullHint(CullHint.Always);
		}
		sky.unset();
		
		done.run();
	}
}
