package online.money_daisuki.api.monkey.basegame.misc;

import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.LinkedList;

import com.jme3.post.SceneProcessor;
import com.jme3.profile.AppProfiler;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.Renderer;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.texture.FrameBuffer;
import com.jme3.util.BufferUtils;

import online.money_daisuki.api.base.Requires;

public final class CaptureScreenPostProcessor implements SceneProcessor {
	private boolean initialized;
	private boolean capture;
	
	private int width;
	private int height;
	
	private Renderer renderer;
	private RenderManager rm;
	private ByteBuffer outBuf;
	
	private final Collection<Runnable> capturedListeners;
	
	public CaptureScreenPostProcessor() {
		this.capturedListeners = new LinkedList<>();
	}
	public void addCapturedListener(final Runnable l) {
		capturedListeners.add(Requires.notNull(l, "l == null"));
	}
	public void removeCapturedListener(final Runnable l) {
		capturedListeners.remove(Requires.notNull(l, "l == null"));
	}
	
	public int getWidth() {
		return (width);
	}
	public int getHeight() {
		return (height);
	}
	public ByteBuffer getBuffer() {
		return (outBuf);
	}
	
	public void capture() {
		capture = true;
	}
	@Override
	public void postFrame(final FrameBuffer out) {
		if(capture) {
			final Camera curCamera = rm.getCurrentCamera();
			final int viewX = (int) (curCamera.getViewPortLeft() * curCamera.getWidth());
			final int viewY = (int) (curCamera.getViewPortBottom() * curCamera.getHeight());
			final int viewWidth = (int) ((curCamera.getViewPortRight() - curCamera.getViewPortLeft()) * curCamera.getWidth());
			final int viewHeight = (int) ((curCamera.getViewPortTop() - curCamera.getViewPortBottom()) * curCamera.getHeight());
			
			renderer.setViewPort(0, 0, width, height);
			renderer.readFrameBuffer(out, outBuf);
			renderer.setViewPort(viewX, viewY, viewWidth, viewHeight);
			
			for(final Runnable l:capturedListeners) {
				l.run();
			}
			
			capture = false;
		}
	}
	
	@Override
	public void initialize(final RenderManager rm, final ViewPort vp) {
		this.rm = rm;
		this.renderer = rm.getRenderer();
		
		final Camera cam = vp.getCamera();
		reshape(vp, cam.getWidth(), cam.getHeight());
		
		this.initialized = true;
	}
	@Override
	public void reshape(final ViewPort vp, final int w, final int h) {
		outBuf = BufferUtils.createByteBuffer(w * h * 4);
		width = w;
		height = h;
	}
	
	@Override
	public boolean isInitialized() {
		return(initialized);
	}
	
	@Override
	public void preFrame(final float tpf) {
		
	}
	@Override
	public void postQueue(final RenderQueue rq) {
		
	}
	@Override
	public void cleanup() {
		
	}
	@Override
	public void setProfiler(final AppProfiler profiler) {
		
	}
}
