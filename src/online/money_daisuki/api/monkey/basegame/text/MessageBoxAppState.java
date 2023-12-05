package online.money_daisuki.api.monkey.basegame.text;

import java.util.List;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.BaseAppState;
import com.jme3.font.BitmapText;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial.CullHint;

public final class MessageBoxAppState extends BaseAppState {
	private Node uiNode;
	
	private boolean visible;
	
	private List<? extends BitmapText> texts;
	
	private final int maxLineChars = 100;
	
	private int actualTextLine;
	private final StringBuilder actualText;
	
	public MessageBoxAppState() {
		actualText = new StringBuilder();
	}
	@Override
	protected void initialize(final Application app) {
		final SimpleApplication sapp = (SimpleApplication) app;
		
		final ShowTextUiBuilder builder = new ShowTextUiBuilder(app.getAssetManager());
		builder.setBackgroundWidth(app.getGuiViewPort().getCamera().getWidth());
		
		uiNode = builder.source();
		uiNode.setLocalTranslation(0, 0, 0);
		uiNode.setCullHint(CullHint.Always);
		sapp.getGuiNode().attachChild(uiNode);
		
		texts = new ShowTextNodeToLinesConverter().convert(uiNode);
	}
	@Override
	protected void cleanup(final Application app) {
		final SimpleApplication sapp = (SimpleApplication) app;
		
		sapp.getGuiNode().detachChild(uiNode);
		uiNode = null;
	}
	@Override
	protected void onEnable() {
		
	}
	@Override
	protected void onDisable() {
		
	}
	
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
	public void clear() {
		for(final BitmapText text:texts) {
			text.setText("");
		}
		actualText.setLength(0);
		actualTextLine = 0;
	}
	
	public void setVisible(final boolean b) {
		if(visible != b) {
			uiNode.setCullHint(b ? CullHint.Dynamic : CullHint.Always);
			visible = b;
		}
	}
}
