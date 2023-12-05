package online.money_daisuki.api.monkey.basegame.character;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

import online.money_daisuki.api.base.Converter;
import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.io.json.JsonDecoder;
import online.money_daisuki.api.io.json.JsonElement;
import online.money_daisuki.api.io.json.JsonList;
import online.money_daisuki.api.io.json.JsonMap;
import online.money_daisuki.api.monkey.basegame.misc.Utils;
import online.money_daisuki.api.monkey.basegame.variables.CompareOperation;
import online.money_daisuki.api.monkey.basegame.variables.VariableContainer;
import online.money_daisuki.api.monkey.basegame.variables.VariablesManager;

/**
 * AddTerrain file x y z scaleX scaleY scaleZ
 * @author Money Daisuki Online
 *
 */
public final class LoadCharacterCommand {
	private final Converter<? super String, ? extends Spatial> factory;
	private final Node node;
	private final BulletAppState bulletAppState;
	private final VariablesManager vars;
	
	public LoadCharacterCommand(final Converter<? super String, ? extends Spatial> factory,
			final Node node, final BulletAppState bulletAppState, final VariablesManager vars) {
		this.factory = Requires.notNull(factory, "factory == null");
		this.node = Requires.notNull(node, "node == null");
		this.bulletAppState = Requires.notNull(bulletAppState, "bulletAppState == null");
		this.vars = Requires.notNull(vars, "vars == null");
	}
	public Spatial execute(final Spatial a, final String[] b, final Runnable done) {
		Requires.lenEqual(b, 8);
		//final File f = new File(b[1]);
		//Requires.isTrue(Utils.isSubdirectory(f, new File("models")));
		
		try(final Reader in = new FileReader(b[1])) {
			final JsonMap map = new JsonDecoder(in).decode().asMap();
			if(!isConditionFulfilled(map)) {
				return(null);
			}
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
		
		final Spatial spatial = factory.convert(b[1]);
		
		final Vector3f location = new Vector3f(
				Float.parseFloat(b[2]),
				Float.parseFloat(b[3]),
				Float.parseFloat(b[4])
		);
		final Vector3f scale = new Vector3f(
				Float.parseFloat(b[5]),
				Float.parseFloat(b[6]),
				Float.parseFloat(b[7])
		);
		
		final RigidBodyControl rigid = Utils.getControlRecursive(spatial, RigidBodyControl.class);
		if(rigid != null) {
			rigid.setPhysicsLocation(location);
			rigid.setPhysicsScale(scale);
		}
		spatial.setLocalTranslation(location);
		spatial.setLocalScale(scale);
		
		return(spatial);
	}
	private boolean isConditionFulfilled(final JsonMap map) {
		if(!map.containsKey("conditions")) {
			return(true);
		}
		
		final JsonList conditions = map.get("conditions").asList();
		for(final JsonElement e:conditions) {
			final JsonMap condition = e.asMap();
			
			final String type = condition.get("type").asData().asString();
			final String name = condition.get("name").asData().asString();
			
			final boolean def = condition.get("default").asData().asBool();
			if(!vars.containsVariable(type, name)) {
				return(def);
			}
			
			final CompareOperation op = Requires.notNull(CompareOperation.getOp(condition.get("op").asData().asString()), "Unknown op");
			final String value = condition.get("value").asData().asString();
			
			final VariableContainer container = vars.getVariable(type, name);
			if(!container.test(op, value)) {
				return(true);
			}
		}
		return(false);
	}
}
