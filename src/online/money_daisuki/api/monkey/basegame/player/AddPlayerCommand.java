package online.money_daisuki.api.monkey.basegame.player;

import com.jme3.app.Application;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.input.ChaseCamera;
import com.jme3.input.InputManager;
import com.jme3.input.controls.ActionListener;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

import online.money_daisuki.api.base.BiConverter;
import online.money_daisuki.api.base.Converter;
import online.money_daisuki.api.base.DataSink;
import online.money_daisuki.api.base.DataSource;
import online.money_daisuki.api.base.NullRunnable;
import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.monkey.basegame.cam.ChaseCamControl;
import online.money_daisuki.api.monkey.basegame.character.control.CharControl;
import online.money_daisuki.api.monkey.basegame.character.control.MoveControlledSpatialControl;
import online.money_daisuki.api.monkey.console.Command;

/**
 * Player file x y z scaleX scaleY scaleZ spatial
 * @author Money Daisuki Online
 *
 */
public final class AddPlayerCommand implements Command {
	private final Converter<? super String, ? extends Spatial> factory;
	private final BiConverter<? super String, ? super Spatial, ? extends Node> nodeTarget;
	private final DataSink<Spatial> playerContainer;
	private final Application app;
	
	private Runnable remover;
	
	public AddPlayerCommand(final Converter<? super String, ? extends Spatial> factory,
			final BiConverter<? super String, ? super Spatial, ? extends Node> nodeTarget,
			final DataSink<Spatial> playerContainer, final Application app) {
		this.factory = Requires.notNull(factory, "factory == null");
		this.nodeTarget = Requires.notNull(nodeTarget, "nodeTarget == null");
		this.playerContainer = playerContainer; // TODO
		this.app = Requires.notNull(app, "app == null");
		
		this.remover = new NullRunnable();
	}
	@Override
	public void execute(final Spatial caller, final String[] cmd, final Runnable done) {
		Requires.lenEqual(cmd, 9);
		//final File f = new File(b[1]);
		//Requires.isTrue(Utils.isSubdirectory(f, new File("models")));
		
		final Spatial spatial = factory.convert(cmd[1]);
		
		final Vector3f location = new Vector3f(
				Float.parseFloat(cmd[2]),
				Float.parseFloat(cmd[3]),
				Float.parseFloat(cmd[4])
		);
		spatial.setLocalTranslation(location);
		spatial.setLocalScale(
				Float.parseFloat(cmd[5]),
				Float.parseFloat(cmd[6]),
				Float.parseFloat(cmd[7])
		);
		
		remover.run();
		
		final BulletAppState bullet = Requires.notNull(app, "app == null").getStateManager().getState(BulletAppState.class);
		
		final String targetName = String.valueOf(cmd[8]);
		
		final Node target = nodeTarget.convert(targetName, caller);
		app.enqueue(new Runnable() {
			@Override
			public void run() {
				target.attachChild(spatial);
				
				final CharControl cc = spatial.getControl(CharControl.class);
				cc.setPhysicsLocation(location);
				
				final PhysicsSpace space = bullet.getPhysicsSpace();
				space.addAll(spatial);
				
				final InputManager input = app.getInputManager();
				
				
				final MoveControlledSpatialControl moveControl = new MoveControlledSpatialControl(app, 0.3f);
				moveControl.setEnabled(true); // TODO
				//spatial.addControl(moveControl);
				
				final ActionListener upListener = new ActionListener() {
					@Override
					public void onAction(final String name, final boolean isPressed, final float tpf) {
						moveControl.setUpPressed(isPressed);
					}
				};
				input.addListener(upListener, "ControlMoveUp");
				final ActionListener downListener = new ActionListener() {
					@Override
					public void onAction(final String name, final boolean isPressed, final float tpf) {
						moveControl.setDownPressed(isPressed);
					}
				};
				input.addListener(downListener, "ControlMoveDown");
				final ActionListener leftListener = new ActionListener() {
					@Override
					public void onAction(final String name, final boolean isPressed, final float tpf) {
						moveControl.setLeftPressed(isPressed);
					}
				};
				input.addListener(leftListener, "ControlMoveLeft");
				final ActionListener rightListener = new ActionListener() {
					@Override
					public void onAction(final String name, final boolean isPressed, final float tpf) {
						moveControl.setRightPressed(isPressed);
					}
				};
				input.addListener(rightListener, "ControlMoveRight");
				final ActionListener strikeListener = new ActionListener() {
					@Override
					public void onAction(final String name, final boolean isPressed, final float tpf) {
						if(isPressed) {
							moveControl.strike();
						}
					}
				};
				input.addListener(strikeListener, "ControlStrike");
				final ActionListener jumpListener = new ActionListener() {
					@Override
					public void onAction(final String name, final boolean isPressed, final float tpf) {
						if(isPressed) {
							moveControl.jump();
						}
					}
				};
				input.addListener(jumpListener, "ControlJump");
				
				input.addListener(new ActionListener() {
					@Override
					public void onAction(final String name, final boolean isPressed, final float tpf) {
						
					}
				}, "ControlJump");
				
				
				final ChaseCamera cam = new ChaseCamera(app.getCamera(), spatial, input);
				cam.setMinDistance(7.0f);
				cam.setInvertVerticalAxis(true);
				cam.setDragToRotate(false);
				
				final ChaseCamControl camControl = new ChaseCamControl(new DataSource<Boolean>() {
					@Override
					public Boolean source() {
						return(Boolean.TRUE);
					}
				});
				spatial.addControl(camControl);
				
				final ActionListener camUpListener = new ActionListener() {
					@Override
					public void onAction(final String name, final boolean isPressed, final float tpf) {
						camControl.setUp(isPressed);
					}
				};
				input.addListener(camUpListener, "CamMoveUp");
				final ActionListener camRightListener = new ActionListener() {
					@Override
					public void onAction(final String name, final boolean isPressed, final float tpf) {
						camControl.setRight(isPressed);
					}
				};
				input.addListener(camRightListener, "CamMoveRight");
				final ActionListener camDownListener = new ActionListener() {
					@Override
					public void onAction(final String name, final boolean isPressed, final float tpf) {
						camControl.setDown(isPressed);
					}
				};
				input.addListener(camDownListener, "CamMoveDown");
				final ActionListener camLeftListener = new ActionListener() {
					@Override
					public void onAction(final String name, final boolean isPressed, final float tpf) {
						camControl.setLeft(isPressed);
					}
				};
				input.addListener(camLeftListener, "CamMoveLeft");
				
				final ActionListener camZoomOutListener = new ActionListener() {
					@Override
					public void onAction(final String name, final boolean isPressed, final float tpf) {
						camControl.setZoomIn(isPressed);
					}
				};
				input.addListener(camZoomOutListener, "CamZoomIn");
				final ActionListener camZoomInListener = new ActionListener() {
					@Override
					public void onAction(final String name, final boolean isPressed, final float tpf) {
						camControl.setZoomOut(isPressed);
					}
				};
				input.addListener(camZoomInListener, "CamZoomOut");
				
				remover = new Runnable() {
					@Override
					public void run() {
						target.detachChild(spatial);
						space.removeAll(spatial);
						
						cam.cleanupWithInput(input);
						
						input.removeListener(upListener);
						input.removeListener(downListener);
						input.removeListener(rightListener);
						input.removeListener(leftListener);
						input.removeListener(strikeListener);
						input.removeListener(jumpListener);
					}
				};
				
				done.run();
				
				playerContainer.sink(spatial);
			}
		});
	}
}
