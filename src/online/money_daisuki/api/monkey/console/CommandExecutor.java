package online.money_daisuki.api.monkey.console;

public interface CommandExecutor extends Command {
	
	public void addCommand(final String name, final Command command);
	
	public void removeCommand(final String name);
	
}
