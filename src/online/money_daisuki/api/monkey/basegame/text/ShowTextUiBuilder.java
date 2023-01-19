package online.money_daisuki.api.monkey.basegame.text;

import com.jme3.asset.AssetManager;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Quad;

import online.money_daisuki.api.base.DataSource;
import online.money_daisuki.api.base.Requires;

public final class ShowTextUiBuilder implements DataSource<Node> {
	private final String backgroundMaterial = "Common/MatDefs/Misc/Unshaded.j3md";
	private final ColorRGBA backgroundColor = new ColorRGBA(0.2f, 0.2f, 0.2f, 0.8f);
	private final BlendMode backgroundBlendMode = BlendMode.Alpha;
	private int backgroundWidth = 640;
	
	private float textSize = 17.0f;
	private String textFont = "Interface/Fonts/Default.fnt";
	private int lineCount = 3;
	private int lineVGap = 5;
	
	private final AssetManager assetManager;
	
	public ShowTextUiBuilder(final AssetManager assetManager) {
		this.assetManager = Requires.notNull(assetManager);
	}
	
	@Override
	public Node source() {
		final Node node = new Node();
		node.setLocalTranslation(0, 200, 0);
		
		final float totalHeight = getTotalHeight();
		
		final Material darkGrayMat = new Material(assetManager, backgroundMaterial);
		darkGrayMat.setColor("Color", backgroundColor);
		darkGrayMat.getAdditionalRenderState().setBlendMode(backgroundBlendMode);
		
		final Geometry background = new Geometry("TextBackground", new Quad(backgroundWidth, totalHeight));
		background.setMaterial(darkGrayMat);
		node.attachChild(background);
		
		final BitmapFont guiFont = assetManager.loadFont(textFont);
		
		float v = lineVGap;
		for(int i = 0; i < lineCount; i++) {
			final BitmapText line = new BitmapText(guiFont);
			line.setLocalTranslation(10, totalHeight - v, 0);
			line.setName("Line " + i);
			line.setColor(ColorRGBA.White);
			node.attachChild(line);
			
			v+= lineVGap + textSize;
		}
		
		return(node);
	}
	
	public ShowTextUiBuilder setTextSize(final float textSize) {
		this.textSize = textSize;
		return(this);
	}
	public void setLineCount(final int lineCount) {
		this.lineCount = Requires.greaterThan(lineCount, 1, "Need at least 2 lines");
	}
	public void setLineVerticalGap(final int lineVGap) {
		this.lineVGap = Requires.positive(lineVGap);
	}
	
	public void setTextFont(final String textFont) {
		this.textFont = Requires.notNull(textFont, "textFont == null");
	}
	
	public void setBackgroundWidth(final int width) {
		this.backgroundWidth = Requires.positive(width);
	}
	
	public float getTotalHeight() {
		return(lineVGap * 2 + (lineCount * (textSize + lineVGap) - lineVGap));
	}
}
