package online.money_daisuki.api.io;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import online.money_daisuki.api.base.Requires;

public final class CharacterStreamConcater implements Runnable {
	private final Reader in;
	private final Writer out;
	private final int bufferSize;
	
	public CharacterStreamConcater(final Reader in, final Writer out) {
		this(in, out, 16 * 1024); // 16 kiB-Chunks
	}
	public CharacterStreamConcater(final Reader in, final Writer out, final int bufferSize) {
		this.in = Requires.notNull(in, "in == null");
		this.out = Requires.notNull(out, "out == null");
		this.bufferSize = Requires.greaterThanZero(bufferSize, "bufferSize <= 0");
	}
	
	@Override
	public void run() {
		final char[] buffer = new char[bufferSize];
		
		try {
			int count;
			do {
				count = in.read(buffer, 0, bufferSize);
				if(count > 0) {
					out.write(buffer, 0, count);
				}
			} while(count >= 0);
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
	}
}
