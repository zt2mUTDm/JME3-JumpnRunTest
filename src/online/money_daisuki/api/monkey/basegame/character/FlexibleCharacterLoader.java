package online.money_daisuki.api.monkey.basegame.character;

import com.jme3.bullet.BulletAppState;
import com.jme3.scene.Spatial;

import online.money_daisuki.api.base.Converter;
import online.money_daisuki.api.base.DataSource;
import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.monkey.basegame.ExtendedApplication;

public final class FlexibleCharacterLoader implements Converter<String, Spatial> {
	private final Converter<String, Spatial> parent;
	
	public FlexibleCharacterLoader(final Converter<String, Spatial> modelLoader,
			final BulletAppState bulletAppState,
			final ExtendedApplication app) {
		Requires.notNull(bulletAppState);
		/*this.cache = new WeakHashMapRefresher<>(new Converter<String, Spatial>() {
			@Override
			public Spatial convert(final String value) {
				final DataSource<Spatial> ldr = new CharacterLoader(
						Requires.notNull(value, "value == null"),
						modelLoader,
						bulletAppState,
						app
				);
				return(ldr.source());
			}
		});*/
		this.parent = new Converter<String, Spatial>() {
			@Override
			public Spatial convert(final String value) {
				final DataSource<Spatial> ldr = new CharacterLoader(
						Requires.notNull(value, "value == null"),
						modelLoader,
						bulletAppState,
						app
				);
				return(ldr.source());
			}
		};
	}
	@Override
	public Spatial convert(final String value) {
		return(parent.convert(value));
	}
}
