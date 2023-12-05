package online.money_daisuki.api.monkey.basegame.character.anim;

import com.jme3.anim.tween.Tween;
import com.jme3.anim.tween.action.BaseAction;

import online.money_daisuki.api.base.Requires;

public final class ListenableAction extends BaseAction {
	private final boolean loop;
	private final Runnable listener;
	
	public ListenableAction(final Tween parent, final boolean loop,
			final Runnable listener) {
		super(parent);
		this.loop = loop;
		this.listener = Requires.notNull(listener, "listener == null");
	}
	@Override
	public boolean interpolate(final double t) {
		final boolean running = super.interpolate(t);
		if (!running) {
			listener.run();
			
			if(!loop) {
				setSpeed(0);
			}
		}
		return(running);
	}
}
