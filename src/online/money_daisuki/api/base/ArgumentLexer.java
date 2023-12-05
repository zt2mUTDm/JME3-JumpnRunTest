package online.money_daisuki.api.base;

import java.util.Arrays;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import online.money_daisuki.api.misc.mapping.FinalMapping;
import online.money_daisuki.api.misc.mapping.Mapping;

public final class ArgumentLexer implements DataSource<ArgumentLexerResult> {
	private final String[] args;
	private final Map<String, Integer> switchArgCount;
	
	private final Deque<String> openSwitches;
	
	private String actualSwitch;
	private int actualSwitchArgCount;
	private Collection<String> actualSwitchArgs;
	
	private final Collection<Mapping<String, String[]>> switches;
	private final Deque<String> floatingArguments;
	
	public ArgumentLexer(final String[] args) {
		this.args = Requires.containsNotNull(
				Arrays.copyOf(
						Requires.notNull(args, "args == null"),
						args.length
				)
		);
		
		openSwitches = new LinkedList<>();
		switchArgCount = new HashMap<>();
		
		switches = new LinkedList<>();
		floatingArguments = new LinkedList<>();
	}
	
	/**
	 * Add a switch and the count of arguments.
	 * @param name The switch without minus. Must be unique.
	 * @param argumentCount The count of arguments of this switch. Must be positive.
	 */
	public void addSwitch(final String name, final int argumentCount) {
		if(switchArgCount.containsKey(name)) {
			throw new IllegalArgumentException("Duplicate switch added: " + name);
		} else if(name.startsWith("-")) {
			throw new IllegalArgumentException("Added switch must not start with \"-\":" + name);
		}
		
		
		switchArgCount.put(
				Requires.notNull(name, "name == null"),
				Requires.positive(argumentCount, "i < 0")
		);
	}
	
	/**
	 * Parse the arguments and return the Result.
	 */
	@Override
	public ArgumentLexerResult source() {
		boolean switchesEnabled = true;
		
		for(int i = 0, size = args.length; i < size; i++) {
			final String arg = args[i];
			
			if(arg.equals("-")) {
				handleFloatingArg(arg);
			} else if(arg.equals("--")) {
				switchesEnabled = false;
			} else if(arg.startsWith("-") && switchesEnabled) {
				handleSwitchArg(arg);
			} else {
				handleFloatingArg(arg);
			}
		}
		
		if(actualSwitch != null) {
			throw new IllegalArgumentException("Missing floating arguments");
		}
		return(new ArgumentLexerResult(switches, floatingArguments));
	}
	
	private void handleSwitchArg(final String arg) {
		if(arg.startsWith("--")) {
			if(arg.length() == 3) {
				throw new IllegalArgumentException("Long switches needs at least 4 characters.");
			}
			openSwitchArg(arg.substring(2));
		} else {
			for(int i = 1, size = arg.length(); i < size; i++) {
				openSwitchArg(String.valueOf(arg.charAt(i)));
			}
		}
	}
	private void handleFloatingArg(final String arg) {
		if(actualSwitch == null) {
			floatingArguments.add(arg);
		} else {
			actualSwitchArgs.add(arg);
			actualSwitchArgCount--;
			if(actualSwitchArgCount == 0) {
				closeSwitchArg();
			}
		}
	}
	
	private void openSwitchArg(final String arg) {
		if(!switchArgCount.containsKey(arg)) {
			throw new IllegalArgumentException("Unknown switch: " + arg);
		}
		
		final int argCount = switchArgCount.get(arg);
		if(argCount == 0) {
			switches.add(new FinalMapping<>(arg, new String[0]));
		} else if(actualSwitch == null) {
			actualSwitch = arg;
			actualSwitchArgCount = argCount;
			actualSwitchArgs = new LinkedList<>();
		} else {
			openSwitches.addLast(arg);
		}
	}
	
	private void closeSwitchArg() {
		switches.add(new FinalMapping<>(actualSwitch, actualSwitchArgs.toArray(new String[actualSwitchArgCount])));
		actualSwitch = null;
		while(!openSwitches.isEmpty() && actualSwitch == null) {
			openSwitchArg(openSwitches.removeFirst());
		}
	}

}
