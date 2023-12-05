package online.money_daisuki.api.monkey.basegame.inventory;

public interface ItemStack<T> {
	
	Item<T> getItem();
	
	boolean isFull();
	
	void incrementCount();
	
	void decrementCount();
	
	int getCount();
	
}
