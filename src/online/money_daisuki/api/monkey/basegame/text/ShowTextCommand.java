package online.money_daisuki.api.monkey.basegame.text;

import java.io.StringReader;

import com.jme3.scene.Spatial;

import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.monkey.console.Command;

public final class ShowTextCommand implements Command {
	private final ShowTextAppState showText;
	
	public ShowTextCommand(final ShowTextAppState showText) {
		this.showText = Requires.notNull(showText, "showText == null");
	}
	@Override
	public void execute(final Spatial a, final String[] b, final Runnable done) {
		Requires.lenGreaterThan(b, 1);
		
		final StringBuilder sb = new StringBuilder();
		for(int i = 1, size = b.length, sizeMO = size - 1; i < size; i++) {
			sb.append(b[i]);
			if(i < sizeMO) {
				sb.append(" ");
			}
		}
		showText.showText(new StringReader(sb.toString()));
		showText.addTextShowedListener(new Runnable() {
			@Override
			public void run() {
				showText.removeTextShowedListener(this);
				done.run();
			}
		});
	}
}
