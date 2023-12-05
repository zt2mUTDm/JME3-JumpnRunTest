package online.money_daisuki.api.monkey.basegame.character.anim;

public interface AnimPlayer {
	
	void play(String name, boolean loop);
	
	void addAnimationListener(AnimListener l);
	
	boolean removeAnimationListener(AnimListener l);
	
	void setSpeed(double d);
	
}
