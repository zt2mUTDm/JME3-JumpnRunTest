package online.money_daisuki.api.base;

import java.math.BigInteger;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Util class for fast checking values (especially arguments) and throw exceptions.
 * @author (c) Money Daisuki Online
 */
public final class Requires {
	private Requires() {
		throw new UnsupportedOperationException();
	}
	
	public static boolean isTrue(final boolean b) {
		return(isTrue(b, "Required to be true"));
	}
	public static boolean isTrue(final boolean b, final String message) {
		return(isTrue(b, new IllegalArgumentException(message)));
	}
	public static boolean isTrue(final boolean b, final RuntimeException t) {
		if(!b) {
			throw t;
		}
		return(b);
	}
	
	
	public static boolean isFalse(final boolean b) {
		return(isFalse(b, "Required to be false"));
	}
	public static boolean isFalse(final boolean b, final String message) {
		return(isFalse(b, new IllegalArgumentException(message)));
	}
	public static boolean isFalse(final boolean b, final RuntimeException t) {
		return(isTrue(!b, t));
	}
	
	
	public static int positive(final int i) {
		return(positive(i, "Must be positive: " + i));
	}
	public static int positive(final int i, final String message) {
		return(positive(i, new IllegalArgumentException(message)));
	}
	public static int positive(final int i, final RuntimeException e) {
		isTrue(i >= 0, e);
		return(i);
	}
	
	public static long positive(final long l) {
		return(positive(l, "Must be positive: " + l));
	}
	public static long positive(final long l, final String message) {
		return(positive(l, new IllegalArgumentException(message)));
	}
	public static long positive(final long l, final RuntimeException e) {
		isTrue(l >= 0, e);
		return(l);
	}
	
	public static float positive(final float f) {
		return(positive(f, "Must be positive: " + f));
	}
	public static float positive(final float f, final String message) {
		return(positive(f, new IllegalArgumentException(message)));
	}
	public static float positive(final float f, final RuntimeException e) {
		isTrue(f >= 0, e);
		return(f);
	}
	
	
	public static BigInteger positive(final BigInteger i) {
		return(positive(i, "Must be positive: " + i));
	}
	public static BigInteger positive(final BigInteger value, final String message) {
		return(positive(value, new IllegalArgumentException(message)));
	}
	public static BigInteger positive(final BigInteger value, final RuntimeException e) {
		isTrue(notNull(value).compareTo(BigInteger.ZERO) >= 0, e);
		return(value);
	}
	
	
	public static int greaterThanZero(final int i) {
		return(greaterThanZero(i, "i have to be greater than zero (is " + i + ")"));
	}
	public static int greaterThanZero(final int i, final String message) {
		return(greaterThanZero(i, new IllegalArgumentException(message)));
	}
	public static int greaterThanZero(final int i, final RuntimeException exept) {
		return(greaterThan(i, 0, exept));
	}
	
	public static long greaterThanZero(final long l) {
		return(greaterThanZero(l, "l have to be greater than zero (is " + l + ")"));
	}
	public static long greaterThanZero(final long l, final String message) {
		return(greaterThanZero(l, new IllegalArgumentException(message)));
	}
	public static long greaterThanZero(final long l, final RuntimeException exept) {
		return(greaterThan(l, 0, exept));
	}
	
	public static float greaterThanZero(final float f) {
		return(greaterThanZero(f, "f have to be greater than zero (is " + f + ")"));
	}
	public static float greaterThanZero(final float f, final String message) {
		return(greaterThanZero(f, new IllegalArgumentException(message)));
	}
	public static float greaterThanZero(final float f, final RuntimeException exept) {
		return(greaterThan(f, 0, exept));
	}
	
	
	public static int greaterThan(final int expectedHigher, final int max) {
		return(greaterThan(expectedHigher, max, expectedHigher + ">=" + max));
	}
	public static int greaterThan(final int expectedHigher, final int max, final String msg) {
		return(greaterThan(expectedHigher, max, new IllegalArgumentException(msg)));
	}
	public static int greaterThan(final int expectedHigher, final int max, final RuntimeException e) {
		isTrue(expectedHigher > max, e);
		return(expectedHigher);
	}
	
	public static long greaterThan(final long expectedHigher, final long max) {
		return(greaterThan(expectedHigher, max, expectedHigher + ">=" + max));
	}
	public static long greaterThan(final long expectedHigher, final long max, final String msg) {
		return(greaterThan(expectedHigher, max, new IllegalArgumentException(msg)));
	}
	public static long greaterThan(final long expectedHigher, final long max, final RuntimeException e) {
		isTrue(expectedHigher > max, e);
		return(expectedHigher);
	}
	
	public static float greaterThan(final float expectedHigher, final float max) {
		return(greaterThan(expectedHigher, max, expectedHigher + ">=" + max));
	}
	public static float greaterThan(final float expectedHigher, final float max, final String msg) {
		return(greaterThan(expectedHigher, max, new IllegalArgumentException(msg)));
	}
	public static float greaterThan(final float expectedHigher, final float max, final RuntimeException e) {
		isTrue(expectedHigher > max, e);
		return(expectedHigher);
	}
	
	
	public static int greaterThanOrEquals(final int expectedHigherOrEquals, final int max) {
		return(greaterThanOrEquals(expectedHigherOrEquals, max,
				expectedHigherOrEquals + ">=" + max));
	}
	public static int greaterThanOrEquals(final int expectedHigherOrEquals, final int max,
			final String msg) {
		return(greaterThanOrEquals(expectedHigherOrEquals, max, new IllegalArgumentException(msg)));
	}
	public static int greaterThanOrEquals(final int expectedHigherOrEquals, final int max,
			final RuntimeException e) {
		isTrue(expectedHigherOrEquals >= max, e);
		return(expectedHigherOrEquals);
	}
	
	public static long greaterThanOrEquals(final long expectedHigherOrEquals, final long max) {
		return(greaterThanOrEquals(expectedHigherOrEquals, max,
				expectedHigherOrEquals + ">=" + max));
	}
	public static long greaterThanOrEquals(final long expectedHigherOrEquals, final long max,
			final String msg) {
		return(greaterThanOrEquals(expectedHigherOrEquals, max, new IllegalArgumentException(msg)));
	}
	public static long greaterThanOrEquals(final long expectedHigherOrEquals, final long max,
			final RuntimeException e) {
		isTrue(expectedHigherOrEquals >= max, e);
		return(expectedHigherOrEquals);
	}
	
	
	public static int lessThan(final int expectedLower, final int max) {
		return(lessThan(expectedLower, max, expectedLower + ">=" + max));
	}
	public static int lessThan(final int expectedLower, final int max, final String msg) {
		return(lessThan(expectedLower, max, new IllegalArgumentException(msg)));
	}
	public static int lessThan(final int expectedLower, final int max, final RuntimeException e) {
		isTrue(expectedLower < max, e);
		return(expectedLower);
	}
	
	
	public static int lessThanOrEquals(final int expectedLowerOrEquals, final int max) {
		return(lessThanOrEquals(expectedLowerOrEquals, max, expectedLowerOrEquals + " > " + max));
	}
	public static int lessThanOrEquals(final int expectedLowerOrEquals, final int max, final String msg) {
		return(lessThanOrEquals(expectedLowerOrEquals, max, new IllegalArgumentException(msg)));
	}
	public static int lessThanOrEquals(final int expectedLowerOrEquals, final int max, final RuntimeException e) {
		isTrue(expectedLowerOrEquals <= max, e);
		return(expectedLowerOrEquals);
	}
	
	public static long lessThanOrEquals(final long expectedLowerOrEquals, final long max) {
		return(lessThanOrEquals(expectedLowerOrEquals, max, expectedLowerOrEquals + " > " + max));
	}
	public static long lessThanOrEquals(final long expectedLowerOrEquals, final long max, final String msg) {
		return(lessThanOrEquals(expectedLowerOrEquals, max, new IllegalArgumentException(msg)));
	}
	public static long lessThanOrEquals(final long expectedLowerOrEquals, final long max, final RuntimeException e) {
		isTrue(expectedLowerOrEquals <= max, e);
		return(expectedLowerOrEquals);
	}
	
	
	public static <T> T notNull(final T value) {
		return(notNull(value, "value == null"));
	}
	public static <T> T notNull(final T value, final String message) {
		return(notNull(value, new IllegalArgumentException(message)));
	}
	public static <T> T notNull(final T value, final RuntimeException e) {
		isTrue(value != null, e);
		return(value);
	}
	
	public static void equal(final Object a, final Object b) {
		equal(a, b, a + " and " + b + " are not equal or null");
	}
	public static void equal(final Object a, final Object b, final String message) {
		equal(a,  b, new IllegalArgumentException(message));
	}
	public static void equal(final Object a, final Object b, final RuntimeException e) {
		isTrue(a != null ? a.equals(b) : false, e);
	}
	
	public static void antiqual(final Object a, final Object b) {
		antiqual(a, b, a + " and " + b + " are equal");
	}
	public static void antiqual(final Object a, final Object b, final String message) {
		antiqual(a, b, new IllegalArgumentException(message));
	}
	public static void antiqual(final Object a, final Object b, final RuntimeException e) {
		isFalse(a != null ? a.equals(b) : true, e);
	}
	
	
	public static void arrayEquals(final byte[] bs0, final byte[] bs1) {
		arrayEquals(bs0, bs1, "Arrays required to be equals");
	}
	public static void arrayEquals(final byte[] bs0, final byte[] bs1,
			final String message) {
		arrayEquals(bs0, bs1, new IllegalArgumentException(message));
	}
	public static void arrayEquals(final byte[] bs0, final byte[] bs1,
			final RuntimeException e) {
		final int size = bs0.length;
		if(size != bs1.length) {
			throw e;
		}
		
		for(int i = 0; i < size; i++) {
			if(bs0[i] != bs1[i]) {
				throw e;
			}
		}
	}
	
	public static float[] lenEqual(final float[] array, final int i) {
		equal(array.length, i);
		return(array);
	}
	public static float[] lenEqual(final float[] array, final int i, final String message) {
		equal(array.length, i, message);
		return(array);
	}
	public static float[] lenEqual(final float[] array, final int i, final RuntimeException e) {
		equal(array.length, i, e);
		return(array);
	}
	
	public static void lenEqual(final Object[] array, final int i) {
		equal(array.length, i);
	}
	public static void lenEqual(final Object[] array, final int i, final String message) {
		equal(array.length, i, message);
	}
	public static void lenEqual(final Object[] array, final int i, final RuntimeException e) {
		equal(array.length, i, e);
	}
	
	public static boolean[] lenGreaterThanZero(final boolean[] value) {
		return(lenGreaterThanZero(value, "value.length == 0"));
	}
	public static boolean[] lenGreaterThanZero(final boolean[] value, final String message) {
		return(lenGreaterThanZero(value, new IllegalArgumentException(message)));
	}
	public static boolean[] lenGreaterThanZero(final boolean[] value,
			final RuntimeException e) {
		greaterThanZero(Requires.notNull(value, "value == null").length, e);
		return(value);
	}
	
	public static byte[] lenGreaterThanZero(final byte[] value) {
		return(lenGreaterThanZero(value, "value.length == 0"));
	}
	public static byte[] lenGreaterThanZero(final byte[] value, final String message) {
		return(lenGreaterThanZero(value, new IllegalArgumentException(message)));
	}
	public static byte[] lenGreaterThanZero(final byte[] value,
			final RuntimeException e) {
		greaterThanZero(Requires.notNull(value, "value == null").length, e);
		return(value);
	}
	
	public static short[] lenGreaterThanZero(final short[] value) {
		return(lenGreaterThanZero(value, "value.length == 0"));
	}
	public static short[] lenGreaterThanZero(final short[] value, final String message) {
		return(lenGreaterThanZero(value, new IllegalArgumentException(message)));
	}
	public static short[] lenGreaterThanZero(final short[] value,
			final RuntimeException e) {
		greaterThanZero(Requires.notNull(value, "value == null").length, e);
		return(value);
	}
	
	public static char[] lenGreaterThanZero(final char[] value) {
		return(lenGreaterThanZero(value, "value.length == 0"));
	}
	public static char[] lenGreaterThanZero(final char[] value, final String message) {
		return(lenGreaterThanZero(value, new IllegalArgumentException(message)));
	}
	public static char[] lenGreaterThanZero(final char[] value,
			final RuntimeException e) {
		greaterThanZero(Requires.notNull(value, "value == null").length, e);
		return(value);
	}
	
	public static int[] lenGreaterThanZero(final int[] value) {
		return(lenGreaterThanZero(value, "value.length == 0"));
	}
	public static int[] lenGreaterThanZero(final int[] value, final String message) {
		return(lenGreaterThanZero(value, new IllegalArgumentException(message)));
	}
	public static int[] lenGreaterThanZero(final int[] value,
			final RuntimeException e) {
		greaterThanZero(Requires.notNull(value, "value == null").length, e);
		return(value);
	}
	
	public static long[] lenGreaterThanZero(final long[] value) {
		return(lenGreaterThanZero(value, "value.length == 0"));
	}
	public static long[] lenGreaterThanZero(final long[] value, final String message) {
		return(lenGreaterThanZero(value, new IllegalArgumentException(message)));
	}
	public static long[] lenGreaterThanZero(final long[] value,
			final RuntimeException e) {
		greaterThanZero(Requires.notNull(value, "value == null").length, e);
		return(value);
	}
	
	public static <T> T[] lenGreaterThanZero(final T[] value) {
		return(lenGreaterThanZero(value, "value.length == 0"));
	}
	public static <T> T[] lenGreaterThanZero(final T[] value,
			final String message) {
		return(lenGreaterThanZero(value, new IllegalArgumentException(message)));
	}
	public static <T> T[] lenGreaterThanZero(final T[] value,
			final RuntimeException e) {
		greaterThanZero(Requires.notNull(value, "value == null").length, e);
		return(value);
	}
	
	public static <T> Collection<T> lenGreaterThanZero(final Collection<T> value) {
		return(lenGreaterThanZero(value, "value is empty"));
	}
	public static <T> Collection<T> lenGreaterThanZero(final Collection<T> value,
			final String message) {
		return(lenGreaterThanZero(value, new IllegalArgumentException("value is empty")));
	}
	public static <T> Collection<T> lenGreaterThanZero(final Collection<T> value,
			final RuntimeException e) {
		greaterThanZero(Requires.notNull(value, "value == null").size(), e);
		return(value);
	}
	
	public static long[] lenGreaterThan(final long[] value, final int greaterAs) {
		return(lenGreaterThanZero(value, "value.length == 0"));
	}
	public static long[] lenGreaterThanZero(final long[] value, final int greaterAs, final String message) {
		return(lenGreaterThanZero(value, greaterAs, new IllegalArgumentException(message)));
	}
	public static long[] lenGreaterThanZero(final long[] value, final int greaterAs,
			final RuntimeException e) {
		greaterThan(Requires.notNull(value, "value == null").length, greaterAs, e);
		return(value);
	}
	
	public static <T> T[] lenGreaterThan(final T[] value, final int greaterAs) {
		return(lenGreaterThanZero(value, "value.length == 0"));
	}
	public static <T> T[] lenGreaterThanZero(final T[] value, final int greaterAs, final String message) {
		return(lenGreaterThanZero(value, greaterAs, new IllegalArgumentException(message)));
	}
	public static <T> T[] lenGreaterThanZero(final T[] value, final int greaterAs,
			final RuntimeException e) {
		greaterThan(Requires.notNull(value, "value == null").length, greaterAs, e);
		return(value);
	}
	
	public static void sizeEqual(final Collection<?> c, final int i) {
		equal(c.size(), i);
	}
	public static void sizeEqual(final Collection<?> c, final int i, final String message) {
		equal(c.size(), i, message);
	}
	public static void sizeEqual(final Collection<?> c, final int i, final RuntimeException e) {
		equal(c.size(), i, e);
	}
	
	public static <T> Collection<T> sizeGreaterThan(final Collection<T> c, final int i) {
		greaterThan(c.size(), i);
		return(c);
	}
	public static <T> Collection<T> sizeGreaterThan(final Collection<T> c, final int i, final String message) {
		greaterThan(c.size(), i, message);
		return(c);
	}
	public static <T> Collection<T> sizeGreaterThan(final Collection<T> c, final int i, final RuntimeException e) {
		greaterThan(c.size(), i, e);
		return(c);
	}
	
	public static <T> List<T> sizeGreaterThan(final List<T> c, final int i) {
		greaterThan(c.size(), i);
		return(c);
	}
	public static <T> List<T> sizeGreaterThan(final List<T> c, final int i, final String message) {
		greaterThan(c.size(), i, message);
		return(c);
	}
	public static <T> List<T> sizeGreaterThan(final List<T> c, final int i, final RuntimeException e) {
		greaterThan(c.size(), i, e);
		return(c);
	}
	
	
	public static void sizeEqual(final Map<?, ?> m, final int i) {
		equal(m.size(), i);
	}
	public static void sizeEqual(final Map<?, ?> m, final int i, final String message) {
		equal(m.size(), i, message);
	}
	public static void sizeEqual(final Map<?, ?> m, final int i, final RuntimeException e) {
		equal(m.size(), i, e);
	}
	
	public static void sizeGreaterThan(final Object[] array, final int i) {
		greaterThan(array.length, i);
	}
	public static void sizeGreaterThan(final Object[] array, final int i, final String message) {
		greaterThan(array.length, i, message);
	}
	public static void sizeGreaterThan(final Object[] array, final int i, final RuntimeException e) {
		greaterThan(array.length, i, e);
	}
	
	public static <T> T[] containsNotNull(final T[] value) {
		return(containsNotNull(value, "value contains null"));
	}
	public static <T> T[] containsNotNull(final T[] value, final String message) {
		return(containsNotNull(value, new IllegalArgumentException(message)));
	}
	public static <T> T[] containsNotNull(final T[] value, final RuntimeException e) {
		for(final T t:value) {
			Requires.notNull(t, e);
		}
		return(value);
	}
	
	public static <T> Collection<T> containsNotNull(final Collection<T> value) {
		return (containsNotNull(value, "value contains null"));
	}
	public static <T> Collection<T> containsNotNull(final Collection<T> value, final String message) {
		return(containsNotNull(value, new RuntimeException(message)));
	}
	public static <T> Collection<T> containsNotNull(final Collection<T> value, final RuntimeException e) {
		return (Collection<T>) (containsNotNull((Iterable<T>)value, e));
	}
	
	public static <T> List<T> containsNotNull(final List<T> value) {
		return (containsNotNull(value, "value contains null"));
	}
	public static <T> List<T> containsNotNull(final List<T> value, final String message) {
		return(containsNotNull(value, new RuntimeException(message)));
	}
	public static <T> List<T> containsNotNull(final List<T> value, final RuntimeException e) {
		return (List<T>) (containsNotNull((Iterable<T>)value, e));
	}
	
	public static <T> Set<T> containsNotNull(final Set<T> value) {
		return (containsNotNull(value, "value contains null"));
	}
	public static <T> Set<T> containsNotNull(final Set<T> value, final String message) {
		return(containsNotNull(value, new RuntimeException(message)));
	}
	public static <T> Set<T> containsNotNull(final Set<T> value, final RuntimeException e) {
		return (Set<T>) (containsNotNull((Iterable<T>)value, e));
	}
	
	
	public static <T> Iterable<T> containsNotNull(final Iterable<T> value) {
		return(containsNotNull(value, "contains null"));
	}
	public static <T> Iterable<T> containsNotNull(final Iterable<T> value, final String message) {
		return(containsNotNull(value, new IllegalArgumentException(message)));
	}
	public static <T> Iterable<T> containsNotNull(final Iterable<T> value, final RuntimeException exception) {
		for(final T t:value) {
			if(t == null) {
				throw exception;
			}
		}
		return(value);
	}
	
	public static <T, U> Map<T, U> containsNotNull(final Map<T, U> value) {
		return(containsNotNull(value, "value contains null"));
	}
	public static <T, U> Map<T, U> containsNotNull(final Map<T, U> value, final String message) {
		return(containsNotNull(value, new IllegalArgumentException(message)));
	}
	public static <T, U> Map<T, U> containsNotNull(final Map<T, U> value, final RuntimeException e) {
		for(final Entry<T, U> entry:notNull(value, "value == null").entrySet()) {
			final T t = entry.getKey();
			final U u = entry.getValue();
			
			notNull(t, e);
			notNull(u, e);
		}
		return(value);
	}

}
