package online.money_daisuki.api.monkey.basegame.mayunused;

import org.python.core.Py;
import org.python.core.PyObjectDerived;

import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.util.TempVars;

import online.money_daisuki.api.base.Requires;

public final class LookForFormControl extends AbstractControl {
	private static final double TAU = Math.PI * 2;
	
	private final CollisionResults result;
	
	private final Spatial viewSpatial;
	private final PyObjectDerived target;
	
	public LookForFormControl(final PyObjectDerived viewSpatial, final PyObjectDerived target) {
		this.viewSpatial = Py.tojava(Requires.notNull(viewSpatial, "viewSpatial == null"), Spatial.class);
		this.target = Requires.notNull(target, "target == null");
		
		this.result = new CollisionResults();
	}
	
	private static double na(final double angle) {
		return Math.IEEEremainder(angle, Math.PI * 2);
	}
	
	private static boolean similarAngle(final double alpha, final double beta, final double tolerance) {
		return Math.abs(na(na(alpha) - na(beta))) <= tolerance;
	}
	
	@Override
	protected void controlUpdate(final float tpf) {
		// XXX Unoptimized!
		
		//final Spatial target = Py.tojava(this.target, Spatial.class);
		
		final Spatial geom = Py.tojava(target.invoke("getViewReceiver"), Spatial.class);
		if(geom == null) {
			return;
		}
		
		final TempVars tmp = TempVars.get();
		try {
			final Vector3f viewLocation = tmp.vect1.set(viewSpatial.getWorldTranslation());
			final Vector3f targetLocation = tmp.vect2.set(geom.getWorldTranslation());
			final Vector3f difference = tmp.vect3.set(targetLocation).subtractLocal(viewLocation);
			
			
			
			//new Ray(viewLocation, difference).collideWith(geom, result);
			geom.collideWith(new Ray(viewLocation, difference), result);
			
			final CollisionResult closedCollision = result.getClosestCollision();
			if(closedCollision == null) {
				return;
			}
			final Geometry geo = closedCollision.getGeometry();
			if(geom == geo || (geom instanceof Node && ((Node)geom).getChild(geo.getName()) == geo)) {
				System.out.println("Collide with");
			} else {
				System.out.println("Doesn't collide with");
			}
		} finally {
			tmp.release();
		}
	}
	@Override
	protected void controlRender(final RenderManager rm, final ViewPort vp) {
		
	}
}
