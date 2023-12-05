package online.money_daisuki.api.monkey.basegame.inventory;

public interface Item<T> {
	
	T getContent();
	
	int getMaxStackSize();
	
}
