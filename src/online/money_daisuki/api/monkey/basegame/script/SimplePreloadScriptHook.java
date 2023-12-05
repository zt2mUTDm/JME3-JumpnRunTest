package online.money_daisuki.api.monkey.basegame.script;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Collection;

import com.jme3.app.Application;
import com.jme3.scene.Spatial;

import online.money_daisuki.api.base.DataSink;
import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.misc.mapping.Mapping;

public final class SimplePreloadScriptHook implements DataSink<Mapping<Spatial, Runnable>> {
	private final Collection<String[]> commands;
	private final Application app;
	
	public SimplePreloadScriptHook(final String url, final Application app) {
		try(final Reader in = new FileReader(url)){
			commands = new ScriptFileLoader(in).source();
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
		this.app = Requires.notNull(app, "app == null");
	}
	@Override
	public void sink(final Mapping<Spatial, Runnable> value) {
		if(!commands.isEmpty()) {
			app.getStateManager().getState(CommandExecutorAppState.class).executeSimpleScript(value.getA(),
					commands,
					value.getB(),
					false // TODO
			);
		}
	}
}
