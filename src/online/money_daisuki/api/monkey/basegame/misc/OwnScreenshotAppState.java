package online.money_daisuki.api.monkey.basegame.misc;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import com.jme3.app.Application;
import com.jme3.app.state.AppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.system.JmeSystem;

import online.money_daisuki.api.base.DataSource;
import online.money_daisuki.api.base.Requires;

public final class OwnScreenshotAppState implements AppState {
	private boolean initialized;
	private boolean enabled;
	
	private CaptureScreenPostProcessor cspp;
	private ViewPort vp;
	
	private File directory;
	
	private final DataSource<? extends File> fileGenerator;
	
	private final Collection<Runnable> capturedListeners;
	private final Collection<Runnable> capturingFailedListeners;
	
	public OwnScreenshotAppState(final DataSource<? extends File> fileGenerator) {
		this.fileGenerator = Requires.notNull(fileGenerator, "fileGenerator == null");
		this.enabled = true;
		
		this.capturedListeners = new LinkedList<>();
		this.capturingFailedListeners = new LinkedList<>();
	}
	
	public void addCapturedListener(final Runnable l) {
		addListener(l, capturedListeners);
	}
	public void addCapturingFailedListener(final Runnable l) {
		addListener(l, capturingFailedListeners);
	}
	public void addListener(final Runnable l, final Collection<Runnable> ls) {
		ls.add(Requires.notNull(l, "l == null"));
	}
	
	public void removeCapturedListener(final Runnable l) {
		removeListener(l, capturedListeners);
	}
	public void removeCapturingFailedListener(final Runnable l) {
		removeListener(l, capturingFailedListeners);
	}
	public void removeListener(final Runnable l, final Collection<Runnable> ls) {
		ls.remove(Requires.notNull(l, "l == null"));
	}
	
	private void fireListeners(final Collection<Runnable> listeners) {
		for(final Runnable r:listeners) {
			r.run();
		}
	}
	
	public void capture() {
		cspp.capture();
	}
	
	@Override
	public void initialize(final AppStateManager stateManager, final Application app) {
		final List<ViewPort> vps = app.getRenderManager().getPostViews();
		vp = vps.get(vps.size() - 1);
		
		cspp = new CaptureScreenPostProcessor();
		cspp.addCapturedListener(new Runnable() {
			@Override
			public void run() {
				try {
					try(final OutputStream out = new FileOutputStream(fileGenerator.source())) {
						JmeSystem.writeImageFile(out, "png", cspp.getBuffer(), cspp.getWidth(), cspp.getHeight());
					}
					fireListeners(capturedListeners);
				} catch(final Throwable t) {
					System.err.println(t.getMessage());
					fireListeners(capturingFailedListeners);
				}
			}
		});
		vp.addProcessor(cspp);
		
		initialized = true;
	}
	
	@Override
	public void cleanup() {
		vp.removeProcessor(cspp);
	}
	
	@Override
	public boolean isInitialized() {
		return(initialized);
	}
	@Override
	public String getId() {
		return(null);
	}
	@Override
	public void setEnabled(final boolean active) {
		this.enabled = active;
	}
	@Override
	public boolean isEnabled() {
		return(enabled);
	}
	
	@Override
	public void update(final float tpf) {
		
	}
	@Override
	public void stateAttached(final AppStateManager stateManager) {
		
	}
	@Override
	public void stateDetached(final AppStateManager stateManager) {
		
	}
	@Override
	public void render(final RenderManager rm) {
		
	}
	@Override
	public void postRender() {
		
	}
}
