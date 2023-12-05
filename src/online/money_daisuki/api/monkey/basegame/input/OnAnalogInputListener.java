package online.money_daisuki.api.monkey.basegame.input;

import org.python.core.PyFloat;
import org.python.core.PyObject;
import org.python.core.PyString;

import com.jme3.input.controls.AnalogListener;

import online.money_daisuki.api.base.Requires;

public final class OnAnalogInputListener implements AnalogListener {
	private final PyObject target;
	
	public OnAnalogInputListener(final PyObject target) {
		this.target = Requires.notNull(target, "target == null");
	}
	@Override
	public void onAnalog(final String name, final float value, final float tpf) {
		target.invoke("onAnalogInputEvent", new PyObject[] {
				new PyString(name), new PyFloat(value), new PyFloat(tpf)
		});
	}
}
