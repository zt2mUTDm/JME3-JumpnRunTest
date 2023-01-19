package online.money_daisuki.api.monkey.basegame.character;

import java.io.IOException;

import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;

import online.money_daisuki.api.base.Converter;
import online.money_daisuki.api.base.NullRunnable;
import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.monkey.basegame.ControlTemplate;
import online.money_daisuki.api.monkey.basegame.ExtendedApplication;
import online.money_daisuki.api.monkey.basegame.character.control.CharControl;
import online.money_daisuki.api.monkey.basegame.material.FlexibleMaterialLoader;
import online.money_daisuki.api.monkey.basegame.model.FlexibleModelLoader;

public final class SpawnControl implements Control {
	private final float firstSpawn;
	private final float respawnRate;
	private final String characterUrl;
	private final Vector3f scale;
	private final ExtendedApplication app;
	
	private Node spatial;
	
	private Spatial actualSpawn;
	
	private float counter;
	
	public SpawnControl(final float firstSpawn, final float respawnRate, final String characterUrl, final Vector3f scale,
			final ExtendedApplication app) {
		this.firstSpawn = firstSpawn;
		this.respawnRate = respawnRate;
		this.characterUrl = characterUrl;
		this.scale = new Vector3f(Requires.notNull(scale, "scale == null"));
		this.app = app; //TODO
	}
	@Override
	public void update(final float tpf) {
		handleSpawn(tpf, actualSpawn == null ? firstSpawn : respawnRate);
	}
	
	private void handleSpawn(final float tpf, final float cooldown) {
		if(actualSpawn == null || !spatial.hasChild(actualSpawn)) {
			if(cooldown == 0.0f) {
				doSpawn();
			} else {
				counter+= tpf;
				if(counter >= cooldown) {
					doSpawn();
					counter = 0.0f;
				}
			}
		}
	}
	private void doSpawn() {
		final BulletAppState bullet = app.getStateManager().getState(BulletAppState.class);
		
		final Converter<String, Spatial> modelLoader = new FlexibleModelLoader(new FlexibleMaterialLoader(app), app);
		actualSpawn = new LoadCharacterCommand(
				new FlexibleCharacterLoader(modelLoader, bullet, app),
				spatial,
				bullet,
				app
		).execute(spatial, new String[] {
				"AddCharacter",
				characterUrl,
				"0",
				"0",
				"0",
				String.valueOf(scale.x),
				String.valueOf(scale.y),
				String.valueOf(scale.z)
		}, new NullRunnable());
		
		final RigidBodyControl rigid = actualSpawn.getControl(RigidBodyControl.class);
		if(rigid != null) {
			rigid.setPhysicsLocation(spatial.getWorldTranslation());
		}
		
		final CharControl cc = actualSpawn.getControl(CharControl.class);
		if(cc != null) {
			cc.setTranslation(spatial.getWorldTranslation());
		}
		
		if(actualSpawn != null) {
			app.enqueue(new Runnable() {
				@Override
				public void run() {
					spatial.attachChild(actualSpawn);
					bullet.getPhysicsSpace().addAll(actualSpawn);
				}
			});
		}
	}
	
	@Override
	public Control cloneForSpatial(final Spatial spatial) {
		return(new ControlTemplate());
	}
	
	@Override
	public void setSpatial(final Spatial spatial) {
		if(!(spatial instanceof Node)) {
			throw new IllegalStateException("Respawn control can only used as Node control");
		}
		this.spatial = (Node) spatial;
	}
	
	@Override
	public void render(final RenderManager rm, final ViewPort vp) {
		
	}
	
	@Override
	public void read(final JmeImporter im) throws IOException {
		throw new UnsupportedOperationException("Auto-generated method stub"); // TODO
	}
	@Override
	public void write(final JmeExporter ex) throws IOException {
		throw new UnsupportedOperationException("Auto-generated method stub"); // TODO
	}
}
