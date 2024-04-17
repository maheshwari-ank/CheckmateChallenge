package com.grandmasters.checkmatechallenge;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;



public class HashMap<K, V> implements Serializable {
     public static class Entry<K, V> {
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
    private final List<Entry<K, V>>[] buckets;

    public HashMap() {
        this.capacity = 10; // Default capacity
        this.buckets = new List[capacity];
        for (int i = 0; i < capacity; i++) {
            buckets[i] = new ArrayList<>();
        }
    }

    private int hash(K key) {
        return Math.abs(key.hashCode() % capacity);
    }

    public void put(K key, V value) {
        int index = hash(key);
        List<Entry<K, V>> bucket = buckets[index];
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
        List<Entry<K, V>> bucket = buckets[index];
        for (Entry<K, V> entry : bucket) {
            if (entry.getKey().equals(key)) {
                return entry.getValue();
            }
        }
        return null; // Key not found
    }

    public void remove(K key) {
        int index = hash(key);
        List<Entry<K, V>> bucket = buckets[index];
        for (Entry<K, V> entry : bucket) {
            if (entry.getKey().equals(key)) {
                bucket.remove(entry);
                return;
            }
        }
    }

    public boolean containsKey(K key) {
        int index = hash(key);
        List<Entry<K, V>> bucket = buckets[index];
        for (Entry<K, V> entry : bucket) {
            if (entry.getKey().equals(key)) {
                return true;
            }
        }
        return false;
    }

    public boolean isEmpty() {
        for (List<Entry<K, V>> bucket : buckets) {
            if (!bucket.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public int size() {
        int size = 0;
        for (List<Entry<K, V>> bucket : buckets) {
            size += bucket.size();
        }
        return size;
    }

    public void clear() {
        for (List<Entry<K, V>> bucket : buckets) {
            bucket.clear();
        }
    }
}
