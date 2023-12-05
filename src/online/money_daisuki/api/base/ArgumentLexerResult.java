package online.money_daisuki.api.base;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import online.money_daisuki.api.misc.mapping.FinalMapping;
import online.money_daisuki.api.misc.mapping.Mapping;

public final class ArgumentLexerResult {
	private final List<Mapping<String, String[]>> switches;
	private final Collection<String> floatingArgs;
	
	public ArgumentLexerResult(final Collection<Mapping<String, String[]>> switches,
			final Collection<String> floatingArgs) {
		// TODO detailed check
		this.switches = new ArrayList<>(switches.size());
		for(final Mapping<String, String[]> m:switches) {
			this.switches.add(new FinalMapping<>(
					Requires.notNull(m.getA()),
					Requires.containsNotNull(Arrays.copyOf(m.getB(), Requires.notNull(m.getB()).length))
			));
		}
		this.floatingArgs = Requires.containsNotNull(new ArrayList<>(Requires.notNull(floatingArgs, "floatingArgs == null")));
	}
	
	/**
	 * Get all arguments that are not arguments of switches.
	 */
	public Collection<String> getFloatingArguments() {
		return(new ArrayList<>(floatingArgs));
	}
	/**
	 * Get the count of all arguments that are not arguments of switches.
	 */
	public int getFloatingArgumentCount() {
		return(floatingArgs.size());
	}
	
	public String getSwitchName(final int i) {
		return(switches.get(i).getA());
	}
	
	/**
	 * Get the count of all switches in this result.
	 */
	public int getSwitchArgumentCount(final int i) {
		return(switches.get(i).getB().length);
	}
	
	public String getSwitchArgument(final int s, final int arg) {
		return(switches.get(s).getB()[arg]);
	}
	
	public String[] getSwitchArguments(final int s) {
		final String[] args = switches.get(s).getB();
		return(Arrays.copyOf(args, args.length));
	}
	
	/**
	 * Get the count of all switches in this result.
	 */
	public int getSwitchCount() {
		return(switches.size());
	}
}
