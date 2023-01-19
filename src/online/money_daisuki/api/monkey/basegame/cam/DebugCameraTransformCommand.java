package online.money_daisuki.api.monkey.basegame.cam;

import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppState;
import com.jme3.font.BitmapText;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

import online.money_daisuki.api.base.DataSink;
import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.base.SetableDataSource;
import online.money_daisuki.api.misc.mapping.Triple;
import online.money_daisuki.api.monkey.console.Command;

public final class DebugCameraTransformCommand implements Command {
	private final Node parent;
	private final SetableDataSource<Spatial> player;
	private final SimpleApplication app;
	
	private BitmapText text;
	private AppState state;
	
	public DebugCameraTransformCommand(final Node parent, final SetableDataSource<Spatial> player, final SimpleApplication app) {
		this.parent = Requires.notNull(parent, "parent == null");
		this.player = Requires.notNull(player, "player == null");
		this.app = Requires.notNull(app, "app == null");
	}
	
	@Override
	public void execute(final Spatial caller, final String[] cmd, final Runnable done) {
		Requires.notNull(caller, "caller == null");
		Requires.containsNotNull(cmd, "cmd contains null");
		Requires.lenEqual(cmd, 1);
		
		if(text != null) {
			parent.detachChild(text);
		}
		if(state != null) {
			app.getStateManager().detach(state);
		}
		text = new BitmapText(app.getAssetManager().loadFont("Interface/Fonts/Default.fnt"));
		text.setLocalTranslation(20, 300, 0);
		parent.attachChild(text);
		
		state = new ReadCameraTransformAppState(new DataSink<Triple<Float, Float, Float>>() {
			@Override
			public void sink(final Triple<Float, Float, Float> value) {
				text.setText("H: " + value.getA() + ", V: " + value.getB() + ", Z: " + value.getC());
			}
		}, player);
		app.getStateManager().attach(state);
	}

}
