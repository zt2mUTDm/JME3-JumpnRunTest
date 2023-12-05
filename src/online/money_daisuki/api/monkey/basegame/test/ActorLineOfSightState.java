package online.money_daisuki.api.monkey.basegame.test;

import org.python.core.Py;
import org.python.core.PyObjectDerived;
import org.python.core.PyString;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.math.Quaternion;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.util.TempVars;

import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.monkey.basegame.SceneGraphAppState;

public final class ActorLineOfSightState extends BaseAppState {
	//private static final double TAU = Math.PI * 2;
	
	private final CollisionResults results;
	
	private final PyObjectDerived viewForm;
	private final Spatial viewSpatial;
	private final String viewerSourceName;
	private final PyObjectDerived target;
	
	private final double targetAngle;
	
	private PyString lastRoundSeenContactPoint;
	
	private Spatial viewerSource;
	private SceneGraphAppState scene;
	
	private Ray ray;

	private final double minDistance;
	
	public ActorLineOfSightState(final PyObjectDerived viewForm,
			final PyObjectDerived viewerSpatial, final String viewerSourceName,
			final PyObjectDerived target, final double minDistance,
			final double targetAngle) {
		this.viewForm = Requires.notNull(viewForm, "viewForm == null");
		this.viewSpatial = Py.tojava(Requires.notNull(viewerSpatial, "viewerSpatial == null"), Spatial.class);
		this.viewerSourceName = Requires.notNull(viewerSourceName, "viewerSourceName == null");
		this.target = Requires.notNull(target, "target == null");
		this.minDistance = minDistance;
		this.targetAngle = targetAngle;
		
		this.results = new CollisionResults();
	}
	
	/*private static double na(final double angle) {
		return Math.IEEEremainder(angle, Math.PI * 2);
	}
	
	private static boolean similarAngle(final double alpha, final double beta, final double tolerance) {
		return Math.abs(na(na(alpha) - na(beta))) <= tolerance;
	}*/
	
	@Override
	public void update(final float tpf) {
		// XXX Unoptimized!
		
		//final Spatial target = Py.tojava(this.target, Spatial.class);
		
		final Spatial geom = Py.tojava(target.invoke("getViewReceiver"), Spatial.class);
		if(geom == null) {
			return;
		}
		final TempVars tmp = TempVars.get();
		try {
			final Vector3f viewLocation = tmp.vect1.set(viewerSource.getLocalTranslation());
			final Vector3f targetLocation = tmp.vect2.set(geom.getLocalTranslation());
			
			// Distance
			final float distance = viewLocation.distance(targetLocation);
			if(distance > minDistance) {
				handleNotSeen();
				return;
			}
			
			
			// Angle
			final Vector3f viewDirection = tmp.vect3.set(Vector3f.UNIT_Z);
			final Quaternion viewerRotation = viewerSource.getLocalRotation();
			viewerRotation.multLocal(viewDirection).normalizeLocal();
			final Vector3f targetDirection = tmp.vect4.set(targetLocation).subtractLocal(viewLocation).normalizeLocal();
			
			final double angleBetween = viewDirection.angleBetween(targetDirection);
			if(angleBetween > targetAngle) {
				handleNotSeen();
				return;
			}
			
			
			// No collision
			final Vector3f difference = tmp.vect5.set(targetLocation).subtractLocal(viewLocation);
			
			results.clear();
			ray.setOrigin(viewLocation);
			ray.setDirection(difference);
			scene.getRootNode().collideWith(ray, results);
			
			final CollisionResult closedCollision = results.getClosestCollision();
			if(closedCollision == null) {
				return;
			}
			
			final CollisionResult result = getMatchingCollisionResult(geom);
			if(result == null) {
				handleNotSeen();
				return;
			}
			handleSeen(result);
		} finally {
			tmp.release();
		}
	}
	private void handleSeen(final CollisionResult result) {
		final Geometry geom = result.getGeometry();
		final String geomName = geom.getName();
		final PyString nameObject = new PyString(geomName);
		
		if(lastRoundSeenContactPoint == null) {
			viewForm.invoke("onLineOfSightEnter", target, nameObject);
			lastRoundSeenContactPoint = nameObject;
		}
		viewForm.invoke("onLineOfSight", target, nameObject);
	}
	private void handleNotSeen() {
		if(lastRoundSeenContactPoint != null) {
			viewForm.invoke("onLineOfSightLeave", target, lastRoundSeenContactPoint);
			lastRoundSeenContactPoint = null;
		}
	}
	
	private CollisionResult getMatchingCollisionResult(final Spatial viewReceiver) {
		for(int i = 0, size = results.size(); i < size; i++) {
			final CollisionResult result = results.getCollision(i);
			
			final Geometry geom = result.getGeometry();
			final String geomName = geom.getName();
			
			final Spatial viewer = getNamedSpatialFromSpatial(viewSpatial, geomName);
			if(viewer != null && viewer == geom) {
				continue;
			}
			
			final Spatial receiver = getNamedSpatialFromSpatial(viewReceiver, geomName);
			if(receiver != null && receiver == geom) {
				return(result);
			} else {
				return(null);
			}
		}
		return(null);
	}
	private Spatial getNamedSpatialFromSpatial(final Spatial spatial, final String name) {
		if(spatial.getName().equals(name)) {
			return(spatial);
		} else if(spatial instanceof Node) {
			return(((Node) spatial).getChild(name));
		}
		return(null);
	}
	
	@Override
	protected void initialize(final Application app) {
		viewerSource = getNamedSpatialFromSpatial(viewSpatial, viewerSourceName);
		if(viewerSource == null) {
			throw new IllegalArgumentException("Could not found Spatial " + viewerSourceName + " in " + viewSpatial);
		}
		
		this.scene = app.getStateManager().getState(SceneGraphAppState.class);
		this.ray = new Ray();
	}
	@Override
	protected void onEnable() {
		
	}
	@Override
	protected void onDisable() {
		
	}
	@Override
	protected void cleanup(final Application app) {
		this.ray = null;
		this.viewerSource = null;
		this.lastRoundSeenContactPoint = null;
		
		this.scene = null;
		//this.app = null;
	}
}
