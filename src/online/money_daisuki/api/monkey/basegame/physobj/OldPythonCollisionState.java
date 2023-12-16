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
import com.jme3.bullet.collision.ContactListener;
import com.jme3.bullet.collision.PersistentManifolds;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.collision.PhysicsCollisionObject;
import com.jme3.scene.Spatial;

import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.misc.mapping.FinalMapping;
import online.money_daisuki.api.misc.mapping.Mapping;

/**
 * Keep if something doesn't work. Use {@link PythonCollisionState} instead.
 */
@Deprecated
public final class OldPythonCollisionState extends BaseAppState {
	private PhysicsSpace space;
	
	private Set<PhysicsCollisionObject> enabledObjects;
	private Map<PhysicsCollisionObject, PyObject> targets;
	
	private Collection<Listener> listeners;
	private PhysicsTickListener tickListener;
	private ContactListener contactListener;
	
	public OldPythonCollisionState() {
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
		
		contactListener = new ContactListener() {
			private final Map<Mapping<PhysicsCollisionObject, PhysicsCollisionObject>, Integer> contacts =
					new HashMap<>();
			
			@Override
			public void onContactStarted(final long manifoldId) {
				if(!isEnabled()) {
					return;
				}
				
				final long bodyAId = PersistentManifolds.getBodyAId(manifoldId);
				final long bodyBId = PersistentManifolds.getBodyBId(manifoldId);
				
				// Read sorted by id 
				final PhysicsCollisionObject pcoA;
				final PhysicsCollisionObject pcoB;
				if(bodyAId < bodyBId) {
					pcoA = PhysicsCollisionObject.findInstance(bodyAId);
					pcoB = PhysicsCollisionObject.findInstance(bodyBId);
				} else {
					pcoA = PhysicsCollisionObject.findInstance(bodyBId);
					pcoB = PhysicsCollisionObject.findInstance(bodyAId);
				}
				
				final Spatial spatialA = getSpatialFromPCO(pcoA);
				final Spatial spatialB = getSpatialFromPCO(pcoB);
				
				if(spatialA == null || spatialB == null) {
					return;
				}
				
				final Mapping<PhysicsCollisionObject, PhysicsCollisionObject> pair = new FinalMapping<>(pcoA, pcoB);
				final Integer contactCount = contacts.get(pair);
				if(contactCount != null) {
					contacts.put(pair, contactCount + 1);
					return;
				} else {
					contacts.put(pair, 0);
				}
				
				final String nameA = spatialA.getName();
				final String nameB = spatialB.getName();
				
				if(nameA.startsWith("ActionRadius") || nameB.startsWith("ActionRadius")) {
					System.out.println("Started: " + nameA + " - " + nameB + " - " + manifoldId);
					System.out.println(pcoA + " - " + pcoB);
					//System.out.println(spatialA.getWorldTranslation() + " - " + spatialB.getWorldTranslation());
				}
			}
			@Override
			public void onContactProcessed(final PhysicsCollisionObject pcoA, final PhysicsCollisionObject pcoB, final long manifoldPointId) {
				if(!isEnabled()) {
					return;
				}
				final Spatial spatialA = getSpatialFromPCO(pcoA);
				final Spatial spatialB = getSpatialFromPCO(pcoB);
				
				if(spatialA == null || spatialB == null) {
					return;
				}
				
				final String nameA = spatialA.getName();
				final String nameB = spatialB.getName();
				
				if(nameA.startsWith("ActionRadius") || nameB.startsWith("ActionRadius")) {
					//System.out.println("Processed:" + nameA + " - " + nameB);
				}
			}
			@Override
			public void onContactEnded(final long manifoldId) {
				if(!isEnabled()) {
					return;
				}
				final long bodyAId = PersistentManifolds.getBodyAId(manifoldId);
				final long bodyBId = PersistentManifolds.getBodyBId(manifoldId);
				
				// Read sorted by id 
				final PhysicsCollisionObject pcoA;
				final PhysicsCollisionObject pcoB;
				if(bodyAId < bodyBId) {
					pcoA = PhysicsCollisionObject.findInstance(bodyAId);
					pcoB = PhysicsCollisionObject.findInstance(bodyBId);
				} else {
					pcoA = PhysicsCollisionObject.findInstance(bodyBId);
					pcoB = PhysicsCollisionObject.findInstance(bodyAId);
				}
				
				final Spatial spatialA = getSpatialFromPCO(pcoA);
				final Spatial spatialB = getSpatialFromPCO(pcoB);
				
				if(spatialA == null || spatialB == null) {
					return;
				}
				
				final Mapping<PhysicsCollisionObject, PhysicsCollisionObject> pair = new FinalMapping<>(pcoA, pcoB);
				final Integer contactCount = contacts.get(pair);
				if(contactCount != null) {
					if(contactCount > 1) {
						contacts.put(pair, contactCount - 1);
					} else {
						contacts.remove(pair);
					}
				} else {
					return;
				}
				
				final String nameA = spatialA.getName();
				final String nameB = spatialB.getName();
				
				if(nameA.startsWith("ActionRadius") || nameB.startsWith("ActionRadius")) {
					System.out.println("Ended:" + nameA + " - " + nameB + " - " + manifoldId);
				}
			}
			private Spatial getSpatialFromPCO(final PhysicsCollisionObject obj) {
				final Object userObject = obj.getUserObject();
				if (userObject instanceof Spatial) {
					return (Spatial) (userObject);
				}
				return(null);
			}
		};
	}
	@Override
	public void update(final float tpf) {
		
	}
	
	@Override
	protected void onEnable() {
		space.addContactListener(contactListener);
	}
	@Override
	protected void onDisable() {
		space.removeContactListener(contactListener);
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
		
		this.space.removeContactListener(contactListener);
		this.contactListener = null;
		
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
					if(myName.equals("SPSTTouchKeepButton0") && otherName.equals("Player")) {
						System.out.flush();
					}
					
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
}
