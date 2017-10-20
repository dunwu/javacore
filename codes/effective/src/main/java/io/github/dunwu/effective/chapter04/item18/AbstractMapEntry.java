// Skeletal Implementation - Page 96
package io.github.dunwu.effective.chapter04.item18;

import java.util.Map;

public abstract class AbstractMapEntry<K, V> implements Map.Entry<K, V> {
	// Primitive operations
	public abstract K getKey();

	public abstract V getValue();

	// Entries in modifiable maps must override this method
	public V setValue(V value) {
		throw new UnsupportedOperationException();
	}

	// Implements the general contract of Map.Entry.equals
	@Override
	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (!(o instanceof Map.Entry))
			return false;
		Map.Entry<?, ?> arg = (Map.Entry) o;
		return equals(getKey(), arg.getKey())
				&& equals(getValue(), arg.getValue());
	}

	private static boolean equals(Object o1, Object o2) {
		return o1 == null ? o2 == null : o1.equals(o2);
	}

	// Implements the general contract of Map.Entry.hashCode
	@Override
	public int hashCode() {
		return hashCode(getKey()) ^ hashCode(getValue());
	}

	private static int hashCode(Object obj) {
		return obj == null ? 0 : obj.hashCode();
	}
}
