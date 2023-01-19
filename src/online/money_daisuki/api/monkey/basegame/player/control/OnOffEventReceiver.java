package online.money_daisuki.api.monkey.basegame.player.control;

import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.GhostControl;
import com.jme3.scene.Spatial;

import online.money_daisuki.api.base.NullRunnable;
import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.monkey.basegame.ExtendedApplication;

public final class OnOffEventReceiver extends GhostControl {
	private final Spatial mainSpatial;
	private final String[] script;
	private final ExtendedApplication app;
	
	private boolean trigger;
	private boolean active;
	
	public OnOffEventReceiver(final CollisionShape shape, final Spatial mainSpatial, final String[] script, final ExtendedApplication app) {
		super(shape);
		this.mainSpatial = Requires.notNull(mainSpatial, "mainSpatial == null");
		this.script = Requires.notNull(script, "script == null");
		this.app = Requires.notNull(app, "app == null");
	}
	public void trigger() {
		trigger = true;
	}
	@Override
	public void update(final float tpf) {
		super.update(tpf);
		
		if(trigger != active) {
			active = trigger;
			
			final int i = active ? 0 : 1;
			app.executeSimpleScript(mainSpatial, script[i], new NullRunnable());
		}
		trigger = false;
	}
}
