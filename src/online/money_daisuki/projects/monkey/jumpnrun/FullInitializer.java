package online.money_daisuki.projects.monkey.jumpnrun;

import java.util.Collection;

import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.bullet.BulletAppState;
import com.jme3.input.ChaseCamera;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.math.ColorRGBA;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.FadeFilter;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.Spatial.CullHint;
import com.jme3.terrain.geomipmap.TerrainQuad;

import online.money_daisuki.api.base.BiConverter;
import online.money_daisuki.api.base.Converter;
import online.money_daisuki.api.base.DataSink;
import online.money_daisuki.api.base.Setable;
import online.money_daisuki.api.base.models.MutableSingleValueModel;
import online.money_daisuki.api.base.models.MutableSingleValueModelImpl;
import online.money_daisuki.api.base.models.SetableMutableSingleValueModel;
import online.money_daisuki.api.base.models.SetableMutableSingleValueModelImpl;
import online.money_daisuki.api.monkey.basegame.ModulesApp;
import online.money_daisuki.api.monkey.basegame.SceneGraphAppState;
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
import online.money_daisuki.api.monkey.basegame.cam.PrintChaseCameraTransformCommand;
import online.money_daisuki.api.monkey.basegame.cam.RemoveChaseCameraCommand;
import online.money_daisuki.api.monkey.basegame.cam.SetCameraFrustumBottomCommand;
import online.money_daisuki.api.monkey.basegame.cam.SetCameraFrustumFarCommand;
import online.money_daisuki.api.monkey.basegame.cam.SetCameraFrustumLeftCommand;
import online.money_daisuki.api.monkey.basegame.cam.SetCameraFrustumNearCommand;
import online.money_daisuki.api.monkey.basegame.cam.SetCameraFrustumRightCommand;
import online.money_daisuki.api.monkey.basegame.cam.SetCameraFrustumTopCommand;
import online.money_daisuki.api.monkey.basegame.cam.SetCameraTransformCommand;
import online.money_daisuki.api.monkey.basegame.cam.SetChaseCameraEnabledCommand;
import online.money_daisuki.api.monkey.basegame.cam.SetChaseCameraTransformCommand;
import online.money_daisuki.api.monkey.basegame.cam.SetFlycamEnabledCommand;
import online.money_daisuki.api.monkey.basegame.character.AddCharacterControlCommand;
import online.money_daisuki.api.monkey.basegame.character.RemoveCharacterControlCommand;
import online.money_daisuki.api.monkey.basegame.character.SetSpatialAnimCommand;
import online.money_daisuki.api.monkey.basegame.character.control.MoveLinearToCommand;
import online.money_daisuki.api.monkey.basegame.character.control.RotateLinearByCommand;
import online.money_daisuki.api.monkey.basegame.debug.DebugBulletCommand;
import online.money_daisuki.api.monkey.basegame.debug.ProfilerCommand;
import online.money_daisuki.api.monkey.basegame.debug.SetStatsCommand;
import online.money_daisuki.api.monkey.basegame.filter.FadeAppState;
import online.money_daisuki.api.monkey.basegame.filter.FadeInCommand;
import online.money_daisuki.api.monkey.basegame.filter.FadeOutCommand;
import online.money_daisuki.api.monkey.basegame.filter.SetFadeDurationCommand;
import online.money_daisuki.api.monkey.basegame.form.AddFormCommand;
import online.money_daisuki.api.monkey.basegame.light.AddAmbientLightCommand;
import online.money_daisuki.api.monkey.basegame.light.AddDirectionalLightCommand;
import online.money_daisuki.api.monkey.basegame.light.AddPointLightCommand;
import online.money_daisuki.api.monkey.basegame.light.AddSpotLightCommand;
import online.money_daisuki.api.monkey.basegame.material.FlexibleMaterialLoader;
import online.money_daisuki.api.monkey.basegame.misc.FrequencyDividingAppState;
import online.money_daisuki.api.monkey.basegame.misc.RemoveDoneAppState;
import online.money_daisuki.api.monkey.basegame.misc.RemoveDoneControlsAppState;
import online.money_daisuki.api.monkey.basegame.model.AddModelCommand;
import online.money_daisuki.api.monkey.basegame.model.FlexibleModelLoader;
import online.money_daisuki.api.monkey.basegame.particles.CreateParticleEmitterCommand;
import online.money_daisuki.api.monkey.basegame.particles.EmitAllParticlesCommand;
import online.money_daisuki.api.monkey.basegame.particles.FlexibleParticleEmitterLoader;
import online.money_daisuki.api.monkey.basegame.player.SetPlayerControlEnabledCommand;
import online.money_daisuki.api.monkey.basegame.player.SetPlayerEnabledCommand;
import online.money_daisuki.api.monkey.basegame.player.SetPlayerJumpSpeedCommand;
import online.money_daisuki.api.monkey.basegame.script.CommandExecutorAppState;
import online.money_daisuki.api.monkey.basegame.script.ExecCommand;
import online.money_daisuki.api.monkey.basegame.sky.CreateSkyCommand;
import online.money_daisuki.api.monkey.basegame.sky.RemoveSkyCommand;
import online.money_daisuki.api.monkey.basegame.spatial.DetachSpatialCommand;
import online.money_daisuki.api.monkey.basegame.spatial.MoveToCommand;
import online.money_daisuki.api.monkey.basegame.spatial.PrintScenegraphJsonCommand;
import online.money_daisuki.api.monkey.basegame.spatial.PrintSpatialTransformCommand;
import online.money_daisuki.api.monkey.basegame.spatial.SetNodeCullHintCommand;
import online.money_daisuki.api.monkey.basegame.spatial.SetSpatialRotationCommand;
import online.money_daisuki.api.monkey.basegame.spatial.SetSpatialTranslationCommand;
import online.money_daisuki.api.monkey.basegame.spatial.TurnToCommand;
import online.money_daisuki.api.monkey.basegame.spatial.UnloadCommand;
import online.money_daisuki.api.monkey.basegame.spatial.UnloadSceneCommand;
import online.money_daisuki.api.monkey.basegame.terrain.AddTerrainCommand;
import online.money_daisuki.api.monkey.basegame.terrain.FlexibleTerrainLoader;
import online.money_daisuki.api.monkey.basegame.test.AddPlayerCommand;
import online.money_daisuki.api.monkey.basegame.text.ClearTextCommand;
import online.money_daisuki.api.monkey.basegame.text.MessageBoxAppState;
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
import online.money_daisuki.api.monkey.basegame.variables.VariablesManager;
import online.money_daisuki.api.monkey.basegame.variables.VariablesManagerAppState;
import online.money_daisuki.api.monkey.basegame.variables.VariablesManagerImpl;
import online.money_daisuki.api.monkey.console.CommandExecutor;
import online.money_daisuki.api.monkey.console.CommandExecutorImpl;
import online.money_daisuki.api.monkey.console.CommandStringDataSink;
import online.money_daisuki.api.monkey.console.ConsoleAppState;
import online.money_daisuki.api.monkey.console.ConsoleGui;
import online.money_daisuki.api.monkey.console.ConsoleNodeBuilder;

public final class FullInitializer implements DataSink<ModulesApp> {
	private RemoveDoneAppState removeDoneState;
	private final SetableMutableSingleValueModel<Spatial> playerContainer = new SetableMutableSingleValueModelImpl<>();
	
	private Collection<DataSink<? super ModulesApp>> initializers;
	private DataSink<? super ModulesApp> callback;
	
	@Override
	public void sink(final ModulesApp app) {
		app.getFlyByCamera().setEnabled(false);
		
		//final MutableSingleValueModel<File> screenshotDirectory = new MutableSingleValueModelImpl<>(new File("."));
		
		final AssetManager assetManager = app.getAssetManager();
		assetManager.registerLocator("", FileLocator.class);
		
		removeDoneState = new RemoveDoneAppState();
		
		final AppStateManager stateManager = app.getStateManager();
		
		final VariablesManager vars = new VariablesManagerImpl();
		stateManager.attach(new VariablesManagerAppState(vars));
		
		final CommandExecutorAppState exe = new CommandExecutorAppState(new CommandExecutorImpl(), vars, app.getRootNode());
		stateManager.attach(exe);
		
		stateManager.attach(new RemoveDoneControlsAppState(30.0f));
//		stateManager.attach(new GraphStructureAppState());
		
		app.setDisplayFps(false);
		app.setDisplayStatView(false);
		
		
		stateManager.attach(new BulletAppState());
		
		
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
						return(app.getStateManager().getState(SceneGraphAppState.class).getRootNode());
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
						
						final Spatial rootChild = app.getRootNode().getChild(a);
						if(rootChild == null) {
							throw new IllegalArgumentException("Node " + a + " not found.");
						}
						return(rootChild);
				}
			}
		};
		final BiConverter<String, Spatial, Node> nodeTarget = new BiConverter<String, Spatial, Node>() {
			@Override
			public Node convert(final String name, final Spatial caller) {
				final Spatial targetSpatial = spatialTarget.convert(name, caller);
				if(!(targetSpatial instanceof Node)) {
					throw new IllegalArgumentException("Target spatial is not a node");
				}
				return((Node) targetSpatial);
			}
		};
		
		final SetableMutableSingleValueModel<Spatial> sky = new SetableMutableSingleValueModelImpl<>();
		
		installCamCommands(app, exe, spatialTarget);
		installLightCommands(exe, spatialTarget);
		installModelCommands(app, exe, vars, nodeTarget);
		installTerrainCommands(app, exe);
		installTextboxCommands(app);
		installSpatialCommands(app, exe, spatialTarget, sky);
		installAudioCommands(app, exe, spatialTarget);
		installParticleCommands(app, exe, spatialTarget, nodeTarget);
		installFilterCommands(app, exe);
		installCharacterCommands(app, exe, spatialTarget);
		
		// Misc
		exe.addCommand("DebugBullet", new DebugBulletCommand());
		exe.addCommand("Profiler", new ProfilerCommand(stateManager));
		exe.addCommand("SetStats", new SetStatsCommand());
		exe.addCommand("SetVariable", new SetVariableCommand(vars));
		exe.addCommand("IncVariable", new IncVariableCommand(vars));
		exe.addCommand("ClearVariablenType", new ClearVariablenTypeCommand(vars));
		exe.addCommand("ClearVariables", new ClearVariablesCommand(vars));
		
		exe.addCommand("CreateSky", new CreateSkyCommand(sky, nodeTarget, app));
		exe.addCommand("RemoveSky", new RemoveSkyCommand(sky));
		
		final Node relectNode = new Node();
		app.getRootNode().attachChild(relectNode);
		
		//installConsole(app);
		
		/*final BitmapFont guiFont = app.getAssetManager().loadFont("Interface/Fonts/Default.fnt");
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
				
				app.getGuiNode().attachChild(tookScreenshotBitmapText);
				
				final MayDoneAppState state = new OneTimeDelayedRunAppState(new DetachSpatialAppState(tookScreenshotBitmapText), 5.0f);
				app.getStateManager().attach(state);
				removeDoneState.addAppState(state);
			}
		});
		screenshot.addCapturingFailedListener(new Runnable() {
			@Override
			public  void run() {
				tookScreenshotBitmapText.setText("Screenshot taken failed");
				tookScreenshotBitmapText.setColor(ColorRGBA.Red);
				
				app.getGuiNode().attachChild(tookScreenshotBitmapText);
				
				final MayDoneAppState state = new OneTimeDelayedRunAppState(new DetachSpatialAppState(tookScreenshotBitmapText), 5.0f);
				stateManager.attach(state);
				removeDoneState.addAppState(state);
			}
		});
		app.getStateManager().attach(screenshot);*/
		
		//exe.addCommand("SetScreenshotDirectory", new SetScreenshotDirectoryCommand(screenshotDirectory));
		//exe.addCommand("TakeScreenshot", new TakeScreenshotCommand(screenshot));
		
		/*final FilterPostProcessor foo = new FilterPostProcessor(app.getAssetManager());
		foo.addFilter(new CartoonEdgeFilter());
		app.getViewPort().addProcessor(foo);*/
		
		app.getInputManager().addMapping("ControlMoveUp", new KeyTrigger(KeyInput.KEY_W));
		app.getInputManager().addMapping("ControlMoveLeft", new KeyTrigger(KeyInput.KEY_A));
		app.getInputManager().addMapping("ControlMoveDown", new KeyTrigger(KeyInput.KEY_S));
		app.getInputManager().addMapping("ControlMoveRight", new KeyTrigger(KeyInput.KEY_D));
		
		app.getInputManager().addMapping("ControlStrike", new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));
		app.getInputManager().addMapping("ControlJump", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
		
		app.getInputManager().addMapping("CamMoveUp", new KeyTrigger(KeyInput.KEY_NUMPAD8));
		app.getInputManager().addMapping("CamMoveRight", new KeyTrigger(KeyInput.KEY_NUMPAD6));
		app.getInputManager().addMapping("CamMoveDown", new KeyTrigger(KeyInput.KEY_NUMPAD2));
		app.getInputManager().addMapping("CamMoveLeft", new KeyTrigger(KeyInput.KEY_NUMPAD4));
		
		app.getInputManager().addMapping("CamZoomIn", new KeyTrigger(KeyInput.KEY_NUMPAD9));
		app.getInputManager().addMapping("CamZoomOut", new KeyTrigger(KeyInput.KEY_NUMPAD3));
		
		app.getInputManager().addMapping("TakeScreenshot", new KeyTrigger(KeyInput.KEY_SYSRQ));
		
		app.getInputManager().addListener(new ActionListener() {
			@Override
			public void onAction(final String name, final boolean isPressed, final float tpf) {
				if(isPressed) {
					//screenshot.capture();
				}
			}
		}, "TakeScreenshot");
		
		app.getStateManager().attach(new FrequencyDividingAppState(removeDoneState, 30.0f));
		exe.addCommand("Exec", new ExecCommand(app));
		
		app.getRootNode().addLight(new AmbientLight(ColorRGBA.White));
		
	}
	private void installCamCommands(final ModulesApp app, final CommandExecutor exe, final BiConverter<String, Spatial, Spatial> spatialTarget) {
		exe.addCommand("SetCameraFrustumFar", new SetCameraFrustumFarCommand(app.getCamera()));
		exe.addCommand("SetCameraFrustumNear", new SetCameraFrustumNearCommand(app.getCamera()));
		exe.addCommand("SetCameraFrustumLeft", new SetCameraFrustumLeftCommand(app.getCamera()));
		exe.addCommand("SetCameraFrustumRight", new SetCameraFrustumRightCommand(app.getCamera()));
		exe.addCommand("SetCameraFrustumBottom", new SetCameraFrustumBottomCommand(app.getCamera()));
		exe.addCommand("SetCameraFrustumTop", new SetCameraFrustumTopCommand(app.getCamera()));
		
		exe.addCommand("SetFlycamMoveSpeed", new FlycamMoveSpeedCommand(app));
		exe.addCommand("SetFlycamEnabled", new SetFlycamEnabledCommand(app.getFlyByCamera()));
		
		exe.addCommand("DebugChaseCamera", new DebugCameraTransformCommand(app.getGuiNode(), playerContainer, app));
		exe.addCommand("PrintChaseCamera", new PrintChaseCameraTransformCommand(playerContainer, System.err));
		exe.addCommand("SetCameraTransform", new SetCameraTransformCommand(playerContainer));
		exe.addCommand("AddChaseCamera", new AddChaseCameraCommand(spatialTarget, app));
		exe.addCommand("RemoveChaseCamera", new RemoveChaseCameraCommand(spatialTarget, app));
		exe.addCommand("SetChaseCameraTransform", new SetChaseCameraTransformCommand(spatialTarget));
		exe.addCommand("SetChaseCameraEnabled", new SetChaseCameraEnabledCommand(spatialTarget));
		
		exe.addCommand("PrintCameraTransform", new PrintCameraTransformCommand(System.err));
	}
	private void installSpatialCommands(final ModulesApp app, final CommandExecutor exe, final BiConverter<String, Spatial, Spatial> spatialTarget,
			final Setable sky) {
		exe.addCommand("SetSpatialTranslation", new SetSpatialTranslationCommand(spatialTarget));
		exe.addCommand("SetSpatialRotation", new SetSpatialRotationCommand(spatialTarget));
		/*exe.addCommand("SetCull", new SetCullHintCommand(new Converter<String, HasSpatial>() {
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
		}));*/
		exe.addCommand("Unload", new UnloadCommand(app));
		exe.addCommand("TurnTo", new TurnToCommand(spatialTarget));
		exe.addCommand("MoveTo", new MoveToCommand(spatialTarget));
		exe.addCommand("UnloadScene", new UnloadSceneCommand(app.getRootNode(), playerContainer, sky, app));
		exe.addCommand("SetSpatialAnim", new SetSpatialAnimCommand(spatialTarget));
		exe.addCommand("MoveLinearTo", new MoveLinearToCommand(spatialTarget));
		exe.addCommand("RotateLinearBy", new RotateLinearByCommand(spatialTarget));
		exe.addCommand("PrintSpatialTransform", new PrintSpatialTransformCommand(spatialTarget, System.err));
		exe.addCommand("PrintScenegraphJson", new PrintScenegraphJsonCommand(spatialTarget, System.err));
		exe.addCommand("DetachSpatial", new DetachSpatialCommand(spatialTarget));
	}
	private void installAudioCommands(final Application app, final CommandExecutor exe, final BiConverter<String, Spatial, Spatial> spatialTarget) {
		exe.addCommand("CreateAudioNode", new CreateAudioNodeCommand(app.getAssetManager(), spatialTarget));
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
	private void installTerrainCommands(final ModulesApp app, final CommandExecutor exe) {
		final Converter<String, TerrainQuad> ldr = new FlexibleTerrainLoader(new FlexibleMaterialLoader(app), app);
		
		exe.addCommand("AddTerrain", new AddTerrainCommand(
				ldr,
				app.getRootNode(),
				app
				)
		);
		/*exe.addCommand("PreloadTerrain", new PreloadTerrainCommand(
				ldr
				));*/
	}
	private void installModelCommands(final Application app, final CommandExecutor exe, final VariablesManager vars, final BiConverter<String, Spatial, Node> nodeTarget) {
		final Converter<String, Spatial> modelLoader = new FlexibleModelLoader(new FlexibleMaterialLoader(app), app);
		
		//final GraphStructureAppState graph = app.getStateManager().getState(GraphStructureAppState.class);
		
		exe.addCommand("AddModel",
				new AddModelCommand(
						modelLoader,
						nodeTarget,
						app
				)
		);
		
		/*exe.addCommand("AddScene",
				new AddSceneCommand(
						new FlexibleSceneLoader(characterLoader),
						graph.getRootNode(),
						app
				)
		);*/
		exe.addCommand("AddPlayer",
				new AddPlayerCommand(
						nodeTarget,
						playerContainer
				)
		);
		exe.addCommand("AddForm", new AddFormCommand());
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
	private void installTextboxCommands(final ModulesApp app) {
		final CommandExecutorAppState exe = app.getStateManager().getState(CommandExecutorAppState.class);
		
		final ShowTextUiBuilder builder = new ShowTextUiBuilder(app.getAssetManager());
		builder.setBackgroundWidth(app.getGuiViewPort().getCamera().getWidth());
		final Node uiNode = builder.source();
		uiNode.setLocalTranslation(0, 0, 0);
		uiNode.setCullHint(CullHint.Always);
		app.getGuiNode().attachChild(uiNode);
		
		final ShowTextAppState textAppState = new ShowTextAppState(new ShowTextAppStateUi(new DataSink<Boolean>() {
			@Override
			public void sink(final Boolean value) {
				uiNode.setCullHint(value ? CullHint.Dynamic : CullHint.Always);
			}
		}, new ShowTextNodeToLinesConverter().convert(uiNode), 100), 0.05f);
		
		app.getStateManager().attach(textAppState);
		
		app.getStateManager().attach(new MessageBoxAppState());
		
		
		final MutableSingleValueModel<String> languageKey = new MutableSingleValueModelImpl<>("en");
		
		exe.addCommand("SetTextCull", new SetNodeCullHintCommand(uiNode));
		exe.addCommand("ShowText", new ShowTextCommand(textAppState));
		exe.addCommand("ShowControlledText", new ShowControlledTextCommand(textAppState, app.getInputManager(), app));
		exe.addCommand("ClearText", new ClearTextCommand(textAppState));
		exe.addCommand("SetLanguageKey", new SetLanguageKeyCommand(languageKey));
		exe.addCommand("ShowControlledTextFromFile", new ShowControlledTextFromFileCommand(textAppState, app.getInputManager(), app,
				languageKey));
	}
	private void installParticleCommands(final ModulesApp app, final CommandExecutor exe, final BiConverter<String, Spatial, Spatial> spatialTarget,
			final BiConverter<String, Spatial, Node> nodeTarget) {
		final FlexibleParticleEmitterLoader particleLoader = new FlexibleParticleEmitterLoader(new FlexibleMaterialLoader(app), app);
		exe.addCommand("CreateParticleEmitter", new CreateParticleEmitterCommand(nodeTarget, particleLoader));
		exe.addCommand("EmitAllParticles", new EmitAllParticlesCommand(spatialTarget));
	}
	private void installFilterCommands(final ModulesApp app, final CommandExecutor exe) {
		final FilterPostProcessor fpp = new FilterPostProcessor(app.getAssetManager());
		app.getViewPort().addProcessor(fpp);
		
		final FadeFilter fade = new FadeFilter();
		fade.setValue(0.0f);
		fpp.addFilter(fade);
		app.getStateManager().attach(new FadeAppState(fade));
		
		exe.addCommand("FadeOut", new FadeOutCommand());
		exe.addCommand("FadeIn", new FadeInCommand());
		exe.addCommand("SetFadeDuration", new SetFadeDurationCommand(app));
	}
	private void installCharacterCommands(final ModulesApp app, final CommandExecutor exe, final BiConverter<String, Spatial, Spatial> spatialTarget) {
		exe.addCommand("AddCharacterControl", new AddCharacterControlCommand(spatialTarget, app));
		exe.addCommand("RemoveCharacterControl", new RemoveCharacterControlCommand(spatialTarget));
	}
	
	private void installConsole(final ModulesApp app) {
		final CommandExecutor exe = app.getStateManager().getState(CommandExecutorAppState.class);
		
		final ViewPort vp = app.getViewPort();
		final ConsoleNodeBuilder cnb = new ConsoleNodeBuilder(app.getAssetManager(), vp.getCamera().getWidth(), vp.getCamera().getHeight(), app.getGuiNode());
		final ConsoleGui console = cnb.source();
		console.setVisible(false);
		app.getStateManager().attach(new ConsoleAppState(console, new CommandStringDataSink(exe, new Node("ConsoleDummyNode"))));
		console.attach();
		
		
		/*final NiftyJmeDisplay niftyDisplay = NiftyJmeDisplay.newNiftyJmeDisplay(
				getAssetManager(),
				getInputManager(),
				getAudioRenderer(),
				getGuiViewPort()
		);
		
		final Nifty nifty = niftyDisplay.getNifty();
		getGuiViewPort().addProcessor(niftyDisplay);
		
		nifty.loadStyleFile("nifty-default-styles.xml");
		nifty.loadControlFile("nifty-default-controls.xml");
		
		nifty.addScreen("Screen_ID", new ScreenBuilder("Hello Nifty Screen"){{
			controller(new DefaultScreenController());
			
			layer(new LayerBuilder("Layer_ID") {{
				childLayoutVertical(); 
				focusable(false);
				
				panel(new PanelBuilder("Panel_ID") {{
					childLayoutCenter();
					focusable(false);
					
					control(new TextFieldBuilder("textfield") {
						{
							text("TestText");
							height("120");
						}
					});
				}});
			}});
		}}.build(nifty));
		
		nifty.gotoScreen("Screen_ID"); // start the screen
		
		nifty.getCurrentScreen().findNiftyControl("textfield", TextField.class).getElement().attachInputControl(new KeyInputHandler() {
			@Override
			public boolean keyEvent(final NiftyInputEvent arg0) {
				return(false);
			}
		});*/
		
		final SetableMutableSingleValueModel<Boolean> cameraWasDisabledOnShow = new SetableMutableSingleValueModelImpl<>();
		
		console.addConsoleShownListener(new Runnable() {
			@Override
			public void run() {
				// TODO more generic
				if(playerContainer.isSet()) {
					final ChaseCamera cam = playerContainer.source().getControl(ChaseCamera.class);
					if(cam != null) {
						if(cam.isEnabled()) {
							cam.setEnabled(false);
							cameraWasDisabledOnShow.sink(Boolean.TRUE);
						}
					}
				}
			}
		});
		console.addConsoleHiddenListener(new Runnable() {
			@Override
			public void run() {
				if(cameraWasDisabledOnShow.isSet()) {
					final ChaseCamera cam = playerContainer.source().getControl(ChaseCamera.class);
					cam.setEnabled(true);
					cameraWasDisabledOnShow.unset();
				}
			}
		});
	}
	
	public void addInititalizer(final DataSink<? super ModulesApp> initalizer) {
		if(initializers == null) {
			throw new IllegalStateException("Already initialized");
		}
		this.initializers.add(initalizer);
	}
}

