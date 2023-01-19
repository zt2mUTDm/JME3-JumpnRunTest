package online.money_daisuki.api.monkey.basegame.character.control;

import java.util.List;

import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.PhysicsSweepTestResult;
import com.jme3.bullet.collision.shapes.ConvexShape;
import com.jme3.bullet.control.GhostControl;
import com.jme3.math.Transform;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

import online.money_daisuki.api.base.DataSource;

public final class CharacterOnGroundSweepTester implements DataSource<Boolean> {
	private boolean initalized;
	
	private GhostControl ghost;
	
	private final ConvexShape collisionShape;
	private final Spatial spatialToAttach;
	private final Vector3f distance;
	private final BulletAppState bullet;
	
	private final Transform tmpTransStart;
	private final Transform tmpTransEnd;
	private final Vector3f tmpVec;
	
	public CharacterOnGroundSweepTester(final ConvexShape collisionShape, final Spatial spatialToAttach,
			final Vector3f distance, final BulletAppState bullet) {
		//null-checks...
		this.collisionShape = collisionShape;
		this.spatialToAttach = spatialToAttach;
		this.distance = new Vector3f(distance);
		this.bullet = bullet;
		
		this.tmpTransStart = new Transform();
		this.tmpTransEnd = new Transform();
		this.tmpVec = new Vector3f();
	}
	@Override
	public Boolean source() {
		if(!initalized) {
			doInit();
			initalized = true;
		}
		
		tmpVec.set(spatialToAttach.getWorldTranslation());
		tmpTransStart.setTranslation(tmpVec);
		tmpVec.addLocal(distance);
		tmpTransEnd.setTranslation(tmpVec);
		
		final List<PhysicsSweepTestResult> results = bullet.getPhysicsSpace().sweepTest(collisionShape, tmpTransStart, tmpTransEnd);
		for(final PhysicsSweepTestResult result:results) {
			if(result.getCollisionObject() != ghost) {
				return(Boolean.TRUE);
			}
		}
		return(Boolean.FALSE);
	}
	private void doInit() {
		ghost = new GhostControl(collisionShape);
		bullet.getPhysicsSpace().add(ghost);
		spatialToAttach.addControl(ghost);
	}
}
