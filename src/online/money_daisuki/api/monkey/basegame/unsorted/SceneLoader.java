package online.money_daisuki.api.monkey.basegame.unsorted;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.jme3.bullet.collision.PhysicsCollisionObject;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.collision.shapes.CompoundCollisionShape;
import com.jme3.bullet.collision.shapes.ConeCollisionShape;
import com.jme3.bullet.collision.shapes.ConvexShape;
import com.jme3.bullet.collision.shapes.CylinderCollisionShape;
import com.jme3.bullet.collision.shapes.SphereCollisionShape;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Transform;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

import online.money_daisuki.api.base.Converter;
import online.money_daisuki.api.base.DataSource;
import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.io.json.JsonDecoder;
import online.money_daisuki.api.io.json.JsonElement;
import online.money_daisuki.api.io.json.JsonList;
import online.money_daisuki.api.io.json.JsonMap;

public final class SceneLoader implements DataSource<SceneData> {
	private final String url;
	private final Converter<String, Spatial> characterLoader;
	
	public SceneLoader(final String url, final Converter<String, Spatial> characterLoader) {
		this.url = Requires.notNull(url, "url == null");
		this.characterLoader = Requires.notNull(characterLoader, "characterLoader == null");
	}
	@Override
	public SceneData source() {
		try {
			final JsonDecoder decoder = new JsonDecoder(new FileReader(url));
			final JsonMap map = decoder.decode().asMap();
			final Node characterNode = loadCharacters(map.get("characters").asList());
			final Collection<EventData> events = loadEvents(map.get("events").asList());
			return(new SceneDataImpl(characterNode, events));
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	private Node loadCharacters(final JsonList l) {
		final Node node = new Node();
		for(final JsonElement e:l) {
			node.attachChild(loadCharacter(e.asMap()));
		}
		return(node);
	}
	private Spatial loadCharacter(final JsonMap m) {
		final String s = m.get("url").asData().asString();
		
		final Spatial character = characterLoader.convert(s);
		// TODO transform
		return(character); 
	}
	
	private Collection<EventData> loadEvents(final JsonList l) {
		final Collection<EventData> events = new ArrayList<>(l.size());
		for(final JsonElement e:l) {
			events.add(loadEvent(e.asMap()));
		}
		return(events);
	}
	private EventData loadEvent(final JsonMap m) {
		final String name = m.get("name").asData().asString();
		final List<EventPart> parts = loadEventParts(m.get("parts").asList());
		final Transform transform = parseTransform(m);
		return(new EventDataImpl(name, parts, transform));
	}
	private List<EventPart> loadEventParts(final JsonList l) {
		final List<EventPart> data = new ArrayList<>(l.size());
		for(final JsonElement e:l) {
			data.add(loadEventPart(e.asMap()));
		}
		return(data);
	}
	private EventPart loadEventPart(final JsonMap m) {
		final String s = m.get("character").asData().asString();
		
		final Spatial character = characterLoader.convert(s);
		
		final JsonMap triggerMap = m.get("trigger").asMap();
		final String triggerType = triggerMap.get("type").asData().asString();
		if(triggerType.equals("action") || triggerType.equals("touch")) {
			final JsonMap shapeMap = triggerMap.get("shape").asMap();
			final CollisionShape shape = loadShapeType(shapeMap, character);
			
			final EventReceivementGhostControl ergc = new EventReceivementGhostControl(shape);
			
			final int collGroup = (triggerType.equals("action") ? PhysicsCollisionObject.COLLISION_GROUP_02 :
				PhysicsCollisionObject.COLLISION_GROUP_03);
			ergc.setCollisionGroup(collGroup);
			ergc.setCollideWithGroups(collGroup);
			character.addControl(ergc);
		}
		
		final JsonMap scriptMap = m.get("script").asMap();
		final JsonList scriptContentList = scriptMap.get("content").asList();
		
		final Collection<String[]> script = new ArrayList<>(scriptContentList.size());
		for(final JsonElement e:scriptContentList) {
			final JsonList eL = e.asList();
			final int size = eL.size();
			final String[] arr = new String[size];
			for(int i = 0; i < size; i++) {
				arr[i] = eL.get(i).asData().asString();
			}
			script.add(arr);
		}
		return(new EventPartImpl(character, script));
	}
	private Transform parseTransform(final JsonMap m) {
		final JsonMap transMap = m.get("transform").asMap();
		
		final JsonList translations = transMap.get("translation").asList();
		final Vector3f translation = new Vector3f(
				translations.get(0).asData().asNumber().asBigDecimal().floatValue(),
				translations.get(1).asData().asNumber().asBigDecimal().floatValue(),
				translations.get(2).asData().asNumber().asBigDecimal().floatValue()
		);
		
		final JsonList rots = transMap.get("rotation").asList();
		final Quaternion rot = new Quaternion().fromAngles(
				new float[] {
						rots.get(0).asData().asNumber().asBigDecimal().floatValue() * FastMath.DEG_TO_RAD,
						rots.get(1).asData().asNumber().asBigDecimal().floatValue() * FastMath.DEG_TO_RAD,
						rots.get(2).asData().asNumber().asBigDecimal().floatValue() * FastMath.DEG_TO_RAD
				}
		);
		
		final JsonList scales = transMap.get("scale").asList();
		final Vector3f scale = new Vector3f(
				scales.get(0).asData().asNumber().asBigDecimal().floatValue(),
				scales.get(1).asData().asNumber().asBigDecimal().floatValue(),
				scales.get(2).asData().asNumber().asBigDecimal().floatValue()
		);
		
		return(new Transform(translation, rot, scale));
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
	
}
