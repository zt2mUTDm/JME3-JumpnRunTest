package online.money_daisuki.api.monkey.basegame;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.BaseAppState;
import com.jme3.scene.Node;

public final class SceneGraphAppState extends BaseAppState {
	private SimpleApplication app;
	
	private Node root;
	
	private Node content;
	private Node water;
	private Node gui;
	
	@Override
	protected void initialize(final Application app) {
		this.app = (SimpleApplication) app;
		
		this.root = new Node("Root node");
		this.app.getRootNode().attachChild(this.root);
		
		this.content = new Node("Content node");
		this.root.attachChild(this.content);
		
		this.water = new Node("Water node");
		this.root.attachChild(this.water);
		
		this.gui = new Node("GUI node");
		this.app.getGuiNode().attachChild(this.gui);
	}
	
	@Override
	protected void cleanup(final Application app) {
		this.app.getGuiNode().detachChild(this.gui);
		this.gui = null;
		
		this.root.detachChild(this.content);
		this.content = null;
		
		this.root.detachChild(this.water);
		this.water = null;
		
		this.app.getRootNode().detachChild(this.root);
		this.root = null;
		
		this.app = null;

	}
	
	@Override
	protected void onEnable() {
		
	}
	@Override
	protected void onDisable() {
		
	}
	
	public Node getRootNode() {
		return (content);
	}
	public Node getWaterNode() {
		return (water);
	}
	public Node getGuiNode() {
		return (gui);
	}
	
	public void clearRoot() {
		getRootNode().detachAllChildren();
	}
}
