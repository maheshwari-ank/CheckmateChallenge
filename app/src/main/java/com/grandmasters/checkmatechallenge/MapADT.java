package com.grandmasters.checkmatechallenge;

import java.util.Set;

public interface MapADT<K, V> {

    void put(K key, V value);

    V get(K key);

    void remove(K key);

    boolean containsKey(K key);

    boolean isEmpty();

    int size();

    void clear();

    V getOrDefault(Object key, V defaultValue);

    Set<K> keySet();

    V[] values();
}

