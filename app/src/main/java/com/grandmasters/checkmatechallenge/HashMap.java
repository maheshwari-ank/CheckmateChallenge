package com.grandmasters.checkmatechallenge;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class HashMap<K, V> implements Serializable, MapADT<K, V>{

    public static class Entry<K, V> implements Serializable{
        private final K key;
        private V value;

        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        public void setValue(V value) {
            this.value = value;
        }
    }

    private final int capacity;
    private final CustomList<Entry<K, V>>[] buckets;

    @SuppressWarnings("unchecked")
    public HashMap() {
        this.capacity = 10; // Default capacity
        this.buckets = new CustomList[capacity];
        for (int i = 0; i < capacity; i++) {
            buckets[i] = new CustomList<>();
        }
    }

    private int hash(K key) {
        return Math.abs(key.hashCode() % capacity);
    }

    public void put(K key, V value) {
        int index = hash(key);
        CustomList<Entry<K, V>> bucket = buckets[index];
        for (Entry<K, V> entry : bucket) {
            if (entry.getKey().equals(key)) {
                entry.setValue(value);
                return;
            }
        }
        bucket.add(new Entry<>(key, value));
    }

    public V get(K key) {
        int index = hash(key);
        CustomList<Entry<K, V>> bucket = buckets[index];
        for (Entry<K, V> entry : bucket) {
            if (entry.getKey().equals(key)) {
                return entry.getValue();
            }
        }
        return null; // Key not found
    }

    public void remove(K key) {
        int index = hash(key);
        CustomList<Entry<K, V>> bucket = buckets[index];
        for (int i = 0; i < bucket.size(); i++) {
            Entry<K, V> entry = bucket.get(i);
            if (entry.getKey().equals(key)) {
                bucket.remove(i);
                return;
            }
        }
    }

    public boolean containsKey(K key) {
        int index = hash(key);
        CustomList<Entry<K, V>> bucket = buckets[index];
        for (Entry<K, V> entry : bucket) {
            if (entry.getKey().equals(key)) {
                return true;
            }
        }
        return false;
    }

    public boolean isEmpty() {
        for (CustomList<Entry<K, V>> bucket : buckets) {
            if (!bucket.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public int size() {
        int size = 0;
        for (CustomList<Entry<K, V>> bucket : buckets) {
            size += bucket.size();
        }
        return size;
    }

    public void clear() {
        for (CustomList<Entry<K, V>> bucket : buckets) {
            bucket.clear();
        }
    }

    public V getOrDefault(Object key, V defaultValue) {
        // Implement getOrDefault
        V value = get((K) key);
        return (value != null) ? value : defaultValue;
    }

    public Set<K> keySet() {
        // Implement keySet
        Set<K> keys = new HashSet<>();
        for (CustomList<Entry<K, V>> bucket : buckets) {
            for (Entry<K, V> entry : bucket) {
                keys.add(entry.getKey());
            }
        }
        return keys;
    }

    public V[] values() {
        CustomList<V> valuesList = new CustomList<>();

        for (CustomList<Entry<K, V>> bucket : buckets) {
            for (Entry<K, V> entry : bucket) {
                valuesList.add(entry.getValue());
            }
        }

        @SuppressWarnings("unchecked")
        V[] valuesArray = (V[]) new Object[valuesList.size()];
        int i = 0;
        for (V value : valuesList) {
            valuesArray[i++] = value;
        }

        return valuesArray;
    }
}

