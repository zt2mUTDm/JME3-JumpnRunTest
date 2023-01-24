package online.money_daisuki.api.monkey.basegame.player;

import com.jme3.scene.Spatial;

import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.base.models.SetableSingleValueModel;
import online.money_daisuki.api.monkey.basegame.character.control.MoveControlledSpatialControl;
import online.money_daisuki.api.monkey.basegame.misc.Utils;
import online.money_daisuki.api.monkey.console.Command;

public final class SetResetPlayerJumpSpeedOnSurfaceCommand implements Command {
	private final SetableSingleValueModel<Spatial> playerContainer;
	
	public SetResetPlayerJumpSpeedOnSurfaceCommand(final SetableSingleValueModel<Spatial> playerContainer) {
		this.playerContainer = Requires.notNull(playerContainer, "playerContainer == null");
	}
	@Override
	public void execute(final Spatial caller, final String[] cmd, final Runnable done) {
		Requires.notNull(caller, "caller == null");
		Requires.containsNotNull(cmd, "cmd contains null");
		Requires.lenEqual(cmd, 1);
		
		if(!playerContainer.isSet()) {
			throw new IllegalStateException("No player created");
		}
		
		final Spatial player = playerContainer.source();
		final MoveControlledSpatialControl control = Utils.getControlRecursive(player, MoveControlledSpatialControl.class);
		control.resetJumpSpeedOnSurfaceGround();
		done.run();
	}
}
