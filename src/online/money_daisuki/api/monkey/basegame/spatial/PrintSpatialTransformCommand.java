package online.money_daisuki.api.monkey.basegame.spatial;

import java.io.PrintStream;

import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

import online.money_daisuki.api.base.BiConverter;
import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.monkey.console.Command;

public final class PrintSpatialTransformCommand implements Command {
	private final BiConverter<? super String, ? super Spatial, ? extends Spatial> source;
	private final PrintStream out;
	
	public PrintSpatialTransformCommand(final BiConverter<? super String, ? super Spatial, ? extends Spatial> source,
			final PrintStream out) {
		this.source = Requires.notNull(source, "source == null");
		this.out = Requires.notNull(out, "out == null");
	}
	@Override
	public void execute(final Spatial caller, final String[] cmd, final Runnable done) {
		Requires.notNull(caller, "caller == null");
		Requires.containsNotNull(cmd, "cmd contains null");
		Requires.lenEqual(cmd, 2);
		
		final Spatial spatial = source.convert(cmd[1], caller);
		if(spatial == null) {
			throw new IllegalArgumentException("spatial not found");
		}
		
		final Vector3f trans = spatial.getLocalTranslation();
		final Vector3f scale = spatial.getLocalScale();
		final Quaternion rot = spatial.getLocalRotation();
		
		
		final float tX = trans.x;
		final float tY = trans.y;
		final float tZ = trans.z;
		
		final float sX = scale.x;
		final float sY = scale.y;
		final float sZ = scale.z;
		
		final float[] rs = new float[3];
		rot.toAngles(rs);
		
		out.println(
				String.format(
						"((%s, %s, %s), (%s, %s, %s), (%s, %s, %s))",
						tX,
						tY,
						tZ,
						sX,
						sY,
						sZ,
						rs[0] * FastMath.RAD_TO_DEG,
						rs[1] * FastMath.RAD_TO_DEG,
						rs[2] * FastMath.RAD_TO_DEG
				)
		);
		done.run();
	}
}
