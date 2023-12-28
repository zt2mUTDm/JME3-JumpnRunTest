package online.money_daisuki.api.monkey.basegame.cam;

import com.jme3.renderer.Camera;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Node;
import com.jme3.scene.control.CameraControl;

public final class GameCameraNode extends Node implements GameCamera {
	private final Runnable camFactory;
	
	private CameraNode node;
	
	public GameCameraNode(final String name, final Camera camera) {
		camFactory = new Runnable() {
			@Override
			public void run() {
				GameCameraNode.this.node = new CameraNode(name, camera);
			}
		};
	}
	public GameCameraNode(final String name, final CameraControl control) {
		camFactory = new Runnable() {
			@Override
			public void run() {
				GameCameraNode.this.node = new CameraNode(name, control);
			}
		};
	}
	
	@Override
	public void acquire() {
		camFactory.run();
		attachChild(node);
	}
	
	@Override
	public void setEnabled(final boolean b) {
		assertNode();
		node.setEnabled(b);
	}
	@Override
	public boolean isEnabled() {
		assertNode();
		return(node.isEnabled());
	}
	@Override
	public void dispose() {
		assertNode();
		detachChild(node);
		node = null;
	}
	
	private void assertNode() {
		assert (node != null) : "Cam not aquired.";
	}
}
