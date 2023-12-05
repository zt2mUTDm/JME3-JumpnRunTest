package online.money_daisuki.api.monkey.basegame.misc;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;

import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.utils.NoDataListenerContainer;

public final class RemoveDoneControlsAppState extends FrequenzDividingBaseAppState {
	private final Collection<MayDoneControl> controls;
	private final NoDataListenerContainer allDoneListener;
	
	public RemoveDoneControlsAppState(final float speed) {
		super(speed);
		this.controls = new LinkedList<>();
		this.allDoneListener = new NoDataListenerContainer();
	}
	
	public void addControl(final MayDoneControl c) {
		controls.add(Requires.notNull(c, "c == null"));
	}
	public void removeControl(final MayDoneControl c) {
		controls.remove(Requires.notNull(c, "c == null"));
	}
	public boolean hasControls() {
		return(!controls.isEmpty());
	}
	
	public void addAllDoneListener(final Runnable l) {
		allDoneListener.addListener(Requires.notNull(l, "l == null"));
	}
	public Collection<Runnable> getAllDoneListeners() {
		return(allDoneListener.getListeners());
	}
	public boolean removeAllDoneListener(final Runnable l) {
		return(allDoneListener.removeListener(Requires.notNull(l, "l == null")));
	}
	public void clearAllDoneListeners() {
		allDoneListener.clearListeners();
	}
	
	@Override
	public void doUpdate(final float tpf) {
		boolean removed = false;
		
		final Iterator<MayDoneControl> it = controls.iterator();
		while(it.hasNext()) {
			final MayDoneControl c = it.next();
			if(c.isDone()) {
				c.getSpatial().removeControl(c);
				it.remove();
				
				removed = true;
			}
		}
		
		if(removed && !hasControls()) {
			allDoneListener.fireListeners();
		}
	}
	
	public void invokeDetachWhenDone(final Application app, final Runnable callback) {
		final AppStateManager state = app.getStateManager();
		Requires.notNull(state, "state == null");
		
		if(hasControls()) {
			addAllDoneListener(new Runnable() {
				@Override
				public void run() {
					final Runnable thisRun = this;
					
					state.detach(RemoveDoneControlsAppState.this);
					app.enqueue(new Runnable() {
						@Override
						public void run() {
							removeAllDoneListener(thisRun);
						}
					});
					callback.run();
				}
			});
		} else {
			state.detach(this);
			callback.run();
		}
	}
	
	@Override
	protected void initialize(final Application app) {
		
	}
	@Override
	protected void onEnable() {
		
	}
	@Override
	protected void onDisable() {
		
	}
	@Override
	protected void cleanup(final Application app) {
		
	}
}
