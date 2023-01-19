package online.money_daisuki.api.monkey.basegame.character.anim;

import java.io.IOException;

import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;

import online.money_daisuki.api.base.Requires;

public final class AnimControl implements Control {
	private final AnimPlayer player;
	
	public AnimControl(final AnimPlayer player) {
		this.player = Requires.notNull(player, "player == null");
	}
	
	public void play(final String name, final boolean once) {
		player.play(name, once);
	}
	public void play(final String name, final boolean once, final Runnable l) {
		player.play(name, once, l);
	}
	
	@Override
	public void update(final float tpf) {
		
	}
	
	@Override
	public Control cloneForSpatial(final Spatial spatial) {
		return(new AnimControl(player));
	}
	
	@Override
	public void setSpatial(final Spatial spatial) {
		//this.spatial = spatial;
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
