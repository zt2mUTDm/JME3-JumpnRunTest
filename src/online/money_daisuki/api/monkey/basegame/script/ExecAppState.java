package online.money_daisuki.api.monkey.basegame.script;

import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.renderer.RenderManager;

import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.monkey.basegame.misc.MayDoneAppState;

public final class ExecAppState implements MayDoneAppState {
	private final ScriptLineExecutor exec;
	private final Runnable doneCallback;
	
	private boolean initialized;
	private boolean enabled;
	
	private boolean done;
	
	private final boolean looped;
	
	private final float speed;
	private float counter;
	
	public ExecAppState(final ScriptLineExecutor exec, final float speed, final boolean looped, final Runnable doneCallback) {
		this.exec = Requires.notNull(exec, "exec == null");
		this.speed = speed;
		this.looped = looped;
		this.doneCallback = Requires.notNull(doneCallback, "doneCallback == null");
		this.enabled = true;
	}
	
	@Override
	public void update(final float tpf) {
		if(done) {
			return;
		}
		
		counter+=tpf;
		//while(counter > speed) {
			exec.sink(tpf);
			
			if(exec.isDone() && looped) {
				exec.reset();
			}
			counter-= speed;
		//}
		
		if(exec.isDone()) {
			done = true;
			doneCallback.run();
		}
	}
	
	
	@Override
	public boolean isDone() {
		return(done);
	}
	
	@Override
	public void initialize(final AppStateManager stateManager, final Application app) {
		this.initialized = true;
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
