package online.money_daisuki.api.monkey.basegame.player.control;

import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.GhostControl;

import online.money_daisuki.api.base.Requires;

public final class NotifySender extends GhostControl {
	private final String description;
	
	public NotifySender(final CollisionShape shape, final String description) {
		super(shape);
		this.description = Requires.notNull(description, "description == null");
	}
	public String getDescription() {
		return (description);
	}
}
