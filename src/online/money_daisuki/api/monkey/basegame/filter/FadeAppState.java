package online.money_daisuki.api.monkey.basegame.filter;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.post.filters.FadeFilter;
import com.jme3.util.SafeArrayList;

import online.money_daisuki.api.base.Requires;

public final class FadeAppState extends BaseAppState {
	private final FadeFilter filter;
	
	private boolean fade;
	private float fadeTarget;
	private SafeArrayList<Runnable> nextListeners;
	
	private SafeArrayList<Runnable> fadeInListeners;
	private SafeArrayList<Runnable> fadeOutListeners;
	
	public FadeAppState(final FadeFilter filter) {
		this.filter = Requires.notNull(filter, "filter == null");
	}
	
	@Override
	public void update(final float tpf) {
		if(fade && filter.getValue() == fadeTarget) {
			fade = false;
			
			if(nextListeners != null) {
				for(final Runnable r:nextListeners.getArray()) {
					r.run();
				}
			}
		}
	}
	
	public void fadeIn() {
		fadeTarget = 1.0f;
		nextListeners = fadeInListeners;
		fade = true;
		filter.fadeIn();
	}
	public void fadeOut() {
		fadeTarget = 0.0f;
		nextListeners = fadeOutListeners;
		fade = true;
		filter.fadeOut();
	}
	public float getValue() {
		return(filter.getValue());
	}
	
	public void setDuration(final float duration) {
		filter.setDuration(Requires.positive(duration));
	}
	
	public void addFadeInListener(final Runnable l) {
		if(fadeInListeners == null) {
			fadeInListeners = new SafeArrayList<>(Runnable.class);
		}
		fadeInListeners.add(Requires.notNull(l, "l == null"));
	}
	
	public void addFadeOutListener(final Runnable l) {
		if(fadeOutListeners == null) {
			fadeOutListeners = new SafeArrayList<>(Runnable.class);
		}
		fadeOutListeners.add(Requires.notNull(l, "l == null"));
	}
	
	public void removeFadeInListener(final Runnable l) {
		if(fadeInListeners.remove(Requires.notNull(l, "l == null")) && fadeInListeners.isEmpty()) {
			fadeInListeners = null;
		}
	}
	public void removeFadeOutListener(final Runnable l) {
		if(fadeOutListeners.remove(Requires.notNull(l, "l == null")) && fadeOutListeners.isEmpty()) {
			fadeOutListeners = null;
		}
	}
	
	@Override
	protected void cleanup(final Application app) {
		nextListeners = null;
		
		if(fadeInListeners != null) {
			fadeInListeners.clear();
			fadeInListeners = null;
		}
		
		if(fadeOutListeners != null) {
			fadeOutListeners.clear();
			fadeOutListeners = null;
		}
	}
	@Override
	protected void initialize(final Application app) {
		
	}
	@Override
	protected void onDisable() {
		
	}
	@Override
	protected void onEnable() {
		
	}
}
