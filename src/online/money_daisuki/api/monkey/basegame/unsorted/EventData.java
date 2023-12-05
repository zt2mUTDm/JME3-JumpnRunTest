package online.money_daisuki.api.monkey.basegame.unsorted;

import java.util.List;

import com.jme3.math.Transform;

public interface EventData {
	
	String getName();
	
	List<EventPart> getParts();
	
	Transform getTransform();
	
}
