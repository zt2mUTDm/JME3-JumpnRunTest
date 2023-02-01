package online.money_daisuki.api.monkey.basegame.spatial;

import java.io.PrintStream;

import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

import online.money_daisuki.api.base.BiConverter;
import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.io.json.DefaultJsonList;
import online.money_daisuki.api.io.json.DefaultJsonMap;
import online.money_daisuki.api.io.json.JsonMap;
import online.money_daisuki.api.io.json.JsonStringDataElement;
import online.money_daisuki.api.io.json.MutableJsonList;
import online.money_daisuki.api.io.json.MutableJsonMap;
import online.money_daisuki.api.monkey.console.Command;

public final class PrintScenegraphJsonCommand implements Command {
	private final BiConverter<? super String, ? super Spatial, ? extends Spatial> source;
	private final PrintStream out;
	
	public PrintScenegraphJsonCommand(final BiConverter<? super String, ? super Spatial, ? extends Spatial> source,
			final PrintStream out) {
		this.source = Requires.notNull(source, "source == null");
		this.out = Requires.notNull(out, "out == null");
	}
	@Override
	public void execute(final Spatial caller, final String[] cmd, final Runnable done) {
		Requires.notNull(caller, "caller == null");
		Requires.containsNotNull(cmd, "cmd contains null");
		Requires.lenEqual(cmd, 2);
		
		final Spatial start = source.convert(cmd[1], caller);
		final JsonMap target = parseNode(start);
		
		out.println(target.toJsonString());
		done.run();
	}
	private JsonMap parseNode(final Spatial spatial) {
		final MutableJsonMap map = new DefaultJsonMap();
		
		final String name = spatial.getName();
		
		map.put("name", new JsonStringDataElement(name != null ? name : "null"));
		map.put("class", new JsonStringDataElement(spatial.getClass().getName()));
		
		if(spatial instanceof Node) {
			final MutableJsonList childs = new DefaultJsonList();
			for(final Spatial child:((Node) spatial).getChildren()) {
				childs.add(parseNode(child));
			}
			map.put("childs", childs);
		}
		return(map);
	}
}
