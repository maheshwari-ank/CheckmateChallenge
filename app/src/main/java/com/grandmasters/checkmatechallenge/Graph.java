package com.grandmasters.checkmatechallenge;

import java.io.Serializable;
import java.util.*;
import java.util.HashMap;

class Graph implements Serializable {

    private Map<Square, List<Square>> adjacencyMap;

    public Graph() {
        this.adjacencyMap = new HashMap<>();
    }

    public void addVertex(Square vertex) {
        adjacencyMap.put(vertex, new LinkedList<>());
    }

    public void addEdge(Square source, Square destination, boolean bidirectional) {
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

    public void removeEdgesFromVertex(Square vertex) {
        if (adjacencyMap.containsKey(vertex)) {
            adjacencyMap.get(vertex).clear();
        }
    }

    public List<Square> getAdjacentVertices(Square vertex) {
        return adjacencyMap.getOrDefault(vertex, new LinkedList<>());
    }

    public Set<Square> getVertices() {
        return adjacencyMap.keySet();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        for (Square vertex : adjacencyMap.keySet()) {
            builder.append(vertex.toString()).append(": ");
            for (Square neighbor : adjacencyMap.get(vertex)) {
                builder.append(neighbor.toString()).append(" ");
            }
            builder.append("\n");
        }

        return builder.toString();
    }
}

