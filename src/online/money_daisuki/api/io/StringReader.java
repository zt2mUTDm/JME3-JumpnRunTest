package online.money_daisuki.api.io;

import java.io.CharArrayReader;
import java.io.IOException;
import java.io.Reader;

/* No GWT source available for Oracle impl.*/
public final class StringReader extends Reader {
	private final Reader backend;
	
	public StringReader(final String input) {
		if(input == null) {
			throw new IllegalArgumentException("input == null");
		}
		backend = new CharArrayReader(input.toCharArray());
	}
	@Override
	public void mark(final int readAheadLimit) throws IOException {
		backend.mark(readAheadLimit);
	}
	@Override
	public boolean markSupported() {
		return(backend.markSupported());
	}
	@Override
	public int read() throws IOException {
		return(backend.read());
	}
	@Override
	public int read(final char[] cbuf) throws IOException {
		return(backend.read(cbuf));
	}
	@Override
	public int read(final char[] cbuf, final int off, final int len) throws IOException {
		return(backend.read(cbuf, off, len));
	}
	@Override
	public boolean ready() throws IOException {
		return(backend.ready());
	}
	@Override
	public void reset() throws IOException {
		backend.reset();
	}
	@Override
	public long skip(final long n) throws IOException {
		return(backend.skip(n));
	}
	@Override
	public void close() throws IOException {
		 backend.close();
	}
}