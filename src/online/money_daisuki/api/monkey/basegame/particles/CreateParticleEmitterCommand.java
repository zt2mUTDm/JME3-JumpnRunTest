package online.money_daisuki.api.monkey.basegame.particles;

import com.jme3.effect.ParticleEmitter;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

import online.money_daisuki.api.base.BiConverter;
import online.money_daisuki.api.base.Converter;
import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.monkey.console.Command;

public final class CreateParticleEmitterCommand implements Command {
	private final BiConverter<? super String, ? super Spatial, ? extends Node> nodeTarget;
	private final Converter<String, ParticleEmitter> particleConverter;
	
	public CreateParticleEmitterCommand(final BiConverter<? super String, ? super Spatial, ? extends Node> nodeTarget,
			final Converter<String, ParticleEmitter> particleConverter) {
		this.nodeTarget = Requires.notNull(nodeTarget, "nodeTarget == null");
		this.particleConverter = Requires.notNull(particleConverter, "particleConverter == null");
	}
	@Override
	public void execute(final Spatial caller, final String[] cmd, final Runnable done) {
		Requires.notNull(caller, "caller == null");
		Requires.containsNotNull(cmd, "cmd contains null");
		Requires.lenEqual(cmd, 7);
		
		final Node target = nodeTarget.convert(cmd[1], caller);
		
		final String nodename = cmd[2];
		
		final ParticleEmitter emitter = particleConverter.convert(cmd[3]);
		
		emitter.setName(nodename);
		
		final Vector3f translation = new Vector3f(
				Float.parseFloat(cmd[4]),
				Float.parseFloat(cmd[5]),
				Float.parseFloat(cmd[6])
		);
		emitter.setLocalTranslation(translation);
		
		target.attachChild(emitter);
		done.run();
	}
}
