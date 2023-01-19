package online.money_daisuki.api.monkey.basegame.character.anim;

import java.util.HashMap;
import java.util.Map;

import online.money_daisuki.api.base.Requires;

public final class NameLinkedAnimPlayer implements AnimPlayer {
	private final AnimPlayer parent;
	private final Map<String, String> links;
	
	public NameLinkedAnimPlayer(final AnimPlayer parent, final Map<String, String> links) {
		this.parent = Requires.notNull(parent, "parent == null");
		this.links = Requires.containsNotNull(new HashMap<>(Requires.notNull(links, "links == null")), "links contains null");
	}
	@Override
	public void play(final String name, final boolean once) {
		final String realName = links.get(Requires.notNull(name, "name == null"));
		parent.play(realName != null ? realName : name, once);
	}
	@Override
	public void play(final String name, final boolean once, final Runnable l) {
		final String realName = links.get(Requires.notNull(name, "name == null"));
		parent.play(realName != null ? realName : name, once, l);
	}
}
