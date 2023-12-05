package online.money_daisuki.api.monkey.basegame.collections;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

import online.money_daisuki.api.base.DataSource;
import online.money_daisuki.api.base.Requires;

public final class SafeSet<T> implements Set<T> {
	private final Class<T> type;
	private final SetFactory<T> factory;
	
	private Set<T> set;
	private T[] array;
	
	public SafeSet(final Class<T> type, final SetFactory<T> factory) {
		this.type = Requires.notNull(type, "type == null");
		this.factory = Requires.notNull(factory, "factory == null");
		
		this.set = factory.createEmpty();
		this.array = null;
	}
	
	@Override
	public int size() {
		if(set != null) {
			return(set.size());
		} else {
			return(array.length);
		}
	}
	@Override
	public boolean isEmpty() {
		if(set != null) {
			return(set.isEmpty());
		} else {
			return(array.length == 0);
		}
	}
	@Override
	public boolean contains(final Object o) {
		if(set != null) {
			return(set.contains(o));
		} else {
			for(final Object o2:array) {
				if(Objects.equals(o2, o)) {
					return(true);
				}
			}
			return(false);
		}
	}	
	@Override
	public Iterator<T> iterator() {
		throw new UnsupportedOperationException("Use #getBuffer");
	}
	@Override
	public Object[] toArray() {
		ensureSet();
		return(set.toArray());
	}
	@Override
	public <U> U[] toArray(final U[] a) {
		ensureSet();
		return(set.toArray(a));
	}
	@Override
	public boolean add(final T e) {
		ensureSet();
		return(set.add(e));
	}
	@Override
	public boolean remove(final Object o) {
		ensureSet();
		return(set.remove(o));
	}
	@Override
	public boolean containsAll(final Collection<?> c) {
		ensureSet();
		return(set.containsAll(c));
	}
	@Override
	public boolean addAll(final Collection<? extends T> c) {
		ensureSet();
		return(set.addAll(c));
	}
	@Override
	public boolean retainAll(final Collection<?> c) {
		ensureSet();
		return(set.retainAll(c));
	}
	@Override
	public boolean removeAll(final Collection<?> c) {
		ensureSet();
		return(set.removeAll(c));
	}
	@Override
	public void clear() {
		ensureSet();
		set.clear();
	}
	
	public T[] getArray() {
		ensureArray();
		return (array);
	}
	
	private void ensureSet() {
		if(set != null) {
			return;
		}
		
		set = factory.createFromArray(array);
		array = null;
	}
	@SuppressWarnings("unchecked")
	private void ensureArray() {
		if(array != null) {
			return;
		}
		
		array = (T[]) Array.newInstance(type, size());
		array = set.toArray(array);
		set = null;
	}
	
	public static final class TreeSetFactory<U> implements SetFactory<U> {
		private final DataSource<Set<U>> subFactory;
		
		public TreeSetFactory() {
			subFactory = new DataSource<Set<U>>() {
				@Override
				public Set<U> source() {
					return(new TreeSet<>());
				}
			};
		}
		public TreeSetFactory(final Comparator<? super U> comparator) {
			// No direct check performed
			subFactory = new DataSource<Set<U>>() {
				@Override
				public Set<U> source() {
					return(new TreeSet<>(comparator));
				}
			};
		}
		
		@Override
		public Set<U> createEmpty() {
			return(subFactory.source());
		}
		@Override
		public Set<U> createFromArray(final U[] arr) {
			final Set<U> set = createEmpty();
			set.addAll(Arrays.asList(arr));
			return(set);
		}
	}
	public static final class HashSetFactory<U> implements SetFactory<U> {
		private final DataSource<Set<U>> subFactory;
		
		public HashSetFactory() {
			subFactory = new DataSource<Set<U>>() {
				@Override
				public Set<U> source() {
					return(new HashSet<>());
				}
			};
		}
		public HashSetFactory(final int initialCapacity) {
			// No direct check performed
			subFactory = new DataSource<Set<U>>() {
				@Override
				public Set<U> source() {
					return(new HashSet<>(initialCapacity));
				}
			};
		}
		public HashSetFactory(final int initialCapacity, final float loadFactor) {
			// No direct check performed
			subFactory = new DataSource<Set<U>>() {
				@Override
				public Set<U> source() {
					return(new HashSet<>(initialCapacity, loadFactor));
				}
			};
		}
		
		@Override
		public Set<U> createEmpty() {
			return(subFactory.source());
		}
		@Override
		public Set<U> createFromArray(final U[] arr) {
			final Set<U> set = createEmpty();
			set.addAll(Arrays.asList(arr));
			return(set);
		}
	}
	
	private interface SetFactory<T> {
		
		public Set<T> createEmpty();
		
		public Set<T> createFromArray(T[] arr);
		
	}
}
