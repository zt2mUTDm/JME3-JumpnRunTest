package online.money_daisuki.projects.monkey.jumpnrun;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.jme3.asset.plugins.FileLocator;
import com.jme3.bullet.BulletAppState;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.math.ColorRGBA;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.CartoonEdgeFilter;
import com.jme3.post.filters.FadeFilter;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.Spatial.CullHint;
import com.jme3.terrain.geomipmap.TerrainQuad;

import online.money_daisuki.api.base.BiConverter;
import online.money_daisuki.api.base.ConstantDataSource;
import online.money_daisuki.api.base.Converter;
import online.money_daisuki.api.base.DataSink;
import online.money_daisuki.api.base.DataSource;
import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.base.Setable;
import online.money_daisuki.api.base.models.MutableSingleValueModel;
import online.money_daisuki.api.base.models.MutableSingleValueModelImpl;
import online.money_daisuki.api.base.models.SetableMutableSingleValueModel;
import online.money_daisuki.api.base.models.SetableMutableSingleValueModelImpl;
import online.money_daisuki.api.monkey.basegame.ExtendedApplication;
import online.money_daisuki.api.monkey.basegame.audio.CreateAudioNodeCommand;
import online.money_daisuki.api.monkey.basegame.audio.PauseAudioCommand;
import online.money_daisuki.api.monkey.basegame.audio.PlayAudioCommand;
import online.money_daisuki.api.monkey.basegame.audio.SetAudioDirectionalCommand;
import online.money_daisuki.api.monkey.basegame.audio.SetAudioLoopingCommand;
import online.money_daisuki.api.monkey.basegame.audio.SetAudioPitchOffsetCommand;
import online.money_daisuki.api.monkey.basegame.audio.SetAudioPositionalCommand;
import online.money_daisuki.api.monkey.basegame.audio.SetAudioTimeOffsetCommand;
import online.money_daisuki.api.monkey.basegame.audio.SetAudioVolumeCommand;
import online.money_daisuki.api.monkey.basegame.audio.SetMaxDistanceCommand;
import online.money_daisuki.api.monkey.basegame.audio.SetRefDistanceCommand;
import online.money_daisuki.api.monkey.basegame.audio.StopAudioCommand;
import online.money_daisuki.api.monkey.basegame.cam.AddChaseCameraCommand;
import online.money_daisuki.api.monkey.basegame.cam.DebugCameraTransformCommand;
import online.money_daisuki.api.monkey.basegame.cam.FlycamMoveSpeedCommand;
import online.money_daisuki.api.monkey.basegame.cam.PrintCameraTransformCommand;
import online.money_daisuki.api.monkey.basegame.cam.RemoveChaseCameraCommand;
import online.money_daisuki.api.monkey.basegame.cam.SetCameraFrustumFarCommand;
import online.money_daisuki.api.monkey.basegame.cam.SetCameraTransformCommand;
import online.money_daisuki.api.monkey.basegame.cam.SetChaseCameraEnabledCommand;
import online.money_daisuki.api.monkey.basegame.cam.SetChaseCameraTransformCommand;
import online.money_daisuki.api.monkey.basegame.character.AddCharacterCommand;
import online.money_daisuki.api.monkey.basegame.character.FlexibleCharacterLoader;
import online.money_daisuki.api.monkey.basegame.character.SetSpatialAnim;
import online.money_daisuki.api.monkey.basegame.character.control.CharControl;
import online.money_daisuki.api.monkey.basegame.character.control.MoveLinearToCommand;
import online.money_daisuki.api.monkey.basegame.character.control.RotateLinearByCommand;
import online.money_daisuki.api.monkey.basegame.debug.DebugBulletCommand;
import online.money_daisuki.api.monkey.basegame.debug.ProfilerCommand;
import online.money_daisuki.api.monkey.basegame.debug.SetStatsCommand;
import online.money_daisuki.api.monkey.basegame.filter.FadeAppState;
import online.money_daisuki.api.monkey.basegame.filter.FadeInCommand;
import online.money_daisuki.api.monkey.basegame.filter.FadeOutCommand;
import online.money_daisuki.api.monkey.basegame.filter.SetFadeDurationCommand;
import online.money_daisuki.api.monkey.basegame.light.AddAmbientLightCommand;
import online.money_daisuki.api.monkey.basegame.light.AddDirectionalLightCommand;
import online.money_daisuki.api.monkey.basegame.light.AddPointLightCommand;
import online.money_daisuki.api.monkey.basegame.light.AddSpotLightCommand;
import online.money_daisuki.api.monkey.basegame.material.FlexibleMaterialLoader;
import online.money_daisuki.api.monkey.basegame.misc.FormatDateAsStringSource;
import online.money_daisuki.api.monkey.basegame.misc.FrequencyDividingAppState;
import online.money_daisuki.api.monkey.basegame.misc.MayDoneAppState;
import online.money_daisuki.api.monkey.basegame.misc.NumeredFileGenerated;
import online.money_daisuki.api.monkey.basegame.misc.OneTimeDelayedRunAppState;
import online.money_daisuki.api.monkey.basegame.misc.OwnScreenshotAppState;
import online.money_daisuki.api.monkey.basegame.misc.RemoveDoneAppState;
import online.money_daisuki.api.monkey.basegame.misc.SetScreenshotDirectoryCommand;
import online.money_daisuki.api.monkey.basegame.misc.TakeScreenshotCommand;
import online.money_daisuki.api.monkey.basegame.misc.Utils;
import online.money_daisuki.api.monkey.basegame.model.AddModelCommand;
import online.money_daisuki.api.monkey.basegame.model.FlexibleModelLoader;
import online.money_daisuki.api.monkey.basegame.particles.CreateParticleEmitterCommand;
import online.money_daisuki.api.monkey.basegame.particles.EmitAllParticlesCommand;
import online.money_daisuki.api.monkey.basegame.particles.FlexibleParticleEmitterLoader;
import online.money_daisuki.api.monkey.basegame.player.AddPlayerCommand;
import online.money_daisuki.api.monkey.basegame.player.SetPlayerControlEnabledCommand;
import online.money_daisuki.api.monkey.basegame.player.SetPlayerEnabledCommand;
import online.money_daisuki.api.monkey.basegame.player.SetPlayerJumpSpeedCommand;
import online.money_daisuki.api.monkey.basegame.script.ExecCommand;
import online.money_daisuki.api.monkey.basegame.sky.CreateSkyCommand;
import online.money_daisuki.api.monkey.basegame.sky.RemoveSkyCommand;
import online.money_daisuki.api.monkey.basegame.spatial.DetachSpatialAppState;
import online.money_daisuki.api.monkey.basegame.spatial.DetachSpatialCommand;
import online.money_daisuki.api.monkey.basegame.spatial.HasSpatial;
import online.money_daisuki.api.monkey.basegame.spatial.HasSpatialAdapter;
import online.money_daisuki.api.monkey.basegame.spatial.MoveToCommand;
import online.money_daisuki.api.monkey.basegame.spatial.PrintScenegraphJsonCommand;
import online.money_daisuki.api.monkey.basegame.spatial.PrintSpatialTransformCommand;
import online.money_daisuki.api.monkey.basegame.spatial.SetNodeCullHintCommand;
import online.money_daisuki.api.monkey.basegame.spatial.SetSpatialRotationCommand;
import online.money_daisuki.api.monkey.basegame.spatial.SetSpatialTranslationCommand;
import online.money_daisuki.api.monkey.basegame.spatial.SetTranslationCommand;
import online.money_daisuki.api.monkey.basegame.spatial.SetVariableSpatialCullHintCommand;
import online.money_daisuki.api.monkey.basegame.spatial.Translatable;
import online.money_daisuki.api.monkey.basegame.spatial.TranslatableSpatial;
import online.money_daisuki.api.monkey.basegame.spatial.TurnToCommand;
import online.money_daisuki.api.monkey.basegame.spatial.UnloadCommand;
import online.money_daisuki.api.monkey.basegame.spatial.UnloadSceneCommand;
import online.money_daisuki.api.monkey.basegame.terrain.AddTerrainCommand;
import online.money_daisuki.api.monkey.basegame.terrain.FlexibleTerrainLoader;
import online.money_daisuki.api.monkey.basegame.text.ClearTextCommand;
import online.money_daisuki.api.monkey.basegame.text.SetLanguageKeyCommand;
import online.money_daisuki.api.monkey.basegame.text.ShowControlledTextCommand;
import online.money_daisuki.api.monkey.basegame.text.ShowControlledTextFromFileCommand;
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
import online.money_daisuki.api.monkey.console.CommandStringDataSink;
import online.money_daisuki.api.monkey.console.ConsoleAppState;
import online.money_daisuki.api.monkey.console.ConsoleNodeBuilder;
import online.money_daisuki.api.monkey.console.ConsoleSpatial;

public final class App extends ExtendedApplication {
	private CommandExecutor exe;
	private RemoveDoneAppState removeDoneState;
	private final SetableMutableSingleValueModel<Spatial> playerContainer = new SetableMutableSingleValueModelImpl<>();
	
	private Map<String, Map<String, VariableContainer>> variables;
	
	@Override
	public void simpleInitApp() {
		getFlyByCamera().setEnabled(false);
		
		final MutableSingleValueModel<File> screenshotDirectory = new MutableSingleValueModelImpl<>(new File("."));
		
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
					case("root"):
						return(getRootNode());
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
		
		final SetableMutableSingleValueModel<Spatial> sky = new SetableMutableSingleValueModelImpl<>();
		
		installCamCommands(exe, spatialTarget);
		installLightCommands(exe, spatialTarget);
		installModelCommands(exe, bulletAppState);
		installTerrainCommands(exe, bulletAppState);
		installTextboxCommands();
		installSpatialCommands(exe, bulletAppState, spatialTarget, sky);
		installAudioCommands(exe, spatialTarget);
		installParticleCommands(exe, spatialTarget);
		installFilterCommands(exe);
		
		// Misc
		exe.addCommand("DebugBullet", new DebugBulletCommand(bulletAppState));
		exe.addCommand("Profiler", new ProfilerCommand(stateManager));
		exe.addCommand("SetStats", new SetStatsCommand(this));
		exe.addCommand("SetVariable", new SetVariableCommand(this));
		exe.addCommand("IncVariable", new IncVariableCommand(this));
		exe.addCommand("ClearVariablenType", new ClearVariablenTypeCommand(this));
		exe.addCommand("ClearVariables", new ClearVariablesCommand(this));
		
		exe.addCommand("CreateSky", new CreateSkyCommand(sky, this));
		exe.addCommand("RemoveSky", new RemoveSkyCommand(sky));
		
		final Node relectNode = new Node();
		getRootNode().attachChild(relectNode);
		
		
		final ViewPort vp = getViewPort();
		
		final ConsoleNodeBuilder cnb = new ConsoleNodeBuilder(assetManager, vp.getCamera().getWidth(), vp.getCamera().getHeight());
		final ConsoleSpatial console = cnb.source();
		console.setVisible(false);
		stateManager.attach(new ConsoleAppState(console, new CommandStringDataSink(exe, new Node("ConsoleDummyNode"))));
		guiNode.attachChild(console.getRoot());
		
		final BitmapText tookScreenshotBitmapText = new BitmapText(guiFont);
		tookScreenshotBitmapText.setText("Screenshot taken");
		tookScreenshotBitmapText.setLocalTranslation(0, 100, 0);
		tookScreenshotBitmapText.setColor(ColorRGBA.Green);
		
		final OwnScreenshotAppState screenshot = new OwnScreenshotAppState(new NumeredFileGenerated(
				screenshotDirectory,
				new FormatDateAsStringSource(
						new ConstantDataSource<>(new SimpleDateFormat("yyyy-MM-dd.HH.mm.ss")),
						new DataSource<Date>() {
							@Override
							public Date source() {
								return(new Date());
							}
						}
				),
				new ConstantDataSource<>("png"),
				255
		));
		screenshot.addCapturedListener(new Runnable() {
			@Override
			public void run() {
				tookScreenshotBitmapText.setText("Screenshot taken");
				tookScreenshotBitmapText.setColor(ColorRGBA.Green);
				
				getGuiNode().attachChild(tookScreenshotBitmapText);
				
				final MayDoneAppState state = new OneTimeDelayedRunAppState(new DetachSpatialAppState(tookScreenshotBitmapText), 5.0f);
				getStateManager().attach(state);
				removeDoneState.addAppState(state);
			}
		});
		screenshot.addCapturingFailedListener(new Runnable() {
			@Override
			public  void run() {
				tookScreenshotBitmapText.setText("Screenshot taken failed");
				tookScreenshotBitmapText.setColor(ColorRGBA.Red);
				
				getGuiNode().attachChild(tookScreenshotBitmapText);
				
				final MayDoneAppState state = new OneTimeDelayedRunAppState(new DetachSpatialAppState(tookScreenshotBitmapText), 5.0f);
				getStateManager().attach(state);
				removeDoneState.addAppState(state);
			}
		});
		getStateManager().attach(screenshot);
		
		exe.addCommand("SetScreenshotDirectory", new SetScreenshotDirectoryCommand(screenshotDirectory));
		exe.addCommand("TakeScreenshot", new TakeScreenshotCommand(screenshot));
		
		final FilterPostProcessor foo = new FilterPostProcessor(getAssetManager());
		foo.addFilter(new CartoonEdgeFilter());
		getViewPort().addProcessor(foo);
		
		getInputManager().addMapping("ControlMoveUp", new KeyTrigger(KeyInput.KEY_W));
		getInputManager().addMapping("ControlMoveLeft", new KeyTrigger(KeyInput.KEY_A));
		getInputManager().addMapping("ControlMoveDown", new KeyTrigger(KeyInput.KEY_S));
		getInputManager().addMapping("ControlMoveRight", new KeyTrigger(KeyInput.KEY_D));
		
		getInputManager().addMapping("ControlStrike", new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));
		getInputManager().addMapping("ControlJump", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
		
		getInputManager().addMapping("CamMoveUp", new KeyTrigger(KeyInput.KEY_NUMPAD8));
		getInputManager().addMapping("CamMoveRight", new KeyTrigger(KeyInput.KEY_NUMPAD6));
		getInputManager().addMapping("CamMoveDown", new KeyTrigger(KeyInput.KEY_NUMPAD2));
		getInputManager().addMapping("CamMoveLeft", new KeyTrigger(KeyInput.KEY_NUMPAD4));
		
		getInputManager().addMapping("CamZoomIn", new KeyTrigger(KeyInput.KEY_NUMPAD9));
		getInputManager().addMapping("CamZoomOut", new KeyTrigger(KeyInput.KEY_NUMPAD3));
		
		getInputManager().addMapping("TakeScreenshot", new KeyTrigger(KeyInput.KEY_SYSRQ));
		
		getInputManager().addListener(new ActionListener() {
			@Override
			public void onAction(final String name, final boolean isPressed, final float tpf) {
				if(isPressed) {
					screenshot.capture();
				}
			}
		}, "TakeScreenshot");
		
		getStateManager().attach(new FrequencyDividingAppState(removeDoneState, 30.0f));
		exe.addCommand("Exec", new ExecCommand(getStateManager(), this, removeDoneState));
		
		exe.execute(new Node(), "Exec Scripts/init.txt".split(" "), new Runnable() {
			@Override
			public void run() {
				
			}
		});
	}
	
	private void installCamCommands(final CommandExecutor exe, final BiConverter<String, Spatial, Spatial> spatialTarget) {
		exe.addCommand("SetFlycamMoveSpeed", new FlycamMoveSpeedCommand(this));
		exe.addCommand("SetCameraFrustumFar", new SetCameraFrustumFarCommand(getCamera()));
		exe.addCommand("DebugChaseCamera", new DebugCameraTransformCommand(getGuiNode(), playerContainer, this));
		exe.addCommand("PrintChaseCamera", new PrintCameraTransformCommand(playerContainer));
		exe.addCommand("SetCameraTransform", new SetCameraTransformCommand(playerContainer));
		exe.addCommand("AddChaseCamera", new AddChaseCameraCommand(spatialTarget, this));
		exe.addCommand("RemoveChaseCamera", new RemoveChaseCameraCommand(spatialTarget, this));
		exe.addCommand("SetChaseCameraTransform", new SetChaseCameraTransformCommand(spatialTarget));
		exe.addCommand("SetChaseCameraEnabled", new SetChaseCameraEnabledCommand(spatialTarget));
	}
	@SuppressWarnings("deprecation")
	private void installSpatialCommands(final CommandExecutor exe, final BulletAppState bullet, final BiConverter<String, Spatial, Spatial> spatialTarget,
			final Setable sky) {
		exe.addCommand("SetTranslation", new SetTranslationCommand(new Converter<String, Translatable>() {
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
		})); // TODO change skripts and remove
		exe.addCommand("SetSpatialTranslation", new SetSpatialTranslationCommand(spatialTarget));
		exe.addCommand("SetSpatialRotation", new SetSpatialRotationCommand(spatialTarget));
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
		exe.addCommand("TurnTo", new TurnToCommand(spatialTarget));
		exe.addCommand("MoveTo", new MoveToCommand(spatialTarget));
		exe.addCommand("UnloadScene", new UnloadSceneCommand(getRootNode(), playerContainer, bullet, sky));
		exe.addCommand("SetSpatialAnim", new SetSpatialAnim(spatialTarget));
		exe.addCommand("MoveLinearTo", new MoveLinearToCommand(spatialTarget, this));
		exe.addCommand("RotateLinearBy", new RotateLinearByCommand(spatialTarget));
		exe.addCommand("PrintSpatialTransform", new PrintSpatialTransformCommand(spatialTarget, System.err));
		exe.addCommand("PrintScenegraphJson", new PrintScenegraphJsonCommand(spatialTarget, System.err));
		exe.addCommand("DetachSpatial", new DetachSpatialCommand(spatialTarget));
	}
	private void installAudioCommands(final CommandExecutor exe, final BiConverter<String, Spatial, Spatial> spatialTarget) {
		exe.addCommand("CreateAudioNode", new CreateAudioNodeCommand(assetManager, spatialTarget));
		exe.addCommand("PlayAudio", new PlayAudioCommand(spatialTarget));
		exe.addCommand("PauseAudio", new PauseAudioCommand(spatialTarget));
		exe.addCommand("StopAudio", new StopAudioCommand(spatialTarget));
		exe.addCommand("SetAudioDirectional", new SetAudioDirectionalCommand(spatialTarget));
		exe.addCommand("SetAudioPositional", new SetAudioPositionalCommand(spatialTarget));
		exe.addCommand("SetAudioLooping", new SetAudioLoopingCommand(spatialTarget));
		exe.addCommand("SetAudioTimeOffset", new SetAudioTimeOffsetCommand(spatialTarget));
		exe.addCommand("SetAudioPitchOffset", new SetAudioPitchOffsetCommand(spatialTarget));
		exe.addCommand("SetAudioVolume", new SetAudioVolumeCommand(spatialTarget));
		exe.addCommand("SetRefDistance", new SetRefDistanceCommand(spatialTarget));
		exe.addCommand("SetMaxDistance", new SetMaxDistanceCommand(spatialTarget));
	}
	private void installLightCommands(final CommandExecutor exe, final BiConverter<String, Spatial, Spatial> spatialTarget) {
		exe.addCommand("AddAmbientLight", new AddAmbientLightCommand(spatialTarget));
		exe.addCommand("AddDirectionalLight", new AddDirectionalLightCommand(spatialTarget));
		exe.addCommand("AddSpotLight", new AddSpotLightCommand(spatialTarget));
		exe.addCommand("AddPointLight", new AddPointLightCommand(spatialTarget));
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
		exe.addCommand("SetPlayerJumpSpeed",
				new SetPlayerJumpSpeedCommand(playerContainer)
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
		
		final MutableSingleValueModel<String> languageKey = new MutableSingleValueModelImpl<>("en");
		
		exe.addCommand("SetTextCull", new SetNodeCullHintCommand(uiNode));
		exe.addCommand("ShowText", new ShowTextCommand(textAppState));
		exe.addCommand("ShowControlledText", new ShowControlledTextCommand(textAppState, getInputManager(), this));
		exe.addCommand("ClearText", new ClearTextCommand(textAppState));
		exe.addCommand("SetLanguageKey", new SetLanguageKeyCommand(languageKey));
		exe.addCommand("ShowControlledTextFromFile", new ShowControlledTextFromFileCommand(textAppState, getInputManager(), this,
				languageKey));
	}
	private void installParticleCommands(final CommandExecutor exe, final BiConverter<String, Spatial, Spatial> spatialTarget) {
		final FlexibleParticleEmitterLoader particleLoader = new FlexibleParticleEmitterLoader(new FlexibleMaterialLoader(this), this);
		exe.addCommand("CreateParticleEmitter", new CreateParticleEmitterCommand(spatialTarget, particleLoader));
		exe.addCommand("EmitAllParticles", new EmitAllParticlesCommand(spatialTarget));
	}
	private void installFilterCommands(final CommandExecutor exe) {
		final FilterPostProcessor fpp = new FilterPostProcessor(getAssetManager());
		getViewPort().addProcessor(fpp);
		
		final FadeFilter fade = new FadeFilter();
		fade.setValue(0.0f);
		fpp.addFilter(fade);
		getStateManager().attach(new FadeAppState(fade));
		
		exe.addCommand("FadeOut", new FadeOutCommand(this));
		exe.addCommand("FadeIn", new FadeInCommand(this));
		exe.addCommand("SetFadeDuration", new SetFadeDurationCommand(this));
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
