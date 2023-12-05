package online.money_daisuki.api.monkey.basegame.debug;

import com.jme3.app.BasicProfilerState;
import com.jme3.app.DetailedProfilerState;
import com.jme3.app.state.AppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.scene.Spatial;

import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.monkey.console.Command;

public final class ProfilerCommand implements Command {
	private final AppStateManager stateManager;
	
	private AppState profiler;
	
	public ProfilerCommand(final AppStateManager stateManager) {
		this.stateManager = Requires.notNull(stateManager, "stateManager == null");
	}
	@Override
	public void execute(final Spatial a, final String[] b, final Runnable done) {
		switch(b[1]) {
			case("basic"):
				setProfiler(new BasicProfilerState(true));
			break;
			case("detail"):
				setProfiler(new DetailedProfilerState());
			break;
			case("off"):
				unsetProfiler();
			break;
			default:
				throw new IllegalArgumentException( );
		}
		done.run();
	}
	private void setProfiler(final AppState p) {
		if(profiler != null) {
			unsetProfiler();
		}
		stateManager.attach(p);
		this.profiler = p;
	}
	private void unsetProfiler() {
		Requires.notNull(profiler, "Profiler is not set");
		stateManager.detach(profiler);
		this.profiler = null;
	}
}
