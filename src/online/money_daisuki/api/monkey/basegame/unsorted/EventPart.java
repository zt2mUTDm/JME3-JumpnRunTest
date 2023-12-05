package online.money_daisuki.api.monkey.basegame.unsorted;

import java.util.Collection;

import com.jme3.scene.Spatial;

public interface EventPart {
	
	Spatial getCharacter();
	
	Collection<String[]> getScript();
	
}
