package online.money_daisuki.projects.monkey.jumpnrun;

import java.io.CharArrayReader;
import java.io.CharArrayWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import com.jme3.app.state.AppState;
import com.jme3.asset.AssetManager;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.bullet.BulletAppState;
import com.jme3.light.AmbientLight;
import com.jme3.math.ColorRGBA;
import com.jme3.system.AppSettings;
import com.jme3.system.NativeLibraryLoader;

import online.money_daisuki.api.base.ArgumentHandler;
import online.money_daisuki.api.base.ArgumentParserResult;
import online.money_daisuki.api.base.DataSink;
import online.money_daisuki.api.io.CharacterStreamConcater;
import online.money_daisuki.api.monkey.basegame.ModulesApp;
import online.money_daisuki.api.monkey.basegame.cam.MoveCameraLinearToAppState;
import online.money_daisuki.api.monkey.basegame.py.PythonAppState;
import online.money_daisuki.api.monkey.basegame.state.OneRunAppState;

public final class Main {
	public static void main(final String[] args) throws IOException {
		final ArgumentHandler handler = new ArgumentHandler(args);
		handler.addSwitch("disable-security-manager-do-not-use", 0);
		
		final ArgumentParserResult result = handler.source();
		
		NativeLibraryLoader.setCustomExtractionFolder("extract/");
		/*if(!result.containsSwitch("disable-security-manager-do-not-use")) {
			setupPolicy();
		}*/
		
		
		final ModulesApp app = new ModulesApp(new DataSink<ModulesApp>() {
			@Override
			public void sink(final ModulesApp app) {
				final OneRunAppState initAppState = new OneRunAppState(new DataSink<AppState>() {
					@Override
					public void sink(final AppState value) {
						// Temporary bootstrap
						
						app.getStateManager().getState(BulletAppState.class).setDebugEnabled(true);
						
						final AssetManager assetManager = app.getAssetManager();
						assetManager.registerLocator("", FileLocator.class);
						
						app.getRootNode().addLight(new AmbientLight(ColorRGBA.White));
						app.getStateManager().getState(PythonAppState.class).addSingleScript("initScript", "InitScript");
						
						//app.getStateManager().getState(FadeAppState.class).fadeIn();
					}
				});
				app.getStateManager().attach(initAppState);
			}
		});
		
		app.getStateManager().attach(new MoveCameraLinearToAppState());
		
		final AppSettings settings = new AppSettings(false);
		settings.setUseJoysticks(true);
		app.setSettings(settings);
		
		app.start();
	}
	private static void setupPolicy() throws IOException {
		final File policyFile = File.createTempFile("java.security.policy", ".txt");
		try(final Reader in = new FileReader("security.policy")) {
			final CharArrayWriter buf = new CharArrayWriter();
			new CharacterStreamConcater(in, buf).run();
			
			String s = buf.toString();
			// Property file needs escaped backslashes
			s = s.replace("_Template:BinDirAbs_", new File("./bin").getAbsolutePath().replace("\\", "\\\\"));
			s = s.replace("_Template:ExtractDirectoryAbs_", new File("extract/").getAbsolutePath().replace("\\", "\\\\"));
			s = s.replace("_Template:PolicyFilePathAbs_", policyFile.getAbsolutePath());
			
			try(final Writer out = new FileWriter(policyFile)) {
				new CharacterStreamConcater(new CharArrayReader(s.toCharArray()), out).run();
			}
		}
		policyFile.deleteOnExit();
		
		System.setProperty("java.security.policy", policyFile.getPath());
		System.setSecurityManager(new SecurityManager());
	}
}
