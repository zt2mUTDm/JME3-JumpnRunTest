package online.money_daisuki.api.monkey.basegame.script;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;

import online.money_daisuki.api.base.DataSource;
import online.money_daisuki.api.base.Requires;

public final class ScriptFileLoader implements DataSource<Collection<String[]>> {
	private final String url;
	
	public ScriptFileLoader(final String url) {
		this.url = Requires.notNull(url, "url == null");
	}
	@Override
	public Collection<String[]> source() {
		final Collection<String[]> c = new LinkedList<>();
		try(final BufferedReader in = new BufferedReader(new FileReader(url))) {
			for(String line; (line = in.readLine()) != null;) {
				line = line.trim();
				line = line.replace("\\\\", "\\");
				line = line.replace("\\n", String.valueOf('\n'));
				if(line.contains("\\")) {
					throw new RuntimeException(new IOException("Script contains '\\'. (Do you mean '\\\\'?)"));
				}
				if(!line.isEmpty() && !line.startsWith("#")) {
					c.add(line.split(" "));
				}
			}
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
		return(c);
	}
}
