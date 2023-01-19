package online.money_daisuki.api.monkey.basegame.character.anim;

public interface AnimPlayer {
	
	void play(String name, boolean once);
	
	void play(String name, boolean once, Runnable l);
	
}
