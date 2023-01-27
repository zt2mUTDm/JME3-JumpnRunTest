package online.money_daisuki.api.monkey.basegame.sky;

import com.jme3.app.SimpleApplication;
import com.jme3.scene.Spatial;
import com.jme3.util.SkyFactory;
import com.jme3.util.SkyFactory.EnvMapType;

import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.base.SetableDataSink;
import online.money_daisuki.api.monkey.console.Command;

public final class CreateSkyCommand implements Command {
	private final SetableDataSink<Spatial> skyTarget;
	private final SimpleApplication app;
	
	public CreateSkyCommand(final SetableDataSink<Spatial> skyTarget, final SimpleApplication app) {
		this.skyTarget = Requires.notNull(skyTarget, "skyTarget == null");
		this.app = Requires.notNull(app, "app == null");
	}
	@Override
	public void execute(final Spatial caller, final String[] cmd, final Runnable done) {
		Requires.notNull(caller, "caller == null");
		Requires.containsNotNull(cmd, "cmd contains null");
		Requires.lenEqual(cmd, 3);
		
		if(skyTarget.isSet()) {
			throw new IllegalArgumentException("Sky already set");
		}
		
		final String resource = cmd[1];
		
		EnvMapType type;
		switch(cmd[2]) {
			case("cube"):
				type = EnvMapType.CubeMap;
			break;
			case("sphere"):
				type = EnvMapType.SphereMap;
			break;
			case("equirect"):
				type = EnvMapType.EquirectMap;
			break;
			default:
				throw new IllegalArgumentException("Expect either cube, sphere or equiret.");
		}
		
		// TODO Maybe syncronize?
		final Spatial sky = SkyFactory.createSky(app.getAssetManager(), resource, type);
		app.getRootNode().attachChild(sky);
		skyTarget.sink(sky);
		done.run();
	}
}
