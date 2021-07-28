package tools;

import java.io.Serializable;
import java.util.Collection;
import java.util.EnumMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public final class ConcurrentEnumMap<K extends Enum<K>, V> extends EnumMap<K, V> implements Serializable { //TODO: Implement Externalizable.

    private static final long serialVersionUID = 11920818021L;
    private ReentrantReadWriteLock reentlock = new ReentrantReadWriteLock();
    private Lock rL = reentlock.readLock(), wL = reentlock.writeLock();

    public ConcurrentEnumMap(Class<K> keyType) {
        super(keyType);
    }

    @Override
    public void clear() {
        wL.lock();
        try {
            super.clear();
        } finally {
            wL.unlock();
        }
    }

    @Override
    public EnumMap<K, V> clone() {
        return super.clone();
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public boolean containsKey(Object key) {
        rL.lock();
        try {
            return super.containsKey(key); // An apparent infinite recursive loop
        } finally {
            rL.unlock();
        }
    }

    @Override
    public boolean containsValue(Object value) {
        rL.lock();
        try {
            return super.containsValue(value); // An apparent infinite recursive loop
        } finally {
            rL.unlock();
        }
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        rL.lock();
        try {
            return super.entrySet();
        } finally {
            rL.unlock();
        }
    }

    @Override
    public V get(Object key) {
        rL.lock();
        try {
            return super.get(key);
        } finally {
            rL.unlock();
        }
    }

    @Override
    public Set<K> keySet() {
        rL.lock();
        try {
            return super.keySet();
        } finally {
            rL.unlock();
        }
    }

    @Override
    public V put(K key, V value) {
        wL.lock();
        try {
            return super.put(key, value);
        } finally {
            wL.unlock();
        }
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        wL.lock();
        try {
            super.putAll(m);
        } finally {
            wL.unlock();
        }
    }

    @Override
    public V remove(Object key) {
        wL.lock();
        try {
            return super.remove(key);
        } finally {
            wL.unlock();
        }
    }

    @Override
    public int size() {
        rL.lock();
        try {
            return super.size();
        } finally {
            rL.unlock();
        }
    }

    @Override
    public Collection<V> values() {
        rL.lock();
        try {
            return super.values();
        } finally {
            rL.unlock();
        }
    }
}
