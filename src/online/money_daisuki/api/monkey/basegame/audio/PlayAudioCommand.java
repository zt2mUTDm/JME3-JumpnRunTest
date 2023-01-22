package online.money_daisuki.api.monkey.basegame.audio;

import com.jme3.audio.AudioNode;
import com.jme3.scene.Spatial;

import online.money_daisuki.api.base.BiConverter;
import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.monkey.console.Command;

public final class PlayAudioCommand implements Command {
	private final BiConverter<String, Spatial, Spatial> spatialTarget;
	
	public PlayAudioCommand(final BiConverter<String, Spatial, Spatial> spatialTarget) {
		this.spatialTarget = Requires.notNull(spatialTarget, "spatialTarget == null");
	}
	@Override
	public void execute(final Spatial caller, final String[] cmd, final Runnable done) {
		Requires.notNull(caller, "caller == null");
		Requires.containsNotNull(cmd, "cmd contains null");
		Requires.lenEqual(cmd, 2);
		
		final Spatial spatial = spatialTarget.convert(cmd[1], caller);
		
		if(spatial instanceof AudioNode) {
			((AudioNode) spatial).play();
		} else {
			throw new IllegalArgumentException("Found spatial is not an AudioNode");
		}
		done.run();
	}
}
