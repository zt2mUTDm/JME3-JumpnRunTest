package online.money_daisuki.api.monkey.basegame.model;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import com.jme3.app.Application;
import com.jme3.audio.AudioData.DataType;
import com.jme3.audio.AudioNode;
import com.jme3.material.Material;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Cylinder;
import com.jme3.scene.shape.Dome;
import com.jme3.scene.shape.Sphere;
import com.jme3.scene.shape.StripBox;

import online.money_daisuki.api.base.Converter;
import online.money_daisuki.api.base.DataSource;
import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.io.json.JsonDecoder;
import online.money_daisuki.api.io.json.JsonElement;
import online.money_daisuki.api.io.json.JsonList;
import online.money_daisuki.api.io.json.JsonMap;
import online.money_daisuki.api.monkey.basegame.spatial.TranslateControl;

public final class ModelLoader implements DataSource<Spatial> {
	private final String s;
	private final Application app;
	private final Converter<String, Material> matLoader;
	
	public ModelLoader(final String s, final Converter<String, Material> matLoader,
			final Application app) {
		this.s = Requires.notNull(s, "s == null");
		//Requires.isTrue(Utils.isSubdirectory(new File(s), new File("models")));
		this.app = Requires.notNull(app, "app == null");
		this.matLoader = Requires.notNull(matLoader, "matLoader == null");
	}
	@Override
	public Spatial source() {
		try(final Reader r = new FileReader(new File(s))) {
			final JsonMap main = new JsonDecoder(r).decode().asMap();
			return(loadSpatial(main));
		} catch(final IOException e) {
			throw new IllegalArgumentException(e);
		}
	}
	private Spatial loadSpatial(final JsonMap map) {
		final Spatial spatial = loadSpatialType(map);
		spatial.setName(map.containsKey("name") ? map.get("name").asData().asString() : "Spatial");
		parseTranslation(map, spatial);
		parseScale(map, spatial);
		parseRotations(map, spatial);
		
		spatial.addControl(new TranslateControl());
		
		return(spatial);
	}
	private Spatial loadSpatialType(final JsonMap map) {
		final String type = map.get("type").asData().asString();
		switch(type) {
			case("node"):
				return(loadNode(map));
			case("audio"):
				return(loadAudioNode(map));
			case("model"):
				return(loadModel(map));
			case("box"):
				return(loadBox(map));
			case("stripBox"):
				return(loadStripBox(map));
			case("cylinder"):
				return(loadCylinder(map));
			case("sphere"):
				return(loadSphere(map));
			case("dome"):
				return(loadDome(map));
			default:
				throw new IllegalStateException("Unreconizable type: " + type);
		}
	}
	private Node loadNode(final JsonMap map) {
		final Node node = new Node();
		
		if(map.containsKey("childs")) {
			final JsonList children = map.get("childs").asList();
			for(final JsonElement e:children) {
				node.attachChild(loadSpatial(e.asMap()));
			}
		}
		return(node);
	}
	private Spatial loadAudioNode(final JsonMap map) {
		final JsonMap e = map.get("audio").asMap();
		
		final String url = map.get("url").asData().asString();
		final DataType dType = Requires.notNull(DataType.valueOf(map.get("dataType").asData().asString()));
		
		final AudioNode audio = new AudioNode(app.getAssetManager(), url, dType);
		audio.setDirectional(map.get("directional").asData().asBool());
		audio.setPositional(map.get("positional").asData().asBool());
		
		if(map.containsKey("direction")) {
			final JsonList direction = map.get("direction").asList();
			final Vector3f vec = new Vector3f(
					direction.get(0).asData().asNumber().asBigInteger().floatValue(),
					direction.get(1).asData().asNumber().asBigInteger().floatValue(),
					direction.get(2).asData().asNumber().asBigInteger().floatValue()
			);
			audio.setDirection(vec);
		}
		return(audio);
	}
	
	private Spatial loadModel(final JsonMap map) {
		final String url = map.get("url").asData().asString();
		return(app.getAssetManager().loadModel(url));
	}
	
	private Spatial loadBox(final JsonMap map) {
		final JsonList e = map.get("extends").asList();
		
		final float x = e.get(0).asData().asNumber().asBigDecimal().floatValue();
		final float y = e.get(1).asData().asNumber().asBigDecimal().floatValue();
		final float z = e.get(2).asData().asNumber().asBigDecimal().floatValue();
		
		final Mesh mesh = new Box(x,  y, z);
		return(meshToGeometry(map, mesh));
	}
	private Spatial loadStripBox(final JsonMap map) {
		final JsonList e = map.get("extends").asList();
		
		final float x = e.get(0).asData().asNumber().asBigDecimal().floatValue();
		final float y = e.get(1).asData().asNumber().asBigDecimal().floatValue();
		final float z = e.get(2).asData().asNumber().asBigDecimal().floatValue();
		
		final Mesh mesh = new StripBox(x,  y, z);
		return(meshToGeometry(map, mesh));
	}
	private Spatial loadCylinder(final JsonMap map) {
		final int axisSamples = map.get("axisSamples").asData().asNumber().asBigInteger().intValueExact();
		final int radialSamples = map.get("radialSamples").asData().asNumber().asBigInteger().intValueExact();
		final float radius = map.get("radius").asData().asNumber().asBigDecimal().floatValue();
		final float height = map.get("height").asData().asNumber().asBigDecimal().floatValue();
		
		final boolean closed = map.containsKey("closed") ? map.get("closed").asData().asBool() : false;
		final boolean inverted = map.containsKey("inverted") ? map.get("closed").asData().asBool() : false;
		
		final Mesh mesh = new Cylinder(axisSamples, radialSamples, radius, height, closed, inverted);
		return(meshToGeometry(map, mesh));
	}
	private Spatial loadSphere(final JsonMap map) {
		final int zSamples = map.get("zSamples").asData().asNumber().asBigInteger().intValueExact();
		final int radialSamples = map.get("radialSamples").asData().asNumber().asBigInteger().intValueExact();
		final float radius = map.get("radius").asData().asNumber().asBigDecimal().floatValue();
		
		final boolean useEvenSlices = map.containsKey("useEvenSlices") ? map.get("closed").asData().asBool() : false;
		final boolean interior = map.containsKey("interior") ? map.get("closed").asData().asBool() : false;
		
		final Mesh mesh = new Sphere(zSamples, radialSamples, radius, useEvenSlices, interior);
		return(meshToGeometry(map, mesh));
	}
	private Spatial loadDome(final JsonMap map) {
		final int planes = map.get("planes").asData().asNumber().asBigInteger().intValueExact();
		final int radialSamples = map.get("radialSamples").asData().asNumber().asBigInteger().intValueExact();
		final float radius = map.get("radius").asData().asNumber().asBigDecimal().floatValue();
		
		final Mesh mesh = new Dome(planes, radialSamples, radius);
		return(meshToGeometry(map, mesh));
	}
	
	private Spatial meshToGeometry(final JsonMap map, final Mesh mesh) {
		final Material mat = matLoader.convert(map.get("material").asData().asString());
		
		final Geometry geo = new Geometry("", mesh);
		geo.setMaterial(mat);
		return(geo);
	}
	
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
	private void parseScale(final JsonMap map, final Spatial s) {
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
	private void parseRotations(final JsonMap map, final Spatial s) {
		if(map.containsKey("rotation")) {
			final JsonList rotateLists = map.get("rotation").asList();
			
			final float[] angles = new float[] {
					rotateLists.get(0).asData().asNumber().asBigInteger().intValueExact() * FastMath.DEG_TO_RAD,
					rotateLists.get(1).asData().asNumber().asBigInteger().intValueExact() * FastMath.DEG_TO_RAD,
					rotateLists.get(2).asData().asNumber().asBigInteger().intValueExact() * FastMath.DEG_TO_RAD
			};
			
			s.setLocalRotation(new Quaternion(angles));
		}
	}
	private Quaternion parseRotation(final JsonList rotateList) {
		Requires.equal(rotateList.size(), 4, "Rotate needs a floating point numbers and 3 integer numers (-1, 0 or 1)");
		
		final float degree = rotateList.get(0).asData().asNumber().asBigDecimal().floatValue();
		
		final int x = rotateList.get(1).asData().asNumber().asBigInteger().intValueExact();
		final int y = rotateList.get(1).asData().asNumber().asBigInteger().intValueExact();
		final int z = rotateList.get(1).asData().asNumber().asBigInteger().intValueExact();
		
		Requires.isTrue(x == -1 || x == 0 || x == 1, "x axis needs to be -1, 0 or 1");
		Requires.isTrue(y == -1 || y == 0 || y == 1, "y axis needs to be -1, 0 or 1");
		Requires.isTrue(z == -1 || z == 0 || z == 1, "z axis needs to be -1, 0 or 1");
		
		return(new Quaternion().fromAngleNormalAxis(degree * FastMath.DEG_TO_RAD, new Vector3f(x, y, z)));
	}
}
