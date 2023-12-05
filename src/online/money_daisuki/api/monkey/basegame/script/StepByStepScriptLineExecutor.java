package online.money_daisuki.api.monkey.basegame.script;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.jme3.app.SimpleApplication;
import com.jme3.scene.Spatial;

import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.monkey.basegame.variables.CompareOperation;
import online.money_daisuki.api.monkey.basegame.variables.VariablesManager;
import online.money_daisuki.api.monkey.console.CommandExecutor;

public final class StepByStepScriptLineExecutor implements ScriptLineExecutor {
	private final SimpleApplication app;
	
	private final List<String[]> commands;
	private final Spatial spatial;
	
	private final CommandExecutor exe;
	private final VariablesManager vars;
	
	private final Set<Long> runningScripts;
	
	private int run;
	private int pointer;
	
	private boolean sleeps;
	private float sleepCounter;
	
	private boolean waits;
	private Long waitingId;
	
	public StepByStepScriptLineExecutor(final Collection<String[]> commands, final Spatial spatial,
			final CommandExecutor exe, final VariablesManager vars, final SimpleApplication app) {
		this.commands = Requires.containsNotNull(new ArrayList<>(Requires.notNull(commands)));
		for(final String[] s:commands) {
			Requires.notNull(s, "s == null");
		}
		this.spatial = Requires.notNull(spatial, "spatial == null");
		this.exe = Requires.notNull(exe, "exe == null");
		this.vars = Requires.notNull(vars, "vars == null");
		
		this.runningScripts = new HashSet<>();
		this.app = Requires.notNull(app, "app == null");
	}
	@Override
	public void sink(final float f) {
		if(!runningScripts.isEmpty()) {
			return;
		}
		
		if(afterLastCommand()) {
			return;
		} else if(sleeps) {
			sleepCounter-= f;
			if(sleepCounter > 0.0f) {
				return;
			}
			sleeps = false;
			sink(f);
			return;
		} else if(waits) {
			if(!runningScripts.contains(waitingId)) {
				waits = false;
				waitingId = null;
				sink(f);
				return;
			} else {
				return;
			}
		}
		sink0();
	}
	private void sink0() {
		final String[] command = commands.get(pointer);
		switch(command[0]) {
			case("atomar"):
				pointer++;
				runAtomar();
				pointer++;
			break;
			case("sleep"):
				sleepCounter = Requires.greaterThanZero(Float.parseFloat(command[1]), "sleep duration <= 0");
				sleeps = true;
				pointer++;
				return;
			case("waitfor"):
				final int waitForId = Requires.positive(Integer.parseInt(command[1]), "wait id < 0");
				if(waitForId >= pointer) {
					throw new IllegalArgumentException("Waiting id is out of bounds");
				}
				final Long waitIdLong = Long.valueOf(run << 32 | pointer - waitForId - 1);
				pointer++;
				
				if(runningScripts.contains(waitIdLong)) {
					waitingId = waitIdLong;
					waits = true;
					return;
				} else if(afterLastCommand()) {
					return;
				}
			default:
				run();
			break;
		}
	}
	private void runAtomar() {
		do {
			final String[] command = commands.get(pointer);
			if(command[0].equals("endatomar")) {
				return;
			} else {
				run();
			}
		} while(true);
	}
	private void run() {
		final String[] command = commands.get(pointer);
		switch(command[0]) {
			case("if"):
				parseIfPart(command);
				return;
			case("elseif"):
			case("else"):
				searchEndIf();
			case("endif"):
				pointer++;
				return;
		}
		
		if(!afterLastCommand()) {
			final String[] realCommand = commands.get(pointer);
			final Long id = Long.valueOf((run << 32) | pointer);
			
			runningScripts.add(id);
			System.out.println(Arrays.toString(realCommand));
			exe.execute(spatial, realCommand, new Runnable() {
				@Override
				public void run() {
					runningScripts.remove(id);
				}
			});
			exe.execute(spatial, realCommand, new Runnable() {
				@Override
				public void run() {
					runningScripts.remove(id);
				}
			}, app);
			pointer++;
		}
	}
	private void parseIfPart(final String[] command) {
		pointer++;
		switch(command[0]) {
			case("if"):
			case("elseif"):
				if(!evaluateIf(command)) {
					searchNextIfPart();
					parseIfPart(commands.get(pointer));
				}
			break;
			case("else"):
			case("endif"):
			break;
			default:
				throw new IllegalStateException();
		}
	}
	private boolean evaluateIf(final String[] command) {
		if(command.length == 5) {
			final String type = command[1];
			final String name = command[2];
			final String op = command[3];
			final String constant = command[4];
			
			if(!vars.containsVariable(type, name)) {
				return(constant.equals("false") || constant.equals("0"));
			}
			return(vars.getVariable(type, name).test(CompareOperation.getOp(op), constant));
		} else {
			throw new UnsupportedOperationException();
		}
	}
	private void searchNextIfPart() {
		int deep = 0;
		
		for(; ; pointer++) {
			final String[] cmd = commands.get(pointer);
			
			if(isDone()) {
				throw new IllegalArgumentException("Missing endif");
			}
			
			switch(cmd[0]) {
				case("if"):
					deep++;
				break;
				case("elseif"):
				case("else"):
					if(deep == 0) {
						return;
					}
				break;
				case("endif"):
					if(deep == 0) {
						return;
					} else {
						deep--;
					}
				break;
			}
		}
	}
	private void searchEndIf() {
		int deep = 0;
		
		for(; ; pointer++) {
			final String[] cmd = commands.get(pointer);
			
			if(isDone()) {
				throw new IllegalArgumentException("Missing endif");
			}
			
			switch(cmd[0]) {
				case("if"):
					deep++;
				return;
				case("endif"):
					if(deep == 0) {
						return;
					} else {
						deep--;
					}
				break;
			}
		}
	}
	
	private boolean afterLastCommand() {
		return(pointer >= commands.size());
	}
	@Override
	public boolean isDone() {
		return(afterLastCommand() && runningScripts.isEmpty());
	}
	@Override
	public void reset() {
		pointer = 0;
		run++;
	}
}
