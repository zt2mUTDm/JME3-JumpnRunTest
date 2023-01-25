package online.money_daisuki.api.monkey.console;

public interface Console {
	
	void appendOutputText(String s);
	
	void appendInputText(String text);
	
	void inputTextBackspace();
	
	void setVisible(boolean visible);
	
	boolean isVisible();
	
	void setInputText(String string);
	
	String getInputText();
	
}
