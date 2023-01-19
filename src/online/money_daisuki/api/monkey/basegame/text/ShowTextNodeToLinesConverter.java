package online.money_daisuki.api.monkey.basegame.text;

import java.util.ArrayList;
import java.util.List;

import com.jme3.font.BitmapText;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

import online.money_daisuki.api.base.Converter;

public final class ShowTextNodeToLinesConverter implements Converter<Node, List<BitmapText>> {
	@Override
	public List<BitmapText> convert(final Node value) {
		final List<BitmapText> texts = new ArrayList<>();
		
		for(int i = 0;; i++) {
			final Spatial spatial = value.getChild("Line " + i);
			if(spatial == null) {
				break;
			} else {
				texts.add((BitmapText) spatial);
			}
		}
		return(texts);
	}
}
