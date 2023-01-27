package online.money_daisuki.api.monkey.console;

import com.jme3.asset.AssetManager;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.font.LineWrapMode;
import com.jme3.font.Rectangle;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Quad;

import online.money_daisuki.api.base.DataSource;
import online.money_daisuki.api.base.Requires;

public final class ConsoleNodeBuilder implements DataSource<ConsoleSpatial> {
	private final AssetManager assetManager;
	
	private final int viewportWidth;
	private final int viewportHeight;
	
	public ConsoleNodeBuilder(final AssetManager assetManager, final int viewportWidth, final int viewportHeight) {
		this.assetManager = Requires.notNull(assetManager, "assetManager == null");
		this.viewportWidth = viewportWidth;
		this.viewportHeight = viewportHeight;
	}
	@Override
	public ConsoleSpatial source() {
		final Node node = new Node("Console");
		
		final Material elementsMaterial = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		elementsMaterial.setColor("Color", new ColorRGBA(0.1f, 0.1f, 0.1f, 0.7f));
		elementsMaterial.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
		
		final Geometry inputGeometry = new Geometry("BgInputQuad");
		inputGeometry.setMaterial(elementsMaterial);
		
		final Geometry scrollbackGeometry = new Geometry("BgScrollingQuad");
		scrollbackGeometry.setMaterial(elementsMaterial);
		
		node.attachChild(inputGeometry);
		node.attachChild(scrollbackGeometry);
		
		
		final BitmapFont guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
		final BitmapText scrollingBitmapText = new BitmapText(guiFont);
		scrollingBitmapText.setName("scrollingBitmapText");
		scrollingBitmapText.setLineWrapMode(LineWrapMode.Word);
		final BitmapText inputBitmapText = new BitmapText(guiFont);
		inputBitmapText.setName("ConsoleInput");
		final BitmapText inputCursorBitmapText = new BitmapText(guiFont);
		inputCursorBitmapText.setName("inputCursorBitmapText");
		node.attachChild(scrollingBitmapText);
		node.attachChild(inputBitmapText);
		node.attachChild(inputCursorBitmapText);
		
		inputCursorBitmapText.setText("|");
		
		
		final float lineHeight = inputCursorBitmapText.getLineHeight();
		final float lineWidth = inputCursorBitmapText.getLineWidth();
		float bgQuadWidth;
		
		final boolean isConsoleUsesFullViewPortWidth = true;
		if (isConsoleUsesFullViewPortWidth) {
			bgQuadWidth = viewportWidth;
		} else {
			bgQuadWidth = lineWidth * 95;
			if (bgQuadWidth > viewportWidth) {
				bgQuadWidth = viewportWidth;
			} else if (bgQuadWidth < viewportWidth / 2) {
				bgQuadWidth = viewportWidth / 2;
			}
		}
		final float verticalPadding = .15f * lineHeight;
		final float horizontalPadding = verticalPadding;

		final boolean isConsoleUsesFullViewPortHeight = false;
		int numConsoleLines = 11;
		if (isConsoleUsesFullViewPortHeight) {
			numConsoleLines = 60;
		}
		float bgQuadHeight = (lineHeight * (numConsoleLines + 1)) + (verticalPadding * 3);
		while (bgQuadHeight > viewportHeight) {
			numConsoleLines--;// yeh this is kind of lazy of me.
			bgQuadHeight = (lineHeight * (numConsoleLines + 1)) + (verticalPadding * 3);
		}
		final float bgFieldQuadWidth = bgQuadWidth - (horizontalPadding * 2);

		//input text
		inputBitmapText.setLocalTranslation(horizontalPadding * 2f, viewportHeight - (lineHeight * numConsoleLines) - (verticalPadding * 1.5f), 1); // TODO: using 1.5 instead of 2 means this isnt totally based on the text and viewport sizes. might want to recheck my math later.
		// TODO: inputBitmapText has an issue of not wrapping text, but setting a box messes up the blinking cursor.
		//inputText.setBox(new Rectangle(0, 0, bgFieldQuadWidth, lineHeight));,
		
		//scrolling text
		scrollingBitmapText.setLocalTranslation(horizontalPadding * 2f, viewportHeight, 1); // subtract 1 vertical padding?
		scrollingBitmapText.setBox(new Rectangle(0, 0, bgFieldQuadWidth * .98f, lineHeight * numConsoleLines));
		scrollingBitmapText.setVerticalAlignment(BitmapFont.VAlign.Bottom);
		
		if (numConsoleLines <= 0) {
			scrollingBitmapText.setCullHint(Spatial.CullHint.Always); //user doesnt even want to show the console apparently.
		} else {
			scrollingBitmapText.setCullHint(Spatial.CullHint.Dynamic);
		}
		
		//background input text geom
		inputGeometry.setMesh(new Quad(bgFieldQuadWidth, lineHeight));
		inputGeometry.setLocalTranslation(horizontalPadding, viewportHeight - (lineHeight * (numConsoleLines + 1)) - (verticalPadding * 2f), .9f);
		
		//background scrolling text geom
		scrollbackGeometry.setMesh(new Quad(bgFieldQuadWidth, lineHeight * numConsoleLines));
		scrollbackGeometry.setLocalTranslation(horizontalPadding, viewportHeight - (lineHeight * numConsoleLines) - verticalPadding, .9f);
		
		return(new ConsoleImpl(node, inputBitmapText, scrollingBitmapText, numConsoleLines));
	}
}
