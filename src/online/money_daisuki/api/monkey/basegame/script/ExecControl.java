package online.money_daisuki.api.monkey.basegame.script;

import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;

import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.monkey.basegame.misc.AbstractMayDoneControl;
import online.money_daisuki.api.monkey.basegame.misc.MayDoneControl;

public final class ExecControl extends AbstractMayDoneControl implements MayDoneControl {
	private final ScriptLineExecutor exec;
	
	private final boolean looped;
	
	public ExecControl(final ScriptLineExecutor exec, final boolean looped, final Runnable doneCallback) {
		super(doneCallback);
		this.exec = Requires.notNull(exec, "exec == null");
		this.looped = looped;
		this.enabled = true;
	}
	
	@Override
	protected void doUpdate(final float tpf) {
		exec.sink(tpf);
		
		if(exec.isDone()) {
			if(looped) {
				exec.reset();
			} else {
				setDone();
			}
		}
	}
	@Override
	protected void controlRender(final RenderManager rm, final ViewPort vp) {
		
	}
}
