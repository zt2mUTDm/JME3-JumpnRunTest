package online.money_daisuki.api.monkey.basegame.misc;

import java.io.File;
import java.io.IOException;

import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;

import online.money_daisuki.api.base.DataSink;
import online.money_daisuki.api.base.Requires;

public final class Utils {
	private Utils() {
		throw new UnsupportedOperationException("Auto-generated method stub");
	}
	public static boolean isSubdirectory(final File f, final File base) {
		Requires.notNull(f, "f == null");
		Requires.notNull(base, "base == null");
		try {
			if(!base.isDirectory()) {
				throw new IOException("base must be a directory");
			}
			
			final String basePath = base.getAbsoluteFile().getCanonicalPath();
			final String rightBasePath = (!basePath.endsWith("/") ? basePath + "/" : basePath).replace('\\', '/');
			final String thisPath = f.getAbsoluteFile().getCanonicalPath();
			final String rightThisPath = (f.isDirectory() && !thisPath.endsWith("/") ? thisPath + "/" : thisPath).replace('\\', '/');
			return(rightThisPath.startsWith(rightBasePath));
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
	}
	public static boolean isDataSubdirectory(final File f) {
		return(isSubdirectory(f, new File("data")));
	}
	
	public static <T extends Control> T getControlRecursive(final Spatial s, final Class<T> c) {
		if(s instanceof Node) {
			final T con = s.getControl(c);
			if(con != null) {
				return(con);
			}
			for(final Spatial child:((Node) s).getChildren()) {
				final T con2 = getControlRecursive(child, c);
				if(con2 != null) {
					return(con2);
				}
			}
			return(null);
		} else if(s instanceof Geometry) {
			return(s.getControl(c));
		} else {
			throw new IllegalArgumentException("Unknown Spatial type: " + s.getClass().getName());
		}
	}
	
	@SuppressWarnings("unchecked")
	public static <T extends Control> void forEachControl(final Spatial s, final Class<T> c, final DataSink<T> target) {
		for(int i = 0, size = s.getNumControls(); i < size; i++) {
			final Control con = s.getControl(i);
			if(c.isInstance(con)) {
				target.sink((T)con);
			}
		}
		
		if(s instanceof Node) {
			final Node n = (Node) s;
			for(final Spatial s2:n.getChildren()) {
				forEachControl(s2, c, target);
			}
		}
	}
	
	public static Spatial getChildWithName(final Spatial spatial, final String name) {
		if(spatial.getName().equals(name)) {
			return(spatial);
		}
		if(spatial instanceof Node) {
			return(((Node)spatial).getChild(name));
		}
		return(null);
	}
}
