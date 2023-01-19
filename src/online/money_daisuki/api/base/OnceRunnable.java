package online.money_daisuki.api.base;

import online.money_daisuki.api.base.models.MutableSingleValueModel;
import online.money_daisuki.api.base.models.MutableSingleValueModelImpl;

/**
 * {@link Runnable} that get one or two parent {@link Runnable}s. On the first
 * call, it calls the first {@link Runnable}, on each other it calls the second,
 * if any.
 * 
 * @author (c) Money Daisuki Online
 */
public final class OnceRunnable implements Runnable {
	private final Runnable once;
	private final Runnable els;
	private final MutableSingleValueModel<Boolean> active;
	
	public OnceRunnable(final Runnable parent) {
		this(parent, new NullRunnable());
	}
	public OnceRunnable(final Runnable once, final Runnable els) {
		this.once = Requires.notNull(once, "once == null");
		this.els = Requires.notNull(els, "els == null");
		this.active = new MutableSingleValueModelImpl<>(Boolean.TRUE);
	}
	@Override
	public void run() {
		if(active.source()) {
			once.run();
			active.sink(Boolean.FALSE);
		} else {
			els.run();
		}
	}
}
