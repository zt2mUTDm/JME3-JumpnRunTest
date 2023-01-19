package online.money_daisuki.api.monkey.basegame.text;

import java.util.ArrayList;
import java.util.List;

import com.jme3.font.BitmapText;

import online.money_daisuki.api.base.DataSink;
import online.money_daisuki.api.base.Requires;

public final class ShowTextAppStateUi implements ShowTextAppStateBackend {
	private final DataSink<Boolean> visibleControl;
	private final List<? extends BitmapText> texts;
	
	private final int maxLineChars;
	
	private int actualTextLine;
	private final StringBuffer actualText;
	
	public ShowTextAppStateUi(final DataSink<Boolean> visibleControl, final List<? extends BitmapText> texts,
			final int maxLineChars) {
		this.visibleControl = Requires.notNull(visibleControl, "visibleControl == null");
		this.texts = Requires.sizeGreaterThan(new ArrayList<>(Requires.notNull(texts, "texts == null")), 1,
				"Need at least 2 Lines to work propably");
		this.maxLineChars = Requires.greaterThanZero(maxLineChars, "maxLineChars <= 0");
		actualTextLine = 0;
		actualText = new StringBuffer();
	}
	
	@Override
	public void addLetter(final char c) {
		if(c == '\n') {
			nextLine();
			return;
		}
		
		if(actualText.length() == maxLineChars) {
			nextLine();
		}
		
		final BitmapText text = texts.get(actualTextLine);
		actualText.append(c);
		text.setText(actualText.toString());
	}
	private void nextLine() {
		final int lineCount = texts.size();
		
		if(actualTextLine == lineCount - 1) {
			for(int i = 1; i < lineCount; i++) {
				final BitmapText prevLine = texts.get(i - 1);
				final BitmapText thisLine = texts.get(i);
				prevLine.setText(thisLine.getText());
			}
			texts.get(lineCount - 1).setText("");
		} else {
			actualTextLine++;
		}
		actualText.setLength(0);
	}
	@Override
	public void clear() {
		for(final BitmapText text:texts) {
			text.setText("");
		}
		actualText.setLength(0);
		actualTextLine = 0;
	}
	@Override
	public void setVisible(final boolean visible) {
		visibleControl.sink(visible);
	}
}
