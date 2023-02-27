package online.money_daisuki.api.monkey.basegame.filter;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.post.filters.FadeFilter;

import online.money_daisuki.api.base.Requires;

/**
 * An AppState to provide global fading extended with a callback.
 */
public final class FadeAppState extends BaseAppState {
	private final FadeFilter filter;
	
	private float listenerTarget;
	private Runnable listener;
	
	public FadeAppState(final FadeFilter filter) {
		this.filter = Requires.notNull(filter, "filter == null");
	}
	
	@Override
	public void update(final float tpf) {
		if(listener != null && filter.getValue() == listenerTarget) {
			// Unset before call to prevent StackOverflowError.
			final Runnable r = listener;
			listener = null;
			r.run();
		}
	}
	
	/**
	 * Start fading the screen in. The callback is called when either fading is done or {@link #fadeIn(Runnable)} or
	 * {@link #fadeOut(Runnable)} is called again, whatever occures first. Not Thread-safe, call only in JME-Thread!
	 * @param l The callback.
	 */
	public void fadeIn(final Runnable l) {
		Requires.notNull(l, "l == null");
		if(listener != null) {
			listener.run();
		}
		
		listenerTarget = 1.0f;
		listener = l;
		filter.fadeIn();
	}
	/**
	 * Start fading the screen out. The callback is called when either fading is done or {@link #fadeIn(Runnable)} or
	 * {@link #fadeOut(Runnable)} is called again, whatever occures first. Not Thread-safe, call only in JME-Thread!
	 * @param l The callback.
	 */
	public void fadeOut(final Runnable l) {
		Requires.notNull(l, "l == null");
		if(listener != null) {
			listener.run();
		}
		
		listenerTarget = 0.0f;
		listener = l;
		filter.fadeOut();
	}
	/**
	 * Set the fade duration of the underlying {@link FadeFilter}.
	 * @param duration Fade duration in seconds. Must be >= 0.
	 */
	public void setDuration(final float duration) {
		filter.setDuration(Requires.positive(duration));
	}
	
	@Override
	protected void cleanup(final Application app) {
		
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
