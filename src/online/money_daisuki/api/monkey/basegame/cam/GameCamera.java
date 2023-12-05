package online.money_daisuki.api.monkey.basegame.cam;

public interface GameCamera {
	
	public void acquire();
	
	public void setEnabled(boolean b);
	
	public boolean isEnabled();
	
	public void dispose();
	
}
