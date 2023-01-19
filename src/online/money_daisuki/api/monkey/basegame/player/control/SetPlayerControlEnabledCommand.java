package online.money_daisuki.api.monkey.basegame.player.control;

import com.jme3.scene.Spatial;

import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.base.models.SetableSingleValueModel;
import online.money_daisuki.api.monkey.basegame.character.control.MoveControlledSpatialControl;
import online.money_daisuki.api.monkey.basegame.misc.Utils;
import online.money_daisuki.api.monkey.console.Command;

public final class SetPlayerControlEnabledCommand implements Command {
	private final SetableSingleValueModel<Spatial> playerContainer;
	
	public SetPlayerControlEnabledCommand(final SetableSingleValueModel<Spatial> playerContainer) {
		this.playerContainer = Requires.notNull(playerContainer, "playerContainer == null");
	}
	@Override
	public void execute(final Spatial a, final String[] b, final Runnable done) {
		Requires.lenEqual(b, 2);
		
		if(!playerContainer.isSet()) {
			throw new IllegalStateException("No player created");
		}
		
		switch(b[1]) {
			case("on"):
				set(true);
			break;
			case("off"):
				set(false);
			break;
			default:
				throw new IllegalArgumentException("Unexpected control enabled: " + b[1]);
		}
		done.run();
	}
	private void set(final boolean b) {
		final Spatial player = playerContainer.source();
		final MoveControlledSpatialControl control = Utils.getControlRecursive(player, MoveControlledSpatialControl.class);
		control.setControlEnabled(b);
	}
}
