package online.money_daisuki.api.io;

import java.io.IOException;
import java.io.Reader;
import java.util.Deque;
import java.util.LinkedList;

public final class PushbackReader extends Reader {
	private final Reader parent;
	// Easy impl, poor performance because boxing.
	private final Deque<Integer> pushbackBuffer;
	
	public PushbackReader(final Reader in) {
		if(in == null) {
			throw new IllegalArgumentException("in == null");
		}
		this.parent = in;
		pushbackBuffer = new LinkedList<>();
	}
	
	@Override
	public int read(final char[] cbuf, final int off, final int len) throws IOException {
		int i = 0;
		for(; i < len; i++) {
			final int j = doRead();
			if(j == -1) {
				return(i > 0 ? i : -1);
			}
			cbuf[off + i] = (char)j;
		}
		return(i);
	}
	private int doRead() throws IOException {
		if(pushbackBuffer.size() > 0) {
			return(pushbackBuffer.removeLast());
		}
		return(parent.read());
	}

	public void pushBack(final char c) {
	    pushBack((int)c);
	}
	public void pushBack(final int i) {
	    pushbackBuffer.addLast(i);
	}
	
	@Override
	public void close() throws IOException {
		parent.close();
	}		
}