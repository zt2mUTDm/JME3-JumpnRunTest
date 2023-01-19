package online.money_daisuki.projects.monkey.jumpnrun;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.jme3.asset.plugins.FileLocator;
import com.jme3.bullet.BulletAppState;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.Spatial.CullHint;
import com.jme3.terrain.geomipmap.TerrainQuad;

import online.money_daisuki.api.base.BiConverter;
import online.money_daisuki.api.base.Converter;
import online.money_daisuki.api.base.DataSink;
import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.base.models.SetableMutableSingleValueModel;
import online.money_daisuki.api.base.models.SetableMutableSingleValueModelImpl;
import online.money_daisuki.api.monkey.basegame.ExtendedApplication;
import online.money_daisuki.api.monkey.basegame.FrequencyDividingAppState;
import online.money_daisuki.api.monkey.basegame.RemoveDoneAppState;
import online.money_daisuki.api.monkey.basegame.cam.DebugCameraTransformCommand;
import online.money_daisuki.api.monkey.basegame.cam.FlycamMoveSpeedCommand;
import online.money_daisuki.api.monkey.basegame.cam.PrintCameraTransformCommand;
import online.money_daisuki.api.monkey.basegame.cam.SetCameraFrustumFarCommand;
import online.money_daisuki.api.monkey.basegame.cam.SetCameraTransformCommand;
import online.money_daisuki.api.monkey.basegame.character.AddCharacterCommand;
import online.money_daisuki.api.monkey.basegame.character.FlexibleCharacterLoader;
import online.money_daisuki.api.monkey.basegame.character.SetSpatialAnim;
import online.money_daisuki.api.monkey.basegame.character.control.CharControl;
import online.money_daisuki.api.monkey.basegame.character.control.MoveLinearToCommand;
import online.money_daisuki.api.monkey.basegame.character.control.RotateLinearByCommand;
import online.money_daisuki.api.monkey.basegame.debug.DebugBulletCommand;
import online.money_daisuki.api.monkey.basegame.debug.ProfilerCommand;
import online.money_daisuki.api.monkey.basegame.debug.SetStatsCommand;
import online.money_daisuki.api.monkey.basegame.material.FlexibleMaterialLoader;
import online.money_daisuki.api.monkey.basegame.misc.AddLightCommand;
import online.money_daisuki.api.monkey.basegame.misc.Utils;
import online.money_daisuki.api.monkey.basegame.model.AddModelCommand;
import online.money_daisuki.api.monkey.basegame.model.FlexibleModelLoader;
import online.money_daisuki.api.monkey.basegame.player.AddPlayerCommand;
import online.money_daisuki.api.monkey.basegame.player.control.SetPlayerControlEnabledCommand;
import online.money_daisuki.api.monkey.basegame.player.control.SetPlayerEnabledCommand;
import online.money_daisuki.api.monkey.basegame.script.ExecCommand;
import online.money_daisuki.api.monkey.basegame.spatial.HasSpatial;
import online.money_daisuki.api.monkey.basegame.spatial.HasSpatialAdapter;
import online.money_daisuki.api.monkey.basegame.spatial.MoveToCommand;
import online.money_daisuki.api.monkey.basegame.spatial.SetNodeCullHintCommand;
import online.money_daisuki.api.monkey.basegame.spatial.SetSpatialTranslationCommand;
import online.money_daisuki.api.monkey.basegame.spatial.SetVariableSpatialCullHintCommand;
import online.money_daisuki.api.monkey.basegame.spatial.Translatable;
import online.money_daisuki.api.monkey.basegame.spatial.TranslatableSpatial;
import online.money_daisuki.api.monkey.basegame.spatial.TurnToCommand;
import online.money_daisuki.api.monkey.basegame.spatial.UnloadCommand;
import online.money_daisuki.api.monkey.basegame.spatial.UnloadSceneCommand;
import online.money_daisuki.api.monkey.basegame.terrain.AddTerrainCommand;
import online.money_daisuki.api.monkey.basegame.terrain.FlexibleTerrainLoader;
import online.money_daisuki.api.monkey.basegame.text.ClearTextCommand;
import online.money_daisuki.api.monkey.basegame.text.ShowControlledTextCommand;
import online.money_daisuki.api.monkey.basegame.text.ShowTextAppState;
import online.money_daisuki.api.monkey.basegame.text.ShowTextAppStateUi;
import online.money_daisuki.api.monkey.basegame.text.ShowTextCommand;
import online.money_daisuki.api.monkey.basegame.text.ShowTextNodeToLinesConverter;
import online.money_daisuki.api.monkey.basegame.text.ShowTextUiBuilder;
import online.money_daisuki.api.monkey.basegame.variables.ClearVariablenTypeCommand;
import online.money_daisuki.api.monkey.basegame.variables.ClearVariablesCommand;
import online.money_daisuki.api.monkey.basegame.variables.IncVariableCommand;
import online.money_daisuki.api.monkey.basegame.variables.SetVariableCommand;
import online.money_daisuki.api.monkey.basegame.variables.VariableContainer;
import online.money_daisuki.api.monkey.console.CommandExecutor;
import online.money_daisuki.api.monkey.console.ConsoleAppState;

public final class App extends ExtendedApplication {
	private CommandExecutor exe;
	private RemoveDoneAppState removeDoneState;
	private final SetableMutableSingleValueModel<Spatial> playerContainer = new SetableMutableSingleValueModelImpl<>();
	
	private Map<String, Map<String, VariableContainer>> variables;
	
	@Override
	public void simpleInitApp() {
		getFlyByCamera().setEnabled(false);
		
		variables = new HashMap<>();
		
		assetManager.registerLocator("", FileLocator.class);
		assetManager.registerLocator("Models", FileLocator.class);
		assetManager.registerLocator("Interface", FileLocator.class);
		
		removeDoneState = new RemoveDoneAppState(getStateManager());
		exe = new CommandExecutor();
		
		
		setDisplayFps(false);
		setDisplayStatView(false);
		
		
		final BulletAppState bulletAppState = new BulletAppState();
		stateManager.attach(bulletAppState);
		
		installCamCommands(exe);
		installLightCommands(exe);
		installModelCommands(exe, bulletAppState);
		installTerrainCommands(exe, bulletAppState);
		installTextboxCommands();
		installSpatialCommands(exe, bulletAppState);
		
		// Misc
		exe.addCommand("DebugBullet", new DebugBulletCommand(bulletAppState));
		exe.addCommand("Profiler", new ProfilerCommand(stateManager));
		exe.addCommand("SetStats", new SetStatsCommand(this));
		exe.addCommand("SetVariable", new SetVariableCommand(this));
		exe.addCommand("IncVariable", new IncVariableCommand(this));
		exe.addCommand("ClearVariablenType", new ClearVariablenTypeCommand(this));
		exe.addCommand("ClearVariables", new ClearVariablesCommand(this));
		
		stateManager.attach(new ConsoleAppState(exe));
		
		
		getInputManager().addMapping("ControlMoveUp", new KeyTrigger(KeyInput.KEY_UP));
		getInputManager().addMapping("ControlMoveLeft", new KeyTrigger(KeyInput.KEY_LEFT));
		getInputManager().addMapping("ControlMoveDown", new KeyTrigger(KeyInput.KEY_DOWN));
		getInputManager().addMapping("ControlMoveRight", new KeyTrigger(KeyInput.KEY_RIGHT));
		getInputManager().addMapping("ControlStrike", new KeyTrigger(KeyInput.KEY_Z));
		getInputManager().addMapping("ControlJump", new KeyTrigger(KeyInput.KEY_X));
		
		getInputManager().addMapping("CamMoveUp", new KeyTrigger(KeyInput.KEY_NUMPAD8));
		getInputManager().addMapping("CamMoveRight", new KeyTrigger(KeyInput.KEY_NUMPAD6));
		getInputManager().addMapping("CamMoveDown", new KeyTrigger(KeyInput.KEY_NUMPAD2));
		getInputManager().addMapping("CamMoveLeft", new KeyTrigger(KeyInput.KEY_NUMPAD4));
		
		getInputManager().addMapping("CamZoomIn", new KeyTrigger(KeyInput.KEY_NUMPAD9));
		getInputManager().addMapping("CamZoomOut", new KeyTrigger(KeyInput.KEY_NUMPAD3));
		
		
		getStateManager().attach(new FrequencyDividingAppState(removeDoneState, 30.0f));
		exe.addCommand("Exec", new ExecCommand(getStateManager(), this, removeDoneState));
		
		exe.execute(new Node(), "Exec Scripts/init.txt".split(" "), new Runnable() {
			@Override
			public void run() {
			}
		});
	}
	private void installCamCommands(final CommandExecutor exe) {
		exe.addCommand("SetFlycamMoveSpeed", new FlycamMoveSpeedCommand(this));
		exe.addCommand("SetCameraFrustumFar", new SetCameraFrustumFarCommand(getCamera()));
		exe.addCommand("DebugChaseCamera", new DebugCameraTransformCommand(getGuiNode(), playerContainer, this));
		exe.addCommand("PrintChaseCamera", new PrintCameraTransformCommand(playerContainer));
		exe.addCommand("SetCameraTransform", new SetCameraTransformCommand(playerContainer));
	}
	private void installSpatialCommands(final CommandExecutor exe, final BulletAppState bullet) {
		exe.addCommand("SetTranslation", new SetSpatialTranslationCommand(new Converter<String, Translatable>() {
			@Override
			public Translatable convert(final String value) {
				switch(value) {
					case("player"):
						if(!playerContainer.isSet()) {
							return(null);
						}
						final CharControl cc = Utils.getControlRecursive(playerContainer.source(), CharControl.class);
						return(cc);
					default:
						return(new TranslatableSpatial(getRootNode().getChild(value)));
				}
			}
		}));
		exe.addCommand("SetCull", new SetVariableSpatialCullHintCommand(new Converter<String, HasSpatial>() {
			@Override
			public HasSpatial convert(final String value) {
				switch(value) {
					case("player"):
						if(!playerContainer.isSet()) {
							return(null);
						}
						final CharControl cc = Utils.getControlRecursive(playerContainer.source(), CharControl.class);
						return(cc);
					default:
						return(new HasSpatialAdapter(getRootNode().getChild(value)));
				}
			}
		}));
		exe.addCommand("Unload", new UnloadCommand(bullet));
		final BiConverter<String, Spatial, Spatial> spatialTarget = new BiConverter<String, Spatial, Spatial>() {
			@Override
			public Spatial convert(final String a, final Spatial b) {
				switch(a) {
					case("player"):
						if(!playerContainer.isSet()) {
							throw new IllegalStateException("Player not set");
						}
						return(playerContainer.source());
					case("this"):
						return(b);
					default:
						final String thisName = b.getName();
						if(thisName != null && thisName.equals(a)) {
							return(b);
						}
						
						if(b instanceof Node) {
							final Spatial thisChild = ((Node) b).getChild(a);
							if(thisChild != null) {
								return(thisChild);
							}
						}
						
						final Spatial rootChild = getRootNode().getChild(a);
						if(rootChild == null) {
							throw new IllegalArgumentException("Node " + a + " not found.");
						}
						return(rootChild);
				}
			}
		};
		exe.addCommand("TurnTo", new TurnToCommand(spatialTarget));
		exe.addCommand("MoveTo", new MoveToCommand(spatialTarget));
		exe.addCommand("UnloadScene", new UnloadSceneCommand(getRootNode(), playerContainer, bullet));
		exe.addCommand("SetSpatialAnim", new SetSpatialAnim(spatialTarget));
		exe.addCommand("MoveLinearTo", new MoveLinearToCommand(spatialTarget, this));
		exe.addCommand("RotateLinearBy", new RotateLinearByCommand(spatialTarget, this));
	}
	private void installLightCommands(final CommandExecutor exe) {
		exe.addCommand("AddLight", new AddLightCommand(getRootNode()));
		//exe.addCommand("ShowLights", new ShowLightsCommand(getRootNode()));
	}
	private void installTerrainCommands(final CommandExecutor exe, final BulletAppState bulletAppState) {
		final Converter<String, TerrainQuad> ldr = new FlexibleTerrainLoader(new FlexibleMaterialLoader(this), this);
		
		exe.addCommand("AddTerrain", new AddTerrainCommand(
				ldr,
				getRootNode(),
				bulletAppState
				));
		/*exe.addCommand("PreloadTerrain", new PreloadTerrainCommand(
				ldr
				));*/
	}
	private void installModelCommands(final CommandExecutor exe, final BulletAppState bulletAppState) {
		final Converter<String, Spatial> modelLoader = new FlexibleModelLoader(new FlexibleMaterialLoader(this), this);
		
		exe.addCommand("AddModel",
				new AddModelCommand(
						modelLoader,
						getRootNode(),
						this
				)
		);
		exe.addCommand("AddCharacter",
				new AddCharacterCommand(
						new FlexibleCharacterLoader(modelLoader, bulletAppState, this),
						getRootNode(),
						bulletAppState,
						this
				)
		);
		exe.addCommand("AddPlayer",
				new AddPlayerCommand(
						new FlexibleCharacterLoader(modelLoader, bulletAppState, this),
						getRootNode(),
						playerContainer,
						bulletAppState,
						this
				)
		);
		exe.addCommand("SetPlayerEnabled",
				new SetPlayerEnabledCommand(playerContainer)
		);
		exe.addCommand("SetPlayerControlEnabled",
				new SetPlayerControlEnabledCommand(playerContainer)
		);
	}
	private void installTextboxCommands() {
		final ShowTextUiBuilder builder = new ShowTextUiBuilder(getAssetManager());
		builder.setBackgroundWidth(getGuiViewPort().getCamera().getWidth());
		final Node uiNode = builder.source();
		uiNode.setLocalTranslation(0, 0, 0);
		uiNode.setCullHint(CullHint.Always);
		getGuiNode().attachChild(uiNode);
		
		final ShowTextAppState textAppState = new ShowTextAppState(new ShowTextAppStateUi(new DataSink<Boolean>() {
			@Override
			public void sink(final Boolean value) {
				uiNode.setCullHint(value ? CullHint.Dynamic : CullHint.Always);
			}
		}, new ShowTextNodeToLinesConverter().convert(uiNode), 100), 0.05f);
		
		getStateManager().attach(textAppState);
		
		exe.addCommand("SetTextCull", new SetNodeCullHintCommand(uiNode));
		exe.addCommand("ShowText", new ShowTextCommand(textAppState));
		exe.addCommand("ShowControlledText", new ShowControlledTextCommand(textAppState, getInputManager(), this));
		exe.addCommand("ClearText", new ClearTextCommand(textAppState));
	}
	
	@Override
	public void simpleUpdate(final float tpf) {
		//TODO: add update code
	}
	
	@Override
	public void simpleRender(final RenderManager rm) {
		//TODO: add render code
	}
	
	
	@Override
	public void execute(final Spatial spatial, final String[] cmd, final Runnable done) {
		exe.execute(spatial, cmd, done);
	}
	
	@Override
	public void executeSimpleScript(final Spatial caller, final String path, final Runnable done) {
		new ExecCommand(getStateManager(), this, removeDoneState).execute(caller, new String[] {
				"Exec",
				path,
		}, done);
	}
	
	@Override
	public void setVariable(final String type, final String name, final VariableContainer var) {
		Map<String, VariableContainer> submap = variables.get(Requires.notNull(type, "type == null"));
		if(submap == null) {
			submap = new HashMap<>();
			variables.put(type, submap);
		}
		submap.put(Requires.notNull(name, "name == null"), Requires.notNull(var, "var == null"));
	}
	@Override
	public VariableContainer getVariable(final String type, final String name) {
		final Map<String, VariableContainer> submap = variables.get(Requires.notNull(type, "type == null"));
		if(submap == null) {
			throw new IllegalArgumentException("Variable type " + type + " don't exists");
		}
		return(submap.get(Requires.notNull(name, "name == null")));
	}
	@Override
	public boolean containsVariable(final String type, final String name) {
		final Map<String, VariableContainer> submap = variables.get(Requires.notNull(type, "type == null"));
		if(submap == null) {
			return(false);
		}
		return(submap.containsKey(Requires.notNull(name, "name == null")));
	}
	@Override
	public void clearVariablenType(final String type) {
		final Map<String, VariableContainer> submap = variables.remove(Requires.notNull(type, "type == null"));
		if(submap != null) {
			submap.clear();
		}
	}
	@Override
	public void clearVariables() {
		for(final Entry<String, Map<String, VariableContainer>> e:variables.entrySet()) {
			final Map<String, VariableContainer> submap = e.getValue();
			submap.clear();
		}
		variables.clear();
	}
}
