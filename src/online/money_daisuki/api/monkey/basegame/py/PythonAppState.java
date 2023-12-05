package online.money_daisuki.api.monkey.basegame.py;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import org.python.core.Py;
import org.python.core.PyFloat;
import org.python.core.PyNone;
import org.python.core.PyObject;
import org.python.core.PyString;
import org.python.util.PythonInterpreter;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.util.SafeArrayList;

import online.money_daisuki.api.io.json.JsonDecoder;
import online.money_daisuki.api.io.json.JsonMap;
import online.money_daisuki.api.monkey.basegame.form.Form;
import online.money_daisuki.api.monkey.basegame.form.FormDataStoreState;

/**
 * Not numeric stable.
 * TODO: Make numeric stable
 * @author Money Daisuki Online
 */
public final class PythonAppState extends BaseAppState {
	private Application app;
	
	private final PythonInterpreter interpreter;
	private final SafeArrayList<PyObject> updatables;
	
	private final PyObject glob;
	private final PyObject forms;
	
	private Collection<PyObject> singleScripts;
	private SafeArrayList<PyObject> firstUpdatables;
	
	public PythonAppState(final File scriptDirectory) {
		this.interpreter = new PythonInterpreter();
		this.interpreter.exec("from sys import path; path.insert(0, \"" + scriptDirectory.getPath() + "/\")");
		this.interpreter.exec("from base import globals as glob");
		this.interpreter.exec("from base import forms");
		this.interpreter.exec("import json");
		
		this.glob = interpreter.eval("glob");
		this.forms = interpreter.eval("forms");
		
		this.updatables = new SafeArrayList<>(PyObject.class);
	}
	@Override
	protected void initialize(final Application app) {
		this.app = app;
		glob.invoke("setApp", Py.java2py(app));
	}
	@Override
	protected void onEnable() {
		
	}
	@Override
	protected void onDisable() {
		
	}
	@Override
	protected void cleanup(final Application app) {
		this.app = null;
		glob.invoke("setApp", Py.java2py(null));
	}
	@Override
	public void update(final float tpf) {
		glob.invoke("setActualTpf", new PyFloat(tpf));
		
		if(singleScripts != null) {
			final Iterator<PyObject> it = singleScripts.iterator();
			while(it.hasNext()) {
				final PyObject obj = it.next();
				
				try {
					obj.invoke("run");
					it.remove();
				} catch(final Throwable t) {
					t.printStackTrace();
				}
			}
			
			if(singleScripts.isEmpty()) {
				singleScripts = null;
			}
		}
		
		if(firstUpdatables != null) {
			for(final PyObject obj:firstUpdatables.getArray()) {
				try {
					obj.invoke("firstUpdate");
					updatables.add(obj);
					firstUpdatables.remove(obj);
				} catch(final Throwable t) {
					throw t;
				}
			}
			if(firstUpdatables.isEmpty()) {
				firstUpdatables = null;
			}
		}
		
		for(final PyObject obj:updatables.getArray()) {
			obj.invoke("update");
		}
	}
	
	public void addSingleScript(final String module, final String name) {
		interpreter.exec("from " + module + " import " + name);
		
		final PyObject clazz = interpreter.get(name);
		final PyObject obj = clazz.__call__();
		
		if(singleScripts == null) {
			singleScripts = new LinkedList<>();
		}
		singleScripts.add(obj);
	}
	public boolean addScript(final Form form) {
		final long id = form.getId();
		final String module = form.getScriptModuleName();
		final String name = form.getScriptClassName();
		
		interpreter.exec("from " + module + " import " + name);
		//final PyObject obj = interpreter.eval(name + "()");
		
		final PyObject clazz = interpreter.get(name);
		final PyObject obj = clazz.__call__();
		
		final FormDataStoreState dataStore = app.getStateManager().getState(FormDataStoreState.class);
		final JsonMap data = dataStore.get(id);
		
		final PyObject json = interpreter.eval("json");
		final PyObject dataPy = json.invoke("loads", new PyString(data.toJsonString()));
		final boolean shouldLoad = Py.tojava(obj.invoke("requestLoad", dataPy), Boolean.class);
		if(!shouldLoad) {
			return(false);
		}
		
		if(obj.__findattr__("update") != null) {
			if(obj.__findattr__("firstUpdate") != null) {
				if(firstUpdatables == null) {
					firstUpdatables = new SafeArrayList<>(PyObject.class);
				}
				
				firstUpdatables.add(obj);
			} else {
				updatables.add(obj);
			}
		}
		
		if(id > -1) {
			forms.invoke("addInstance", new PyObject[] {
					obj,
					Py.java2py(form)
			});
		}
		obj.invoke("init");
		
		return(true);
	}
	public void removeScript(final PyObject formInstance) {
		//app.enqueue(new Runnable() {
			//@Override
			//public void run() {
				final Form form = Py.tojava(formInstance.__getattr__("form"), Form.class);
				final long id = form.getId();
				
				if(id >= 0) {
					final PyObject dataPy = formInstance.invoke("beforeUnload");
					
					final FormDataStoreState dataStore = app.getStateManager().getState(FormDataStoreState.class);
					if(!(dataPy instanceof PyNone)) {
						final PyObject json = interpreter.eval("json");
						final String data = Py.tojava(json.invoke("dumps", dataPy), String.class);
						try {
							dataStore.put(id, new JsonDecoder(new StringReader(data)).decode().asMap());
						} catch (final IOException e) {
							throw new RuntimeException(e);
						}
					} else {
						dataStore.removePersistent(id);
						dataStore.removeTemporary(id);
					}
				}
				
				if(formInstance.__findattr__("cleanup") != null) {
					formInstance.invoke("cleanup");
				}
				
				if(firstUpdatables != null) {
					firstUpdatables.remove(formInstance);
				}
				updatables.remove(formInstance);
			//}
		//});
	}
	public void executeScript(final String module, final String name) {
		app.enqueue(new Runnable() {
			@Override
			public void run() {
				interpreter.exec("from " + module + " import " + name);
				//final PyObject obj = interpreter.eval(name + "()");
				
				final PyObject clazz = interpreter.get(name);
				final PyObject obj = clazz.__call__();
				
				obj.invoke("init");
			}
		});
	}
}
