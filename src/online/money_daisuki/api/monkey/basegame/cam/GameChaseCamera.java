package online.money_daisuki.api.monkey.basegame.cam;

import com.jme3.input.ChaseCamera;
import com.jme3.input.InputManager;
import com.jme3.renderer.Camera;
import com.jme3.scene.Spatial;

public final class GameChaseCamera implements GameCamera {
	private final Runnable camFactory;
	
	private Spatial target;
	private InputManager inputManager;
	
	private ChaseCamera cam;
	
	public GameChaseCamera(final Camera cam, final Spatial target) {
		camFactory = new Runnable() {
			@Override
			public void run() {
				GameChaseCamera.this.target = target;
				GameChaseCamera.this.cam = new ChaseCamera(cam);
			}
		};
	}
	public GameChaseCamera(final Camera cam) {
		camFactory = new Runnable() {
			@Override
			public void run() {
				GameChaseCamera.this.cam = new ChaseCamera(cam);
			}
		};
	}
	public GameChaseCamera(final Camera cam, final InputManager inputManager) {
		camFactory = new Runnable() {
			@Override
			public void run() {
				GameChaseCamera.this.inputManager = inputManager;
				GameChaseCamera.this.cam = new ChaseCamera(cam);
			}
		};
	}
	public GameChaseCamera(final Camera cam, final Spatial target, final InputManager inputManager) {
		camFactory = new Runnable() {
			@Override
			public void run() {
				GameChaseCamera.this.target = target;
				GameChaseCamera.this.inputManager = inputManager;
				GameChaseCamera.this.cam = new ChaseCamera(cam);
			}
		};
	}
	
	@Override
	public void acquire() {
		camFactory.run();
		
		if(target != null) {
			target.addControl(cam);
		}
		if(inputManager != null) {
			cam.registerWithInput(inputManager);
		}
	}
	
	@Override
	public void setEnabled(final boolean b) {
		cam.setEnabled(b);
	}
	@Override
	public boolean isEnabled() {
		return(cam.isEnabled());
	}
	@Override
	public void dispose() {
		if(inputManager != null) {
			cam.cleanupWithInput(inputManager);
			inputManager = null;
		}
		if(target != null) {
			target.removeControl(cam);
		}
		cam = null;
	}
	
	public void setMinDistance(final float f) {
		assertCam();
		cam.setMinDistance(f);
	}
	public void setInvertVerticalAxis(final boolean b) {
		assertCam();
		cam.setInvertVerticalAxis(b);
	}
	public void setDragToRotate(final boolean b) {
		assertCam();
		cam.setDragToRotate(b);
	}
	public void setDefaultHorizontalRotation(final float f) {
		assertCam();
		cam.setDefaultHorizontalRotation(f);
	}
	public void setDefaultVerticalRotation(final float f) {
		assertCam();
		cam.setDefaultVerticalRotation(f);
	}
	public void setDefaultDistance(final float f) {
		assertCam();
		cam.setDefaultDistance(f);
	}
	
	private void assertCam() {
		assert (cam != null) : "Cam not aquired.";
	}
}
