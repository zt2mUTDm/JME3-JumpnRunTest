package online.money_daisuki.api.monkey.basegame.cinematic;

import org.python.core.Py;
import org.python.core.PyObject;

import com.jme3.cinematic.MotionPathListener;
import com.jme3.cinematic.events.MotionEvent;

import online.money_daisuki.api.base.Requires;

public final class PythonMotionPathListener implements MotionPathListener {
	private final PyObject target;
	
	public PythonMotionPathListener(final PyObject target) {
		this.target = Requires.notNull(target, "target == null");
	}
	@Override
	public void onWayPointReach(final MotionEvent motionControl, final int wayPointIndex) {
		target.invoke("onWaypointReach", new PyObject[] {
				Py.java2py(motionControl),
				Py.java2py(wayPointIndex)
		});
	}
}
