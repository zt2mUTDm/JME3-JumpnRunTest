package online.money_daisuki.api.monkey.basegame.inventory;

import java.util.HashMap;
import java.util.Map;

import com.jme3.app.state.AppState;

import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.monkey.basegame.state.AppStateAdapter;

/**
 * <p>{@link AppState}, manages Inventories. Support linked inventories for games where multiple inventories are need or an unlinked inventory if only one is need.</p>
 * 
 * <p>Requires:</p>
 * 
 * <ul>
 * <li>A key (hashable) for linked inventories.</li>
 * <li>An object, representing the item. Requires {@link #equals(Object)} and optional but strong recommended {@link #hashCode()}. For example "Axe", "SmallHealPortion".</li>
 * <li>All Items wrapped into type {@link Item}, for example {@link ItemImpl}. That links the items with there allowed stack size. For example new ItemImpl("Axe", 1); or new ItemImpl("SmallHealPortion", 9);</li>
 * <li>Call {@link #createInventory(Object)}, {@link #getInventory(Object)} and {@link #removeInventory(Object)} to create, get and remove a linked inventory or {@link #createDefaultInventory()} and {@link #removeDefaultInventory()} for a single Inventory.</li>
 * </ul>
 * 
 * @author Money Daisuki Online
 *
 * @param <K>
 * @param <V>
 */
public final class InventoryAppState<K, V> extends AppStateAdapter {
	private Map<K, Inventory<V>> inventories;
	private Inventory<V> defaultInventory;
	
	private int maxSize;
	private int initSize;
	
	public InventoryAppState() {
		this(128);
	}
	public InventoryAppState(final int maxSize) {
		this(maxSize, Math.min(maxSize, 16));
	}
	public InventoryAppState(final int maxSize, final int initSize) {
		this.maxSize = Requires.greaterThanZero(maxSize, "maxSize <= 0");
		this.initSize = Requires.positive(Requires.lessThanOrEquals(initSize, maxSize, "initSize > maxSize"), "initSize < 0");
	}
	
	/**
	 * Creates an {@link Inventory} and link it with the key.
	 * @param key
	 * @return The new created {@link Inventory}.
	 */
	public Inventory<V> createInventory(final K key) {
		final Inventory<V> inv = createInventoryImpl();
		if(inventories == null) {
			inventories = new HashMap<>();
		}
		inventories.put(key, inv);
		return(inv);
	}
	/**
	 * Get a {@link Inventory} that was linked with the key.
	 * @param key
	 * @return The linked {@link Inventory} or null, if none.
	 */
	public Inventory<V> getInventory(final K key) {
		return(inventories.get(key));
	}
	/**
	 * Remove a linked {@link Inventory}.
	 * @param key
	 */
	public void removeInventory(final K key) {
		inventories.remove(key);
		if(inventories.isEmpty()) {
			inventories = null;
		}
	}
	
	/**
	 * Create the default {@link Inventory}. A second call overrides the old one.
	 * @return The default {@link Inventory}.
	 */
	public Inventory<V> createDefaultInventory() {
		defaultInventory = createInventoryImpl();
		return(defaultInventory);
	}
	/**
	 * Get the default inventory.
	 * @return The default {@link Inventory} or null, if there is none.
	 */
	public Inventory<V> getDefaultInventory() {
		return(defaultInventory);
	}
	/**
	 * Remove the default inventory.
	 */
	public void removeDefaultInventory() {
		defaultInventory = null;
	}
	
	private Inventory<V> createInventoryImpl() {
		return(new InventoryImpl<>(maxSize, initSize));
	}
}
