package online.money_daisuki.api.monkey.basegame.unsorted;

import java.util.ArrayList;
import java.util.List;

import com.jme3.math.Transform;

import online.money_daisuki.api.base.Requires;

public final class EventDataImpl implements EventData {
	private final String name;
	private final List<EventPart> parts;
	private final Transform transform;
	
	public EventDataImpl(final String name, final List<EventPart> parts, final Transform transform) {
		this.name = Requires.notNull(name, "name == null");
		this.parts = Requires.containsNotNull(new ArrayList<>(Requires.notNull(parts, "parts == null")), "parts contains null");
		this.transform = new Transform(
				Requires.notNull(transform.getTranslation()),
				Requires.notNull(transform.getRotation()),
				Requires.notNull(transform.getScale())
		);
	}
	@Override
	public String getName() {
		return (name);
	}
	@Override
	public List<EventPart> getParts() {
		return (new ArrayList<>(parts));
	}
	@Override
	public Transform getTransform() {
		return (transform);
	}
}
