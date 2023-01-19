package online.money_daisuki.api.monkey.basegame.spatial;

import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.Spatial.CullHint;

import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.monkey.console.Command;

public final class SetNodeCullHintCommand implements Command {
	private final Node node;
	
	public SetNodeCullHintCommand(final Node node) {
		this.node = Requires.notNull(node, "node == null");
	}
	@Override
	public void execute(final Spatial a, final String[] b, final Runnable done) {
		Requires.lenEqual(b, 2);
		
		switch(b[1]) {
			case("always"):
				setCullHint(CullHint.Always);
			break;
			case("dynamic"):
				setCullHint(CullHint.Dynamic);
			break;
			case("inherit"):
				setCullHint(CullHint.Inherit);
			break;
			case("never"):
				setCullHint(CullHint.Never);
			break;
			default:
				throw new IllegalStateException("Unexpected CullHint");
		}
		done.run();
	}
	private void setCullHint(final CullHint hint) {
		node.setCullHint(hint);
	}
}
