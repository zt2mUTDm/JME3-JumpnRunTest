package online.money_daisuki.api.monkey.basegame.shapes;

import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.collision.shapes.CompoundCollisionShape;
import com.jme3.bullet.collision.shapes.ConeCollisionShape;
import com.jme3.bullet.collision.shapes.ConvexShape;
import com.jme3.bullet.collision.shapes.CylinderCollisionShape;
import com.jme3.bullet.collision.shapes.SphereCollisionShape;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

import online.money_daisuki.api.base.Converter;
import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.io.json.JsonElement;
import online.money_daisuki.api.io.json.JsonList;
import online.money_daisuki.api.io.json.JsonMap;

public final class CollisionShapeLoaderImpl implements CollisionShapeLoader {
	private final Converter<? super CollisionShape, ? extends CollisionShape> parent;
	
	public CollisionShapeLoaderImpl(final Converter<? super CollisionShape, ? extends CollisionShape> parent) {
		this.parent = Requires.notNull(parent, "parent == null");
	}
	@Override
	public CollisionShape convert(final JsonMap map, final Spatial spatial) {
		final CollisionShape shape = loadShapeType(map, spatial);
		return(parent.convert(shape));
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
