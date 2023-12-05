package online.money_daisuki.api.monkey.basegame.inventory;

import java.util.Collection;

public interface Inventory<T> {
	
	boolean addItem(Item<T> item);
	
	Collection<Item<T>> addAllItems(Collection<Item<T>> items);
	
	boolean removeItem(Item<T> item);
	
	Collection<Item<T>> removeAllItems(Collection<Item<T>> items);
	
	int getItemCount(Item<T> item);
	
	int getStackCount(Item<T> item);
	
	int getContentCount(Item<T> item);
	
}
