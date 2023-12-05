package online.money_daisuki.api.monkey.basegame.mayunused;

import com.jme3.scene.Spatial;

import online.money_daisuki.api.base.Converter;
import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.monkey.basegame.unsorted.SceneData;
import online.money_daisuki.api.monkey.basegame.unsorted.SceneLoader;

public final class FlexibleSceneLoader implements Converter<String, SceneData> {
	private final Converter<? super String, ? extends SceneData> parent;
	
	public FlexibleSceneLoader(final Converter<String, Spatial> characterLoader) {
		Requires.notNull(characterLoader, "characterLoader == null");
		
		this.parent = new Converter<String, SceneData>() {
			@Override
			public SceneData convert(final String value) {
				return(new SceneLoader(value, characterLoader).source());
			}
		};
	}
	@Override
	public SceneData convert(final String value) {
		return(parent.convert(Requires.notNull(value, "value == null")));
	}
}
