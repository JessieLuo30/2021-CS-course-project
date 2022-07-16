package hw8.spp;

import hw8.graph.Edge;
import hw8.graph.Graph;
import hw8.graph.Vertex;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class DijkstraStreetSearcher extends StreetSearcher {

  /**
   * Creates a StreetSearcher object.
   *
   * @param graph an implementation of Graph ADT.
   */
  public DijkstraStreetSearcher(Graph<String, String> graph) {
    super(graph);
  }

  @Override
  public void findShortestPath(String startName, String endName) {
    Vertex<String> start = vertices.get(startName);
    if (checkInputValidity(startName, endName)) {
      Map<Vertex<String>, VertexNodeD> set = new HashMap<>();
      PriorityQueue<VertexNodeD> queue = new PriorityQueue<>(new NodeComparator());
      VertexNodeD startNode = new VertexNodeD(start);
      startNode.distance = 0;
      queue.add(startNode);    // start with only start node
      int count = setup(start, graph.vertices().iterator(), set);
      for (int i = 0; i < count; i++) {
        VertexNodeD fet = queue.poll();   //get the node with shortest distance in queue
        Vertex<String> v = fet.vertex;
        if (v == vertices.get(endName)) {
          break;
        }
        search(v, queue, set);
      }
      printPath(endName, set, start);
    }
  }

  /**
   * Check whether the input start and end name exist in the vertices of the graoh.
   *
   * @param startName name of start vertex.
   * @param endName name of start vertex.
   */
  private boolean checkInputValidity(String startName, String endName) {
    if (vertices.get(startName) == null || vertices.get(endName) == null) {
      printPath(null, 0);
      return false;
    }
    return true;
  }

  /**
   * Initialize the set with the existing vertices.
   *
   * @param start start vertex.
   * @param iterator iterator to the graph vertices.
   * @param set that will contain all vertices in graph.
   */
  private int setup(Vertex<String> start, Iterator<Vertex<String>> iterator, Map<Vertex<String>, VertexNodeD> set) {
    int count = 0;
    while (iterator.hasNext()) {
      Vertex<String> next = iterator.next();
      VertexNodeD newNode = new VertexNodeD(next);
      if (next == start) {
        newNode.distance = 0;
      }
      set.put(next, newNode);
      count++;
    }
    return count;
  }

  /**
   * Key part of Dijkstra Search.
   *
   * @param v the vertex with the shortest distance, the one this operation is operating on.
   * @param queue the priority queue used to .
   * @param set that will contain all vertices in graph.
   */
  private void search(Vertex<String> v, PriorityQueue<VertexNodeD> queue, Map<Vertex<String>,
          VertexNodeD> set) {
    set.get(v).explore = true;   // mark as explored
    for (Vertex<String> u : set.get(v).outgoingVertex) {
      // for all unexplored outgoing vertex
      if (!set.get(u).explore) {
        double a = set.get(v).distance;
        Edge<String> edge = set.get(v).matchLabel(u);
        double d = a + (double) graph.label(edge);
        if (d < set.get(u).distance) {    // update if the new distance is shorter
          queue.remove(set.get(u));
          set.get(u).setDistance(d);
          set.get(u).setLabel(v);
          graph.label(u, edge);
          queue.add(set.get(u));
        }
      }
    }
  }

  /**
   * Check whether the input start and end name exist in the vertices of the graoh.
   *
   * @param start start vertex.
   * @param endName name of start vertex.
   * @param set set that contains information about the shortest distance.
   */
  private void printPath(String endName, Map<Vertex<String>, VertexNodeD> set, Vertex<String> start) {
    Vertex<String> end = vertices.get(endName);
    double totalDist = set.get(end).distance;
    // These method calls will create and print the path for you
    List<Edge<String>> path = getPath(end, start);
    if (VERBOSE) {
      printPath(path, totalDist);
    }
  }

  private static class NodeComparator implements Comparator<VertexNodeD> {
    // Overriding compare()method of Comparator
    // for ascending order of distance
    public int compare(VertexNodeD s1, VertexNodeD s2) {
      if (s1.distance < s2.distance) {
        return -1;
      } else if (s1.distance > s2.distance) {
        return 1;
      }
      return 0;
    }
  }

  private final class VertexNodeD implements Comparable<VertexNodeD> {
    boolean explore;  //  explored or not
    String data;  //  the coordinates, stored as a String
    Vertex<String> label;  //  previous
    double distance;
    Vertex<String> vertex;
    ArrayList<Vertex<String>> outgoingVertex; // what vertex the outgoing edges go
    ArrayList<Edge<String>> outgoingEdges; // the outgoing edges from this vertex

    VertexNodeD(Vertex<String> vertex) {
      data = vertex.get();
      distance = MAX_DISTANCE;   // distance initially set to infinity
      this.label = null;
      explore = false;
      this.vertex = vertex;
      outgoingVertex = new ArrayList<>();
      outgoingEdges = new ArrayList<>();
      Iterator<Edge<String>> itsOut = graph.outgoing(vertex).iterator();
      // set outgoing vertex and edges
      while (itsOut.hasNext()) {
        Edge<String> next = itsOut.next();
        outgoingVertex.add(graph.to(next));
        outgoingEdges.add(next);
      }
    }

    public String getData() {
      return data;
    }

    public Edge<String> matchLabel(Vertex<String> u) {
      for (Edge<String> edge: outgoingEdges) {
        if (graph.to(edge) == u) {
          return edge;
        }
      }
      return null;
    }

    public void setLabel(Vertex label) {
      this.label = label;
    }

    public void setDistance(double d) {
      distance = d;
    }

    @Override
    public int compareTo(VertexNodeD o) {
      if ((this.distance - o.distance) < 0) {
        return -1;
      } else if ((this.distance - o.distance) > 0) {
        return 1;
      }
      return 0;
    }
  }
}


