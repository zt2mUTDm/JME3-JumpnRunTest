package online.money_daisuki.api.monkey.basegame.particles;

import com.jme3.effect.ParticleEmitter;
import com.jme3.scene.Spatial;

import online.money_daisuki.api.base.BiConverter;
import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.monkey.console.Command;

public final class EmitAllParticlesCommand implements Command {
	private final BiConverter<? super String, ? super Spatial, ? extends Spatial> spatialTarget;
	
	public EmitAllParticlesCommand(final BiConverter<? super String, ? super Spatial, ? extends Spatial> spatialTarget) {
		this.spatialTarget = Requires.notNull(spatialTarget, "spatialTarget == null");
	}
	@Override
	public void execute(final Spatial caller, final String[] cmd, final Runnable done) {
		Requires.notNull(caller, "caller == null");
		Requires.containsNotNull(cmd, "cmd contains null");
		Requires.lenEqual(cmd, 2);
		
		final Spatial spatial = spatialTarget.convert(cmd[1], caller);
		
		if(spatial instanceof ParticleEmitter) {
			((ParticleEmitter) spatial).emitAllParticles();
		} else {
			throw new IllegalArgumentException("Found spatial is not a ParticleEmitter");
		}
		done.run();
	}
}
