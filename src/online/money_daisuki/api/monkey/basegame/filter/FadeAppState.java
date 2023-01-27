package online.money_daisuki.api.monkey.basegame.filter;

import com.jme3.app.Application;
import com.jme3.app.state.AppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.post.filters.FadeFilter;
import com.jme3.renderer.RenderManager;

import online.money_daisuki.api.base.NullRunnable;
import online.money_daisuki.api.base.Requires;

public final class FadeAppState implements AppState {
	private final FadeFilter filter;
	
	private boolean initialized;
	private boolean enabled;
	
	private boolean awaitListener;
	private float listenerTarget;
	private Runnable listener;
	
	public FadeAppState(final FadeFilter filter) {
		this.filter = Requires.notNull(filter, "filter == null");
		this.enabled = true;
	}
	
	@Override
	public void update(final float tpf) {
		if(awaitListener) {
			if(filter.getValue() == listenerTarget) {
				listener.run();
				listener = new NullRunnable();
				awaitListener = false;
			}
		}
	}
	
	public void fadeIn(final Runnable l) {
		Requires.notNull(l, "l == null");
		if(awaitListener) {
			listener.run();
		} else {
			awaitListener = true;
		}
		
		listenerTarget = 1.0f;
		listener = l;
		filter.fadeIn();
	}
	public void fadeOut(final Runnable l) {
		Requires.notNull(l, "l == null");
		if(awaitListener) {
			listener.run();
		} else {
			awaitListener = true;
		}
		
		listenerTarget = 0.0f;
		listener = l;
		filter.fadeOut();
	}
	public void setDuration(final float duration) {
		filter.setDuration(duration);
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
		this.enabled = active;
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
