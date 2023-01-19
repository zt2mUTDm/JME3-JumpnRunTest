package online.money_daisuki.api.monkey.basegame.spatial;

import com.jme3.scene.Spatial;

import online.money_daisuki.api.base.Requires;

public final class HasSpatialAdapter implements HasSpatial {
	private final Spatial spatial;
	
	public HasSpatialAdapter(final Spatial spatial) {
		this.spatial = Requires.notNull(spatial, "spatial == null");
	}
	@Override
	public Spatial getSpatial() {
		return(spatial);
	}
}
