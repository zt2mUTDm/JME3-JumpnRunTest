package online.money_daisuki.api.monkey.console;

public interface Console {
	
	void appendOutputText(String s);
	
	void appendInputText(String text);
	
	void inputTextBackspace();
	
	void setVisible(boolean visible);
	
	boolean isVisible();
	
	void setInputText(String string);
	
	String getInputText();
	
	void addConsoleShownListener(Runnable l);
	
	void addConsoleHiddenListener(Runnable l);
	
	void removeConsoleShownListener(Runnable l);
	
	void removeConsoleHiddenListener(Runnable l);
	
}
