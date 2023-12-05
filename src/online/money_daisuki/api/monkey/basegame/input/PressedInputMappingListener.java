package online.money_daisuki.api.monkey.basegame.input;

import java.util.HashSet;
import java.util.Set;

import com.jme3.input.controls.ActionListener;

public final class PressedInputMappingListener implements ActionListener {
	private final Set<String> mappings;
	
	public PressedInputMappingListener() {
		mappings = new HashSet<>();
	}
	
	@Override
	public void onAction(final String name, final boolean isPressed, final float tpf) {
		if(isPressed) {
			mappings.add(name);
		} else {
			mappings.remove(name);
		}
	}
	public boolean isKeyPressed(final String mapping) {
		return(mappings.contains(mapping));
	}
}
