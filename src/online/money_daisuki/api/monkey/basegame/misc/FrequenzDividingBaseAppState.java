package online.money_daisuki.api.monkey.basegame.misc;

import com.jme3.app.state.BaseAppState;

import online.money_daisuki.api.base.Requires;

public abstract class FrequenzDividingBaseAppState extends BaseAppState {
	private float counter;
	
	private final float speed;
	
	protected FrequenzDividingBaseAppState(final float speed) {
		this.speed = Requires.greaterThanZero(speed, "speed <= 0");
	}
	
	@Override
	public final void update(final float tpf) {
		counter+= tpf;
		while(counter >= speed) {
			doUpdate(tpf);
			counter-= speed;
		}
	}
	protected abstract void doUpdate(final float tpf);
}
