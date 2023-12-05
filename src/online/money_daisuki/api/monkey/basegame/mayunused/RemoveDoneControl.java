package online.money_daisuki.api.monkey.basegame.mayunused;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;

import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.monkey.basegame.misc.MayDoneControl;
import online.money_daisuki.api.utils.NoDataListenerContainer;

public final class RemoveDoneControl extends AbstractControl {
	private final Collection<MayDoneControl> controls;
	private final NoDataListenerContainer allDoneListener;
	
	public RemoveDoneControl() {
		this.controls = new LinkedList<>();
		this.allDoneListener = new NoDataListenerContainer();
	}
	
	public void addControl(final MayDoneControl state) {
		controls.add(Requires.notNull(state, "state == null"));
	}
	public void removeControl(final MayDoneControl state) {
		controls.remove(Requires.notNull(state, "state == null"));
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
	protected void controlUpdate(final float tpf) {
		boolean removed = false;
		
		final Iterator<MayDoneControl> it = controls.iterator();
		while(it.hasNext()) {
			final MayDoneControl c = it.next();
			if(c.isDone()) {
				final Spatial s = c.getSpatial();
				if(s != null) {
					s.removeControl(c);
				}
				it.remove();
				
				removed = true;
			}
		}
		
		if(removed && controls.isEmpty()) {
			allDoneListener.fireListeners();
		}
	}
	@Override
	protected void controlRender(final RenderManager rm, final ViewPort vp) {
		
	}
}
