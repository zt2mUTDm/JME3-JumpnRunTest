package online.money_daisuki.api.monkey.basegame.text;

import java.io.StringReader;

import com.jme3.app.SimpleApplication;
import com.jme3.input.InputManager;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.InputListener;
import com.jme3.scene.Spatial;

import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.monkey.console.Command;

public final class ShowControlledText2Command implements Command {
	private final ShowTextAppState showText;
	private final InputManager input; 
	private final SimpleApplication app;
	
	public ShowControlledText2Command(final ShowTextAppState showText, final InputManager input, final SimpleApplication app) {
		this.showText = Requires.notNull(showText, "showText == null");
		this.input = Requires.notNull(input, "input == null");
		this.app = Requires.notNull(app, "app == null");
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
		
		showText.setVisible(true);
		showText.showText(new StringReader(sb.toString()));
		
		final boolean[] textShowed = {
				false
		};
		
		final InputListener speedListener = new ActionListener() {
			@Override
			public void onAction(final String name, final boolean isPressed, final float tpf) {
				if(!textShowed[0]) {
					showText.setDoubleSpeed(isPressed);
				}
			}
		};
		final InputListener skipListener = new ActionListener() {
			@Override
			public void onAction(final String name, final boolean isPressed, final float tpf) {
				if(isPressed) {
					if(!textShowed[0]) {
						showText.showAll();
					}
				}
			}
		};
		final InputListener discardListener = new ActionListener() {
			@Override
			public void onAction(final String name, final boolean isPressed, final float tpf) {
				if(textShowed[0] && isPressed) {
					final InputListener thisListener = this;
					app.enqueue(new Runnable() {
						@Override
						public void run() {
							input.removeListener(speedListener);
							input.removeListener(skipListener);
							input.removeListener(thisListener);
							
							showText.setVisible(false);
							showText.clear();
							
							done.run();
						}
					});
				}
			}
		};
		
		
		input.addListener(speedListener, "ControlJump");
		input.addListener(skipListener, "ControlStrike");
		input.addListener(discardListener, "ControlJump");
		input.addListener(discardListener, "ControlStrike");
		showText.addTextShowedListener(new Runnable() {
			@Override
			public void run() {
				textShowed[0] = true;
				showText.removeTextShowedListener(this);
			}
		});
	}
}
