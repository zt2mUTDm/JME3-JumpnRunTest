package online.money_daisuki.api.monkey.basegame.unsorted;

import java.util.Collection;

import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.scene.Node;

import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.utils.Installer;
import online.money_daisuki.api.utils.Uninstaller;

public final class SceneInstaller implements Installer, Uninstaller {
	private final SceneData data;
	private final Node parentNode;
	private final Application app;
	
	public SceneInstaller(final SceneData data, final Node parentNode, final Application app) {
		this.data = Requires.notNull(data, "data == null");
		this.parentNode = Requires.notNull(parentNode, "parentNode == null");
		this.app = Requires.notNull(app, "app == null");
	}
	@Override
	public void install() {
 		final Node characterNode = data.getCharacterNode();
		
		final Node parent = new Node();
		
		parent.attachChild(characterNode);
		final AppStateManager state = app.getStateManager();
		final BulletAppState bullet = state.getState(BulletAppState.class);
		if(bullet != null) {
			bullet.getPhysicsSpace().addAll(characterNode);
		}
		
		final Collection<EventData> eventData = data.getEvents();
		for(final EventData data:eventData) {
			state.attach(new EventAppState(data, parent));
		}
		
		parentNode.attachChild(parent);
	}
	@Override
	public void uninstall() {
		throw new UnsupportedOperationException("Auto-generated method stub"); // TODO
	}
}
