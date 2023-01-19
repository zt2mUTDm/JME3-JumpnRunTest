package online.money_daisuki.api.monkey.basegame.text;

import com.jme3.scene.Spatial;

import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.monkey.console.Command;

public final class ClearTextCommand implements Command {
	private final ShowTextAppState showText;
	
	public ClearTextCommand(final ShowTextAppState showText) {
		this.showText = Requires.notNull(showText, "showText == null");
	}
	@Override
	public void execute(final Spatial a, final String[] b, final Runnable done) {
		Requires.lenEqual(b, 1);
		showText.clear();
		done.run();
	}
}
