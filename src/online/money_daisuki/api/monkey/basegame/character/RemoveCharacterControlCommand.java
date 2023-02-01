package online.money_daisuki.api.monkey.basegame.character;

import com.jme3.scene.Spatial;

import online.money_daisuki.api.base.BiConverter;
import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.monkey.basegame.character.control.CharControl;
import online.money_daisuki.api.monkey.basegame.character.control.CharacterControlAdapter;
import online.money_daisuki.api.monkey.console.Command;

public final class RemoveCharacterControlCommand implements Command {
	private final BiConverter<? super String, ? super Spatial, ? extends Spatial> source;
	
	public RemoveCharacterControlCommand(final BiConverter<? super String, ? super Spatial, ? extends Spatial> source) {
		this.source = Requires.notNull(source, "source == null");
	}
	@Override
	public void execute(final Spatial caller, final String[] cmd, final Runnable done) {
		Requires.notNull(caller, "caller == null");
		Requires.containsNotNull(cmd, "cmd contains null");
		Requires.lenEqual(cmd, 2);
		
		final Spatial spatial = source.convert(cmd[1], caller);
		
		final CharControl cc = spatial.getControl(CharControl.class);
		if(cc != null) {
			spatial.removeControl(CharacterControlAdapter.class);
		}
		done.run();
	}
}
