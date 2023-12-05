package online.money_daisuki.api.monkey.basegame.character.anim;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import online.money_daisuki.api.base.Requires;

public final class NameLinkedAnimPlayer implements AnimPlayer {
	private final AnimPlayer parent;
	// One-to-one linking
	private final Map<String, String> links;
	
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
		parent.addAnimationListener(new AnimListener() {
			@Override
			public void animationEvent(final String name, final boolean loop) {
				// TODO 1:1
				for(final Entry<String, String> e:links.entrySet()) {
					if(e.getValue().equals(name)) {
						l.animationEvent(e.getKey(), loop);
					}
				}
			}
		});
	}
	@Override
	public boolean removeAnimationListener(final AnimListener l) {
		return(parent.removeAnimationListener(l));
	}
}
