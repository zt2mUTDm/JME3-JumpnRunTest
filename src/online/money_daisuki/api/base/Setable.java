package online.money_daisuki.api.base;

/**
 * Interface for an Object that is maybe "set" and has the ability to get "unset".
 * 
 * @author (c) Money Daisuki Online
 */
public interface Setable {
	
	public boolean isSet();
	
	public void unset();
}
