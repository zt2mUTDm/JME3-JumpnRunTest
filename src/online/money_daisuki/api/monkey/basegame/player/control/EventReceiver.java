package online.money_daisuki.api.monkey.basegame.player.control;

import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.GhostControl;
import com.jme3.scene.Spatial;

import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.monkey.basegame.ExtendedApplication;

public final class EventReceiver extends GhostControl {
	private final Spatial mainSpatial;
	private final TriggerType type;
	private final String script;
	private final ExtendedApplication app;
	
	private boolean active;
	private boolean running;
	
	public EventReceiver(final CollisionShape shape, final Spatial mainSpatial, final TriggerType type, final String script, final ExtendedApplication app) {
		super(shape);
		this.type = Requires.notNull(type, "type == null");
		this.mainSpatial = Requires.notNull(mainSpatial, "mainSpatial == null");
		this.script = Requires.notNull(script, "script == null");
		this.app = Requires.notNull(app, "app == null");
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
			
			app.executeSimpleScript(mainSpatial, script, new Runnable() {
				@Override
				public void run() {
					active = false;
					running = false;
				}
			});
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
