package online.money_daisuki.api.monkey.basegame.spatial;

import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

import online.money_daisuki.api.base.Requires;

public final class TranslatableSpatial implements Translatable {
	private final Spatial spatial;
	
	public TranslatableSpatial(final Spatial spatial) {
		this.spatial = Requires.notNull(spatial, "spatial == null");
	}
	@Override
	public void setTranslation(final Vector3f vector) {
		spatial.getLocalTranslation().set(vector);
	}
}
