package online.money_daisuki.api.monkey.console;

import java.util.Deque;
import java.util.LinkedList;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.event.KeyInputEvent;
import com.jme3.renderer.RenderManager;

import online.money_daisuki.api.base.DataSink;
import online.money_daisuki.api.base.Requires;

public class ConsoleAppState implements AppState {
	private final DataSink<String> target;
	
	private final SoftstoringHistory<String> history;
	
	private boolean initialized = false;
	
	private SimpleApplication app;
	private InputManager inputManager;
	
	private ConsoleRawInputListener rawInputListener;
	
	private final Console console;
	
	public ConsoleAppState(final Console console, final DataSink<String> target) {
		this.console = Requires.notNull(console, "console == null");
		this.target = Requires.notNull(target, "target == null");
		
		this.history = new SoftstoringHistory<>(255, true);
	}
	
	@Override
	public void initialize(final AppStateManager pstateManager, final Application papp) {
		initialized = true;
		app = (SimpleApplication) papp;
		inputManager = app.getInputManager();
		
		rawInputListener = new ConsoleRawInputListener();
		inputManager.addRawInputListener(rawInputListener);
	}
	
	@Override
	public void cleanup() {
		inputManager.removeRawInputListener(rawInputListener);
		
		if (isVisible()) {
			setVisible(false);
		}
		
		rawInputListener.leftShiftPressed = false;
		
		initialized = false;
	}
	
	private void submit(final String input) {
		console.appendOutputText("> " + input);
		
		try {
			target.sink(input);
		} catch(final RuntimeException e) {
			Throwable t = e;
			final Deque<Throwable> ts = new LinkedList<>();
			ts.add(t);
			for(; (t = t.getCause()) != null ;) {
				ts.addLast(t);
			}
			
			for(; (t = ts.pollFirst()) != null;) {
				console.appendOutputText("Error: " + t.toString());
			}
		}
	}
	
	private void appendInputText(final String text) {
		console.appendInputText(text);
	}
	private void appendInputTextBackspace() {
		console.inputTextBackspace();
	}
	
	@Override
	public void update(final float tpf) {
		
	}
	
	@Override
	public void stateAttached(final AppStateManager stateManager) {
	}
	
	@Override
	public void stateDetached(final AppStateManager stateManager) {
	}
	
	@Override
	public void render(final RenderManager rm) {
	}
	
	@Override
	public void postRender() {
	}
	
	public void setVisible(final boolean visible) {
		if(console != null) {
			console.setVisible(visible);
		}
	}
	
	public void toggleVisible() {
		setVisible(!isVisible());
	}
	
	public boolean isVisible() {
		return(console.isVisible());
	}
	
	@Override
	public void setEnabled(final boolean e) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public boolean isEnabled() {
		return(console.isVisible());
	}
	
	@Override
	public boolean isInitialized() {
		return initialized;
	}
	
	@Override
	public String getId() {
		return(null);
	}
	
	public void addConsoleShownListener(final Runnable l) {
		console.addConsoleShownListener(l);
	}
	public void addConsoleHiddenListener(final Runnable l) {
		console.addConsoleHiddenListener(l);
	}
	public void removeConsoleShownListener(final Runnable l) {
		console.removeConsoleShownListener(l);
	}
	public void removeConsoleHiddenListener(final Runnable l) {
		console.removeConsoleHiddenListener(l);
	}
	
	private final class ConsoleRawInputListener extends RawInputAdapter {
		private boolean leftShiftPressed;
		private boolean previousCursorVisibility;
		
		@Override
		public void onKeyEvent(final KeyInputEvent evt) {
			if (!evt.isPressed()) {
				switch (evt.getKeyCode()) {
					case KeyInput.KEY_LSHIFT:
						leftShiftPressed = false;
						break;
				}
				return;
			}
			
			switch (evt.getKeyCode()) {
				case KeyInput.KEY_ESCAPE:
					if (isVisible()) {
						setVisible(false);
						evt.setConsumed();
					}
				break;
				case KeyInput.KEY_LSHIFT:
					leftShiftPressed = true;
				break;
				case KeyInput.KEY_TAB:
					toggleVisible();
					
					if(isVisible()) {
						previousCursorVisibility = app.getInputManager().isCursorVisible();
						app.getInputManager().setCursorVisible(true);
					} else {
						app.getInputManager().setCursorVisible(previousCursorVisibility);
					}
					evt.setConsumed();
				break;
				case KeyInput.KEY_BACK:
					if (isVisible()) {
						if (leftShiftPressed) {
							console.setInputText("");
						} else {
							appendInputTextBackspace();
						}
						evt.setConsumed();
					}
				break;
				case KeyInput.KEY_RETURN:
					if (isVisible()) {
						final String text = console.getInputText();
						if(text.equals("")) {
							setVisible(false);
							app.getInputManager().setCursorVisible(previousCursorVisibility);
						} else {
							submit(text);
							history.submit(text);
							console.setInputText("");
						}
						evt.setConsumed();
					}
				break;
				case KeyInput.KEY_UP:
					if (isVisible()) {
						console.setInputText(history.previous(console.getInputText()));
						evt.setConsumed();
					}
				break;
				case KeyInput.KEY_DOWN:
					if (isVisible()) {
						console.setInputText(history.next(console.getInputText()));
						evt.setConsumed();
					}
				break;
				default:
					if (isVisible()) {
						final char keyChar = evt.getKeyChar();
						if (keyChar != 0) {
							appendInputText(String.valueOf(keyChar));
						}
						evt.setConsumed();
					}
				break;
			}
		}
	}
}
