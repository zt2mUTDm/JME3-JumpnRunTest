package online.money_daisuki.api.monkey.basegame.cam;

import java.io.IOException;

import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.input.ChaseCamera;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;

import online.money_daisuki.api.base.DataSource;
import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.monkey.basegame.ControlTemplate;

public final class ChaseCamControl implements Control {
	private Spatial spatial;
	
	private final float moveSpeed = 2.0f;
	private final float zoomSpeed = 20.0f;
	
	private final Vector3f moveVector = new Vector3f();
	
	private boolean up;
	private boolean right;
	private boolean down;
	private boolean left;
	private boolean zoomIn;
	private boolean zoomOut;

	private final DataSource<Boolean> collisionDetection;
	
	public ChaseCamControl(final DataSource<Boolean> collisionDetection) {
		this.collisionDetection = Requires.notNull(collisionDetection, "collisionDetection == null");
	}
	@Override
	public void update(final float tpf) {
		updateMoveVector();
		
		final float x = moveVector.x;
		if(x != 0.0f) {
			final ChaseCamera cam = spatial.getControl(ChaseCamera.class);
			final float prev = cam.getHorizontalRotation();
			final float f = (prev + (tpf * moveSpeed * -x)) % FastMath.TWO_PI;
			cam.setDefaultHorizontalRotation(f);
			
			if(!collisionDetection.source()) {
				cam.setDefaultHorizontalRotation(prev);
			}
		}
		
		final float y = moveVector.y;
		if(y != 0.0f) {
			final ChaseCamera cam = spatial.getControl(ChaseCamera.class);
			float f = (cam.getVerticalRotation() + (tpf * moveSpeed * -y)) % FastMath.TWO_PI;
			if(f < cam.getMinVerticalRotation()) {
				f = cam.getMinVerticalRotation();
			} else if(f > cam.getMaxVerticalRotation()) {
				f = cam.getMaxVerticalRotation();
			}
			cam.setDefaultVerticalRotation(f);
		}
		
		final float z = moveVector.z;
		if(z != 0.0f) {
			final ChaseCamera cam = spatial.getControl(ChaseCamera.class);
			float f = (cam.getDistanceToTarget() + (tpf * zoomSpeed * -z));
			if(f < cam.getMinDistance()) {
				f = cam.getMinDistance();
			} else if(f > cam.getMaxDistance()) {
				f = cam.getMaxDistance();
			}
			cam.setDefaultDistance(f);
		}
	}
	
	private void updateMoveVector() {
		int x = 0;
		if(right) {
			x++;
		}
		if(left) {
			x--;
		}
		
		int y = 0;
		if(up) {
			y++;
		}
		if(down) {
			y--;
		}
		
		int z = 0;
		if(zoomIn) {
			z++;
		}
		if(zoomOut) {
			z--;
		}
		moveVector.set(x, y, z);
	}
	
	public void setUp(final boolean up) {
		this.up = up;
	}
	public void setRight(final boolean right) {
		this.right = right;
	}
	public void setDown(final boolean down) {
		this.down = down;
	}
	public void setLeft(final boolean left) {
		this.left = left;
	}
	public void setZoomIn(final boolean zoomIn) {
		this.zoomIn = zoomIn;
	}
	public void setZoomOut(final boolean zoomOut) {
		this.zoomOut = zoomOut;
	}
	
	@Override
	public Control cloneForSpatial(final Spatial spatial) {
		return(new ControlTemplate());
	}
	
	@Override
	public void setSpatial(final Spatial spatial) {
		this.spatial = spatial;
	}
	
	@Override
	public void render(final RenderManager rm, final ViewPort vp) {
		
	}
	
	@Override
	public void read(final JmeImporter im) throws IOException {
		throw new UnsupportedOperationException("Auto-generated method stub"); // TODO
	}
	@Override
	public void write(final JmeExporter ex) throws IOException {
		throw new UnsupportedOperationException("Auto-generated method stub"); // TODO
	}
}
