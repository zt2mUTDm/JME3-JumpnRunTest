package online.money_daisuki.api.monkey.console;

import java.util.Collection;
import java.util.LinkedList;

import com.jme3.font.BitmapText;
import com.jme3.scene.Spatial;
import com.jme3.scene.Spatial.CullHint;

import online.money_daisuki.api.base.Requires;

public final class ConsoleImpl implements ConsoleSpatial {
	private final Spatial root;
	private final BitmapText inputText;
	private final BitmapText outputText;
	private final int outputTextLineCount;
	
	private final StringBuilder input;
	
	private final Collection<Runnable> consoleShownListener;
	private final Collection<Runnable> consoleHiddenListener;
	
	private boolean visible;
	
	public ConsoleImpl(final Spatial root, final BitmapText inputText, final BitmapText outputText, final int outputTextLineCount) {
		this.root = Requires.notNull(root, "root == null");
		this.inputText = Requires.notNull(inputText, "inputText == null");
		this.outputText = Requires.notNull(outputText, "outputText == null");
		this.outputTextLineCount = Requires.greaterThanZero(outputTextLineCount, "outputTextLineCount <= 0");
		
		this.input = new StringBuilder();
		
		this.consoleShownListener = new LinkedList<>();
		this.consoleHiddenListener = new LinkedList<>();
	}
	
	@Override
	public void appendOutputText(final String s) {
		outputText.setText(outputText.getText() + '\n' + Requires.notNull(s, "s == null"));
		
		final int linesToRemove = outputText.getLineCount() - outputTextLineCount;
		if (linesToRemove > 0) {
			final String oldText = outputText.getText();
			final String[] textAsArray = oldText.split("\r\n|\r|\n");
			
			final StringBuilder sb = new StringBuilder();
			
			for (int i = linesToRemove; i < textAsArray.length; i++) {
				if (!textAsArray[i].equals("") && !textAsArray[i].equals(" ")) {
					sb.append(textAsArray[i]);
					if (i != textAsArray.length - 1) {
						sb.append("\n");
					}
				}
			}
			outputText.setText(sb.toString());
		}
	}
	
	@Override
	public void appendInputText(final String text) {
		input.append(Requires.notNull(text, "text == null"));
		inputText.setText(input.toString());
	}
	
	@Override
	public void inputTextBackspace() {
		final int len = input.length();
		input.setLength(len > 0 ? len - 1 : 0);
		inputText.setText(input.toString());
	}
	
	@Override
	public void setVisible(final boolean visible) {
		if(this.visible != visible) {
			if(visible) {
				root.setCullHint(CullHint.Dynamic);
				fireListener(consoleShownListener);
			} else {
				root.setCullHint(CullHint.Always);
				fireListener(consoleHiddenListener);
			}
			this.visible = visible;
		}
	}
	
	@Override
	public boolean isVisible() {
		return(root.getCullHint() != CullHint.Always);
	}
	
	@Override
	public void setInputText(final String s) {
		input.setLength(0);
		input.append(Requires.notNull(s, "s == null"));
		inputText.setText(s);
	}
	
	@Override
	public String getInputText() {
		return(input.toString());
	}
	
	@Override
	public Spatial getRoot() {
		return (root);
	}
	
	@Override
	public void addConsoleShownListener(final Runnable l) {
		addListener(l, consoleShownListener);
	}
	@Override
	public void addConsoleHiddenListener(final Runnable l) {
		addListener(l, consoleHiddenListener);
	}
	private <T> void addListener(final T l, final Collection<T> target) {
		target.add(Requires.notNull(l, "l == null"));
	}
	
	@Override
	public void removeConsoleShownListener(final Runnable l) {
		removeListener(l, consoleShownListener);
	}
	@Override
	public void removeConsoleHiddenListener(final Runnable l) {
		removeListener(l, consoleHiddenListener);
	}
	private <T> void removeListener(final T l, final Collection<T> target) {
		target.remove(Requires.notNull(l, "l == null"));
	}
	
	private void fireListener(final Collection<Runnable> target) {
		for(final Runnable l:target) {
			l.run();
		}
	}
}
