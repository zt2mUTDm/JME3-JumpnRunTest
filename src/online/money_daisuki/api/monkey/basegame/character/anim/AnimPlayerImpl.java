package online.money_daisuki.api.monkey.basegame.character.anim;

import com.jme3.anim.AnimComposer;
import com.jme3.anim.tween.action.Action;

import online.money_daisuki.api.base.NullRunnable;
import online.money_daisuki.api.base.Requires;

public final class AnimPlayerImpl implements AnimPlayer {
	private final AnimComposer composer;
	
	private String oldName;
	private Action oldAction;
	
	public AnimPlayerImpl(final AnimComposer composer) {
		this.composer = Requires.notNull(composer, "composer == null");
	}
	
	@Override
	public void play(final String name, final boolean once) {
		play(name, once, new NullRunnable());
	}
	@Override
	public void play(final String name, final boolean once, final Runnable l) {
		Requires.notNull(name, "name == null");
		Requires.notNull(l, "l == null");
		
		unsetOld();
		
		oldName = name;
		oldAction = composer.action(name);
		
		composer.addAction(name, new ListenableAction(oldAction, once, l));
		composer.setCurrentAction(name);
	}
	
	private void unsetOld() {
		if(oldName != null) {
			composer.addAction(oldName, oldAction);
			
			oldName = null;
			oldAction = null;
		}
	}
}
