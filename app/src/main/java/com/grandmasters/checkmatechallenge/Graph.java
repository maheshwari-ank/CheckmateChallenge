package com.grandmasters.checkmatechallenge;

import java.io.Serializable;
import java.util.Set;

class Graph<T> implements Serializable, GraphADT<T> {

    private HashMap<T, CustomList<T>> adjacencyMap;

    public Graph() {
        this.adjacencyMap = new HashMap<>();
    }

    public void addVertex(T vertex) {
        adjacencyMap.put(vertex, new CustomLinkedList<>());
    }

    public void addEdge(T source, T destination, boolean bidirectional) {
        if (!adjacencyMap.containsKey(source)) {
            addVertex(source);
        }

        if (!adjacencyMap.containsKey(destination)) {
            addVertex(destination);
        }

        adjacencyMap.get(source).add(destination);

        if (bidirectional) {
            adjacencyMap.get(destination).add(source);
        }
    }

    public void removeEdgesFromVertex(T vertex) {
        if (adjacencyMap.containsKey(vertex)) {
            adjacencyMap.get(vertex).clear();
        }
    }
    public CustomList<T> getAdjacentVertices(T vertex) {
        return  adjacencyMap.getOrDefault(vertex, new CustomLinkedList<>());
    }

    public Set<T> getVertices() {
        return adjacencyMap.keySet();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        for (T vertex : adjacencyMap.keySet()) {
            builder.append(vertex.toString()).append(": ");
            for (T neighbor : adjacencyMap.get(vertex)) {
                builder.append(neighbor.toString()).append(" ");
            }
            builder.append("\n");
        }

        return builder.toString();
    }
}

