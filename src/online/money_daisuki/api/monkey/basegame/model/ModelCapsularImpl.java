package online.money_daisuki.api.monkey.basegame.model;

import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.scene.Spatial;

import online.money_daisuki.api.base.Requires;

public final class ModelCapsularImpl implements ModelCapsular {
	private final Spatial spatial;
	private final CharacterControl charControl;
	private final RigidBodyControl bodyControl;
	
	public ModelCapsularImpl(final Spatial spatial,
			final CharacterControl charControl,
			final RigidBodyControl bodyControl) {
		this.spatial = Requires.notNull(spatial, "spatial == null");
		this.charControl = charControl;
		this.bodyControl = bodyControl;
	}
	@Override
	public Spatial getSpatial() {
		return (spatial);
	}
	
	@Override
	public boolean hasCharControl() {
		return (charControl != null);
	}
	@Override
	public CharacterControl getCharControl() {
		return (charControl);
	}
	@Override
	public boolean hasBodyControl() {
		return (bodyControl != null);
	}
	@Override
	public RigidBodyControl getBodyControl() {
		return (bodyControl);
	}
}
