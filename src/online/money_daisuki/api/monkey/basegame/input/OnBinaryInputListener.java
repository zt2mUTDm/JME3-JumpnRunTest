package online.money_daisuki.api.monkey.basegame.input;

import org.python.core.PyBoolean;
import org.python.core.PyFloat;
import org.python.core.PyObject;
import org.python.core.PyString;

import com.jme3.input.controls.ActionListener;

import online.money_daisuki.api.base.Requires;

public final class OnBinaryInputListener implements ActionListener {
	private final PyObject target;
	
	public OnBinaryInputListener(final PyObject target) {
		this.target = Requires.notNull(target, "target == null");
	}
	@Override
	public void onAction(final String name, final boolean isPressed, final float tpf) {
		target.invoke("onBinaryInputEvent", new PyObject[] {
				new PyString(name), new PyBoolean(isPressed), new PyFloat(tpf)
		});
	}
}
