package online.money_daisuki.api.monkey.basegame.state;

import java.util.Collection;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;

import online.money_daisuki.api.base.DataSink;
import online.money_daisuki.api.utils.DataListenerContainer;
import online.money_daisuki.api.utils.NoDataListenerContainer;

public class AppStateAdapter extends BaseAppState {
	private final DataListenerContainer<Application> initializeListeners;
	private final DataListenerContainer<Application> cleanupListeners;
	private final NoDataListenerContainer enableListeners;
	private final NoDataListenerContainer disableListeners;
	
	public AppStateAdapter() {
		this.initializeListeners = new DataListenerContainer<>();
		this.cleanupListeners = new DataListenerContainer<>();
		
		this.enableListeners = new NoDataListenerContainer();
		this.disableListeners = new NoDataListenerContainer();
	}
	
	public void addInitializeListener(final DataSink<? super Application> l) {
		initializeListeners.addListener(l);
	}
	public Collection<DataSink<? super Application>> getInitializeListeners() {
		return(initializeListeners.getListeners());
	}
	public boolean removeInitializeListener(final DataSink<? super Application> l) {
		return(initializeListeners.removeListener(l));
	}
	public void clearInitializeListeners() {
		initializeListeners.clearListeners();
	}
	@Override
	protected final void initialize(final Application app) {
		initializeListeners.fireListeners(app);
	}
	
	public void addCleanupListener(final DataSink<? super Application> l) {
		cleanupListeners.addListener(l);
	}
	public Collection<DataSink<? super Application>> getCleanupListeners() {
		return(cleanupListeners.getListeners());
	}
	public boolean removeCleanupListener(final DataSink<? super Application> l) {
		return(cleanupListeners.removeListener(l));
	}
	public void clearCleanupListeners() {
		cleanupListeners.clearListeners();
	}
	@Override
	protected final void cleanup(final Application app) {
		cleanupListeners.fireListeners(app);
	}
	
	public void addEnableListener(final Runnable l) {
		enableListeners.addListener(l);
	}
	public Collection<Runnable> getEnableListeners() {
		return(enableListeners.getListeners());
	}
	public boolean removeEnableListener(final Runnable l) {
		return(enableListeners.removeListener(l));
	}
	public void clearEnableListeners() {
		enableListeners.clearListeners();
	}
	@Override
	protected final void onEnable() {
		enableListeners.fireListeners();
	}
	
	public void addDisableListener(final Runnable l) {
		disableListeners.addListener(l);
	}
	public Collection<Runnable> getDisableListeners() {
		return(disableListeners.getListeners());
	}
	public boolean removeDisableListener(final Runnable l) {
		return(disableListeners.removeListener(l));
	}
	public void clearDisableListeners() {
		disableListeners.clearListeners();
	}
	@Override
	protected final void onDisable() {
		disableListeners.fireListeners();
	}
}
