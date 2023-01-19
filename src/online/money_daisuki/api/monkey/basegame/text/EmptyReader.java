package online.money_daisuki.api.monkey.basegame.text;

import java.io.IOException;
import java.io.Reader;

public final class EmptyReader extends Reader {
	
	@Override
	public int read() throws IOException {
		return(-1);
	}
	
	@Override
	public int read(final char[] cbuf) throws IOException {
		return(-1);
	}
	
	@Override
	public int read(final char[] cbuf, final int off, final int len) throws IOException {
		return(-1);
	}
	
	@Override
	public boolean markSupported() {
		return(false);
	}
	
	@Override
	public boolean ready() throws IOException {
		return(true);
	}
	
	@Override
	public void close() throws IOException {
		
	}
	
}
