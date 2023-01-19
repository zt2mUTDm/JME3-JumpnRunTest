package online.money_daisuki.api.monkey.basegame.text;

public interface ShowTextAppStateBackend {
	
	void addLetter(char c);
	
	void clear();
	
	void setVisible(boolean visible);
	
}
