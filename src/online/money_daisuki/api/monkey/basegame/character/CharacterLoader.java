package online.money_daisuki.api.monkey.basegame.character;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.jme3.anim.AnimComposer;
import com.jme3.anim.SkinningControl;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.PhysicsCollisionObject;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.collision.shapes.CompoundCollisionShape;
import com.jme3.bullet.collision.shapes.ConeCollisionShape;
import com.jme3.bullet.collision.shapes.ConvexShape;
import com.jme3.bullet.collision.shapes.CylinderCollisionShape;
import com.jme3.bullet.collision.shapes.SphereCollisionShape;
import com.jme3.bullet.control.BetterCharacterControl;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.control.GhostControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;

import online.money_daisuki.api.base.Converter;
import online.money_daisuki.api.base.DataSource;
import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.io.json.JsonDecoder;
import online.money_daisuki.api.io.json.JsonElement;
import online.money_daisuki.api.io.json.JsonList;
import online.money_daisuki.api.io.json.JsonMap;
import online.money_daisuki.api.monkey.basegame.ExtendedApplication;
import online.money_daisuki.api.monkey.basegame.RotateControl;
import online.money_daisuki.api.monkey.basegame.character.anim.AnimControl;
import online.money_daisuki.api.monkey.basegame.character.anim.AnimPlayer;
import online.money_daisuki.api.monkey.basegame.character.anim.AnimPlayerImpl;
import online.money_daisuki.api.monkey.basegame.character.anim.NameLinkedAnimPlayer;
import online.money_daisuki.api.monkey.basegame.character.anim.NullAnimPlayer;
import online.money_daisuki.api.monkey.basegame.character.control.CharacterControlAdapter;
import online.money_daisuki.api.monkey.basegame.misc.Utils;
import online.money_daisuki.api.monkey.basegame.player.control.EventReceiver;
import online.money_daisuki.api.monkey.basegame.player.control.EventReceiver.TriggerType;
import online.money_daisuki.api.monkey.basegame.player.control.EventTriggerer;
import online.money_daisuki.api.monkey.basegame.player.control.NamedEventTriggerControl;
import online.money_daisuki.api.monkey.basegame.player.control.OnOffEventReceiver;
import online.money_daisuki.api.monkey.basegame.script.ScriptControl;
import online.money_daisuki.api.monkey.basegame.script.ScriptFileLoader;
import online.money_daisuki.api.monkey.basegame.script.ScriptLineExecutorImpl;

public final class CharacterLoader implements DataSource<Spatial> {
	private final String s;
	private final ExtendedApplication app;
	private final Converter<String, Spatial> modelLoader;
	private final BulletAppState bullet;
	
	private final Map<String, EventTriggerer> trigger;
	
	public CharacterLoader(final String s,
			final Converter<String, Spatial> modelLoader,
			final BulletAppState bullet,
			final ExtendedApplication app) {
		this.s = Requires.notNull(s, "s == null");
		this.modelLoader = Requires.notNull(modelLoader, "modelLoader == null");
		this.bullet = Requires.notNull(bullet, "bullet == null");
		this.app = Requires.notNull(app, "app == null");
		
		this.trigger = new HashMap<>();
	}
	@Override
	public Spatial source() {
		try(final Reader r = new FileReader(new File(s))) {
			final JsonMap main = new JsonDecoder(r).decode().asMap();
			return(loadCharacter(main));
		} catch(final IOException e) {
			throw new IllegalArgumentException(e);
		}
	}
	private Spatial loadCharacter(final JsonMap map) throws IOException {
		final String modelUrl = map.get("model").asData().asString();
		final Spatial spatial = modelLoader.convert(modelUrl);
		
		// Load Animations before control
		loadAnimations(map, spatial);
		loadSkeleton(map, spatial);
		
		loadPhysic(map, spatial);
		
		// Load controls as last
		loadControls(map, spatial);
		return(spatial);
	}
	private void loadControls(final JsonMap map, final Spatial spatial) {
		if(!map.containsKey("controls")) {
			return;
		}
		
		final JsonList controlsMap = map.get("controls").asList();
		for(final JsonElement e:controlsMap) {
			final JsonMap controlMap = e.asMap();
			final String attachName = controlMap.get("attach").asData().asString();
			final Spatial child = spatial.getName().equals(attachName) ? spatial : ((Node)spatial).getChild(attachName);
			Requires.notNull(child, "Model spatial with name " + attachName + " not found");
			child.addControl(loadControl(controlMap, child));
		}
		
		spatial.addControl(new NamedEventTriggerControl(trigger));
	}
	private Control loadControl(final JsonMap map, final Spatial spatial) {
		final String type = map.get("type").asData().asString();
		switch(type) {
			case("BCC"):
				return(loadBetterCharacterControl(map));
			case("CC"):
				return(loadCharacterControl(map, spatial));
			case("rotate"):
				return(loadRotateControl(map, spatial));
			case("spawn"):
				return(loadSpawnControl(map, spatial));
			case("script"):
				return(loadScriptControl(map, spatial));
			default:
				throw new IllegalArgumentException("Unknown type: " + type);
		}
	}
	private Control loadRotateControl(final JsonMap map, final Spatial spatial) {
		final JsonList list = map.get("axises").asList();
		final float[] axises = new float[] {
				list.get(0).asData().asNumber().asBigDecimal().floatValue() * FastMath.DEG_TO_RAD,
				list.get(1).asData().asNumber().asBigDecimal().floatValue() * FastMath.DEG_TO_RAD,
				list.get(2).asData().asNumber().asBigDecimal().floatValue() * FastMath.DEG_TO_RAD
		};
		return(new RotateControl(axises));
	}
	private Control loadSpawnControl(final JsonMap map, final Spatial spatial) {
		final float firstSpawn = map.get("firstspawn").asData().asNumber().asBigDecimal().floatValue();
		final float respawn = map.get("respawn").asData().asNumber().asBigDecimal().floatValue();
		final String characterUrl = map.get("character").asData().asString();
		final Vector3f scale = parseScale(map);
		return(new SpawnControl(firstSpawn, respawn, characterUrl, scale, app));
	}
	private Control loadScriptControl(final JsonMap map, final Spatial spatial) {
		final String url = map.get("url").asData().asString();
		
		final Collection<String[]> commands = new ScriptFileLoader(url).source();
		return(new ScriptControl(new ScriptLineExecutorImpl(commands, spatial, app), 0.1f, true));
	}
	
	private void loadAnimations(final JsonMap map, final Spatial spatial) {
		spatial.addControl(new AnimControl(loadPlayer(map, spatial)));
	}
	private AnimPlayer loadPlayer(final JsonMap map, final Spatial spatial) {
		final AnimComposer control = Utils.getControlRecursive(spatial, AnimComposer.class); //TODO
		if(control == null) {
			return(new NullAnimPlayer());
		}
		
		final Map<String, String> animationNames = new HashMap<>();
		if(map.containsKey("animations")) {
			for(final Entry<String, JsonElement> e:map.get("animations").asMap().entrySet()) {
				animationNames.put(e.getKey(), e.getValue().asData().asString());
			}
		}
		return(new NameLinkedAnimPlayer(new AnimPlayerImpl(control), animationNames));
	}
	private void loadSkeleton(final JsonMap map, final Spatial spatial) {
		final SkinningControl control = Utils.getControlRecursive(spatial, SkinningControl.class); //TODO
		if(control == null) {
			return;
		}
		
		if(!map.containsKey("skinning")) {
			return;
		}
		
		final JsonMap skinningMap = map.get("skinning").asMap();
		if(skinningMap.containsKey("attachments")) {
			final JsonList attachmentsList = skinningMap.get("attachments").asList();
			for(final JsonElement e:attachmentsList) {
				final JsonMap attachmentList = e.asMap();
				for(final Entry<String, JsonElement> e2:attachmentList.entrySet()) {
					final String joint = e2.getKey();
					final JsonMap attachmentSetting = e2.getValue().asMap();
					
					//loadSubspatial(skinningMap);
					final String url = attachmentSetting.get("url").asData().asString();
					final Spatial attachmentModel = modelLoader.convert(url);
					
					parseTranslation(attachmentSetting, attachmentModel);
					parseScaleAndSet(attachmentSetting, attachmentModel);
					parseRotations(attachmentSetting, attachmentModel);
					control.getAttachmentsNode(joint).attachChild(attachmentModel);
				}
			}
		}
	}
	
	private void loadPhysic(final JsonMap map, final Spatial spatial) {
		if(!map.containsKey("physic")) {
			return;
		}
		
		final JsonMap physicMap = map.get("physic").asMap();
		loadShapes(physicMap, spatial);
	}
	private void loadShapes(final JsonMap map, final Spatial spatial) {
		if(!map.containsKey("shapes")) {
			return;
		}
		
		for(final JsonElement e:map.get("shapes").asList()) {
			loadShape(e.asMap(), spatial);
		}
	}
	private void loadShape(final JsonMap map, final Spatial spatial) {
		final CollisionShape shape = loadShapeType(map, spatial);
		
		final String attachName = map.get("attach").asData().asString();
		parsePurpose(map, spatial, shape, attachName);
	}
	private CollisionShape loadShapeType(final JsonMap map, final Spatial spatial) {
		final String type = map.get("type").asData().asString();
		switch(type) {
			case("Box"):
				return(loadBoxShape(map));
			case("Capsule"):
				return(loadCapsuleShape(map));
			case("Cone"):
				return(loadConeShape(map));
			case("Sphere"):
				return(loadSphereShape(map));
			case("Cylinder"):
				return(loadCylinderShape(map));
			case("Compound"):
				return(loadCompoundShape(map, spatial));
			case("Mesh"):
				return(loadMeshShape(map, spatial));
			default:
				throw new IllegalArgumentException("Unknown shape type: " + type);
		}
	}
	private ConvexShape loadBoxShape(final JsonMap map) {
		final JsonList hExtends = map.get("extends").asList();
		Requires.equal(hExtends.size(), 3, "extends of Box shape need 3 elements");
		
		final Vector3f vec = new Vector3f(
				hExtends.get(0).asData().asNumber().asBigDecimal().floatValue(),
				hExtends.get(1).asData().asNumber().asBigDecimal().floatValue(),
				hExtends.get(2).asData().asNumber().asBigDecimal().floatValue()
		);
		
		return(new BoxCollisionShape(vec));
	}
	private ConvexShape loadCapsuleShape(final JsonMap map) {
		final float radius = map.get("radius").asData().asNumber().asBigDecimal().floatValue();
		final float height = map.get("height").asData().asNumber().asBigDecimal().floatValue();
		
		return(new CapsuleCollisionShape(radius, height));
	}
	private ConvexShape loadConeShape(final JsonMap map) {
		final float radius = map.get("radius").asData().asNumber().asBigDecimal().floatValue();
		final float height = map.get("height").asData().asNumber().asBigDecimal().floatValue();
		final int axis = map.containsKey("axis") ? map.get("axis").asData().asNumber().asBigInteger().intValueExact() : 1;
		return(new ConeCollisionShape(radius, height, axis));
	}
	private ConvexShape loadSphereShape(final JsonMap map) {
		final float radius = map.get("radius").asData().asNumber().asBigDecimal().floatValue();
		return(new SphereCollisionShape(radius));
	}
	private ConvexShape loadCylinderShape(final JsonMap map) {
		final float radius = map.get("radius").asData().asNumber().asBigDecimal().floatValue();
		final float height = map.get("height").asData().asNumber().asBigDecimal().floatValue();
		final int axis = map.containsKey("axis") ? map.get("axis").asData().asNumber().asBigInteger().intValueExact() : 1;
		return(new CylinderCollisionShape(radius, height, axis));
	}
	private CollisionShape loadCompoundShape(final JsonMap map, final Spatial spatial) {
		final CompoundCollisionShape cs = new CompoundCollisionShape();
		final JsonList childs = map.get("childs").asList();
		for(final JsonElement e:childs) {
			final JsonMap childMap = e.asMap();
			final CollisionShape childShape = loadShapeType(childMap, spatial);
			
			float x = 0;
			float y = 0;
			float z = 0;
			
			if(childMap.containsKey("translation")) {
				final JsonList translationList = childMap.get("translation").asList();
				Requires.equal(translationList.size(), 3, "Translation needs 3 floating point numbers");
				
				x = translationList.get(0).asData().asNumber().asBigDecimal().floatValue();
				y = translationList.get(1).asData().asNumber().asBigDecimal().floatValue();
				z = translationList.get(2).asData().asNumber().asBigDecimal().floatValue();
			}
			cs.addChildShape(childShape, x, y, z);
		}
		
		return(cs);
	}
	private CollisionShape loadMeshShape(final JsonMap map, final Spatial spatial) {
		return(CollisionShapeFactory.createMeshShape(spatial));
	}
	
	private void parsePurpose(final JsonMap map, final Spatial spatial, final CollisionShape shape, final String attachName) {
		if(!map.containsKey("purpose")) {
			return;
		}
		
		final String purpose = map.get("purpose").asData().asString();
		switch(purpose) {
			case("rigid"):
				loadRigidPurpose(map, shape, spatial, attachName);
			break;
			case("ghost"):
				loadGhostPurpose(map, shape, spatial, attachName);
			break;
			case("eventTrigger"):
				loadEventTriggerPurpose(map, shape, spatial, attachName);
			break;
			case("eventReceive"):
				loadEventReceivePurpose(map, shape, spatial, attachName);
			break;
			case("onOffEventReceive"):
				loadOnOffEventReceivePurpose(map, shape, spatial, attachName);
			break;
			default:
				throw new IllegalArgumentException("Unknown purpose: " + purpose);
		}
	}
	private void loadGhostPurpose(final JsonMap map, final CollisionShape shape, final Spatial spatial, final String attachName) {
		final GhostControl c = new GhostControl(shape);
		loadPhysicalCollisionGroup(map, c);
		Requires.notNull(Utils.getChildWithName(spatial, attachName), "Attach point " + attachName + " not found.").addControl(c);
	}
	private void loadRigidPurpose(final JsonMap map, final CollisionShape shape, final Spatial spatial, final String attachName) {
		final float mass = map.get("mass").asData().asNumber().asBigDecimal().floatValue();
		
		final RigidBodyControl c = new RigidBodyControl(shape, mass);
		loadPhysicalCollisionGroup(map, c);
		Utils.getChildWithName(spatial, attachName).addControl(c);
	}
	private void loadEventTriggerPurpose(final JsonMap map, final CollisionShape shape, final Spatial spatial, final String attachName) {
		final EventTriggerer c = new EventTriggerer(shape, bullet);
		loadPhysicalCollisionGroup(map, c);
		Utils.getChildWithName(spatial, attachName).addControl(c);
		
		if(map.containsKey("eventName")) {
			trigger.put(map.get("eventName").asData().asString(), c);
		}
	}
	private void loadEventReceivePurpose(final JsonMap map, final CollisionShape shape, final Spatial spatial, final String attachName) {
		final String simpleScript = map.get("simpleScript").asData().asString();
		
		final GhostControl c = new EventReceiver(shape, spatial, parseEventReceiveTriggerType(map), simpleScript, app);
		loadPhysicalCollisionGroup(map, c);
		
		final Spatial attachSpatial = Utils.getChildWithName(spatial, attachName);
		Requires.notNull(attachSpatial, "Node to attach shape not found");
		attachSpatial.addControl(c);
	}
	private void loadOnOffEventReceivePurpose(final JsonMap map, final CollisionShape shape, final Spatial spatial, final String attachName) {
		final JsonList scriptsJson = map.get("scripts").asList();
		Requires.equal(scriptsJson.size(), 2);
		
		final String[] scripts = {
				scriptsJson.get(0).asData().asString(),
				scriptsJson.get(1).asData().asString()
		};
		
		final GhostControl c = new OnOffEventReceiver(shape, spatial, scripts, app);
		loadPhysicalCollisionGroup(map, c);
		
		final Spatial attachSpatial = Utils.getChildWithName(spatial, attachName);
		Requires.notNull(attachSpatial, "Node to attach shape not found");
		attachSpatial.addControl(c);
	}
	
	private TriggerType parseEventReceiveTriggerType(final JsonMap map) {
		final String triggerType = map.get("trigger").asData().asString();
		switch(triggerType) {
			case("shot"):
				return(TriggerType.SHOT);
			case("touch"):
				return(TriggerType.TOUCH);
			default:
				throw new IllegalArgumentException("Unknown trigger type: " + triggerType);
		}
	}
	
	private void loadPhysicalCollisionGroup(final JsonMap map, final PhysicsCollisionObject obj) {
		if(map.containsKey("collisionGroup")) {
			final int collisionGroupShift = map.get("collisionGroup").asData().asNumber().asBigInteger().intValueExact();
			final int collisionGroupMask = (1 << collisionGroupShift);
			obj.setCollisionGroup(collisionGroupMask);
			obj.setCollideWithGroups(collisionGroupMask);
		}
	}
	
	private BetterCharacterControl loadBetterCharacterControl(final JsonMap map) {
		final float r = (float) map.get("radius").asData().asNumber().asFloat();
		final float h = (float) map.get("height").asData().asNumber().asFloat();
		final float m = (float) map.get("mass").asData().asNumber().asFloat();
		
		final BetterCharacterControl bcc = new BetterCharacterControl(r, h, m);
		bcc.setJumpForce(Vector3f.ZERO);
		if(map.containsKey("gravity")) {
			final JsonList list = map.get("gravity").asList();
			
			final float x = (float) list.get(0).asData().asNumber().asFloat();
			final float y = (float) list.get(1).asData().asNumber().asFloat();
			final float z = (float) list.get(2).asData().asNumber().asFloat();
			
			//bcc.setGravity(new Vector3f(x, y, z));
		}
		return(bcc);
	}
	private Control loadCharacterControl(final JsonMap map, final Spatial spatial) {
		final float r = (float) map.get("radius").asData().asNumber().asFloat();
		final float h = (float) map.get("height").asData().asNumber().asFloat();
		final float m = (float) map.get("mass").asData().asNumber().asFloat();
		
		final CapsuleCollisionShape capsule = new CapsuleCollisionShape(r, h);
		final CharacterControl cc = new CharacterControl(capsule, 0.6f);
		//bcc.setJumpForce(Vector3f.ZERO);
		if(map.containsKey("gravity")) {
			final JsonList list = map.get("gravity").asList();
			
			final float x = (float) list.get(0).asData().asNumber().asFloat();
			final float y = (float) list.get(1).asData().asNumber().asFloat();
			final float z = (float) list.get(2).asData().asNumber().asFloat();
			
			//bcc.setGravity(new Vector3f(x, y, z));
		}
		cc.setGravity(50f);
		cc.setJumpSpeed(20f);
		return(new CharacterControlAdapter(cc, app, (Node)spatial, bullet));
	}
	/*private Control loadJCharacterControl(final JsonMap map, final Spatial spatial) {
		final float r = (float) map.get("radius").asData().asNumber().asFloat();
		final float h = (float) map.get("height").asData().asNumber().asFloat();
		final float m = (float) map.get("mass").asData().asNumber().asFloat();
		
		final CapsuleCollisionShape capsule = new CapsuleCollisionShape(r, h);
		final RigidBodyControl rigid = new RigidBodyControl(capsule);
		bulletAppState.getPhysicsSpace().add(rigid);
		final JCharacterControl cc = new JCharacterControl(rigid/*, 0.6f*//*);
		//bcc.setJumpForce(Vector3f.ZERO);
		/*if(map.containsKey("gravity")) {
			final JsonList list = map.get("gravity").asList();
			
			final float x = (float) list.get(0).asData().asNumber().asFloat();
			final float y = (float) list.get(1).asData().asNumber().asFloat();
			final float z = (float) list.get(2).asData().asNumber().asFloat();
			
			//bcc.setGravity(new Vector3f(x, y, z));
		}
		//cc.setGravity(50f);
		//cc.setJumpSpeed(20f);
		return(cc);
	}*/
	
	private void parseTranslation(final JsonMap map, final Spatial s) {
		if(map.containsKey("translation")) {
			final JsonList translationList = map.get("translation").asList();
			Requires.equal(translationList.size(), 3, "Translation needs 3 floating point numbers");
			
			s.setLocalTranslation(
					translationList.get(0).asData().asNumber().asBigDecimal().floatValue(),
					translationList.get(1).asData().asNumber().asBigDecimal().floatValue(),
					translationList.get(2).asData().asNumber().asBigDecimal().floatValue()
			);
		}
	}
	private void parseScaleAndSet(final JsonMap map, final Spatial s) {
		if(map.containsKey("scale")) {
			final JsonList scaleList = map.get("scale").asList();
			Requires.equal(scaleList.size(), 3, "Scale needs 3 floating point numbers");
			
			s.setLocalScale(
					scaleList.get(0).asData().asNumber().asBigDecimal().floatValue(),
					scaleList.get(1).asData().asNumber().asBigDecimal().floatValue(),
					scaleList.get(2).asData().asNumber().asBigDecimal().floatValue()
			);
		}
	}
	private Vector3f parseScale(final JsonMap map) {
		if(!map.containsKey("scale")) {
			return(Vector3f.ZERO);
		}
		
		final JsonList scaleList = map.get("scale").asList();
		Requires.equal(scaleList.size(), 3, "Scale needs 3 floating point numbers");
			
		return(new Vector3f(
					scaleList.get(0).asData().asNumber().asBigDecimal().floatValue(),
					scaleList.get(1).asData().asNumber().asBigDecimal().floatValue(),
					scaleList.get(2).asData().asNumber().asBigDecimal().floatValue()
		));
	}
	private void parseRotations(final JsonMap map, final Spatial s) {
		if(map.containsKey("rotate")) {
			final JsonList rotateLists = map.get("rotate").asList();
			for(final JsonElement e:rotateLists) {
				s.rotate(parseRotation(e.asList()));
			}
		}
	}
	private Quaternion parseRotation(final JsonList rotateList) {
		Requires.equal(rotateList.size(), 4, "Rotate needs a floating point numbers and 3 integer numers (-1, 0 or 1)");
		
		final float degree = rotateList.get(0).asData().asNumber().asBigDecimal().floatValue();
		
		final int x = rotateList.get(1).asData().asNumber().asBigInteger().intValueExact();
		final int y = rotateList.get(2).asData().asNumber().asBigInteger().intValueExact();
		final int z = rotateList.get(3).asData().asNumber().asBigInteger().intValueExact();
		
		Requires.isTrue(x == -1 || x == 0 || x == 1, "x axis needs to be -1, 0 or 1");
		Requires.isTrue(y == -1 || y == 0 || y == 1, "y axis needs to be -1, 0 or 1");
		Requires.isTrue(z == -1 || z == 0 || z == 1, "z axis needs to be -1, 0 or 1");
		
		return(new Quaternion().fromAngleNormalAxis(degree * FastMath.DEG_TO_RAD, new Vector3f(x, y, z)));
	}
}
