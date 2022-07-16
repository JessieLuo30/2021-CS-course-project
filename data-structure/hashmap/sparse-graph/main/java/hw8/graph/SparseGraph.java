package hw8.graph;


import exceptions.InsertionException;
import exceptions.PositionException;
import exceptions.RemovalException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * An implementation of Graph ADT using incidence lists
 * for sparse graphs where most nodes aren't connected.
 *
 * @param <V> Vertex element type.
 * @param <E> Edge element type.
 */
public class SparseGraph<V, E> implements Graph<V, E> {

  // edgeE and vertexV contains similar values as edge and vertex, but different type
  ArrayList<VertexNode<V>> vertex = new ArrayList<>();
  ArrayList<V> vertexV = new ArrayList<>();    // make it easier for searching for V and E value directly
  ArrayList<EdgeNode<E>> edge = new ArrayList<>();
  ArrayList<E> edgeE = new ArrayList<>();

  // Converts the vertex back to a VertexNode to use internally
  private VertexNode<V> convert(Vertex<V> v) throws PositionException {
    try {
      VertexNode<V> gv = (VertexNode<V>) v;
      if (gv.owner != this) {
        throw new PositionException();
      }
      return gv;
    } catch (NullPointerException | ClassCastException ex) {
      throw new PositionException();
    }
  }

  // Converts and edge back to a EdgeNode to use internally
  private EdgeNode<E> convert(Edge<E> e) throws PositionException {
    try {
      EdgeNode<E> ge = (EdgeNode<E>) e;
      if (ge.owner != this) {
        throw new PositionException();
      }
      return ge;
    } catch (NullPointerException | ClassCastException ex) {
      throw new PositionException();
    }
  }

  @Override
  public Vertex<V> insert(V v) throws InsertionException {
    if (v == null || vertexV.contains(v)) {    //check v existence
      throw new InsertionException();
    }
    VertexNode<V> insert = new VertexNode<>(v);
    insert.owner = this;  // declare owner when initialize
    vertex.add(insert);
    vertexV.add(v);
    return insert;
  }

  @Override
  public Edge<E> insert(Vertex<V> from, Vertex<V> to, E e)
      throws PositionException, InsertionException {
    VertexNode<V> fromVertex = convert(from);  // no need for test null, convert do it for us
    VertexNode<V> toVertex = convert(to);
    if (!vertex.contains(fromVertex) || !vertex.contains(toVertex)) {   // if input node not valid
      throw new PositionException();
    }
    EdgeNode<E> insert = new EdgeNode<>(fromVertex, toVertex, e);
    insert.owner = this;
    // if input node creat duplicate or self-loop
    if (fromVertex.neighbors.contains(toVertex) || from == to) {
      throw new InsertionException();
    }
    // update all variables, insertion succeed
    fromVertex.outgoing.add(insert);
    fromVertex.neighbors.add(toVertex);
    toVertex.incoming.add(insert);
    edge.add(insert);
    edgeE.add(e);
    return insert;
  }

  @Override
  public V remove(Vertex<V> v) throws PositionException, RemovalException {
    VertexNode<V> vnode = convert(v);
    if (!vertex.contains(vnode)) {    // no need for test null, convert do it for us
      throw new PositionException();
    }
    if (!vnode.outgoing.isEmpty() || !vnode.incoming.isEmpty()) {       // this vertex has incident edges!
      throw new RemovalException();
    }
    vertex.remove(vnode);
    return v.get();
  }

  @Override
  public E remove(Edge<E> e) throws PositionException {
    EdgeNode<E> edgeNode = convert(e);
    if (!edgeE.contains(e.get()) || !edge.contains(edgeNode)) {     // edges doesn't exist
      throw new PositionException();
    }
    VertexNode<V> from = edgeNode.getFrom();
    VertexNode<V> to = edgeNode.getTo();
    from.outgoing.remove(edgeNode);   // updated all, remove succeed
    to.incoming.remove(edgeNode);
    edge.remove(edgeNode);
    edgeE.remove(e.get());
    return e.get();
  }

  @Override
  public Iterable<Vertex<V>> vertices() {
    return Collections.unmodifiableCollection(vertex);   //make it unmodifiable
  }

  @Override
  public Iterable<Edge<E>> edges() {
    return Collections.unmodifiableCollection(edge);
  }

  @Override
  public Iterable<Edge<E>> outgoing(Vertex<V> v) throws PositionException {
    VertexNode<V> vnode = convert(v);
    return Collections.unmodifiableSet(vnode.outgoing);
  }

  @Override
  public Iterable<Edge<E>> incoming(Vertex<V> v) throws PositionException {
    if (!vertex.contains(convert(v))) {    // no need for extra test null
      throw new PositionException();
    }
    VertexNode<V> vnode = convert(v);
    return Collections.unmodifiableSet(vnode.incoming);
  }

  @Override
  public Vertex<V> from(Edge<E> e) throws PositionException {
    EdgeNode<E> from = convert(e);
    if (!edgeE.contains(e.get()) || !edge.contains(from)) {     // edges doesn't exist
      throw new PositionException();
    }
    return from.getFrom();
  }

  @Override
  public Vertex<V> to(Edge<E> e) throws PositionException {
    EdgeNode<E> to = convert(e);
    if (!edgeE.contains(e.get()) || !edge.contains(to)) {     // edges doesn't exist
      throw new PositionException();
    }
    return to.getTo();
  }

  @Override
  public void label(Vertex<V> v, Object l) throws PositionException {
    VertexNode<V> v1 = convert(v);
    if (!vertex.contains(v1)) {    // vertex doesn't exist
      throw new PositionException();
    }
    vertex.remove(v1);   // update by remove and add after change it's parameter
    v1.setLabel(l);
    v1.owner = this;
    vertex.add(v1);
  }

  @Override
  public void label(Edge<E> e, Object l) throws PositionException {
    EdgeNode<E> e1 = convert(e);
    if (!edge.contains(e1)) {    // edges doesn't exist
      throw new PositionException();
    }
    edge.remove(e1);
    e1.setLabel(l);
    e1.owner = this;
    edge.add(e1);
  }

  @Override
  public Object label(Vertex<V> v) throws PositionException {
    VertexNode<V> nodeV = convert(v);
    if (!vertex.contains(nodeV)) {     // no such vertex
      throw new PositionException();
    }
    V get = v.get();
    Object target = null;
    for (VertexNode<V> ver: vertex) {
      if (ver.data == get) {
        target = ver.label;
      }
    }
    return target;
  }

  @Override
  public Object label(Edge<E> e) throws PositionException {
    EdgeNode<E> edgeNode = convert(e);
    if (!edge.contains(edgeNode)) {     // no such edge
      throw new PositionException();
    }
    E get = e.get();
    Object target = null;
    for (EdgeNode<E> ed: edge) {
      if (ed.data == get) {    // target found
        target = ed.label;
        break;
      }
    }
    return target;
  }

  @Override
  public void clearLabels() {
    // loop through all edges and vertex and clear
    for (EdgeNode<E> each : edge) {
      each.label = null;
    }
    for (VertexNode<V> each : vertex) {
      each.label = null;
    }
  }

  @Override
  public String toString() {
    GraphPrinter<V, E> gp = new GraphPrinter<>(this);
    return gp.toString();
  }

  // Class for a vertex of type V
  private final class VertexNode<V> implements Vertex<V> {
    V data;
    Graph<V, E> owner;
    Object label;
    Set<EdgeNode<E>> outgoing;   // make it easier for outgoing(vertex v)
    Set<EdgeNode<E>> incoming;   // make it easier for incoming(vertex v)
    Set<VertexNode<V>> neighbors;   // the vertex that's connected with the node's incident edge'

    VertexNode(V v) {
      this.data = v;
      this.label = null;
      outgoing = new HashSet<>();
      incoming = new HashSet<>();
      neighbors = new HashSet<>();
    }

    @Override
    public V get() {
      return this.data;
    }

    private void setLabel(Object label) {
      this.label = label;
    }

  }

  //Class for an edge of type E
  private final class EdgeNode<E> implements Edge<E> {
    E data;
    Graph<V, E> owner;
    VertexNode<V> from;   // make it easier for from(edge e)
    VertexNode<V> to;   // make it easier for to(edge e)
    Object label;

    // Constructor for a new edge
    EdgeNode(VertexNode<V> f, VertexNode<V> t, E e) {
      this.from = f;
      this.to = t;
      this.data = e;
      this.label = null;
    }

    @Override
    public E get() {
      return this.data;
    }

    private VertexNode<V> getFrom() {
      return from;
    }

    private void setLabel(Object label) {
      this.label = label;
    }

    private VertexNode<V> getTo() {
      return to;
    }
  }
}
