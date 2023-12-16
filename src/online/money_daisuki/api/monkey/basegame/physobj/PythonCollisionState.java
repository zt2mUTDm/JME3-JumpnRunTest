package online.money_daisuki.api.monkey.basegame.physobj;

import java.util.HashMap;
import java.util.Map;

import org.python.core.Py;
import org.python.core.PyObject;
import org.python.core.PyString;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.ContactListener;
import com.jme3.bullet.collision.PersistentManifolds;
import com.jme3.bullet.collision.PhysicsCollisionObject;
import com.jme3.scene.Spatial;

import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.misc.mapping.FinalMapping;
import online.money_daisuki.api.misc.mapping.Mapping;

public final class PythonCollisionState extends BaseAppState {
	private PhysicsSpace space;
	
	private Map<PhysicsCollisionObject, PyObject> targets;
	
	private ContactListener contactListener;
	
	public PythonCollisionState() {
		setEnabled(false);
	}
	@Override
	protected void initialize(final Application app) {
		this.targets = new HashMap<>();
		
		space = app.getStateManager().getState(BulletAppState.class).getPhysicsSpace();
		contactListener = new ContactListener() {
			private final Map<Mapping<PhysicsCollisionObject, PhysicsCollisionObject>, Integer> contacts =
					new HashMap<>();
			
			@Override
			public void onContactStarted(final long manifoldId) {
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
				
				if(nameA != null && nameB != null) {
					handleContactStart(pcoA, nameA, pcoB, nameB);
				}
			}
			private void handleContactStart(final PhysicsCollisionObject pcoA, final String nameA,
					final PhysicsCollisionObject pcoB, final String nameB) {
				final PyObject targetA = targets.get(pcoA);
				final PyObject targetB = targets.get(pcoB);
				
				if(targetA != null || targetB != null) {
					final PyString nameAPy = new PyString(nameA);
					final PyString nameBPy = new PyString(nameB);
					
					final PyObject[] args = new PyObject[] {
							new PyString(nameA), Py.java2py(pcoB), new PyString(nameB)
					};
					
					getApplication().enqueue(new Runnable() {
						@Override
						public void run() {
							if(targetA != null) {
								targetA.invoke("handleTouchEnter", args);
							}
							if(targetB != null) {
								args[0] = nameBPy;
								args[1] = Py.java2py(pcoA);
								args[2] = nameAPy;
								
								targetB.invoke("handleTouchEnter", args);
							}
						}
					});
				}
			}
			
			@Override
			public void onContactProcessed(final PhysicsCollisionObject pcoA, final PhysicsCollisionObject pcoB, final long manifoldPointId) {
				final Spatial spatialA = getSpatialFromPCO(pcoA);
				final Spatial spatialB = getSpatialFromPCO(pcoB);
				
				if(spatialA == null || spatialB == null) {
					return;
				}
				
				final String nameA = spatialA.getName();
				final String nameB = spatialB.getName();
				
				if(nameA != null && nameB != null) {
					handleContactProcessed(pcoA, nameA, pcoB, nameB);
				}
			}
			private void handleContactProcessed(final PhysicsCollisionObject pcoA, final String nameA,
					final PhysicsCollisionObject pcoB, final String nameB) {
				final PyObject targetA = targets.get(pcoA);
				final PyObject targetB = targets.get(pcoB);
				
				if(targetA != null || targetB != null) {
					final PyString nameAPy = new PyString(nameA);
					final PyString nameBPy = new PyString(nameB);
					
					final PyObject[] args = new PyObject[] {
							new PyString(nameA), Py.java2py(pcoB), new PyString(nameB)
					};
					
					getApplication().enqueue(new Runnable() {
						@Override
						public void run() {
							if(targetA != null) {
								targetA.invoke("handleTouch", args);
							}
							if(targetB != null) {
								args[0] = nameBPy;
								args[1] = Py.java2py(pcoA);
								args[2] = nameAPy;
								
								targetB.invoke("handleTouch", args);
							}
						}
					});
				}
			}
			@Override
			public void onContactEnded(final long manifoldId) {
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
				
				if(nameA != null && nameB != null) {
					handleContactEnded(pcoA, nameA, pcoB, nameB);
				}
			}
			private void handleContactEnded(final PhysicsCollisionObject pcoA, final String nameA,
					final PhysicsCollisionObject pcoB, final String nameB) {
				final PyObject targetA = targets.get(pcoA);
				final PyObject targetB = targets.get(pcoB);
				
				if(targetA != null || targetB != null) {
					final PyString nameAPy = new PyString(nameA);
					final PyString nameBPy = new PyString(nameB);
					
					final PyObject[] args = new PyObject[] {
							nameAPy, Py.java2py(pcoB), nameBPy
					};
					
					getApplication().enqueue(new Runnable() {
						@Override
						public void run() {
							if(targetA != null) {
								targetA.invoke("handleTouchLeave", args);
							}
							if(targetB != null) {
								args[0] = nameBPy;
								args[1] = Py.java2py(pcoA);
								args[2] = nameAPy;
								
								targetB.invoke("handleTouchLeave", args);
							}
						}
					});
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
		
		this.contactListener = null;
		
		this.space = null;
	}
	
	public void addCollisionTester(final String name, final PhysicsCollisionObject obj, final PyObject parent) {
		addCollisionReceiver(obj, parent);
	}
	public void addCollisionReceiver(final PhysicsCollisionObject obj, final PyObject parent) {
		Requires.notNull(parent, "parent == null");
		targets.put(Requires.notNull(obj, "obj == null"), Requires.notNull(parent, "parent == null"));
	}
	public void removeCollisionTester(final PhysicsCollisionObject obj) {
		/*getApplication().enqueue(new Runnable() {
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
		});*/
	}
}
