package online.money_daisuki.api.monkey.basegame.character.anim;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import online.money_daisuki.api.base.Requires;

public final class NameLinkedAnimPlayer implements AnimPlayer {
	private final AnimPlayer parent;
	// One-to-one linking
	private final Map<String, String> links;
	
	private Map<AnimListener, AnimListener> animationListener;
	
	public NameLinkedAnimPlayer(final AnimPlayer parent, final Map<String, String> links) {
		this.parent = Requires.notNull(parent, "parent == null");
		this.links = Requires.containsNotNull(new HashMap<>(Requires.notNull(links, "links == null")), "links contains null");
	}
	@Override
	public void play(final String name, final boolean loop) {
		final String realName = links.get(Requires.notNull(name, "name == null"));
		parent.play(realName != null ? realName : name, loop);
	}
	
	@Override
	public void setSpeed(final double d) {
		parent.setSpeed(d);
	}
	
	@Override
	public void addAnimationListener(final AnimListener l) {
		if(animationListener != null && animationListener.containsKey(l)) {
			return;
		}
		
		final AnimListener subListener = new AnimListener() {
			@Override
			public void animationEvent(final String name, final boolean loop) {
				// TODO 1:1
				boolean found = false;
				for(final Entry<String, String> e:links.entrySet()) {
					if(e.getValue().equals(name)) {
						l.animationEvent(e.getKey(), loop);
						
						found = true;
					}
				}
				
				if(!found) {
					l.animationEvent(name, loop);
				}
			}
		};
		parent.addAnimationListener(subListener);
		
		if(animationListener == null) {
			animationListener = new HashMap<>();
		}
		animationListener.put(l, subListener);
	}
	@Override
	public boolean removeAnimationListener(final AnimListener l) {
		if(animationListener == null) {
			return(false);
		}
		
		final AnimListener subListener = animationListener.remove(l);
		if(subListener != null) {
			parent.removeAnimationListener(subListener);
			
			if(animationListener == null) {
				animationListener = null;
			}
			return(true);
		} else {
			return(false);
		}
	}
}
