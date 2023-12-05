package online.money_daisuki.api.monkey.basegame.sky;

import com.jme3.app.Application;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.util.SkyFactory;
import com.jme3.util.SkyFactory.EnvMapType;

import online.money_daisuki.api.base.BiConverter;
import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.base.SetableDataSink;
import online.money_daisuki.api.monkey.console.Command;

public final class CreateSkyCommand implements Command {
	private final SetableDataSink<? super Spatial> skyTarget;
	private final BiConverter<? super String, ? super Spatial, ? extends Node> nodeTarget;
	private final Application app;
	
	public CreateSkyCommand(final SetableDataSink<? super Spatial> skyTarget,
			final BiConverter<? super String, ? super Spatial, ? extends Node> nodeTarget,
			final Application app) {
		this.skyTarget = Requires.notNull(skyTarget, "skyTarget == null");
		this.nodeTarget = Requires.notNull(nodeTarget, "nodeTarget == null");
		this.app = Requires.notNull(app, "app == null");
	}
	@Override
	public void execute(final Spatial caller, final String[] cmd, final Runnable done) {
		Requires.notNull(caller, "caller == null");
		Requires.containsNotNull(cmd, "cmd contains null");
		Requires.lenEqual(cmd, 4);
		
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
		
		final Spatial sky = SkyFactory.createSky(app.getAssetManager(), resource, type);
		nodeTarget.convert(cmd[3], sky).attachChild(sky);
		skyTarget.sink(sky);
		done.run();
	}
}
