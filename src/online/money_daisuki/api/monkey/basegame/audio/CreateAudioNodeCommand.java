package online.money_daisuki.api.monkey.basegame.audio;

import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioData.DataType;
import com.jme3.audio.AudioNode;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

import online.money_daisuki.api.base.BiConverter;
import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.monkey.console.Command;

public final class CreateAudioNodeCommand implements Command {
	private final BiConverter<String, Spatial, Spatial> spatialTarget;
	private final AssetManager asset;
	
	public CreateAudioNodeCommand(final AssetManager asset, final BiConverter<String, Spatial, Spatial> spatialTarget) {
		this.asset = Requires.notNull(asset, "asset == null");
		this.spatialTarget = Requires.notNull(spatialTarget, "spatialTarget == null");
	}
	@Override
	public void execute(final Spatial caller, final String[] cmd, final Runnable done) {
		Requires.notNull(caller, "caller == null");
		Requires.containsNotNull(cmd, "cmd contains null");
		Requires.lenEqual(cmd, 5);
		
		final Spatial target = spatialTarget.convert(cmd[1], caller);
		
		final String nodename = cmd[2];
		final String filename = cmd[3];
		
		DataType type;
		switch(cmd[4]) {
			case("buffer"):
				type = DataType.Buffer;
			break;
			case("stream"):
				type = DataType.Stream;
			break;
			default:
				throw new IllegalArgumentException("Expect either buffer or stream.");
		}
		
		final AudioNode audio = new AudioNode(asset, filename, type);
		audio.setLooping(true);
		audio.setName(nodename);
		
		if(target instanceof Node) {
			((Node) target).attachChild(audio);
		} else  {
			throw new IllegalArgumentException("Target spatial needs to be a node");
		}
		done.run();
	}
}
