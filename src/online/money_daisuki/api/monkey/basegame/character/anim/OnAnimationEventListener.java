package online.money_daisuki.api.monkey.basegame.character.anim;

import org.python.core.PyBoolean;
import org.python.core.PyObject;
import org.python.core.PyString;

import online.money_daisuki.api.base.Requires;

public final class OnAnimationEventListener implements AnimListener {
	private final PyObject target;
	private final PyObject sender;
	
	public OnAnimationEventListener(final PyObject target, final PyObject sender) {
		this.target = Requires.notNull(target, "target == null");
		this.sender = Requires.notNull(sender, "sender == null");
	}
	@Override
	public void animationEvent(final String name, final boolean loop) {
		target.invoke("onAnimationEvent", new PyObject[] {
				new PyString(name), new PyBoolean(loop), sender
		});
	}
}
