package online.money_daisuki.api.monkey.basegame.inventory;

import java.util.Objects;

import online.money_daisuki.api.base.Requires;

public final class ItemImpl<T> implements Item<T> {
	private final T content;
	private final int stackSize;

	public ItemImpl(final T content, final int stackSize) {
		this.content = Requires.notNull(content, "content == null");
		this.stackSize = Requires.positive(stackSize, "stackSize < 0");
	}
	@Override
	public T getContent() {
		return(content);
	}
	@Override
	public int getMaxStackSize() {
		return(stackSize);
	}
	
	@Override
	public boolean equals(final Object obj) {
		if(obj == this) {
			return(true);
		} else if(obj.getClass() != getClass()) {
			return(false);
		}
		final ItemImpl<?> cast = (ItemImpl<?>) obj;
		return(content.equals(cast.content) && stackSize == cast.stackSize);
	}
	@Override
	public int hashCode() {
		return(Objects.hash(content, stackSize));
	}
}
