package com.grandmasters.checkmatechallenge;

import java.util.Iterator;

public interface ListADT<T> extends Iterable<T>{
    public void add(T element);
    public T get(int index);
    public int size();
    public boolean isEmpty();
    public void clear();
    public Iterator<T> iterator();
}
