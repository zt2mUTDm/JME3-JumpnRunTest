package online.money_daisuki.api.monkey.basegame.character;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

import online.money_daisuki.api.base.BiConverter;
import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.monkey.basegame.character.control.CharControl;
import online.money_daisuki.api.monkey.basegame.character.control.CharacterControlAdapter;
import online.money_daisuki.api.monkey.console.Command;

public final class AddCharacterControlCommand implements Command {
	private final BiConverter<? super String, ? super Spatial, ? extends Spatial> source;
	private final SimpleApplication app;
	
	public AddCharacterControlCommand(final BiConverter<? super String, ? super Spatial, ? extends Spatial> source,
			final SimpleApplication app) {
		this.source = Requires.notNull(source, "source == null");
		this.app = Requires.notNull(app, "app == null");
	}
	@Override
	public void execute(final Spatial caller, final String[] cmd, final Runnable done) {
		Requires.notNull(caller, "caller == null");
		Requires.containsNotNull(cmd, "cmd contains null");
		Requires.lenEqual(cmd, 5);
		
		final Spatial spatial = source.convert(cmd[1], caller);
		
		final CharControl cc = spatial.getControl(CharControl.class);
		if(cc != null) {
			throw new IllegalArgumentException("Has already a CharControl");
		}
		
		final float r = Float.parseFloat(cmd[2]);
		final float h = Float.parseFloat(cmd[3]);
		final float stepHeight = Float.parseFloat(cmd[4]);
		
		final CapsuleCollisionShape capsule = new CapsuleCollisionShape(r, h);
		
		final CharacterControl ccon = new CharacterControl(capsule, stepHeight);
		spatial.addControl(new CharacterControlAdapter(ccon, app, new Node(), app.getStateManager().getState(BulletAppState.class)));
		
		done.run();
	}
}
