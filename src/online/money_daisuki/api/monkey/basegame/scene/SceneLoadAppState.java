package online.money_daisuki.api.monkey.basegame.scene;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.util.Collection;
import java.util.LinkedList;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.BaseAppState;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.PhysicsCollisionObject;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;
import com.jme3.util.SkyFactory;
import com.jme3.util.SkyFactory.EnvMapType;
import com.jme3.util.TempVars;

import online.money_daisuki.api.io.json.JsonDecoder;
import online.money_daisuki.api.io.json.JsonElement;
import online.money_daisuki.api.io.json.JsonList;
import online.money_daisuki.api.io.json.JsonMap;
import online.money_daisuki.api.monkey.basegame.SceneGraphAppState;
import online.money_daisuki.api.monkey.basegame.form.Form;
import online.money_daisuki.api.monkey.basegame.form.FormLoadAppState;
import online.money_daisuki.api.monkey.basegame.physobj.PhysicsObject;
import online.money_daisuki.api.monkey.basegame.physobj.PhysicsObjectLoadAppState;
import online.money_daisuki.api.monkey.basegame.py.PythonAppState;

public final class SceneLoadAppState extends BaseAppState {
	private SimpleApplication app;
	
	public void load(final String name) {
		try(final Reader r = new FileReader(new File(name))) {
			final JsonMap map = new JsonDecoder(r).decode().asMap();
			load(map);
		} catch(final Throwable e) {
			throw new RuntimeException("Exception while parsing file " + name, e);
		}
	}
	private void load(final JsonMap map) {
		if(map.containsKey("preScripts")) {
			executeScript(map.get("preScripts").asList());
		}
		
		if(map.containsKey("forms")) {
			loadForms(map.get("forms").asList());
		}
		if(map.containsKey("physicsObjects")) {
			loadPhysicsObject(map.get("physicsObjects").asList());
		}
		
		if(map.containsKey("skies")) {
			loadSkies(map.get("skies").asList());
		}
		
		if(map.containsKey("postScripts")) {
			executeScript(map.get("postScripts").asList());
		}
	}
	public void executeScript(final JsonList list) {
		final PythonAppState py = app.getStateManager().getState(PythonAppState.class);
		
		for(final JsonElement e:list) {
			final JsonMap m = e.asMap();
			final String module = m.get("module").asData().asString();
			final String clazz = m.get("class").asData().asString();
			py.executeScript(module, clazz);
		}
	}
	
	private void loadForms(final JsonList list) {
		final SceneGraphAppState rootState = app.getStateManager().getState(SceneGraphAppState.class);
		final FormLoadAppState formLoad = app.getStateManager().getState(FormLoadAppState.class);
		final BulletAppState bullet = app.getStateManager().getState(BulletAppState.class);
		final PythonAppState py = app.getStateManager().getState(PythonAppState.class);
		
		final Collection<Spatial> toAdd = new LinkedList<>();
		
		final Node root = rootState.getRootNode();
		
		for(final JsonElement e:list) {
			final JsonMap m = e.asMap();
			final String url = m.get("url").asData().asString();
			final Form form = formLoad.load(url);
			
			if(!py.addScript(form)) {
				continue;
			}
			
			final Spatial spatial = form.getPhysicsObject().getSpatial();
			parseTransform(spatial, m);
			
			toAdd.add(spatial);
		}
		
		app.enqueue(new Runnable() {
			@Override
			public void run() {
				for(final Spatial spatial:toAdd) {
					root.attachChild(spatial);
					bullet.getPhysicsSpace().addAll(spatial);
				}
				toAdd.clear();
			}
		});
	}
	private void loadPhysicsObject(final JsonList list) {
		final SceneGraphAppState rootState = app.getStateManager().getState(SceneGraphAppState.class);
		final PhysicsObjectLoadAppState formLoad = app.getStateManager().getState(PhysicsObjectLoadAppState.class);
		final BulletAppState bullet = app.getStateManager().getState(BulletAppState.class);
		
		final Collection<Spatial> toAdd = new LinkedList<>();
		
		final Node root = rootState.getRootNode();
		
		for(final JsonElement e:list) {
			final JsonMap m = e.asMap();
			final String url = m.get("url").asData().asString();
			final PhysicsObject obj = formLoad.load(url);
			
			final Spatial spatial = obj.getSpatial();
			parseTransform(spatial, m);
			
			toAdd.add(spatial);
		}
		
		app.enqueue(new Runnable() {
			@Override
			public void run() {
				for(final Spatial spatial:toAdd) {
					root.attachChild(spatial);
					bullet.getPhysicsSpace().addAll(spatial);
				}
				toAdd.clear();
			}
		});
		
	}
	private void loadSkies(final JsonList list) {
		final SceneGraphAppState rootState = app.getStateManager().getState(SceneGraphAppState.class);

		final Collection<Spatial> toAdd = new LinkedList<>();
		
		final Node root = rootState.getRootNode();
		
		for(final JsonElement e:list) {
			final JsonMap m = e.asMap();
			final String url = m.get("url").asData().asString();
			final EnvMapType type;
			switch(m.get("type").asData().asString()) {
				case("cube"):
					type = EnvMapType.CubeMap;
				break;
				case("sphere"):
					type = EnvMapType.SphereMap;
				break;
				case("equirect"):
					type = EnvMapType.EquirectMap;
				break;
				default:
					throw new IllegalArgumentException("Expect either cube, sphere or equiret.");
			}
			
			final Spatial sky = SkyFactory.createSky(app.getAssetManager(), url, type);
			toAdd.add(sky);
		}
		
		app.enqueue(new Runnable() {
			@Override
			public void run() {
				for(final Spatial spatial:toAdd) {
					root.attachChild(spatial);
				}
				toAdd.clear();
			}
		});
	}
	
	private void parseTransform(final Spatial spatial, final JsonMap m) {
		if(!m.containsKey("transform")) {
			return;
		}
		final JsonMap transformMap = m.get("transform").asMap();
		
		final RigidBodyControl rigid = spatial.getControl(RigidBodyControl.class);
		if(rigid != null) {
			if(rigid.isStatic() || rigid.isDynamic() || !rigid.isKinematicSpatial()) {
				parsePhysicsTransform(spatial, rigid, transformMap);
			} else {
				parseSpatialTransform(spatial, transformMap);
			}
		} else {
			final CharacterControl c = spatial.getControl(CharacterControl.class);
			if(c != null) {
				parseCharacterTransform(spatial, c, transformMap);
			} else {
				parseSpatialTransform(spatial, transformMap);
			}
		}
	}
	
	private void parsePhysicsTransform(final Spatial spatial, final RigidBodyControl rigid, final JsonMap transformMap) {
		final TempVars vars = TempVars.get();
		final Vector3f vec = vars.vect1;
		
		if(transformMap.containsKey("translation")) {
			final JsonList translationList = transformMap.get("translation").asList();
			final float x = translationList.get(0).asData().asNumber().asBigDecimal().floatValue();
			final float y = translationList.get(1).asData().asNumber().asBigDecimal().floatValue();
			final float z = translationList.get(2).asData().asNumber().asBigDecimal().floatValue();
			
			vec.set(x, y, z);
			rigid.setPhysicsLocation(vec);
		}
		
		if(transformMap.containsKey("scale")) {
			final JsonList scaleList = transformMap.get("scale").asList();
			final float x = scaleList.get(0).asData().asNumber().asBigDecimal().floatValue();
			final float y = scaleList.get(1).asData().asNumber().asBigDecimal().floatValue();
			final float z = scaleList.get(2).asData().asNumber().asBigDecimal().floatValue();
			
			vec.set(x, y, z);
			spatial.setLocalScale(vec);
			rigid.setPhysicsScale(vec);
		}
		
		if(transformMap.containsKey("rotation")) {
			final JsonList rotationList = transformMap.get("rotation").asList();
			final float x = rotationList.get(0).asData().asNumber().asBigDecimal().floatValue() * FastMath.DEG_TO_RAD;
			final float y = rotationList.get(1).asData().asNumber().asBigDecimal().floatValue() * FastMath.DEG_TO_RAD;;
			final float z = rotationList.get(2).asData().asNumber().asBigDecimal().floatValue() * FastMath.DEG_TO_RAD;;
			
			final Quaternion q = vars.quat1;
			q.fromAngles(new float[] {
					x,
					y,
					z
			});
			rigid.setPhysicsRotation(q);
		}
		vars.release();
	}
	private void parseSpatialTransform(final Spatial spatial, final JsonMap transformMap) {
		if(transformMap.containsKey("translation")) {
			final JsonList translationList = transformMap.get("translation").asList();
			final float x = translationList.get(0).asData().asNumber().asBigDecimal().floatValue();
			final float y = translationList.get(1).asData().asNumber().asBigDecimal().floatValue();
			final float z = translationList.get(2).asData().asNumber().asBigDecimal().floatValue();
			
			spatial.setLocalTranslation(x, y, z);
		}
		
		if(transformMap.containsKey("scale")) {
			final JsonList scaleList = transformMap.get("scale").asList();
			final float x = scaleList.get(0).asData().asNumber().asBigDecimal().floatValue();
			final float y = scaleList.get(1).asData().asNumber().asBigDecimal().floatValue();
			final float z = scaleList.get(2).asData().asNumber().asBigDecimal().floatValue();
			
			spatial.setLocalScale(x, y, z);
			
			if(x != 1 || y != 1 || z != 1) {
				setShapesScale(spatial, new Vector3f(x, y, z));
			}
		}
		
		if(transformMap.containsKey("rotation")) {
			final JsonList rotationList = transformMap.get("rotation").asList();
			final float x = rotationList.get(0).asData().asNumber().asBigDecimal().floatValue() * FastMath.DEG_TO_RAD;
			final float y = rotationList.get(1).asData().asNumber().asBigDecimal().floatValue() * FastMath.DEG_TO_RAD;;
			final float z = rotationList.get(2).asData().asNumber().asBigDecimal().floatValue() * FastMath.DEG_TO_RAD;;
			
			final Quaternion q = new Quaternion().fromAngles(new float[] {
					x,
					y,
					z
			});
			spatial.setLocalRotation(q);
		}
	}
	private void setShapesScale(final Spatial spatial, final Vector3f scale) {
		for(int i = 0, size = spatial.getNumControls(); i < size; i++) {
			final Control c = spatial.getControl(i);
			if(c instanceof PhysicsCollisionObject) {
				final CollisionShape shape = ((PhysicsCollisionObject) c).getCollisionShape();
				shape.setScale(scale);
			}
		}
		
		if(spatial instanceof Node) {
			final Node cast = (Node) spatial;
			for(final Spatial child:cast.getChildren()) {
				setShapesScale(child, scale);
			}
		}
	}
	private void parseCharacterTransform(final Spatial spatial, final CharacterControl c, final JsonMap transformMap) {
		final TempVars vars = TempVars.get();
		final Vector3f vec = vars.vect1;
		
		if(transformMap.containsKey("translation")) {
			final JsonList translationList = transformMap.get("translation").asList();
			final float x = translationList.get(0).asData().asNumber().asBigDecimal().floatValue();
			final float y = translationList.get(1).asData().asNumber().asBigDecimal().floatValue();
			final float z = translationList.get(2).asData().asNumber().asBigDecimal().floatValue();
			
			vec.set(x, y, z);
			c.setPhysicsLocation(vec);
		}
		
		if(transformMap.containsKey("scale")) {
			final JsonList scaleList = transformMap.get("scale").asList();
			final float x = scaleList.get(0).asData().asNumber().asBigDecimal().floatValue();
			final float y = scaleList.get(1).asData().asNumber().asBigDecimal().floatValue();
			final float z = scaleList.get(2).asData().asNumber().asBigDecimal().floatValue();
			
			vec.set(x, y, z);
			spatial.setLocalScale(vec);
			c.getCharacter().getCollisionShape().setScale(vec);
		}
		
		if(transformMap.containsKey("rotation")) {
			final JsonList rotationList = transformMap.get("rotation").asList();
			final float x = rotationList.get(0).asData().asNumber().asBigDecimal().floatValue() * FastMath.DEG_TO_RAD;
			final float y = rotationList.get(1).asData().asNumber().asBigDecimal().floatValue() * FastMath.DEG_TO_RAD;;
			final float z = rotationList.get(2).asData().asNumber().asBigDecimal().floatValue() * FastMath.DEG_TO_RAD;;
			
			final Quaternion q = vars.quat1;
			q.fromAngles(new float[] {
					x,
					y,
					z
			});
			
			vec.set(Vector3f.UNIT_Z);
			q.multLocal(vec);
			
			c.setViewDirection(vec);
		}
		vars.release();
	}
	
	@Override
	protected void initialize(final Application app) {
		this.app = (SimpleApplication) app;
	}
	@Override
	protected void cleanup(final Application app) {
		this.app = null;
	}
	@Override
	protected void onEnable() {
		
	}
	@Override
	protected void onDisable() {
		
	}
}
