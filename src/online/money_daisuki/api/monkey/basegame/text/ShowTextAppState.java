package online.money_daisuki.api.monkey.basegame.text;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.renderer.RenderManager;

import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.monkey.basegame.MayDoneAppState;

public final class ShowTextAppState implements MayDoneAppState {
	private boolean enabled;
	private boolean initialized;
	
	private final ShowTextAppStateBackend backend;
	private final Collection<Runnable> textShowedListener;
	
	private Reader textIn;
	
	private final float speed;
	private float speedCounter;
	
	private boolean done;
	private boolean showAll;
	private boolean doubleSpeed;
	
	public ShowTextAppState(final ShowTextAppStateBackend backend, final float speed) {
		this.backend = Requires.notNull(backend, "backend == null");
		this.textShowedListener = new LinkedList<>();
		
		this.speed = Requires.greaterThanZero(speed, "Speed requires to be greater than zero");
		
		this.done = true;
		this.showAll = false;
		this.enabled = true;
		
		this.textIn = new StringReader("");
	}
	@Override
	public void update(final float tpf) {
		if(done) {
			return;
		}
		
		if(!showAll) {
			speedCounter+= tpf;
			final float realSpeed = (doubleSpeed ? speed * 0.5f : speed);
			while(speedCounter >= realSpeed && !done) {
				addLetter();
				speedCounter-= realSpeed;
			}
		} else {
			while(!done) {
				addLetter();
			}
		}
	}
	private void addLetter() {
		final int nextChar = nextChar();
		if(nextChar >= 0 && nextChar <= 65535) {
			backend.addLetter((char) nextChar);
		} else if(nextChar == -1) {
			done = true;
			fireTextShowedListener();
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
	
	@Override
	public boolean isDone() {
		return (done);
	}
	
	public void showText(final Reader textIn) {
		this.textIn = Requires.notNull(textIn, "textIn == null");
		this.showAll = false;
		this.done = false;
	}
	public void clear() {
		backend.clear();
	}
	public void showAll() {
		this.showAll = true;
	}
	public void setVisible(final boolean visible) {
		backend.setVisible(visible);
	}
	
	public void setDoubleSpeed(final boolean doubleSpeed) {
		this.doubleSpeed = doubleSpeed;
	}
	
	public void fireTextShowedListener() {
		for(final Runnable l:textShowedListener) {
			l.run();
		}
	}
	
	public void addTextShowedListener(final Runnable l) {
		this.textShowedListener.add(Requires.notNull(l, "l == null"));
	}
	public void removeTextShowedListener(final Runnable l) {
		this.textShowedListener.remove(Requires.notNull(l, "l == null"));
	}
	public Collection<Runnable> getTextShowedListeners() {
		return(new ArrayList<>(textShowedListener));
	}
	
	@Override
	public void initialize(final AppStateManager stateManager, final Application app) {
		initialized = true;
	}
	
	@Override
	public boolean isInitialized() {
		return(initialized);
	}
	
	@Override
	public String getId() {
		return(null);
	}
	
	@Override
	public void setEnabled(final boolean active) {
		enabled = active;
	}
	
	@Override
	public boolean isEnabled() {
		return(enabled);
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
	
	@Override
	public void cleanup() {
		
	}
}
