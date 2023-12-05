package online.money_daisuki.api.monkey.basegame.unsorted;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.jme3.scene.Spatial;

import online.money_daisuki.api.base.Requires;

public final class EventPartImpl implements EventPart {
	private final Spatial character;
	private final Collection<String[]> script;
	
	public EventPartImpl(final Spatial character, final Collection<String[]> script) {
		this.character = Requires.notNull(character, "character == null");
		this.script = copyScript(Requires.notNull(script, "script == null"));
	}
	private List<String[]> copyScript(final Collection<String[]> origin) {
		final List<String[]> script = new ArrayList<>(origin.size());
		for(final String[] arr:origin) {
			script.add(Requires.containsNotNull(Arrays.copyOf(arr, arr.length)));
		}
		return(script);
	}
	
	@Override
	public Spatial getCharacter() {
		return (character);
	}
	@Override
	public Collection<String[]> getScript() {
		return(copyScript(script));
	}
}
