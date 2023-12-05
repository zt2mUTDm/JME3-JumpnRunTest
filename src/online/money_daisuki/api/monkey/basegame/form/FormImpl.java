package online.money_daisuki.api.monkey.basegame.form;

import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.monkey.basegame.physobj.PhysicsObject;

public final class FormImpl implements Form {
	private final long id;
	private final PhysicsObject physicsObject;
	private final String scriptModuleName;
	private final String scriptClassName;

	public FormImpl(final long id, final PhysicsObject physicsObject, final String scriptModuleName, final String scriptClassName) {
		this.id = id;
		this.physicsObject = Requires.notNull(physicsObject, "physicsObject == null");
		this.scriptModuleName = Requires.notNull(scriptModuleName, "scriptModuleName == null");
		this.scriptClassName = Requires.notNull(scriptClassName, "scriptClassName == null");
	}
	@Override
	public long getId() {
		return (id);
	}
	@Override
	public PhysicsObject getPhysicsObject() {
		return (physicsObject);
	}
	@Override
	public String getScriptModuleName() {
		return (scriptModuleName);
	}
	@Override
	public String getScriptClassName() {
		return (scriptClassName);
	}
}
