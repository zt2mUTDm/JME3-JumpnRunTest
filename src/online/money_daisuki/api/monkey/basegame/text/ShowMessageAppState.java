package online.money_daisuki.api.monkey.basegame.text;

import java.io.IOException;
import java.io.Reader;
import java.util.Collection;
import java.util.LinkedList;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;

import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.io.PushbackReader;
import online.money_daisuki.api.io.StringReader;

public final class ShowMessageAppState extends BaseAppState {
	private final PushbackReader textIn;
	
	private int linesLeft;
	
	private boolean showAll;
	private boolean eof;
	
	private float tpfCounter;
	private float tpfThreshold;
	
	private State state;
	
	private MessageBoxAppState parent;
	
	private boolean triggerAdded;
	private ActionListener inputListener;
	
	private final Collection<Runnable> doneListeners;
	
	public ShowMessageAppState(final String s) {
		this(new StringReader(s));
	}
	public ShowMessageAppState(final Reader textIn) {
		this.textIn = new PushbackReader(Requires.notNull(textIn, "textIn == null"));
		this.state = State.INIT;
		
		this.linesLeft = 3;
		this.tpfThreshold = 0.05f;
		
		this.doneListeners = new LinkedList<>();
	}
	@Override
	protected void initialize(final Application app) {
		parent = app.getStateManager().getState(MessageBoxAppState.class);
	}
	@Override
	protected void cleanup(final Application app) {
		parent = null;
	}
	@Override
	protected void onEnable() {
		
	}
	@Override
	protected void onDisable() {
		
	}
	
	@Override
	public void update(final float tpf) {
		switch(state) {
			case INIT:
				parent.clear();
				parent.setVisible(true);
				state = State.SHOW_TEXT;
			break;
			case SHOW_TEXT:
				if(showAll) {
					while(state != State.WAIT_FOR_SUBMIT) {
						addLetter();
					}
				} else {
					tpfCounter+= tpf;
					while(tpfCounter >= tpfThreshold && state != State.WAIT_FOR_SUBMIT) {
						addLetter();
						tpfCounter-= tpfThreshold;
					}
				}
			break;
			case WAIT_FOR_SUBMIT:
			case DONE:
			break;
			default:
				throw new UnsupportedOperationException("Unhandled state");
		}
	}
	private void addLetter() {
		final int nextChar = nextChar();
		if(nextChar >= 0 && nextChar <= 65535) {
			if(nextChar == '\n') {
				if(linesLeft == 1) {
					final int nextNextChar = nextChar();
					
					if(nextNextChar != -1) {
						textIn.pushBack(nextNextChar);
						textIn.pushBack(nextChar);
					} else {
						eof = true;
					}
					state = State.WAIT_FOR_SUBMIT;
				} else {
					linesLeft--;
					parent.addLetter((char) nextChar);
				}
			} else {
				parent.addLetter((char) nextChar);
			}
		} else if(nextChar == -1) {
			state = State.WAIT_FOR_SUBMIT;
			eof = true;
		} else {
			throw new RuntimeException(new IOException("WTF"));
		}
	}
	private int nextChar() {
		try {
			return(textIn.read());
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public void registerInputListener(final InputManager input) {
		if(inputListener == null) {
			inputListener = new InputListener();
			input.addListener(inputListener, "MessageBoxSubmit");
			input.addListener(inputListener, "MessageBoxShowAll");
		}
	}
	public void unregisterInputListener(final InputManager input) {
		if(inputListener != null) {
			input.removeListener(inputListener);
			inputListener = null;
		}
	}
	
	public void registerDefaultTriggers(final InputManager input) {
		if(!triggerAdded) {
			input.addMapping("MessageBoxSubmit", new KeyTrigger(KeyInput.KEY_Q));
			input.addMapping("MessageBoxShowAll", new KeyTrigger(KeyInput.KEY_E));
			
			triggerAdded = true;
		}
	}
	public void unregisterDefaultTriggers(final InputManager input) {
		if(triggerAdded) {
			input.deleteMapping("MessageBoxSubmit");
			input.deleteMapping("MessageBoxShowAll");
			
			triggerAdded = false;
		}
	}
	
	public void addDoneListener(final Runnable l) {
		doneListeners.add(l);
	}
	public void removeDoneListener(final Runnable l) {
		doneListeners.remove(l);
	}
	private void fireDoneListeners() {
		for(final Runnable l:doneListeners) {
			l.run();
		}
	}
	
	private final class InputListener implements ActionListener {
		@Override
		public void onAction(final String name, final boolean isPressed, final float tpf) {
			switch(name) {
				case("MessageBoxSubmit"):
					if(state == State.WAIT_FOR_SUBMIT) {
						if(isPressed) {
							if(eof) {
								state = State.DONE;
								parent.setVisible(false);
								
								fireDoneListeners();
							} else {
								showAll = false;
								linesLeft = 3;
								state = State.SHOW_TEXT;
							}
						}
					} else {
						tpfThreshold = (isPressed ? 0.025f : 0.05f);
					}
				break;
				case("MessageBoxShowAll"):
					if(isPressed) {
						showAll = true;
					}
				break;
			}
		}
	}
	
	private enum State {
		INIT,
		SHOW_TEXT,
		WAIT_FOR_SUBMIT,
		DONE;
	}
}
