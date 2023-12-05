package online.money_daisuki.api.base;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class ArgumentParser implements DataSource<ArgumentParserResult> {
	private final Map<String, String> aliases;
	private final Map<String, String[]> replacements;
	private final Map<String, Set<String>> conflicts;
	
	private final ArgumentLexerResult input;
	
	public ArgumentParser(final ArgumentLexerResult input) {
		this.input = Requires.notNull(input, "input == null");
		
		this.aliases = new HashMap<>();
		this.replacements = new HashMap<>();
		this.conflicts = new HashMap<>();
	}
	@Override
	public ArgumentParserResult source() {
		final Map<String, String[]> switches = new HashMap<>();
		
		for(int i = 0, size = input.getSwitchCount(); i < size; i++) {
			final String origS = input.getSwitchName(i);
			final String s = aliases.containsKey(origS) ? aliases.get(origS) : origS;
			
			if(replacements.containsKey(s)) {
				final String[] rep = replacements.get(s);
				for(final String r:rep) {
					switches.remove(r);
				}
			}
			
			if(conflicts.containsKey(s)) {
				final Set<String> con = conflicts.get(s);
				for(final String c:con) {
					if(switches.containsKey(c)) {
						throw new IllegalArgumentException("Conflicting switches: " + origS + " and " + c);
					}
				}
			}
			
			if(switches.containsKey(s)) {
				throw new IllegalArgumentException("Dublicating switches: " + origS);
			}
			switches.put(s, input.getSwitchArguments(i));
		}
		
		return(new ArgumentParserResult(switches, input.getFloatingArguments()));
	}
	
	/**
	 * Replace a switch by another switch. When there are two switches with the same function (say --config and -c),
	 * it is easier to read them from the results. Since this is the first that happening for each switch, it is more easy
	 * to set up other options only for the one of the possibilities.
	 * @param alias The switch to replace (without \"-"\).
	 * @param replaceWith The switch to replace the other with (without \"-"\).
	 */
	public void addAlias(final String alias, final String replaceWith) {
		Requires.isFalse(Requires.notNull(alias, "alias == null").startsWith("-"), "alias must not start with \"-\"");
		Requires.isFalse(Requires.notNull(replaceWith, "replaceWith == null").startsWith("-"), "replaceWith must not start with \"-\"");
		Requires.antiqual(alias, replaceWith, "alias and replaceWith cannot be equals");
		aliases.put(alias, replaceWith);
	}
	/**
	 * Set up an array of switches, that replace each other inclusive itselves. That means, if one of the switch is handled,
	 * all current switches get removed, if present.
	 * @param replacements
	 */
	public void addReplaceAll(final String... replacements) {
		final String[] copy = Requires.containsNotNull(Arrays.copyOf(replacements, Requires.notNull(replacements, "replacements == null").length));
		for(final String s:copy) {
			this.replacements.put(s, copy);
		}
	}
	/**
	 * Set up one switches that replace all switches containing in an array. That means, if this switch is handled,
	 * all already present switches of the array get removed.
	 * @param target
	 * @param replacements
	 */
	public void addReplaceOne(final String target, final String... replacements) {
		final String[] copy = Requires.containsNotNull(Arrays.copyOf(replacements, Requires.notNull(replacements, "replacements == null").length));
		this.replacements.put(Requires.notNull(target, "target == null"), copy);
	}
	/**
	 * Add an array of conflicting switches. Any two switches of this array cannot used together. A switch always conflict
	 * with itself, as long it doesn't replace itself.
	 * @param conflicts
	 * @see ArgumentHandler#addReplaceOne(String, String...)
	 * @see ArgumentHandler#addReplaceAll(String...)
	 */
	public void addConflicts(final String... conflicts) {
		final String[] copy = Requires.containsNotNull(Arrays.copyOf(conflicts, Requires.notNull(conflicts, "conflicts == null").length));
		
		for(final String s:copy) {
			if(!this.conflicts.containsKey(s)) {
				this.conflicts.put(s, new HashSet<>());
			}
			this.conflicts.get(s).addAll(Arrays.asList(copy));
		}
	}
}
