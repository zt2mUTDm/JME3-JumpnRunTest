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
import com.jme3.scene.Node;

import online.money_daisuki.api.base.NullRunnable;
import online.money_daisuki.api.base.Requires;

public class ConsoleAppState implements AppState {
	private final Command target;
	
	private final Node refNode;
	private final SoftstoringHistory<String> history;
	
	private final Runnable doneListener;
	
	private boolean initialized = false;
	
	private SimpleApplication app;
	private InputManager inputManager;
	
	private ConsoleRawInputListener rawInputListener;
	
	private ConsoleUi ui;
	
	public ConsoleAppState(final Command target) {
		this.target = Requires.notNull(target, "target == null");
		this.refNode = new Node();
		this.history = new SoftstoringHistory<>(255, true);
		this.doneListener = new NullRunnable();
	}

	@Override
	final public void initialize(final AppStateManager pstateManager, final Application papp) {
		initialized = true;
		app = (SimpleApplication) papp;
		inputManager = app.getInputManager();
		
		ui = new ConsoleUi(app);
		ui.applyViewPortChange();
		
		ui.setInputText(ui.getInputText());
		
		rawInputListener = new ConsoleRawInputListener();
		inputManager.addRawInputListener(rawInputListener);
	}
	
	@Override
	public final void cleanup() {
		inputManager.removeRawInputListener(rawInputListener);
		
		if (isVisible()) {
			setVisible(false);
		}
		
		rawInputListener.leftShiftPressed = false;
		
		initialized = false;
	}
	
	public void applyViewPortChange() {
		ui.applyViewPortChange();
	}
	
	public int getConsoleNumLines() {
		return(ui.getConsoleNumLines());
	}
	
	public float getConsoleTextSize() {
		if(!initialized) {
			throw new IllegalStateException("Not initialized");
		}
		return(ui.getConsoleTextSize());
	}
	
	private void submit(final String input) {
		ui.appendConsole("> " + input);
		
		try {
			target.execute(refNode, input.split(" "), doneListener);
		} catch(final RuntimeException e) {
			Throwable t = e;
			final Deque<Throwable> ts = new LinkedList<>();
			ts.add(t);
			for(; (t = t.getCause()) != null ;) {
				ts.addLast(t);
			}
			
			for(; (t = ts.pollFirst()) != null;) {
				ui.appendConsole("Error: " + t.toString());
			}
		}
	}
	
	private void appendInputText(final String text) {
		ui.appendInputText(text);
	}
	private void appendInputTextBackspace() {
		ui.appendInputTextBackspace();
	}
	
	@Override
	public void update(final float tpf) {
		ui.update(tpf);
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
	
	public final void setVisible(final boolean visible) {
		if(ui != null) {
			ui.setVisible(visible);
		}
	}
	
	public final void toggleVisible() {
		setVisible(!isVisible());
	}
	
	public final boolean isVisible() {
		return(ui.isVisible());
	}
	
	@Override
	public final void setEnabled(final boolean setEnabled) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public final boolean isEnabled() {
		return(ui.isVisible());
	}
	
	@Override
	public final boolean isInitialized() {
		return initialized;
	}
	
	@Override
	public String getId() {
		return(null);
	}
	
	private class ConsoleRawInputListener extends RawInputAdapter {
		private boolean leftShiftPressed;
		
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
					app.getInputManager().setCursorVisible(isVisible());
					evt.setConsumed();
				break;
				case KeyInput.KEY_BACK:
					if (isVisible()) {
						if (leftShiftPressed) {
							ui.setInputText("");
						} else {
							appendInputTextBackspace();
						}
						evt.setConsumed();
					}
				break;
				case KeyInput.KEY_RETURN:
					if (isVisible()) {
						final String text = ui.getInputText();
						if(text.equals("")) {
							setVisible(false);
						} else {
							submit(text);
							history.submit(text);
							ui.clearInput();
						}
						evt.setConsumed();
					}
				break;
				case KeyInput.KEY_UP:
					if (isVisible()) {
						ui.setInputText(history.previous(ui.getInputText()));
						evt.setConsumed();
						
					}
				break;
				case KeyInput.KEY_DOWN:
					if (isVisible()) {
						ui.setInputText(history.next(ui.getInputText()));
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
