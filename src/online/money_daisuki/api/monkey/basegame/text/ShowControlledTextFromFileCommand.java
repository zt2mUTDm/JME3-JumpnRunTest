package online.money_daisuki.api.monkey.basegame.text;

import java.io.CharArrayReader;
import java.io.CharArrayWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import com.jme3.app.SimpleApplication;
import com.jme3.input.InputManager;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.InputListener;
import com.jme3.scene.Spatial;

import online.money_daisuki.api.base.DataSource;
import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.io.CharacterStreamConcater;
import online.money_daisuki.api.monkey.console.Command;

public final class ShowControlledTextFromFileCommand implements Command {
	private final ShowTextAppState showText;
	private final InputManager input; 
	private final SimpleApplication app;
	private final DataSource<String> languageKeySource;
	
	public ShowControlledTextFromFileCommand(final ShowTextAppState showText, final InputManager input, final SimpleApplication app,
			final DataSource<String> languageKeySource) {
		this.showText = Requires.notNull(showText, "showText == null");
		this.input = Requires.notNull(input, "input == null");
		this.app = Requires.notNull(app, "app == null");
		this.languageKeySource = Requires.notNull(languageKeySource, "languageKeySource == null");
	}
	@Override
	public void execute(final Spatial caller, final String[] cmd, final Runnable done) {
		Requires.notNull(caller, "caller == null");
		Requires.containsNotNull(cmd, "cmd contains null");
		Requires.lenEqual(cmd, 2);
		
		final CharArrayWriter out = new CharArrayWriter();
		try(final Reader in = new FileReader(cmd[1] + "_" + languageKeySource.source() + ".txt")) {
			final Runnable r = new CharacterStreamConcater(in, out);
			r.run();
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
		
		showText.showText(new CharArrayReader(out.toCharArray()));
		
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
