package online.money_daisuki.api.monkey.basegame.misc;

import java.io.File;

import com.jme3.scene.Spatial;

import online.money_daisuki.api.base.DataSink;
import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.monkey.console.Command;

public final class SetScreenshotDirectoryCommand implements Command {
	private final DataSink<? super File> screenshotDirectory;
	
	public SetScreenshotDirectoryCommand(final DataSink<? super File> screenshotDirectory) {
		this.screenshotDirectory = Requires.notNull(screenshotDirectory, "screenshotDirectory == null");
	}
	@Override
	public void execute(final Spatial caller, final String[] cmd, final Runnable done) {
		Requires.notNull(caller, "caller == null");
		Requires.containsNotNull(cmd, "cmd contains null");
		Requires.lenEqual(cmd, 2);
		
		screenshotDirectory.sink(new File(cmd[1]));
		done.run();
	}
}
