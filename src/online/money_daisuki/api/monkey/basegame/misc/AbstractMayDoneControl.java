package online.money_daisuki.api.monkey.basegame.misc;

import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.control.Control;

import online.money_daisuki.api.base.NullRunnable;
import online.money_daisuki.api.base.Requires;

public abstract class AbstractMayDoneControl extends AbstractControl implements MayDone, Control {
	private boolean done;
	private final Runnable doneCallback;
	
	public AbstractMayDoneControl() {
		this(new NullRunnable());
	}
	public AbstractMayDoneControl(final Runnable doneCallback) {
		this.doneCallback = Requires.notNull(doneCallback, "doneCallback == null");
	}
	@Override
	protected void controlUpdate(final float tpf) {
		if(done) {
			return;
		}
		doUpdate(tpf);
	}
	protected abstract void doUpdate(float tpf);
	
	@Override
	protected void controlRender(final RenderManager rm, final ViewPort vp) {
		
	}
	@Override
	public boolean isDone() {
		return(done);
	}
	public void setDone() {
		done = true;
		doneCallback.run();
	}
}
