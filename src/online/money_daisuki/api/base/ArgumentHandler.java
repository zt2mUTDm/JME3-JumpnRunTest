package online.money_daisuki.api.base;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

public final class ArgumentHandler implements DataSource<ArgumentParserResult> {
	private final String[] arguments;
	
	private final Map<String, Integer> lexerSwitches;
	
	private final Map<String, String> aliases;
	private final Map<String, String[]> replacements;
	private final Collection<String[]> conflicts;
	
	private String helpText;
	private String helpSwitch;
	
	public ArgumentHandler(final String[] arguments) {
		this.arguments = Requires.containsNotNull(Arrays.copyOf(arguments, Requires.notNull(arguments).length));
		
		this.lexerSwitches = new HashMap<>();
		
		this.aliases = new HashMap<>();
		this.replacements = new HashMap<>();
		this.conflicts = new LinkedList<>();
	}
	
	@Override
	public ArgumentParserResult source() {
		try {
			final ArgumentParserResult result = handle();
			
			if(helpSwitch != null) {
				if(result.containsSwitch(helpSwitch)) {
					System.out.println(helpText);
					Runtime.getRuntime().exit(0);
				}
			}
			
			return(result);
		} catch(final RuntimeException e) {
			if(helpText != null) {
				e.printStackTrace();
				
				System.err.println();
				System.err.println(helpText);
				Runtime.getRuntime().exit(1);
				
				throw new UnsupportedOperationException("Should never reaches");
			} else {
				throw e;
			}
		}
	}
	private ArgumentParserResult handle() {
		final ArgumentLexer lexer = new ArgumentLexer(arguments);
		for(final Entry<String, Integer> e:lexerSwitches.entrySet()) {
			lexer.addSwitch(e.getKey(), e.getValue());
		}
		if(helpSwitch != null && !lexerSwitches.containsKey(helpSwitch)) {
			lexer.addSwitch(helpSwitch, 0);
		}
		
		final ArgumentParser parser = new ArgumentParser(lexer.source());
		for(final Entry<String, String> e:aliases.entrySet()) {
			parser.addAlias(e.getKey(), e.getValue());
		}
		for(final Entry<String, String[]> e:replacements.entrySet()) {
			parser.addReplaceOne(e.getKey(), e.getValue());
		}
		for(final String[] c:conflicts) {
			parser.addConflicts(c);
		}
		
		final ArgumentParserResult result = parser.source();
		return(result);
	}
	
	/**
	 * Add a switch and the count of arguments.
	 * @param name The switch without minus. Must be unique.
	 * @param argumentCount The count of arguments of this switch. Must be positive.
	 */
	public void addSwitch(final String name, final int argumentCount) {
		if(lexerSwitches.containsKey(Requires.notNull(name, "name == null"))) {
			throw new IllegalArgumentException("Duplicate switch added: " + name);
		} else if(name.startsWith("-")) {
			throw new IllegalArgumentException("Added switch must not start with \"-\":" + name);
		}
		
		lexerSwitches.put(
				name,
				Requires.positive(argumentCount, "i < 0")
		);
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
		this.conflicts.add(Requires.containsNotNull(Arrays.copyOf(conflicts, Requires.notNull(conflicts, "conflicts == null").length)));
	}
	
	public void setHelpText(final String text) {
		this.helpText = Requires.notNull(text, "helpText == null");
		this.helpSwitch = null;
	}
	public void setHelpText(final String text, final String helpSwitch) {
		Requires.isFalse(Requires.notNull(helpSwitch, "helpSwitch == null").startsWith("-"), "helpSwitch must not start with \"-\"");
		
		this.helpText = Requires.notNull(text, "helpText == null");
		this.helpSwitch = helpSwitch;
	}
}
