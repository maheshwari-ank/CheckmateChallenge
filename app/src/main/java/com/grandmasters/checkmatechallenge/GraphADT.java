package com.grandmasters.checkmatechallenge;

import java.util.Set;

public interface GraphADT<T>  {

        // Adds a vertex to the graph
        void addVertex(T vertex);

        // Adds an edge between two vertices
        void addEdge(T source, T destination, boolean bidirectional);

        // Removes all edges from a vertex
        void removeEdgesFromVertex(T vertex);

        // Returns a list of adjacent vertices to the given vertex
        CustomList<T> getAdjacentVertices(T vertex);
        Set<T> getVertices();

        // Returns a string representation of the graph
        @Override
        String toString();

}
