package online.money_daisuki.api.monkey.basegame.script;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.BaseAppState;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.monkey.basegame.misc.RemoveDoneControlsAppState;
import online.money_daisuki.api.monkey.console.Command;
import online.money_daisuki.api.monkey.console.CommandExecutor;
import online.money_daisuki.api.utils.NoDataListenerContainer;

public final class CommandExecutorAppState extends BaseAppState implements CommandExecutor {
	private boolean foregroundJobRunning;
	private final Queue<ExecControl> foregroundQueue;
	
	private final CommandExecutor exe;
	
	private final Node parent;
	private RemoveDoneControlsAppState removeDoneControls;
	
	private final NoDataListenerContainer foregroundTaskStartedListener;
	private final NoDataListenerContainer foregroundTaskDoneListener;
	
	private SimpleApplication app;
	
	public CommandExecutorAppState(final CommandExecutor exe, final Node parent) {
		this.exe = Requires.notNull(exe, "exe == null");
		
		this.foregroundQueue = new LinkedList<>();
		this.parent = Requires.notNull(parent, "parent == null");
		
		this.foregroundTaskStartedListener = new NoDataListenerContainer();
		this.foregroundTaskDoneListener = new NoDataListenerContainer();
	}
	
	public void addForegroundTaskStartedListener(final Runnable l) {
		foregroundTaskStartedListener.addListener(Requires.notNull(l, "l == null"));
	}
	public Collection<Runnable> getForegroundTaskStartedListeners() {
		return(foregroundTaskStartedListener.getListeners());
	}
	public boolean removeForegroundTaskStartedListener(final Runnable l) {
		return(foregroundTaskStartedListener.removeListener(Requires.notNull(l, "l == null")));
	}
	public void clearForegroundTaskStartedListeners() {
		foregroundTaskStartedListener.clearListeners();
	}
	
	public void addForegroundTaskDoneListener(final Runnable l) {
		foregroundTaskDoneListener.addListener(Requires.notNull(l, "l == null"));
	}
	public Collection<Runnable> getForegroundTaskDoneListeners() {
		return(foregroundTaskDoneListener.getListeners());
	}
	public boolean removeForegroundTaskDoneListener(final Runnable l) {
		return(foregroundTaskDoneListener.removeListener(Requires.notNull(l, "l == null")));
	}
	public void clearForegroundTaskDoneListeners() {
		foregroundTaskDoneListener.clearListeners();
	}
	
	@Override
	public void addCommand(final String name, final Command command) {
		exe.addCommand(name, command);
	}
	@Override
	public void removeCommand(final String name) {
		exe.removeCommand(name);
	}
	
	@Override
	public void execute(final Spatial spatial, final String[] cmd, final Runnable done) {
		exe.execute(spatial, cmd, done);
		exe.execute(spatial, cmd, done, app);
	}
	
	public void executeSimpleScript(final Spatial caller, final Collection<String[]> commands, final Runnable done, final boolean foreground) {
		if(commands.isEmpty()) {
			done.run();
			return;
		}
		
		final ExecControl s = new ExecControl(new StepByStepScriptLineExecutor(commands, caller, exe, app), false, new Runnable() {
			@Override
			public void run() {
				if(foreground) {
					if(foregroundQueue.isEmpty()) {
						foregroundJobRunning = false;
						foregroundTaskDoneListener.fireListeners();
					} else {
						final ExecControl next = foregroundQueue.remove();
						parent.addControl(next);
						removeDoneControls.addControl(next);
					}
				}
				done.run();
			}
		});
		if(foreground && foregroundJobRunning) {
			foregroundQueue.add(s);
		} else {
			parent.addControl(s);
			removeDoneControls.addControl(s);
			
			if(foreground) {
				foregroundJobRunning = true;
				foregroundTaskStartedListener.fireListeners();
			}
		}
	}
	
	@Override
	protected void initialize(final Application app) {
		this.app = (SimpleApplication) app;
		removeDoneControls = app.getStateManager().getState(RemoveDoneControlsAppState.class);
	}
	@Override
	protected void onDisable() {
		
	}
	@Override
	protected void onEnable() {
		
	}
	@Override
	protected void cleanup(final Application app) {
		this.app = null;
		removeDoneControls = null;
	}
}
