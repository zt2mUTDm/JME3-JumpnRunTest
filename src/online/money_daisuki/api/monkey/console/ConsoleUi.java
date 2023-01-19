package online.money_daisuki.api.monkey.console;

import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.asset.AssetManager;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.font.LineWrapMode;
import com.jme3.font.Rectangle;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Quad;

import online.money_daisuki.api.base.Requires;

public final class ConsoleUi {
	//private static final BlendMode BLEND_MODE = BlendMode.Alpha;

	private final Application app;

	private boolean visible;

	private final AssetManager assetManager;
	
	private final Node guiNode;
	private final ViewPort guiViewPort;
	
	private final Node node;
	//private final Geometry bgQuad;
	private final Geometry inputGeometry;
	private final Geometry scrollbackGeometry;
	
	private BitmapText scrollingBitmapText;
	
	private Float enqConsoleTextSize = null;
	private String inputText = "";
	private BitmapText inputBitmapText;
	
	private BitmapText inputCursorBitmapText;
	private float blinkCount = 0;
	private float blinkDuration = .4f;
	
	private final ConcurrentLinkedQueue<String> enqScrollingText = new ConcurrentLinkedQueue<String>(); // each element is a line to be added to the console output. The queue gets emptied and applied to the screen by the update() method.
	private int numConsoleLines = 11;
	
	private final boolean isConsoleUsesFullViewPortWidth = true;
	private final boolean isConsoleUsesFullViewPortHeight = false;
	
	private final Callable<Void> setInputTextCallable = new Callable<Void>() {
		@Override
		public Void call() throws Exception {
			inputBitmapText.setText(inputText);
			updateCursorLocation();
			return null;
		}
	};
	private final Callable<Void> setVisibleCallable = new Callable<Void>() {
		@Override
		public Void call() throws Exception {
			if (visible) {
				if (node.getParent() == null) {
					guiNode.attachChild(node);
				}
			} else {
				node.removeFromParent();
			}
			return null;
		}

	};
	
	public ConsoleUi(final SimpleApplication app) {
		this.app = app;
		this.assetManager = app.getAssetManager();
		
		this.guiViewPort = app.getGuiViewPort();
		this.guiNode = app.getGuiNode();
		
		node = new Node("Console");
		
		
		final Material elementsMaterial = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		elementsMaterial.setColor("Color", new ColorRGBA(0.1f, 0.1f, 0.1f, 0.7f));
		elementsMaterial.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);

		inputGeometry = new Geometry("BgInputQuad");
		inputGeometry.setMaterial(elementsMaterial);

		scrollbackGeometry = new Geometry("BgScrollingQuad");
		scrollbackGeometry.setMaterial(elementsMaterial);

		node.attachChild(inputGeometry);
		node.attachChild(scrollbackGeometry);
	}
	public void applyViewPortChange() {
		if (scrollingBitmapText != null) {
			scrollingBitmapText.removeFromParent();
			inputBitmapText.removeFromParent();
			inputCursorBitmapText.removeFromParent();
		}
		final BitmapFont guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
		scrollingBitmapText = new BitmapText(guiFont);
		scrollingBitmapText.setName("scrollingBitmapText");
		scrollingBitmapText.setLineWrapMode(LineWrapMode.Word);
		inputBitmapText = new BitmapText(guiFont);
		inputBitmapText.setName("inputBitmapText");
		inputCursorBitmapText = new BitmapText(guiFont);
		inputCursorBitmapText.setName("inputCursorBitmapText");
		node.attachChild(scrollingBitmapText);
		node.attachChild(inputBitmapText);
		node.attachChild(inputCursorBitmapText);


		if (enqConsoleTextSize != null) {
			// enqConsoleTextSize gets set by setConsoleSize(), make use of it here
			this.inputBitmapText.setSize(enqConsoleTextSize);
			this.scrollingBitmapText.setSize(enqConsoleTextSize);
			this.inputCursorBitmapText.setSize(enqConsoleTextSize);
			enqConsoleTextSize = null; //reset the enqueue variable because next time theres a viewport change we wont need to change the size.
		}
		inputCursorBitmapText.setText("|");
		blinkCount = 0;
		final int viewPortHeight = guiViewPort.getCamera().getHeight();
		final int viewPortWidth = guiViewPort.getCamera().getWidth();
		final float lineHeight = inputCursorBitmapText.getLineHeight();
		final float lineWidth = inputCursorBitmapText.getLineWidth();
		float bgQuadWidth;
		
		if (isConsoleUsesFullViewPortWidth) {
			bgQuadWidth = viewPortWidth;
		} else {
			bgQuadWidth = lineWidth * 95;
			if (bgQuadWidth > viewPortWidth) {
				bgQuadWidth = viewPortWidth;
			} else if (bgQuadWidth < viewPortWidth / 2) {
				bgQuadWidth = viewPortWidth / 2;
			}
		}
		final float verticalPadding = .15f * lineHeight;
		final float horizontalPadding = verticalPadding;

		//TODO: this is a lazy way to calculate the max number of console lines
		//      I should be able to reverse the bgQuadHeight equation and solve for
		//      numConsoleLines.
		if (isConsoleUsesFullViewPortHeight) {
			numConsoleLines = 60;
		}
		float bgQuadHeight = (lineHeight * (numConsoleLines + 1)) + (verticalPadding * 3);
		while (bgQuadHeight > viewPortHeight) {
			numConsoleLines--;// yeh this is kind of lazy of me.
			bgQuadHeight = (lineHeight * (numConsoleLines + 1)) + (verticalPadding * 3);
		}
		final float bgFieldQuadWidth = bgQuadWidth - (horizontalPadding * 2);

		//input text
		inputBitmapText.setLocalTranslation(horizontalPadding * 2f, viewPortHeight - (lineHeight * numConsoleLines) - (verticalPadding * 1.5f), 1); // TODO: using 1.5 instead of 2 means this isnt totally based on the text and viewport sizes. might want to recheck my math later.
		// TODO: inputBitmapText has an issue of not wrapping text, but setting a box messes up the blinking cursor.
		//inputText.setBox(new Rectangle(0, 0, bgFieldQuadWidth, lineHeight));,

		//scrolling text
		scrollingBitmapText.setLocalTranslation(horizontalPadding * 2f, viewPortHeight, 1); // subtract 1 vertical padding?
		scrollingBitmapText.setBox(new Rectangle(0, 0, bgFieldQuadWidth * .98f, lineHeight * numConsoleLines));
		scrollingBitmapText.setVerticalAlignment(BitmapFont.VAlign.Bottom);

		if (numConsoleLines <= 0) {
			scrollingBitmapText.setCullHint(Spatial.CullHint.Always); //user doesnt even want to show the console apparently.
		} else {
			scrollingBitmapText.setCullHint(Spatial.CullHint.Dynamic);
		}

		//background geom
		/*if (reducedConsoleLinesToFit) {
			//This means the console is the full height of the screen.
			//there might be a small thin line left at the bottom of the screen
			//so lets just use the whole screen height to make it look nice
			bgQuad.setMesh(new Quad(bgQuadWidth, viewPortHeight));
			bgQuad.setLocalTranslation(0, 0, .8f);
		} else {
			bgQuad.setMesh(new Quad(bgQuadWidth, bgQuadHeight));
			bgQuad.setLocalTranslation(0, viewPortHeight - bgQuadHeight, .8f);
		}*/


		//background input text geom
		inputGeometry.setMesh(new Quad(bgFieldQuadWidth, lineHeight));
		inputGeometry.setLocalTranslation(horizontalPadding, viewPortHeight - (lineHeight * (numConsoleLines + 1)) - (verticalPadding * 2f), .9f);

		//background scrolling text geom
		scrollbackGeometry.setMesh(new Quad(bgFieldQuadWidth, lineHeight * numConsoleLines));
		scrollbackGeometry.setLocalTranslation(horizontalPadding, viewPortHeight - (lineHeight * numConsoleLines) - verticalPadding, .9f);
	}
	
	public final void setVisible(final boolean setVisible) {
		visible = setVisible;
		app.enqueue(setVisibleCallable);
	}
	private void updateCursorLocation() {
		final float x = inputBitmapText.getLineWidth();
		final Vector3f inputTextLocation = inputBitmapText.getLocalTranslation();
		inputCursorBitmapText.setLocalTranslation((inputTextLocation.x / 1.6f) + x, inputTextLocation.y, 1);
		inputCursorBitmapText.setText("|");
		blinkCount = 0;
	}
	public void appendInputText(final String text) {
		inputText+= Requires.notNull(text, "text == null");
		app.enqueue(setInputTextCallable);
	}
	public void appendInputTextBackspace() {
		try {
			inputText = inputText.substring(0, inputText.length() - 1);
			app.enqueue(setInputTextCallable);
		} catch (final StringIndexOutOfBoundsException ex) {
			//This is expected and normal (if you push backspace too much)
		}
	}
	public void setInputText(final String text) {
		inputText = text != null ? text : null;
		app.enqueue(setInputTextCallable);
	}

	public String getInputText() {
		return (inputText);
	}
	
	public boolean isVisible() {
		return (visible);
	}
	
	public void appendConsole(final String text) {
		enqScrollingText.offer(text);
	}
	
	public void clearInput() {
		setInputText("");
	}
	
	public void update(final float tpf) {
		if (isVisible()) {
			blinkCount += tpf;
			if (blinkCount >= blinkDuration) {
				if (this.inputCursorBitmapText.getText().isEmpty()) {
					inputCursorBitmapText.setText("|");
					blinkDuration = .5f;
				} else {
					inputCursorBitmapText.setText("");
					blinkDuration = .4f;
				}
				blinkCount = 0;
			}
			updateAppendConsole(enqScrollingText.poll());
		}
	}
	private void updateAppendConsole(final String newLine) {
		if (newLine == null || newLine.length() == 0) {
			return;
		}
		scrollingBitmapText.setText(scrollingBitmapText.getText() + "\n" + newLine);

		//Now remove overflow lines
		final int linesToRemove = scrollingBitmapText.getLineCount() - numConsoleLines;
		if (linesToRemove > 0) {
			String oldText = scrollingBitmapText.getText();
			final String[] textAsArray = oldText.split("\r\n|\r|\n");
			oldText = "";
			for (int i = linesToRemove; i < textAsArray.length; i++) {
				if (!textAsArray[i].equals("") && !textAsArray[i].equals(" ")) {
					oldText += textAsArray[i];
					if (i != textAsArray.length - 1) {
						oldText += "\n";
					}
				}
			}
			scrollingBitmapText.setText(oldText);
		}
	}
	
	public float getConsoleTextSize() {
		return inputBitmapText.getFont().getPreferredSize();
	}

	public void setConsoleNumLines(final int numLines) {
		this.numConsoleLines = (numLines < 0 ? 0 : numLines);
		this.applyViewPortChange();
	}
	public int getConsoleNumLines() {
		return numConsoleLines;
	}
}
