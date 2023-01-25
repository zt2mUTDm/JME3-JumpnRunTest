package online.money_daisuki.api.monkey.console;

import com.jme3.scene.Spatial;

import online.money_daisuki.api.base.DataSink;
import online.money_daisuki.api.base.NullRunnable;
import online.money_daisuki.api.base.Requires;

public final class CommandStringDataSink implements DataSink<String> {
	private final Spatial spatial;
	private final Runnable callback;
	
	private final CommandExecutor exe;
	
	public CommandStringDataSink(final CommandExecutor exe, final Spatial spatial) {
		this(exe, spatial, new NullRunnable());
	}
	public CommandStringDataSink(final CommandExecutor exe, final Spatial spatial, final Runnable callback) {
		this.exe = Requires.notNull(exe, "exe == null");
		this.spatial = Requires.notNull(spatial, "spatial == null");
		this.callback = Requires.notNull(callback, "callback == null");
	}
	@Override
	public void sink(final String value) {
		exe.execute(spatial, value.split(" "), callback);
	}
}
