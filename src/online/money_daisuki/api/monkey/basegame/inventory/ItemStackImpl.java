package online.money_daisuki.api.monkey.basegame.inventory;

import java.util.Objects;

import online.money_daisuki.api.base.Requires;

final class ItemStackImpl<T> implements ItemStack<T> {
	private final Item<T> item;
	private final int maxSize;
	
	private int size;
	
	public ItemStackImpl(final Item<T> item, final int initSize, final int maxSize) {
		this.item = Requires.notNull(item, "item == null");
		this.size = Requires.positive(Requires.lessThanOrEquals(initSize, maxSize, "initSize > maxSize"), "initSize < 0");
		this.maxSize = Requires.greaterThanZero(maxSize, "maxSize <= 0");
	}
	@Override
	public Item<T> getItem() {
		return(item);
	}
	@Override
	public boolean isFull() {
		return(size == maxSize);
	}
	@Override
	public void incrementCount() {
		if(isFull()) {
			throw new IllegalStateException("Stack is already full");
		}
		size++;
	}
	@Override
	public void decrementCount() {
		if(size == 0) {
			throw new IllegalStateException("Stack is empty");
		}
		size--;
	}
	@Override
	public int getCount() {
		return(size);
	}
	
	@Override
	public boolean equals(final Object obj) {
		if(obj == this) {
			return(true);
		} else if(obj.getClass() != getClass()) {
			return(false);
		}
		final ItemStackImpl<?> cast = (ItemStackImpl<?>) obj;
		return(Objects.equals(item, cast.item) && Objects.equals(maxSize, maxSize) && Objects.equals(size, size));
	}
	@Override
	public int hashCode() {
		return(Objects.hash(item, maxSize, size));
	}
}
