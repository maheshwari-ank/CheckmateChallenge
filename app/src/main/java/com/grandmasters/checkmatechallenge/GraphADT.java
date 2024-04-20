package com.grandmasters.checkmatechallenge;

import java.util.Set;
import java.util.List;
public interface GraphADT<T>  {

        // Adds a vertex to the graph
        void addVertex(T vertex);

        // Adds an edge between two vertices
        void addEdge(T source, T destination, boolean bidirectional);

        // Removes all edges from a vertex
        void removeEdgesFromVertex(T vertex);

        // Returns a list of adjacent vertices to the given vertex
        List<T> getAdjacentVertices(T vertex);

        // Returns a set of all vertices in the graph
        Set<T> getVertices();

        // Returns a string representation of the graph
        @Override
        String toString();

}
