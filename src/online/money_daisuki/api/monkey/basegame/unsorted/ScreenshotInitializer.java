package online.money_daisuki.api.monkey.basegame.unsorted;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.math.ColorRGBA;

import online.money_daisuki.api.base.ConstantDataSource;
import online.money_daisuki.api.base.DataSink;
import online.money_daisuki.api.base.DataSource;
import online.money_daisuki.api.base.models.MutableSingleValueModel;
import online.money_daisuki.api.base.models.MutableSingleValueModelImpl;
import online.money_daisuki.api.monkey.basegame.ModulesApp;
import online.money_daisuki.api.monkey.basegame.misc.FormatDateAsStringSource;
import online.money_daisuki.api.monkey.basegame.misc.MayDoneAppState;
import online.money_daisuki.api.monkey.basegame.misc.NumeredFileGenerated;
import online.money_daisuki.api.monkey.basegame.misc.OneTimeDelayedRunAppState;
import online.money_daisuki.api.monkey.basegame.misc.OwnScreenshotAppState;
import online.money_daisuki.api.monkey.basegame.spatial.DetachSpatialAppState;

public final class ScreenshotInitializer implements DataSink<ModulesApp> {
	@Override
	public void sink(final ModulesApp app) {
		final MutableSingleValueModel<File> screenshotDirectory = new MutableSingleValueModelImpl<>(new File(".")); // TODO
		
		final BitmapFont guiFont = app.getAssetManager().loadFont("Interface/Fonts/Default.fnt");
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
				//removeDoneState.addAppState(state);
			}
		});
		screenshot.addCapturingFailedListener(new Runnable() {
			@Override
			public  void run() {
				tookScreenshotBitmapText.setText("Screenshot taken failed");
				tookScreenshotBitmapText.setColor(ColorRGBA.Red);
				
				app.getGuiNode().attachChild(tookScreenshotBitmapText);
				
				final MayDoneAppState state = new OneTimeDelayedRunAppState(new DetachSpatialAppState(tookScreenshotBitmapText), 5.0f);
				app.getStateManager().attach(state);
				//removeDoneState.addAppState(state);
			}
		});
		app.getStateManager().attach(screenshot);
	}
}
