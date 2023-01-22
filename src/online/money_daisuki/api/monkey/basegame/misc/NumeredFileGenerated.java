package online.money_daisuki.api.monkey.basegame.misc;

import java.io.File;
import java.io.IOException;

import online.money_daisuki.api.base.DataSource;
import online.money_daisuki.api.base.Requires;

public final class NumeredFileGenerated implements DataSource<File> {
	private final DataSource<? extends File> parent;
	private final DataSource<? extends String> filename;
	private final DataSource<? extends String> extension;
	private final int maxTries;
	
	public NumeredFileGenerated(final DataSource<? extends File> parent, final DataSource<? extends String> filename,
			final DataSource<? extends String> extension, final int maxTries) {
		this.parent = Requires.notNull(parent, "parent == null");
		this.filename = Requires.notNull(filename, "filename == null");
		this.extension = Requires.notNull(extension, "extension == null");
		this.maxTries = Requires.greaterThanZero(maxTries, "maxTries < 1");
	}
	@Override
	public File source() {
		final File p = parent.source();
		final String fn = filename.source();
		final String e = extension.source();
		
		File f = null;
		
		final StringBuffer sb = new StringBuffer(fn);
		for(int i = -1; i < maxTries && f == null; i++) {
			if(i == -1) {
				sb.append(".");
				sb.append(e);
			} else {
				sb.setLength(fn.length());
				sb.append(".");
				sb.append(i);
				sb.append(".");
				sb.append(e);
			}
			
			final String newFilename = sb.toString();
			final File newFile = new File(p, newFilename);
			if(!newFile.exists()) {
				try {
					newFile.createNewFile();
				} catch (final IOException e1) {
					throw new RuntimeException(e1);
				}
				f = newFile;
			}
		}
		
		if(f != null) {
			return(f);
		}
		throw new IllegalArgumentException("Found no available filename with " + maxTries + "tries");
	}
}
