package online.money_daisuki.api.monkey.basegame.character.control;

import java.io.IOException;

import com.jme3.app.SimpleApplication;
import com.jme3.bounding.BoundingBox;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.objects.PhysicsCharacter;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;

import online.money_daisuki.api.base.DataSource;
import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.monkey.basegame.character.anim.AnimControl;

public final class CharacterControlAdapter implements CharControl {
	private final CharacterControl control;
	//private final SimpleApplication app;
	private final BoundingBox box;
	private final BulletAppState state;
	
	private final DataSource<Boolean> onGroundTester;
	
	public CharacterControlAdapter(final CharacterControl control, final SimpleApplication app,
			final Node collisionNode, final BulletAppState state) {
		//this.app = app;
		this.state = state;
		this.control = Requires.notNull(control, "control == null;");
		this.box = new BoundingBox();
		
		final Node testNode = new Node();
		testNode.setLocalTranslation(0, -5f, 0);
		collisionNode.attachChild(testNode);
		
		onGroundTester = new CharacterOnGroundContactTester(
				new BoxCollisionShape(0.1f, 0.3f, 0.1f),
				testNode,
				0,
				null,
				state
		);
	}
	
	@Override
	public void setMoveVector(final Vector3f vec) {
		control.setWalkDirection(vec);
	}
	
	@Override
	public Vector3f getMoveVector(final Vector3f vec) {
		return(control.getCharacter().getWalkDirection(vec));
	}
	
	@Override
	public void setViewDirection(final Vector3f direction) {
		control.setViewDirection(direction);
	}
	
	@Override
	public void jump() {
		control.jump();
	}
	
	@Override
	public boolean isOnGround() {
		return(onGroundTester.source().booleanValue());
	}
	
	@Override
	public Vector3f getLinearVelocity(final Vector3f vec) {
		return(control.getCharacter().getLinearVelocity(vec));
	}
	
	@Override
	public void setTranslation(final Vector3f location) {
		control.setPhysicsLocation(location);
	}
	
	@Override
	public Control cloneForSpatial(final Spatial spatial) {
		return(control.cloneForSpatial(spatial));
	}
	
	@Override
	public void setSpatial(final Spatial spatial) {
		control.setSpatial(spatial);
	}
	
	@Override
	public void update(final float tpf) {
		control.update(tpf);
	}
	
	@Override
	public void render(final RenderManager rm, final ViewPort vp) {
		control.render(rm, vp);
	}
	
	@Override
	public void write(final JmeExporter ex) throws IOException {
		control.write(ex);
	}
	
	@Override
	public void read(final JmeImporter im) throws IOException {
		control.read(im);
	}
	
	@Override
	public PhysicsSpace getPhysicsSpace() {
		return(control.getPhysicsSpace());
	}
	
	@Override
	public void setPhysicsSpace(final PhysicsSpace space) {
		control.setPhysicsSpace(space);
	}
	
	@Override
	public boolean isEnabled() {
		return(control.isEnabled());
	}
	
	@Override
	public void setEnabled(final boolean state) {
		control.setEnabled(state);
	}
	
	@Override
	public Spatial getSpatial() {
		return(control.getSpatial());
	}
	
	@Override
	public void playAnimation(final String name, final boolean once) {
		final AnimControl c = control.getSpatial().getControl(AnimControl.class);
		c.play(name, once);
		//player.play(name, once);
	}
	@Override
	public void playAnimation(final String name, final boolean once, final Runnable l) {
		final AnimControl c = control.getSpatial().getControl(AnimControl.class);
		c.play(name, once, l);
		//player.play(name, once, l);
	}
	
	@Override
	public PhysicsCharacter getCharacter() {
		return(control.getCharacter());
	}
}
