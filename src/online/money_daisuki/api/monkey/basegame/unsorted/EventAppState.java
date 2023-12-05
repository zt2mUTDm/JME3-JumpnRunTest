package online.money_daisuki.api.monkey.basegame.unsorted;

import java.util.List;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.PhysicsCollisionObject;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

import online.money_daisuki.api.base.DataSink;
import online.money_daisuki.api.base.NullRunnable;
import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.base.models.SetableMutableSingleValueModel;
import online.money_daisuki.api.base.models.SetableMutableSingleValueModelImpl;
import online.money_daisuki.api.monkey.basegame.script.CommandExecutorAppState;
import online.money_daisuki.api.monkey.basegame.spatial.TranslateControl;

public final class EventAppState extends BaseAppState {
	private final EventData data;
	private final Node parent;
	
	private List<EventPart> parts;
	
	private int activePart = -1;
	private boolean scriptInvoked;
	
	private Runnable uninstaller;
	
	private BulletAppState bullet;
	private CommandExecutorAppState exe;
	
	private Application app;
	
	public EventAppState(final EventData data, final Node parent) {
		this.data = Requires.notNull(data, "data == null");
		this.parent = new Node();
		parent.attachChild(this.parent);
		
		this.uninstaller = new NullRunnable();
	}
	@Override
	protected void cleanup(final Application app) {
		
	}
	@Override
	protected void initialize(final Application app) {
		parts = data.getParts();
		//this.parent.setLocalTransform(data.getTransform());
		this.app = app;
		this.bullet = app.getStateManager().getState(BulletAppState.class);
		this.exe = app.getStateManager().getState(CommandExecutorAppState.class);
	}
	@Override
	protected void onDisable() {
		
	}
	@Override
	protected void onEnable() {
		
	}
	@Override
	public void update(final float tpf) {
		if(!scriptInvoked) {
			final int newActive = findActivePart();
			if(newActive != -1) {
				if(activePart != newActive) {
					uninstaller.run();
					
					final EventPart part = parts.get(newActive);
					final Spatial character = part.getCharacter();
					character.getControl(TranslateControl.class).relocate(data.getTransform().getTranslation(), true);
					parent.attachChild(character);
					
					bullet.getPhysicsSpace().addAll(character);
					
					final SetableMutableSingleValueModel<DataSink<PhysicsCollisionObject>> eventCallback =
							new SetableMutableSingleValueModelImpl<>();
					final EventReceivementControl eventReceivement = character.getControl(EventReceivementControl.class);
					if(eventReceivement != null) {
						final DataSink<PhysicsCollisionObject> callback = new DataSink<PhysicsCollisionObject>() {
							@Override
							public void sink(final PhysicsCollisionObject value) {
								if(!scriptInvoked) {
									scriptInvoked = true;
									exe.executeSimpleScript(character, part.getScript(), new Runnable() {
										@Override
										public void run() {
											scriptInvoked = false;
										}
									}, true);
								}
							}
						};
						eventReceivement.addEventListener(callback);
						eventCallback.sink(callback);
					}
					
					uninstaller = new Runnable() {
						@Override
						public void run() {
							bullet.getPhysicsSpace().removeAll(character);
							parent.detachChild(character);
							
							if(eventReceivement != null) {
								eventReceivement.removeEventListener(eventCallback.source());
							}
						}
					};
					
					this.activePart = newActive;
				}
			}
		}
	}
	private int findActivePart() {
		for(int i = parts.size() - 1; i >= 0; i--) {
			//final EventPart part = parts.get(i);
			// TODO check
			return(i);
		}
		return(-1);
	}
}
