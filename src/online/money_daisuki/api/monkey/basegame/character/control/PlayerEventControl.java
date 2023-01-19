package online.money_daisuki.api.monkey.basegame.character.control;

import com.jme3.bullet.control.GhostControl;

import online.money_daisuki.api.base.Requires;

public final class PlayerEventControl extends GhostControl {
	private final ActivationType type;
	
	private boolean triggered;
	
	public PlayerEventControl(final ActivationType type) {
		this.type = Requires.notNull(type, "type == null");
	}
	@Override
	public void update(final float tpf) {
		super.update(tpf);
		
		if(triggered) {
			System.out.println("lol");
			triggered = false;
		}
	}
	
	public void trigger() {
		triggered = true;
	}
	public ActivationType getType() {
		return (type);
	}
	
	
	private enum ActivationType {
		TOUCH,
		TRIGGER
	}
}
