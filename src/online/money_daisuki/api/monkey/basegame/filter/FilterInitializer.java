package online.money_daisuki.api.monkey.basegame.filter;

import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.FadeFilter;

import online.money_daisuki.api.base.DataSink;
import online.money_daisuki.api.monkey.basegame.ModulesApp;

public final class FilterInitializer implements DataSink<ModulesApp> {
	@Override
	public void sink(final ModulesApp app) {
		final FilterPostProcessor fpp = new FilterPostProcessor(app.getAssetManager());
		app.getViewPort().addProcessor(fpp);
		
		final FadeFilter fade = new FadeFilter();
		fade.setValue(0.0f);
		fpp.addFilter(fade);
		app.getStateManager().attach(new FadeAppState(fade));
	}
}
