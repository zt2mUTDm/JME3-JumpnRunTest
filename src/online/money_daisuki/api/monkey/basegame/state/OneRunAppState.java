package online.money_daisuki.api.monkey.basegame.state;

import online.money_daisuki.api.base.DataSink;
import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.monkey.basegame.misc.MayDoneAppState;

public final class OneRunAppState extends AppStateAdapter implements MayDoneAppState {
	private boolean done;
	
	private final DataSink<? super OneRunAppState> parent;
	
	public OneRunAppState(final DataSink<? super OneRunAppState> parent) {
		this.parent = Requires.notNull(parent, "parent == null");
	}
	@Override
	public void update(final float tpf) {
		if(!done && isInitialized()) {
			parent.sink(this);
			done = true;
		}
	}
	@Override
	public boolean isDone() {
		return(done);
	}
}
