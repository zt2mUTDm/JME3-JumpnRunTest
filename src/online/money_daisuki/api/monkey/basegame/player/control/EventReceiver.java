package online.money_daisuki.api.monkey.basegame.player.control;

import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.GhostControl;
import com.jme3.scene.Spatial;

import online.money_daisuki.api.base.DataSink;
import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.misc.mapping.FinalMapping;
import online.money_daisuki.api.misc.mapping.Mapping;

public final class EventReceiver extends GhostControl {
	private final Spatial mainSpatial;
	private final TriggerType type;
	private final DataSink<Mapping<Spatial, Runnable>> hook;
	
	private boolean active;
	private boolean running;
	
	public EventReceiver(final CollisionShape shape, final Spatial mainSpatial, final TriggerType type,
			final DataSink<Mapping<Spatial, Runnable>> hook) {
		super(shape);
		this.type = Requires.notNull(type, "type == null");
		this.mainSpatial = Requires.notNull(mainSpatial, "mainSpatial == null");
		this.hook = Requires.notNull(hook, "hook == null");
	}
	public void trigger() {
		if(!running) {
			active = true;
		}
	}
	@Override
	public void update(final float tpf) {
		super.update(tpf);
		
		if(active && !running) {
			running = true;
			
			final Runnable callback = new Runnable() {
				@Override
				public void run() {
					active = false;
					running = false;
				}
			};
			final Mapping<Spatial, Runnable> mapping = new FinalMapping<>(mainSpatial, callback);
			hook.sink(mapping);
		}
	}
	public TriggerType getTriggerType() {
		return(type);
	}
	
	public static enum TriggerType {
		TOUCH,
		SHOT
	}
}
