package online.money_daisuki.api.monkey.basegame.unsorted;

import java.util.ArrayList;
import java.util.Collection;

import com.jme3.scene.Node;

import online.money_daisuki.api.base.Requires;

public final class SceneDataImpl implements SceneData {
	private final Node characterNode;
	private final Collection<EventData> events;
	
	public SceneDataImpl(final Node characterNode, final Collection<EventData> events) {
		this.characterNode = Requires.notNull(characterNode, "characterNode == null");
		this.events = new ArrayList<>(Requires.notNull(events, "events == null"));
	}
	@Override
	public Node getCharacterNode() {
		return (characterNode);
	}
	@Override
	public Collection<EventData> getEvents() {
		return (new ArrayList<>(events));
	}
}
