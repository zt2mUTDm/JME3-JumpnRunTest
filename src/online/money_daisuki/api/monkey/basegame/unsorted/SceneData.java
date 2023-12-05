package online.money_daisuki.api.monkey.basegame.unsorted;

import java.util.Collection;

import com.jme3.scene.Node;

public interface SceneData {
	
	Node getCharacterNode();
	
	Collection<EventData> getEvents();
	
}
