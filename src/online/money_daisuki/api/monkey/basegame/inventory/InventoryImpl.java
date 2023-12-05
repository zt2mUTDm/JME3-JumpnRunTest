package online.money_daisuki.api.monkey.basegame.inventory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import online.money_daisuki.api.base.Requires;

class InventoryImpl<T> implements Inventory<T> {
	private final Collection<ItemStack<T>> content;
	
	private final int maxSize;
	
	public InventoryImpl(final int maxSize, final int initSize) {
		this.content = new ArrayList<>(initSize);
		this.maxSize = Requires.greaterThan(initSize, maxSize, "initSize > maxSize");
	}
	@Override
	public boolean addItem(final Item<T> item) {
		for(final ItemStack<T> stack:content) {
			if(stack.getItem().equals(item.getContent())) {
				if(!stack.isFull()) {
					stack.incrementCount();
					return(true);
				}
			}
		}
		
		if(content.size() == maxSize) {
			return(false);
		}
		content.add(new ItemStackImpl<T>(item, 1, item.getMaxStackSize()));
		return(true);
	}
	@Override
	public Collection<Item<T>> addAllItems(final Collection<Item<T>> items) {
		final Collection<Item<T>> couldNotAdd = new LinkedList<>();
		for(final Item<T> item:items) {
			if(!addItem(item)) {
				couldNotAdd.add(item);
			}
		}
		return(couldNotAdd);
	}
	@Override
	public boolean removeItem(final Item<T> item) {
		final Iterator<ItemStack<T>> it = content.iterator();
		while(it.hasNext()) {
			final ItemStack<T> stack = it.next();
			if(stack.getItem().equals(item.getContent())) {
				stack.decrementCount();
				if(stack.getCount() == 0) {
					it.remove();
				}
				return(true);
			}
		}
		return(false);
	}
	@Override
	public Collection<Item<T>> removeAllItems(final Collection<Item<T>> items) {
		final Collection<Item<T>> couldNotRemove = new LinkedList<>();
		for(final Item<T> item:items) {
			if(!removeItem(item)) {
				couldNotRemove.add(item);
			}
		}
		return(couldNotRemove);
	}
	
	@Override
	public int getItemCount(final Item<T> item) {
		int i = 0;
		for(final ItemStack<T> stack:content) {
			if(stack.getItem().equals(item.getContent())) {
				i+= stack.getCount();
			}
		}
		return(i);
	}
	@Override
	public int getStackCount(final Item<T> item) {
		return(content.size());
	}
	@Override
	public int getContentCount(final Item<T> item) {
		return(content.size());
	}
}
