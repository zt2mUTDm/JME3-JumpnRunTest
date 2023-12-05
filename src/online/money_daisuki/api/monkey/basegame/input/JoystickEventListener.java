package online.money_daisuki.api.monkey.basegame.input;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

import org.python.core.Py;
import org.python.core.PyBoolean;
import org.python.core.PyFloat;
import org.python.core.PyObject;
import org.python.core.PyString;

import com.jme3.input.InputManager;
import com.jme3.input.JoystickAxis;
import com.jme3.input.JoystickButton;
import com.jme3.input.RawInputListener;
import com.jme3.input.event.JoyAxisEvent;
import com.jme3.input.event.JoyButtonEvent;
import com.jme3.input.event.KeyInputEvent;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.input.event.MouseMotionEvent;
import com.jme3.input.event.TouchEvent;

import online.money_daisuki.api.base.Requires;

public final class JoystickEventListener implements RawInputListener {
	private final InputManager inputManager;
	
	private final Map<JoystickAxis, Float> lastAxisValues;
	private Map<JoystickAxis, Collection<String>> axisMappings;
	private Map<String, Collection<PyObject>> axisListeners;
	
	private Map<JoystickButton, Collection<String>> buttonMappings;
	private Map<String, Collection<PyObject>> buttonListeners;
	
	public JoystickEventListener(final InputManager inputManager) {
		this.inputManager = Requires.notNull(inputManager, "inputManager == null");
		this.lastAxisValues = new HashMap<>();
	}
	
	@Override
	public void onJoyAxisEvent(final JoyAxisEvent evt) {
		final JoystickAxis axis = evt.getAxis();
		
		final Float last = lastAxisValues.remove(evt.getAxis());
		float value = evt.getValue();
		
		final float effectiveDeadZone = Math.max(inputManager.getAxisDeadZone(), axis.getDeadZone());
		if(Math.abs(value) < effectiveDeadZone) {
			if(last == null) {
				return;
			}
			lastAxisValues.remove(evt.getAxis());
			value = 0;
		}
		
		if(axisMappings != null && axisListeners != null) {
			if(axisMappings.containsKey(axis)) {
				for(final String s:axisMappings.get(axis)) {
					if(axisListeners.containsKey(s)) {
						final Collection<PyObject> c = axisListeners.get(s);
						for(final PyObject l:c) {
							l.invoke("onJoystickAxisEvent", new PyObject[] {
									new PyString(s),
									new PyFloat(value)
							});
						}
					}
				}
			}
		}
		
		if(value != 0) {
			lastAxisValues.put(evt.getAxis(), value);
		} 
	}
	
	public void addAxisMappings(final JoystickAxis axis, final String... mappings) {
		if(mappings.length >= 1) {
			if(axisMappings == null) {
				axisMappings = new HashMap<>();
			}
			
			if(!axisMappings.containsKey(axis)) {
				axisMappings.put(axis, new LinkedList<>());
			}
			final Collection<String> m = axisMappings.get(axis);
			m.addAll(Arrays.asList(mappings));
		}
	}
	public void removeAxisMappings(final String... mappings) {
		if(mappings.length >= 1) {
			final Collection<String> m = Arrays.asList(mappings);
			
			final Iterator<Entry<JoystickAxis, Collection<String>>> it = axisMappings.entrySet().iterator();
			while(it.hasNext()) {
				final Entry<JoystickAxis, Collection<String>> e = it.next();
				final Collection<String> c = e.getValue();
				if(c.removeAll(m) && c.isEmpty()) {
					it.remove();
				}
			}
			
			if(axisMappings.isEmpty()) {
				axisMappings = null;
			}
		}
	}
	public int getAxisMappingCount() {
		return (axisMappings != null ? axisMappings.size() : 0);
	}
	public void clearAxisMappingCount() {
		axisMappings = null;
	}
	
	public void addAxisListener(final PyObject listener, final String... mappings) {
		if(mappings.length >= 1) {
			if(axisListeners == null) {
				axisListeners = new HashMap<>();
			}
			
			for(final String mapping:mappings) {
				if(!axisListeners.containsKey(mapping)) {
					axisListeners.put(mapping, new LinkedList<>());
				}
				final Collection<PyObject> m = axisListeners.get(mapping);
				m.add(listener);
			}
		}
	}
	public void removeAxisListener(final PyObject listener) {
		final Iterator<Entry<String, Collection<PyObject>>> it = axisListeners.entrySet().iterator();
		while(it.hasNext()) {
			final Entry<String, Collection<PyObject>> e = it.next();
			final Collection<PyObject> c = e.getValue();
			if(c.remove(listener) && c.isEmpty()) {
				it.remove();
			}
			
			if(axisListeners.isEmpty()) {
				axisListeners = null;
			}
		}
	}
	public int getAxisListenerCount() {
		return (axisListeners != null ? axisListeners.size() : 0);
	}
	public void clearAxisListeners() {
		axisListeners = null;
	}
	
	@Override
	public void onJoyButtonEvent(final JoyButtonEvent evt) {
		if(buttonMappings == null || buttonListeners == null) {
			return;
		}
		
		final JoystickButton button = evt.getButton();
		if(buttonMappings.containsKey(button)) {
			for(final String s:buttonMappings.get(button)) {
				if(buttonListeners.containsKey(s)) {
					final Collection<PyObject> c = buttonListeners.get(s);
					for(final PyObject l:c) {
						l.invoke("onJoystickButtonEvent", new PyObject[] {
								new PyString(s),
								Py.java2py(button),
								new PyBoolean(evt.isPressed())
						});
					}
				}
			}
		}
	}
	public void addButtonMappings(final JoystickButton button, final String... mappings) {
		if(mappings.length >= 1) {
			if(this.buttonMappings == null) {
				this.buttonMappings = new HashMap<>();
			}
			
			if(!this.buttonMappings.containsKey(button)) {
				this.buttonMappings.put(button, new HashSet<>());
			}
			final Collection<String> m = this.buttonMappings.get(button);
			m.addAll(Arrays.asList(mappings));
		}
	}
	public void removeButtonMappings(final String... mappings) {
		if(mappings.length >= 1) {
			final Collection<String> m = Arrays.asList(mappings);
			
			final Iterator<Entry<JoystickButton, Collection<String>>> it = this.buttonMappings.entrySet().iterator();
			while(it.hasNext()) {
				final Entry<JoystickButton, Collection<String>> e = it.next();
				final Collection<String> c = e.getValue();
				if(c.removeAll(m) && c.isEmpty()) {
					it.remove();
				}
			}
			
			if(this.buttonMappings.isEmpty()) {
				this.buttonMappings = null;
			}
		}
	}
	public int getButtonMappingCount() {
		return (buttonMappings != null ? buttonMappings.size() : 0);
	}
	public void clearButtonMappings() {
		this.buttonMappings = null;
	}
	
	public void addButtonListener(final PyObject listener, final String... mappings) {
		if(mappings.length >= 1) {
			if(this.buttonListeners == null) {
				this.buttonListeners = new HashMap<>();
			}
			
			for(final String mapping:mappings) {
				if(!this.buttonListeners.containsKey(mapping)) {
					this.buttonListeners.put(mapping, new HashSet<>());
				}
				final Collection<PyObject> m = this.buttonListeners.get(mapping);
				m.add(listener);
			}
		}
	}
	public void removeButtonListener(final PyObject listener) {
		if(this.buttonListeners == null) {
			return;
		}
		
		final Iterator<Entry<String, Collection<PyObject>>> it = this.buttonListeners.entrySet().iterator();
		while(it.hasNext()) {
			final Entry<String, Collection<PyObject>> e = it.next();
			final Collection<PyObject> c = e.getValue();
			if(c.remove(listener) && c.isEmpty()) {
				it.remove();
			}
		}
		
		if(this.buttonListeners.isEmpty()) {
			this.buttonListeners = null;
		}
	}
	public int getButtonListenerCount() {
		return (buttonListeners != null ? buttonListeners.size() : 0);
	}
	public void clearButtonListeners() {
		this.buttonListeners = null;
	}
	
	@Override
	public void beginInput() {}
	@Override
	public void endInput() {}
	@Override
	public void onMouseMotionEvent(final MouseMotionEvent evt) {}
	@Override
	public void onMouseButtonEvent(final MouseButtonEvent evt) {}
	@Override
	public void onKeyEvent(final KeyInputEvent evt) {}
	@Override
	public void onTouchEvent(final TouchEvent evt) {}
}
