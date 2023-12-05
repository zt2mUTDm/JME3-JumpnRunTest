package online.money_daisuki.api.monkey.basegame.physobj;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import org.python.core.Py;
import org.python.core.PyObject;
import org.python.core.PyString;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.PhysicsTickListener;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.collision.PhysicsCollisionObject;
import com.jme3.scene.Spatial;

import online.money_daisuki.api.base.Requires;

public final class PythonCollisionState extends BaseAppState {
	//private Application app;
	private PhysicsSpace space;
	
	private Set<PhysicsCollisionObject> enabledObjects;
	private Map<PhysicsCollisionObject, PyObject> targets;
	
	private Collection<Listener> listeners;
	private PhysicsTickListener tickListener;
	
	public PythonCollisionState() {
		setEnabled(false);
	}
	@Override
	protected void initialize(final Application app) {
		this.enabledObjects = new HashSet<>();
		this.targets = new HashMap<>();
		
		listeners = new LinkedList<>();
		
		tickListener= new PhysicsTickListener() {
			@Override
			public void prePhysicsTick(final PhysicsSpace space, final float timeStep) {
				
			}
			@Override
			public void physicsTick(final PhysicsSpace space, final float timeStep) {
				for(final Listener l:listeners) {
					doContactTest(l);
				}
			}
			private void doContactTest(final Listener l) {
				space.contactTest(l.getObj(), l);
				l.concludeRun();
			}
		};
		
		space = app.getStateManager().getState(BulletAppState.class).getPhysicsSpace();
		space.addTickListener(tickListener);
	}
	@Override
	public void update(final float tpf) {
		
	}
	
	
	@Override
	protected void onEnable() {
		
	}
	@Override
	protected void onDisable() {
		
	}
	@Override
	protected void cleanup(final Application app) {
		this.targets.clear();
		this.targets = null;
		
		this.enabledObjects.clear();
		this.enabledObjects = null;
		
		this.listeners.clear();
		this.listeners = null;
		
		this.space.removeTickListener(tickListener);
		this.tickListener = null;
		
		this.space = null;
	}
	
	public void addCollisionTester(final String name, final PhysicsCollisionObject obj, final PyObject parent) {
		Requires.notNull(parent, "parent == null");
		if(listeners.add(new Listener(Requires.notNull(name, "name == null"), Requires.notNull(obj, "obj == null")))) {
			targets.put(obj, parent);
		}
	}
	public void addCollisionReceiver(final PhysicsCollisionObject obj, final PyObject parent) {
		Requires.notNull(parent, "parent == null");
		targets.put(Requires.notNull(obj, "obj == null"), Requires.notNull(parent, "parent == null"));
	}
	public void removeCollisionTester(final PhysicsCollisionObject obj) {
		getApplication().enqueue(new Runnable() {
			@Override
			public void run() {
				final Iterator<Listener> it = listeners.iterator();
				while(it.hasNext()) {
					final Listener l = it.next();
					if(l.getObj() == obj) {
						it.remove();
						targets.remove(obj);
						break;
					}
				}
			}
		});
	}
	
	private final class Listener implements PhysicsCollisionListener {
		private final String ownName;
		private final PhysicsCollisionObject obj;
		
		private Set<PhysicsCollisionObject> lastRun;
		private Set<PhysicsCollisionObject> thisRun;
		
		private final Map<PhysicsCollisionObject, PyString> spatialNames;
		
		public Listener(final String ownName, final PhysicsCollisionObject obj) {
			this.obj = Requires.notNull(obj, "obj == null");
			this.ownName = Requires.notNull(ownName, "ownName == null");
			this.lastRun = new HashSet<>();
			this.thisRun = new HashSet<>();
			
			this.spatialNames = new HashMap<>();
		}
		@Override
		public void collision(final PhysicsCollisionEvent event) {
			final PhysicsCollisionObject a = event.getObjectA();
			final PhysicsCollisionObject b = event.getObjectB();
			
			//final String na = getName(a);
			//final String nb = getName(b);
			
			
			final Spatial nodeA = event.getNodeA();
			final Spatial nodeB = event.getNodeB();
			
			if(nodeA == null || nodeB == null) {
				return;
			}
			
			final String na = event.getNodeA().getName();
			final String nb = event.getNodeB().getName();
			
			if(na != null && nb != null) {
				if(a == obj) {
					handle(a, na, b, nb);
				} else if(b == obj) {
					handle(b, nb, a, na);
				}
			}
		}
		private void handle(final PhysicsCollisionObject myObj, final String myName, final PhysicsCollisionObject otherObj, final String otherName) {
			final PyObject target = targets.get(myObj);
			if(target != null) {
				if(!lastRun.remove(otherObj)) {
					final PyString otherNamePy = new PyString(otherName);
					invoke(target, "handleTouchEnter", new PyObject[] {
							new PyString(myName), Py.java2py(otherObj), otherNamePy
					});
					spatialNames.put(otherObj, otherNamePy);
				}
				
				final PyObject otherNamePy = spatialNames.get(otherObj);
				invoke(target, "handleTouch", new PyObject[] {
						new PyString(myName), Py.java2py(otherObj), otherNamePy
				});
				thisRun.add(otherObj);
			}
			
		}
		public PhysicsCollisionObject getObj() {
			return (obj);
		}
		
		private void invoke(final PyObject target, final String methodName, final PyObject[] args) {
			getApplication().enqueue(new Runnable() {
				@Override
				public void run() {
					target.invoke(methodName, args);
				}
			});
		}
		
		public void concludeRun() {
			final PyObject target = targets.get(obj);
			for(final PhysicsCollisionObject last:lastRun) {
				final PyObject otherNamePy = spatialNames.remove(last);
				invoke(target, "handleTouchLeave", new PyObject[] {
						new PyString(ownName), Py.java2py(last), otherNamePy
				});
			}
			lastRun.clear();
			final Set<PhysicsCollisionObject> s = lastRun;
			
			lastRun = thisRun;
			thisRun = s;
		}
	}
	/*private final class Listener implements PhysicsCollisionListener {
		private final String ownName;
		private final PhysicsCollisionObject obj;
		
		private Set<String> lastRun;
		private Set<String> thisRun;
		
		public Listener(final String ownName, final PhysicsCollisionObject obj) {
			this.obj = Requires.notNull(obj, "obj == null");
			this.ownName = Requires.notNull(ownName, "ownName == null");
			this.lastRun = new HashSet<>();
			this.thisRun = new HashSet<>();
		}
		@Override
		public void collision(final PhysicsCollisionEvent event) {
			final PhysicsCollisionObject a = event.getObjectA();
			final PhysicsCollisionObject b = event.getObjectB();
			
			//final String na = getName(a);
			//final String nb = getName(b);
			
			final String na = event.getNodeA().getName();
			final String nb = event.getNodeB().getName();
			
			if(na != null && nb != null) {
				if(a == obj) {
					handle(a, na, nb);
				} else if(b == obj) {
					handle(b, nb, na);
				}
			}
		}
		private void handle(final PhysicsCollisionObject obj, final String myName, final String otherName) {
			final PyObject target = targets.get(obj);
			if(target != null) {
				target.invoke("onTouch", new PyString(myName), new PyString(otherName));
				if(!lastRun.remove(otherName)) {
					target.invoke("onTouchEnter", new PyString(myName), new PyString(otherName));
				}
				thisRun.add(otherName);
			}
		}
		public PhysicsCollisionObject getObj() {
			return (obj);
		}
		
		public void concludeRun() {
			final PyObject target = targets.get(obj);
			for(final String last:lastRun) {
				target.invoke("onTouchLeave", new PyString(ownName), new PyString(last));
			}
			lastRun = thisRun;
			thisRun = new HashSet<>();
		}
	}*/
}
