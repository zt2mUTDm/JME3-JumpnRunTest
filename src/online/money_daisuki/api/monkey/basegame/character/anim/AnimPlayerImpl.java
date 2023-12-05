package online.money_daisuki.api.monkey.basegame.character.anim;

import java.util.LinkedList;
import java.util.List;

import com.jme3.anim.AnimComposer;
import com.jme3.anim.tween.action.Action;

import online.money_daisuki.api.base.Requires;

public final class AnimPlayerImpl implements AnimPlayer {
	private final AnimComposer composer;
	
	private double speed;
	
	private String oldName;
	private Action oldAction;
	
	private Action actualAction;
	
	private List<AnimListener> listeners;
	
	public AnimPlayerImpl(final AnimComposer composer) {
		this.composer = Requires.notNull(composer, "composer == null");
	}
	
	@Override
	public void play(final String name, final boolean loop) {
		Requires.notNull(name, "name == null");
		
		unsetOld();
		
		oldName = name;
		oldAction = composer.action(name);
		
		actualAction = new ListenableAction(oldAction, loop, new Runnable() {
			@Override
			public void run() {
				if(listeners != null) {
					for(final AnimListener listener:listeners) {
						listener.animationEvent(name, loop);
					}
				}
			}
		});
		actualAction.setSpeed(speed);
		
		composer.addAction(name, actualAction);
		composer.setCurrentAction(name);
	}
	
	@Override
	public void setSpeed(final double d) {
		this.speed = d;
		
		if(actualAction != null) {
			actualAction.setSpeed(d);
		}
	}
	
	@Override
	public void addAnimationListener(final AnimListener l) {
		Requires.notNull(l, "l == null");
		
		if(listeners == null) {
			listeners = new LinkedList<>();
		}
		listeners.add(l);
	}
	@Override
	public boolean removeAnimationListener(final AnimListener l) {
		if(l == null) {
			return(false);
		}
		
		final boolean b = listeners.remove(l);
		if(listeners.isEmpty()) {
			listeners = null;
		}
		return(b);
	}
	
	private void unsetOld() {
		if(oldName != null) {
			composer.addAction(oldName, oldAction);
			
			oldName = null;
			oldAction = null;
			actualAction = null;
		}
	}
}
