package online.money_daisuki.api.monkey.basegame;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;

import com.jme3.app.Application;
import com.jme3.app.state.AppState;
import com.jme3.app.state.BaseAppState;

import online.money_daisuki.api.base.DataSink;
import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.io.json.JsonDecoder;
import online.money_daisuki.api.io.json.JsonElement;
import online.money_daisuki.api.io.json.JsonList;
import online.money_daisuki.api.io.json.JsonMap;
import online.money_daisuki.api.monkey.basegame.state.OneRunAppState;

public final class InitializeAppModulesState extends BaseAppState {
	private ModulesApp app;
	
	private boolean paused;
	private boolean done;
	
	private Deque<JsonMap> modulesToLoad;

	private DataSink<? super ModulesApp> callback;
	
	public InitializeAppModulesState(final DataSink<? super ModulesApp> callback) {
		this.callback = Requires.notNull(callback, "callback == null");
	}
	
	@Override
	public void update(final float tpf) {
		if(paused || done) {
			return;
		} else if(modulesToLoad == null) {
			indexModules();
		}
		
		if(modulesToLoad.isEmpty()) {
			enqueueCallback();
			removeMyself();
			
			done = true;
			modulesToLoad = null;
		} else {
			loadModules();
		}
	}
	private void indexModules() {
		modulesToLoad = new LinkedList<>();
		
		final Collection<String> modulesAdded = new HashSet<>();
		final Collection<String> dependenciesLoadRequest = new HashSet<>();
		
		try(final Reader in = new FileReader(new File("appmodules.json"))) {
			final JsonList modules = new JsonDecoder(in).decode().asList();
			for(final JsonElement e:modules) {
				indexModule(e.asData().asString(), modulesAdded, dependenciesLoadRequest);
			}
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
		
		modulesAdded.clear();
		dependenciesLoadRequest.clear();
	}
	private void indexModule(final String moduleName, final Collection<String> modulesAdded, final Collection<String> dependenciesLoadRequest) {
		if(!modulesAdded.add(moduleName)) {
			return;
		}
		
		dependenciesLoadRequest.add(moduleName);
		try(final Reader in = new FileReader(new File("appmodules/" + moduleName + ".json"))) {
			final JsonMap map = new JsonDecoder(in).decode().asMap();
			
			for(final JsonElement depE:map.get("dependencies").asList()) {
				final String dep = depE.asData().asString();
				if(dep.isEmpty()) {
					continue;
				}
				
				if(dep.equals(moduleName) || !dependenciesLoadRequest.add(dep)) {
					throw new IllegalArgumentException("Circulary dependencies: " + dependenciesLoadRequest + ";" +
							"current module: " + moduleName + ", current dependency: " + dep);
				}
				indexModule(dep, modulesAdded, dependenciesLoadRequest);
				dependenciesLoadRequest.remove(dep);
			}
			if(!map.get("className").asData().asString().isEmpty()) {
				modulesToLoad.add(map);
			}
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
		dependenciesLoadRequest.remove(moduleName);
	}
	
	private void loadModules() {
		while(!modulesToLoad.isEmpty()) {
			final JsonMap moduleData = modulesToLoad.removeFirst();
			if(moduleData.get("waitForStateInit").asData().asBool()) {
				paused = true;
				app.getStateManager().attach(new OneRunAppState(new DataSink<AppState>() {
					@Override
					public void sink(final AppState value) {
						loadModule(moduleData);
						app.enqueue(new Runnable() {
							@Override
							public void run() {
								app.getStateManager().detach(value);
							}
						});
						paused = false;
					}
				}));
				break;
			} else {
				loadModule(moduleData);
			}
		}
	}
	private void loadModule(final JsonMap moduleData) {
		final String className = moduleData.get("className").asData().asString();
		
		try {
			@SuppressWarnings("unchecked")
			final DataSink<ModulesApp> initializer = (DataSink<ModulesApp>) Class.forName(className).getConstructor().newInstance();
			initializer.sink(app);
		} catch (final InstantiationException e1) {
			throw new RuntimeException(e1);
		} catch (final InvocationTargetException e1) {
			throw new RuntimeException(e1);
		} catch (final NoSuchMethodException e1) {
			throw new RuntimeException(e1);
		} catch (final SecurityException e1) {
			throw new RuntimeException(e1);
		} catch (final ClassNotFoundException e1) {
			throw new RuntimeException(e1);
		} catch (final IllegalAccessException e1) {
			throw new RuntimeException(e1);
		}
	}
	
	private void enqueueCallback() {
		final ModulesApp app = this.app;
		app.getStateManager().attach(new OneRunAppState(new DataSink<AppState>() {
			@Override
			public void sink(final AppState value) {
				final DataSink<? super ModulesApp> c = callback;
				callback = null;
				c.sink(app);
				
				app.enqueue(new Runnable() {
					@Override
					public void run() {
						app.getStateManager().detach(value);
					}
				});
			}
		}));
	}
	private void removeMyself() {
		app.enqueue(new Runnable() {
			@Override
			public void run() {
				app.getStateManager().detach(InitializeAppModulesState.this);
			}
		});
	}
	
	@Override
	protected void initialize(final Application app) {
		this.app = (ModulesApp) app;
	}
	@Override
	protected void cleanup(final Application app) {
		this.app = null;
	}
	@Override
	protected void onEnable() {
		
	}
	@Override
	protected void onDisable() {
		
	}
}
