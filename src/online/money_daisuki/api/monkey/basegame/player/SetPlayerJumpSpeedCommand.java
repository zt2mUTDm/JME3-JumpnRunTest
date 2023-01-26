package online.money_daisuki.api.monkey.basegame.player;

import com.jme3.scene.Spatial;

import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.base.models.SetableSingleValueModel;
import online.money_daisuki.api.monkey.basegame.character.control.CharControl;
import online.money_daisuki.api.monkey.basegame.misc.Utils;
import online.money_daisuki.api.monkey.console.Command;

public final class SetPlayerJumpSpeedCommand implements Command {
	private final SetableSingleValueModel<Spatial> playerContainer;
	
	public SetPlayerJumpSpeedCommand(final SetableSingleValueModel<Spatial> playerContainer) {
		this.playerContainer = Requires.notNull(playerContainer, "playerContainer == null");
	}
	@Override
	public void execute(final Spatial caller, final String[] cmd, final Runnable done) {
		Requires.notNull(caller, "caller == null");
		Requires.containsNotNull(cmd, "cmd contains null");
		Requires.lenEqual(cmd, 2);
		
		if(!playerContainer.isSet()) {
			throw new IllegalStateException("No player created");
		}
		
		final float f = Float.parseFloat(cmd[1]);
		
		final Spatial player = playerContainer.source();
		final CharControl control = Utils.getControlRecursive(player, CharControl.class);
		control.getCharacter().setJumpSpeed(f);
		done.run();
	}
}
