package online.money_daisuki.api.monkey.basegame.character.control;

import java.io.IOException;

import com.jme3.app.Application;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.Spatial.CullHint;
import com.jme3.scene.control.Control;
import com.jme3.util.TempVars;

import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.monkey.basegame.misc.Utils;
import online.money_daisuki.api.monkey.basegame.player.control.NamedEventTriggerControl;

public final class MoveControlledSpatialControl implements Control {
	private Spatial spatial;
	
	private boolean up;
	private boolean right;
	private boolean down;
	private boolean left;
	private boolean wantStrike;
	private boolean wantJump;
	
	private State state;
	
	private float longIdleCounter;
	private boolean inLongIdle;
	
	private boolean inJumpAir;
	
	private final Application app;
	private final float speed;
	
	private boolean enabled;
	private boolean controlEnabled;
	
	private boolean inDoubleJump;
	
	private boolean resetJumpSpeedOnSurface;
	
	private boolean hitted;
	private float untilBlinkCounter;
	private int hittedBlinkCounter;
	
	private final Vector3f tmpVec = new Vector3f();
	
	public MoveControlledSpatialControl(final Application app, final float speed) {
		this.app = Requires.notNull(app, "cam == null");
		this.speed = Requires.greaterThanZero(speed, "speed <= 0");
		
		this.state = State.STAND;
		this.controlEnabled = true;
	}
	
	@Override
	public void update(final float tpf) {
		if(!enabled) {
			return;
		}
		
		if(!hitted) {
			if(triggerEvent("HitPlayer", false)) {
				hitted = true;
				handleHittedNow();
			}
		} else {
			handleHitted(tpf);
		}
		
		triggerEvent("SeePlayer", false);
		
		switch(state) {
			case INIT:
				init(tpf);
			break;
			case STAND:
				stand(tpf);
			break;
			case WALK:
				walk(tpf);
			break;
			case STRIKE_START:
				strikeStart(tpf);
			break;
			case STRIKE_MIDDLE:
				strikeMiddle(tpf);
			break;
			case FREE_FALL:
				freeFall(tpf);
			break;
			case JUMP_START:
				jumpStart(tpf);
			break;
			case JUMP_MIDDLE:
				jumpMiddle(tpf);
			break;
			case JUMP_END:
				jumpEnd(tpf);
			break;
			case STRIKE_IN_AIR:
				strikeInAir(tpf);
			break;
			case STRIKE_IN_AIR_DONE:
				strikeInAirDone(tpf);
			break;
			case WAIT:
			break;
			
			default:
				throw new UnsupportedOperationException("Missing case: " + state);
		}
	}
	
	private void init(final float tpf) {
		final CharControl cc = getUnderlyingControl();
		cc.setMoveVector(Vector3f.ZERO);
		state = State.STAND;
	}
	private void stand(final float tpf) {
		final CharControl cc = getUnderlyingControl();
		
		if(!controlEnabled) {
			wantJump = false;
			wantStrike = false;
			return;
		} else if(wantStrike) {
			state = State.STRIKE_START;
			wantStrike = false;
			inLongIdle = false;
			longIdleCounter = 0f;
		} else if(wantJump) {
			if(!triggerEvent("Main", true)) {
				state = State.JUMP_START;
			}
			wantJump = false;
			inLongIdle = false;
			longIdleCounter = 0f;
		} else if(getPressedDirectionalKeyCombination() >= 0) {
			triggerEvent("Main", false);
			
			//anim.play("RunBase", false);
			cc.playAnimation("Run", false);
			state = State.WALK;
			inLongIdle = false;
			longIdleCounter = 0f;
		} else {
			triggerEvent("Main", false);
			
			cc.setMoveVector(Vector3f.ZERO);
			//cc.setWalkDirection(Vector3f.ZERO);
			
			resetJumpSpeed();
			
			if(!inLongIdle) {
				longIdleCounter+= tpf;
				if(longIdleCounter >= 10) {
					cc.playAnimation("LongIdle", false);
					inLongIdle = true;
				}
			}
		}
	}
	private void walk(final float tpf) {
		final CharControl cc = getUnderlyingControl();
		if(!controlEnabled) {
			cc.setMoveVector(Vector3f.ZERO);
			state = State.STAND;
			cc.playAnimation("Idle", false);
			return;
		};
		
		if(wantStrike) {
			state = State.STRIKE_START;
			wantStrike = false;
			return;
		} else if(wantJump) {
			if(!triggerEvent("Main", true)) {
				state = State.JUMP_START;
			}
			wantJump = false;
			return;
		}
		
		if(!cc.isOnGround() && cc.getCharacter().getLinearVelocity(null).y != 0f) {
			cc.playAnimation("JumpMiddle", false);
			state = State.FREE_FALL;
		} else if(getPressedDirectionalKeyCombination() == -1) {
			triggerEvent("Main", false);
			cc.setMoveVector(Vector3f.ZERO);
			state = State.STAND;
			cc.playAnimation("Idle", false);
		}
		triggerEvent("Main", false);
		updateMoveDirection(true);
		resetJumpSpeed();
	}
	private void strikeStart(final float tpf) {
		state = State.STRIKE_MIDDLE;
		
		final CharControl cc = getUnderlyingControl();
		cc.setMoveVector(Vector3f.ZERO);
		
		cc.playAnimation("Attack", true, new Runnable() {
			@Override
			public void run() {
				cc.playAnimation("Idle", false);
				state = State.STAND;
			}
		});
		
		wantJump = false;
		wantStrike = false;
	}
	private void strikeMiddle(final float tpf) {
		final NamedEventTriggerControl c = spatial.getControl(NamedEventTriggerControl.class);
		c.run("HitEnemy", true);
		
		wantJump = false;
		wantStrike = false;
	}
	private void freeFall(final float tpf) {
		final CharControl cc = getUnderlyingControl();
		
		if(cc.isOnGround()) {
			state = State.JUMP_END;
			
			cc.setMoveVector(Vector3f.ZERO);
			
			cc.playAnimation("JumpEnd", true, new Runnable() {
				@Override
				public void run() {
					cc.playAnimation("Idle", false);
					state = State.STAND;
				}
			});
		}
		
		wantJump = false;
		wantStrike = false;
	}
	private void jumpStart(final float tpf) {
		state = State.JUMP_MIDDLE;
		
		inJumpAir = false;
		
		final CharControl cc = getUnderlyingControl();
		cc.jump();
		
		cc.playAnimation("JumpStart", true, new Runnable() {
			@Override
			public void run() {
				cc.playAnimation("JumpMiddle", false);
			}
		});
		
		updateMoveDirection(true);
		
		wantJump = false;
		wantStrike = false;
	}
	private void jumpMiddle(final float tpf) {
		final CharControl cc = getUnderlyingControl();
		
		if(!inJumpAir && !cc.isOnGround()) {
			inJumpAir = true;
		} else if(inJumpAir && cc.isOnGround()) {
			state = State.JUMP_END;
			
			cc.setMoveVector(Vector3f.ZERO);
			
			resetJumpSpeed();
			
			cc.playAnimation("JumpEnd", true, new Runnable() {
				@Override
				public void run() {
					if(state == State.JUMP_END) {
						cc.playAnimation("Idle", false);
						state = State.STAND;
					}
				}
			});
			inDoubleJump = false;
		} else if(wantStrike && !inDoubleJump) {
			cc.playAnimation("Attack", true);
			
			state = State.STRIKE_IN_AIR;
			inDoubleJump = false;
		} else if(wantJump && !inDoubleJump) {
			final float jumpSpeed = cc.getCharacter().getJumpSpeed();
			cc.getCharacter().setJumpSpeed(jumpSpeed * 0.85f);
			cc.jump();
			cc.getCharacter().setJumpSpeed(jumpSpeed);
			
			inDoubleJump = true;
		} else {
			updateMoveDirection(false);
		}
		
		wantStrike = false;
		wantJump = false;
	}
	private void jumpEnd(final float tpf) {
		if(wantStrike) {
			state = State.STRIKE_START;
			wantStrike = false;
		} else if(wantJump) {
			state = State.JUMP_START;
			wantJump = false;
		}
	}
	private void strikeInAir(final float tpf) {
		final CharControl cc = getUnderlyingControl();
		if(cc.isOnGround()) {
			state = State.JUMP_END;
			
			cc.setMoveVector(Vector3f.ZERO);
			
			cc.playAnimation("JumpEnd", true, new Runnable() {
				@Override
				public void run() {
					if(state == State.JUMP_END) {
						cc.playAnimation("Idle", false);
						state = State.STAND;
					}
				}
			});
		} else {
			longIdleCounter+= tpf;
			if(longIdleCounter > 0.3) {
				longIdleCounter = 0;
				
				final float jumpSpeed = cc.getCharacter().getJumpSpeed();
				cc.getCharacter().setJumpSpeed(jumpSpeed * 0.5f);
				cc.jump();
				cc.getCharacter().setJumpSpeed(jumpSpeed);
				cc.getMoveVector(tmpVec);
				tmpVec.multLocal(-0.50f);
				cc.setMoveVector(tmpVec);
				
				state = State.STRIKE_IN_AIR_DONE;
			}
			triggerEvent("HitEnemy", true);
		}
	}
	private void strikeInAirDone(final float tpf) {
		final CharControl cc = getUnderlyingControl();
		if(cc.isOnGround()) {
			state = State.JUMP_END;
			
			cc.setMoveVector(Vector3f.ZERO);
			
			cc.playAnimation("JumpEnd", true, new Runnable() {
				@Override
				public void run() {
					if(state == State.JUMP_END) {
						cc.playAnimation("Idle", false);
						state = State.STAND;
					}
				}
			});
		}
	}
	
	private boolean triggerEvent(final String name, final boolean b) {
		return(spatial.getControl(NamedEventTriggerControl.class).run(name, b));
	}
	
	private void handleHitted(final float tpf) {
		untilBlinkCounter+= tpf;
		while(untilBlinkCounter >= 0.5f) {
			hittedBlinkCounter++;
			if(hittedBlinkCounter == 12) {
				hitted = false;
				untilBlinkCounter = 0.0f;
				hittedBlinkCounter = 0;
				return;
			}
			
			handleHittedNow();
			untilBlinkCounter-= 0.5f;
		}
	}
	private void handleHittedNow() {
		spatial.setCullHint(spatial.getCullHint() == CullHint.Dynamic ? CullHint.Always : CullHint.Dynamic);
	}
	
	private void updateMoveDirection(final boolean viewAlso) {
		final CharControl cc = getUnderlyingControl();
		
		final TempVars tmp = TempVars.get();
		try {
			final Vector3f moveVector = getMoveVector(tmp.vect1, tmp.quat1);
			if(moveVector == null) {
				cc.setMoveVector(Vector3f.ZERO);
				return;
			}
			
			if(viewAlso) {
				cc.setViewDirection(moveVector);
			}
			cc.setMoveVector(moveVector.mult(speed));
		} finally {
			tmp.release();
		}
	}
	private Vector3f getMoveVector(final Vector3f vec, final Quaternion quad) {
		final Camera cam = app.getCamera();
		vec.set(cam.getDirection().getX(), 0, cam.getDirection().getZ());
		vec.normalizeLocal();
		
		final Quaternion q = getRotation(quad);
		if(q == null) {
			return(null);
		}
		return(q.multLocal(vec));
	}
	private Quaternion getRotation(final Quaternion q) {
		// TODO: Use array
		float angle;
		switch(getPressedDirectionalKeyCombination()) {
			case(-1):
				return(null);
			case(0):
				angle = 0f;
			break;
			case(1):
				angle = (float) -(Math.PI / 4);
			break;
			case(2):
				angle = (float) -(Math.PI / 2);
			break;
			case(3):
				angle = (float) (Math.PI + (Math.PI / 4));
			break;
			case(4):
				angle = (float) Math.PI;
			break;
			case(5):
				angle = (float) (Math.PI - (Math.PI / 4));
			break;
			case(6):
				angle = (float) (Math.PI / 2);
			break;
			case(7):
				angle = (float) (Math.PI / 4);
			break;
			default:
				throw new IllegalStateException();
		}
		return(q.fromAngleNormalAxis(angle, Vector3f.UNIT_Y));
	}
	private int getPressedDirectionalKeyCombination() {
		// Not really nice
		if(up) {
			if(left) {
				return(7);
			} else if(right) {
				return(1);
			} else {
				return(0);
			}
		} else if(right) {
			if(up) {
				return(1);
			} else if(down) {
				return(3);
			} else {
				return(2);
			}
		} else if(down) {
			if(left) {
				return(5);
			} else if(right) {
				return(3);
			} else {
				return(4);
			}
		} else if(left) {
			if(up) {
				return(7);
			} else if(down) {
				return(5);
			} else {
				return(6);
			}
		} else {
			return(-1);
		}
	}
	
	private void resetJumpSpeed() {
		if(resetJumpSpeedOnSurface) {
			getUnderlyingControl().getCharacter().setJumpSpeed(20.0f);
			resetJumpSpeedOnSurface = false;
		}
	}
	
	public void setJumpSpeed(final float jumpSpeed) {
		getUnderlyingControl().getCharacter().setJumpSpeed(100.0f);
	}
	public void resetJumpSpeedOnSurfaceGround() {
		resetJumpSpeedOnSurface = true;
	}
	
	private CharControl getUnderlyingControl() {
		return(Utils.getControlRecursive(spatial, CharControl.class));
	}
	
	public void setUpPressed(final boolean b) {
		this.up = b;
	}
	public void setRightPressed(final boolean b) {
		this.right = b;
	}
	public void setDownPressed(final boolean b) {
		this.down = b;
	}
	public void setLeftPressed(final boolean b) {
		this.left = b;
	}
	public void strike() {
		wantStrike = true;
	}
	public void jump() {
		wantJump = true;
	}
	
	@Override
	public void setSpatial(final Spatial spatial) {
		this.spatial = Requires.notNull(spatial, "spatial == null");
	}
	
	public void setEnabled(final boolean b) {
		enabled = b;
	}
	public void setControlEnabled(final boolean b) {
		controlEnabled = b;
	}
	
	@Override
	public Control cloneForSpatial(final Spatial spatial) {
		return(new MoveControlledSpatialControl(app, speed));
	}
	
	@Override
	public void render(final RenderManager rm, final ViewPort vp) {
		
	}
	
	@Override
	public void write(final JmeExporter ex) throws IOException {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void read(final JmeImporter im) throws IOException {
		throw new UnsupportedOperationException();
	}
	
	private enum State {
		INIT,
		STAND,
		WALK,
		STRIKE_START,
		STRIKE_MIDDLE,
		FREE_FALL,
		JUMP_START,
		JUMP_MIDDLE,
		JUMP_END,
		STRIKE_IN_AIR,
		STRIKE_IN_AIR_DONE,
		WAIT;
	}
}
