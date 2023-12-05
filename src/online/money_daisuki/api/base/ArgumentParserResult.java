package online.money_daisuki.api.base;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public final class ArgumentParserResult {
	private final Map<String, String[]> switches;
	private final Collection<String> floatingArguments;
	
	public ArgumentParserResult(final Map<String, String[]> switches, final Collection<String> floatingArguments) {
		this.switches = new HashMap<>();
		for(final Entry<String, String[]> e:switches.entrySet()) {
			this.switches.put(
					Requires.notNull(e.getKey()),
					Requires.containsNotNull(Arrays.copyOf(e.getValue(), Requires.notNull(e.getValue()).length))
			);
		}
		this.floatingArguments = Requires.containsNotNull(new ArrayList<>(Requires.notNull(floatingArguments, "floatingArguments == null")));
	}
	
	public Collection<String> getFloatingArguments() {
		return(new ArrayList<>(floatingArguments));
	}
	
	public boolean containsSwitch(final String s) {
		return(switches.containsKey(s));
	}
	
	public String[] getSwitchArguments(final String s) {
		return(switches.get(s));
	}
}
