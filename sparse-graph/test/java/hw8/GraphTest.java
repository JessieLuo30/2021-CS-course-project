package hw8;

import exceptions.InsertionException;
import exceptions.PositionException;
import exceptions.RemovalException;
import hw8.graph.Edge;
import hw8.graph.Graph;
import hw8.graph.Vertex;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;


public abstract class GraphTest {

  protected Graph<String, String> graph;
  protected Graph<String, String> graph2;

  @BeforeEach
  public void setupGraph() {
    this.graph = createGraph();
    this.graph2 = createGraph();
  }

  protected abstract Graph<String, String> createGraph();

  @Test
  @DisplayName("insert(v) returns a vertex with given data")
  public void canGetVertexAfterInsert() {
    Vertex<String> v1 = graph.insert("v1");
    assertEquals(v1.get(), "v1");
  }

  @Test
  @DisplayName("insert(U, V, e) returns an edge with given data")
  public void canGetEdgeAfterInsert() {
    Vertex<String> v1 = graph.insert("v1");
    Vertex<String> v2 = graph.insert("v2");
    Edge<String> e1 = graph.insert(v1, v2, "v1-v2");
    assertEquals(e1.get(), "v1-v2");
  }

  @Test
  @DisplayName("insert(null, V, e) throws exception")
  public void insertEdgeThrowsPositionExceptionWhenFirstVertexIsNull() {
    try {
      Vertex<String> v = graph.insert("v");
      graph.insert(null, v, "e");
      fail("The expected exception was not thrown");
    } catch (PositionException ex) {
      return;
    }
  }

  @Test
  @DisplayName("insert(V, U, e) throws exception")
  public void insertEdgeThrowsPositionExceptionWhenFirstVertexIsEmpty() {
    try {
      Vertex<String> v = graph.insert("v");
      Vertex<String> v2 = graph.insert("v2");
      graph.remove(v);
      graph.insert(v, v2, "e");
      fail("The expected exception was not thrown");
    } catch (PositionException ex) {
      return;
    }
  }

  @Test
  @DisplayName("insert(V, U, e) throws exception")
  public void insertEdgeThrowsPositionExceptionWhenSecondVertexIsEmpty() {
    try {
      Vertex<String> v = graph.insert("v");
      Vertex<String> v2 = graph.insert("v2");
      graph.remove(v);
      graph.insert(v2, v, "e");
      fail("The expected exception was not thrown");
    } catch (PositionException ex) {
      return;
    }
  }

  @Test
  @DisplayName("insert(V, U, e) throws exception")
  public void insertEdgeThrowsPositionExceptionWhenVertexFromDifferentGraph() {
    try {
      Vertex<String> v = graph.insert("v");
      Vertex<String> v2 = graph2.insert("v2");
      graph.insert(v, v2, "e");
      fail("The expected exception was not thrown");
    } catch (PositionException ex) {
      return;
    }
  }

  @Test
  @DisplayName("insert(V, null, e) throws exception")
  public void insertEdgeThrowsPositionExceptionWhenSecondVertexIsNull() {
    try {
      Vertex<String> v = graph.insert("v");
      graph.insert(v, null, "e");
      fail("The expected exception was not thrown");
    } catch (PositionException ex) {
      return;
    }
  }

  @Test
  @DisplayName("insert(null) throws exception")
  public void insertVertexWhenVertexIsNullThrowException() {
    try {
      graph.insert(null);
      fail("The expected exception was not thrown");
    } catch (InsertionException ex) {
      return;
    }
  }

  @Test
  @DisplayName("insert(v) throws exception")
  public void insertVertexWhenVertexIsAlreadyInTheGraphThroughException() {
    try {
      graph.insert("v1");
      graph.insert("v1");
      fail("The expected exception was not thrown");
    } catch (InsertionException ex) {
      return;
    }
  }

  @Test
  @DisplayName("insert(V, V, e) throws exception")
  public void insertEdgeThrowsInsertionExceptionWhenCreateSelfLoop() {
    try {
      Vertex<String> v = graph.insert("v");
      graph.insert(v, v, "e");
      fail("The expected exception was not thrown");
    } catch (InsertionException ex) {
      return;
    }
  }

  @Test
  @DisplayName("insert(V, U, e) throws exception when U not in G")
  public void insertEdgeThrowsPositionExceptionWhenUNotExist() {
    try {
      Vertex<String> v1 = graph.insert("v1");
      Vertex<String> v2 = graph2.insert("v2");
      graph.insert(v1, v2, "v1-v2");
      fail("The expected exception was not thrown");
    } catch (PositionException ex) {
      return;
    }
  }

  @Test
  @DisplayName("insert(U, V, e) throws exception when U was removed")
  public void insertEdgeThrowsPositionExceptionWhenUisRemoved() {
    try {
      Vertex<String> v1 = graph.insert("v1");
      Vertex<String> v2 = graph.insert("v2");
      graph.remove(v1);
      graph.insert(v1, v2, "v1-v2");
      fail("The expected exception was not thrown");
    } catch (PositionException ex) {
      return;
    }
  }

  @Test
  @DisplayName("insert(V, U, e) throws exception")
  public void insertEdgeThrowsInsertionExceptionWhenCreateDuplicateEdge() {
    try {
      Vertex<String> v1 = graph.insert("v1");
      Vertex<String> v2 = graph.insert("v2");
      graph.insert(v1, v2, "v1-v2");
      graph.insert(v1, v2, "v1-v3");
      fail("The expected exception was not thrown");
    } catch (InsertionException ex) {
      return;
    }
  }

  @Test
  @DisplayName("remove(v) returns a vertex with given data")
  public void insertEdgeDontThrowsInsertionExceptionWhenCreateNonDuplicateEdge() {
    Vertex<String> v1 = graph.insert("v1");
    Vertex<String> v2 = graph.insert("v2");
    Vertex<String> v3 = graph.insert("v3");
    graph.insert(v1, v2, "v1-v2");
    graph.insert(v1, v3, "v2-v1");
  }

  @Test
  @DisplayName("remove(v) returns a vertex with given data")
  public void canGetVertexInRemove() {
    Vertex<String> v1 = graph.insert("v1");
    assertEquals(graph.remove(v1), "v1");
  }

  @Test
  @DisplayName("remove the same vertex again throws exception")
  public void removeSameVertexThrowsException() {
    try {
      Vertex<String> v1 = graph.insert("v1");
      graph.remove(v1);
      graph.remove(v1);
      fail("The expected exception was not thrown");
    } catch (PositionException ex) {
      return;
    }
  }

  @Test
  @DisplayName("remove the same edge again throws exception")
  public void removeSameEdgeThrowsException() {
    try {
      Vertex<String> v1 = graph.insert("v1");
      Vertex<String> v2 = graph.insert("v2");
      Edge<String> e1 = graph.insert(v1, v2, "v1-v2");
      graph.remove(e1);
      graph.remove(e1);
      fail("The expected exception was not thrown");
    } catch (PositionException ex) {
      return;
    }
  }

  @Test
  @DisplayName("remove(v) returns a vertex if V has incoming edge")
  public void removeVertexThrowsInsertionExceptionWhenExistIncomingEdges() {
    try {
      Vertex<String> v1 = graph.insert("v1");
      Vertex<String> v2 = graph.insert("v2");
      graph.insert(v2, v1, "v1-v2");
      graph.remove(v1);
      fail("The expected exception was not thrown");
    } catch (RemovalException ex) {
      return;
    }
  }

  @Test
  @DisplayName("remove(v) returns a vertex if V has outgoing edge")
  public void removeVertexThrowsInsertionExceptionWhenExistOutgoingEdges() {
    try {
      Vertex<String> v1 = graph.insert("v1");
      Vertex<String> v2 = graph.insert("v2");
      graph.insert(v1, v2, "v1-v2");
      graph.remove(v1);
      fail("The expected exception was not thrown");
    } catch (RemovalException ex) {
      return;
    }
  }

  @Test
  @DisplayName("remove(e) returns a edge with given data")
  public void canGetEdgeInRemove() {
    Vertex<String> v1 = graph.insert("v1");
    Vertex<String> v2 = graph.insert("v2");
    Edge<String> e1 = graph.insert(v1, v2, "v1-v2");
    assertEquals(graph.remove(e1), "v1-v2");
  }

  @Test
  @DisplayName("remove(null) throws exception")
  public void removeVertexThrowsPositionExceptionWhenPositionInvalid() {
    try {
      Vertex<String> v1 = null;
      graph.remove(v1);
      fail("The expected exception was not thrown");
    } catch (PositionException ex) {
      return;
    }
  }

  @Test
  @DisplayName("remove(e) not in this graph throws exception")
  public void removeEdgeThrowsPositionExceptionWhenNotExist() {
    try {
      Vertex<String> v1 = graph.insert("v1");
      Vertex<String> v2 = graph.insert("v2");
      Edge<String> e1 = graph.insert(v1, v2, "v1-v2");
      graph2.remove(e1);
      fail("The expected exception was not thrown");
    } catch (PositionException ex) {
      return;
    }
  }

  @Test
  @DisplayName("remove(v) throws exception")
  public void removeVertexThrowsRemovalExceptionWhenStillHaveIncidentEdge() {
    try {
      Vertex<String> v1 = graph.insert("v1");
      Vertex<String> v2 = graph.insert("v2");
      graph.insert(v1, v2, "v1-v2");
      graph.remove(v1);
      fail("The expected exception was not thrown");
    } catch (RemovalException ex) {
      return;
    }
  }

  @Test
  @DisplayName("remove(null) throws exception")
  public void removeEdgeThrowsPositionExceptionWhenPositionInvalid() {
    try {
      Edge<String> e1 = null;
      graph.remove(e1);
      fail("The expected exception was not thrown");
    } catch (PositionException ex) {
      return;
    }
  }

  @Test
  @DisplayName("remove the same edge again throws exception")
  public void removeSameEdgeAgainThrowsException() {
    try {
      Vertex<String> v1 = graph.insert("v1");
      Vertex<String> v2 = graph.insert("v2");
      Edge<String> e1 = graph.insert(v1, v2, "v1-v2");
      graph.remove(e1);
      graph.remove(e1);
      fail("The expected exception was not thrown");
    } catch (PositionException ex) {
      return;
    }
  }

  @Test
  @DisplayName("remove(v) not in this graph throws exception")
  public void removeVertexThrowsPositionExceptionWhenNotExist() {
    try {
      Vertex<String> v1 = graph.insert("v1");
      graph2.remove(v1);
      fail("The expected exception was not thrown");
    } catch (PositionException ex) {
      return;
    }
  }

  @Test
  @DisplayName("from(e) returns an vertex with given data")
  public void fromEdgeReturnsCorrectVertex() {
    Vertex<String> v1 = graph.insert("v1");
    Vertex<String> v2 = graph.insert("v2");
    Edge<String> e1 = graph.insert(v1, v2, "v1-v2");
    Vertex<String> result = graph.from(e1);
    assertEquals(result, v1);
    assertEquals(result.get(), "v1");
  }

  @Test
  @DisplayName("from(null) throws exception")
  public void fromEdgeThrowsPositionExceptionWhenPositionNull() {
    try {
      Edge<String> e1 = null;
      graph.from(e1);
      fail("The expected exception was not thrown");
    } catch (PositionException ex) {
      return;
    }
  }

  @Test
  @DisplayName("from(e) throws exception")
  public void fromEdgeThrowsPositionExceptionWhenPositionInvalid() {
    try {
      Vertex<String> v1 = graph.insert("v1");
      Vertex<String> v2 = graph.insert("v2");
      Edge<String> e1 = graph.insert(v1, v2, "v1-v2");
      graph2.from(e1);
      fail("The expected exception was not thrown");
    } catch (PositionException ex) {
      return;
    }
  }

  @Test
  @DisplayName("from(e) throws exception when not exist")
  public void fromEdgeThrowsPositionExceptionWhenNotExist() {
    try {
      Vertex<String> v1 = graph2.insert("v1");
      Vertex<String> v2 = graph2.insert("v2");
      Edge<String> e1 = graph2.insert(v1, v2, "v1-v2");
      graph2.remove(e1);
      graph2.to(e1);
      fail("The expected exception was not thrown");
    } catch (PositionException ex) {
      return;
    }
  }

  @Test
  @DisplayName("to(e) throws exception when not exist")
  public void toEdgeThrowsPositionExceptionWhenNotExist() {
    try {
      Vertex<String> v1 = graph.insert("v1");
      Vertex<String> v2 = graph.insert("v2");
      Edge<String> e1 = graph.insert(v1, v2, "v1-v2");
      graph.remove(e1);
      graph.to(e1);
      fail("The expected exception was not thrown");
    } catch (PositionException ex) {
      return;
    }
  }

  @Test
  @DisplayName("to(e) returns an vertex with given data")
  public void toEdgeReturnsCorrectVertex() {
    Vertex<String> v1 = graph.insert("v1");
    Vertex<String> v2 = graph.insert("v2");
    Edge<String> e1 = graph.insert(v1, v2, "v1-v2");
    Vertex<String> result = graph.to(e1);
    assertEquals(result, v2);
    assertEquals(result.get(), "v2");
  }

  @Test
  @DisplayName("to(null) throws exception")
  public void toEdgeThrowsPositionExceptionWhenPositionNull() {
    try {
      Edge<String> e1 = null;
      graph.to(e1);
      fail("The expected exception was not thrown");
    } catch (PositionException ex) {
      return;
    }
  }

  @Test
  @DisplayName("to(e) throws exception")
  public void toEdgeThrowsPositionExceptionWhenPositionInvalid() {
    try {
      Vertex<String> v1 = graph.insert("v1");
      Vertex<String> v2 = graph.insert("v2");
      Edge<String> e1 = graph.insert(v1, v2, "v1-v2");
      graph2.to(e1);
      fail("The expected exception was not thrown");
    } catch (PositionException ex) {
      return;
    }
  }

  @Test
  @DisplayName("label(v, l) gives a vertex with given label")
  public void labelVertexGivesCorrectLabel() {
    Vertex<String> v1 = graph.insert("v1");
    graph.label(v1, "v1 label");
    assertEquals(graph.label(v1), "v1 label");
  }

  @Test
  @DisplayName("label(null, l) throws exception")
  public void labelVertexThrowsPositionExceptionWhenPositionNull() {
    try {
      Vertex<String> v1 = null;
      graph.label(v1, "v1 label");
      fail("The expected exception was not thrown");
    } catch (PositionException ex) {
      return;
    }
  }

  @Test
  @DisplayName("label(v, l) throws exception")
  public void labelVertexThrowsPositionExceptionWhenPositionInvalid() {
    try {
      Vertex<String> v1 = graph.insert("v1");
      graph.remove(v1);
      graph.label(v1, "v1 label");
      fail("The expected exception was not thrown");
    } catch (PositionException ex) {
      return;
    }
  }

  @Test
  @DisplayName("label(v, l) throws exception")
  public void labelVertexThrowsPositionExceptionWhenPositionNotExist() {
    try {
      Vertex<String> v1 = graph.insert("v1");
      graph2.label(v1, "v1 label");
      fail("The expected exception was not thrown");
    } catch (PositionException ex) {
      return;
    }
  }

  @Test
  @DisplayName("label(e, l) returns an edge with given label")
  public void labelEdgeGivesCorrectLabel() {
    Vertex<String> v1 = graph.insert("v1");
    Vertex<String> v2 = graph.insert("v2");
    Edge<String> e1 = graph.insert(v1, v2, "v1-v2");
    graph.label(e1, "e1 label");
    assertEquals(graph.label(e1), "e1 label");
  }

  @Test
  @DisplayName("label(null, l) throws exception")
  public void labelEdgeThrowsPositionExceptionWhenPositionNull() {
    try {
      Edge<String> v1 = null;
      graph.label(v1, "v1 label");
      fail("The expected exception was not thrown");
    } catch (PositionException ex) {
      return;
    }
  }

  @Test
  @DisplayName("label(e, l) throws exception")
  public void labelEdgeThrowsPositionExceptionWhenPositionNotExist() {
    try {
      Vertex<String> v1 = graph.insert("v1");
      Vertex<String> v2 = graph.insert("v2");
      Edge<String> e1 = graph.insert(v1, v2, "v1-v2");
      graph2.label(e1, "e1 label");
      fail("The expected exception was not thrown");
    } catch (PositionException ex) {
      return;
    }
  }

  @Test
  @DisplayName("label(e, l) throws exception")
  public void labelEdgeThrowsPositionExceptionWhenPositionInvalid() {
    try {
      Vertex<String> v1 = graph.insert("v1");
      Vertex<String> v2 = graph.insert("v2");
      Edge<String> e1 = graph.insert(v1, v2, "v1-v2");
      graph.remove(e1);
      graph.label(e1, "v1 label");
      fail("The expected exception was not thrown");
    } catch (PositionException ex) {
      return;
    }
  }

  @Test
  @DisplayName("label(null) throws exception")
  public void labelOfVertexThrowsPositionExceptionWhenPositionNull() {
    try {
      Vertex<String> v1 = null;
      graph.label(v1);
      fail("The expected exception was not thrown");
    } catch (PositionException ex) {
      return;
    }
  }

  @Test
  @DisplayName("label(v) throws exception")
  public void labelOfVertexThrowsPositionExceptionWhenPositionNotExist() {
    try {
      Vertex<String> v1 = graph2.insert("v1");
      graph2.label(v1, "v1 label");
      graph.label(v1);
      fail("The expected exception was not thrown");
    } catch (PositionException ex) {
      return;
    }
  }

  @Test
  @DisplayName("label(v) throws exception")
  public void labelOfVertexThrowsPositionExceptionWhenPositionInvalid() {
    try {
      Vertex<String> v1 = graph2.insert("v1");
      graph2.remove(v1);
      graph.label(v1);
      fail("The expected exception was not thrown");
    } catch (PositionException ex) {
      return;
    }
  }

  @Test
  @DisplayName("label(e) throws exception")
  public void labelOfEdgeThrowsPositionExceptionWhenPositionNull() {
    try {
      Edge<String> e1 = null;
      graph.label(e1);
      fail("The expected exception was not thrown");
    } catch (PositionException ex) {
      return;
    }
  }

  @Test
  @DisplayName("label(e) throws exception")
  public void labelOfEdgeThrowsPositionExceptionWhenPositionNotExist() {
    try {
      Vertex<String> v1 = graph2.insert("v1");
      Vertex<String> v2 = graph2.insert("v2");
      Edge<String> e1 = graph2.insert(v1, v2, "v1-v2");
      graph2.label(e1, "e1 label");
      graph.label(e1);
      fail("The expected exception was not thrown");
    } catch (PositionException ex) {
      return;
    }
  }

  @Test
  @DisplayName("label(e) throws exception")
  public void labelOfEdgeThrowsPositionExceptionWhenPositionInvalid() {
    try {
      Vertex<String> v1 = graph2.insert("v1");
      Vertex<String> v2 = graph2.insert("v2");
      Edge<String> e1 = graph2.insert(v1, v2, "v1-v2");
      graph2.label(e1, "e1 label");
      graph2.remove(e1);
      graph.label(v1);
      fail("The expected exception was not thrown");
    } catch (PositionException ex) {
      return;
    }
  }

  @Test
  @DisplayName("clearLabels() clears all labels")
  public void clearLabelsClearAllLabel() {
    Vertex<String> v1 = graph.insert("v1");
    Vertex<String> v2 = graph.insert("v2");
    Edge<String> e1 = graph.insert(v1, v2, "v1-v2");
    graph.label(v1, "v1 label");
    graph.label(v2, "v1 label");
    graph.label(e1, "e1 label");
    graph.clearLabels();
    assertNull(graph.label(e1));
    assertNull(graph.label(v1));
    assertNull(graph.label(v2));
    graph.label(v1, "v1");
    graph.clearLabels();
    assertNull(graph.label(v1));
  }

  @Test
  @DisplayName("vertices() returns good Iterable")
  public void verticesReturnsGoodIterable() {
    Vertex<String> v1 = graph.insert("v1");
    Vertex<String> v2 = graph.insert("v2");
    Vertex<String> v3 = graph.insert("v3");
    Iterable<Vertex<String>> it = graph.vertices();
    Iterator<Vertex<String>> its = it.iterator();
    ArrayList<Vertex<String>> expect = new ArrayList<>();
    ArrayList<Vertex<String>> output = new ArrayList<>();
    expect.add(v1);
    expect.add(v2);
    expect.add(v3);
    while (its.hasNext()) {
      output.add(its.next());
    }
    assertEquals(expect.size(),output.size());
    for (Vertex<String> add : expect) {
      assertTrue(output.contains(add));
    }
  }

  @Test
  @DisplayName("vertices() returns good Iterable for one vertex")
  public void verticesReturnsGoodIterableForOne() {
    Vertex<String> v1 = graph.insert("v1");
    Iterable<Vertex<String>> it = graph.vertices();
    Iterator<Vertex<String>> its = it.iterator();
    ArrayList<Vertex<String>> expect = new ArrayList<>();
    ArrayList<Vertex<String>> output = new ArrayList<>();
    expect.add(v1);
    while (its.hasNext()) {
      output.add(its.next());
    }
    assertEquals(expect.size(),output.size());
    for (Vertex<String> add : expect) {
      assertTrue(output.contains(add));
    }
  }

  @Test
  @DisplayName("vertices() returns good Iterable for one edge")
  public void edgesReturnsGoodIterableForOne() {
    Vertex<String> v3 = graph.insert("v3");
    Vertex<String> v1 = graph.insert("v1");
    Edge<String> e1 = graph.insert(v1, v3, "v1-v3");
    Iterable<Edge<String>> it = graph.edges();
    Iterator<Edge<String>> its = it.iterator();
    ArrayList<Edge<String>> expect = new ArrayList<>();
    ArrayList<Edge<String>> output = new ArrayList<>();
    expect.add(e1);
    while (its.hasNext()) {
      output.add(its.next());
    }
    assertEquals(expect.size(),output.size());
    for (Edge<String> add : expect) {
      assertTrue(output.contains(add));
    }
  }

  @Test
  @DisplayName("vertices() returns good Iterable for when empty")
  public void verticesReturnsGoodIterableWhenEmpty() {
    Iterable<Vertex<String>> it = graph.vertices();
    Iterator<Vertex<String>> its = it.iterator();
    ArrayList<Vertex<String>> expect = new ArrayList<>();
    ArrayList<Vertex<String>> output = new ArrayList<>();
    while (its.hasNext()) {
      output.add(its.next());
    }
    assertEquals(expect.size(),output.size());
    for (Vertex<String> add : expect) {
      assertTrue(output.contains(add));
    }
  }

  @Test
  @DisplayName("edges() returns good Iterable when empty")
  public void edgesReturnsGoodIterableWhenEmpty() {
    Iterable<Edge<String>> it = graph.edges();
    Iterator<Edge<String>> its = it.iterator();
    ArrayList<Edge<String>> expect = new ArrayList<>();
    ArrayList<Edge<String>> output = new ArrayList<>();
    while (its.hasNext()) {
      output.add(its.next());
    }
    assertEquals(expect.size(),output.size());
    for (Edge<String> add : expect) {
      assertTrue(output.contains(add));
    }
  }

  @Test
  @DisplayName("vertices() returns good Iterable after remove")
  public void verticesReturnsGoodIterableAfterRemove() {
    Vertex<String> v1 = graph.insert("v1");
    Vertex<String> v2 = graph.insert("v2");
    Vertex<String> v3 = graph.insert("v3");
    graph.remove(v3);
    Iterable<Vertex<String>> it = graph.vertices();
    Iterator<Vertex<String>> its = it.iterator();
    ArrayList<Vertex<String>> expect = new ArrayList<>();
    ArrayList<Vertex<String>> output = new ArrayList<>();
    expect.add(v1);
    expect.add(v2);
    while (its.hasNext()) {
      output.add(its.next());
    }
    assertEquals(expect.size(),output.size());
    for (Vertex<String> add : expect) {
      assertTrue(output.contains(add));
    }
  }

  @Test
  @DisplayName("edges() returns good Iterable")
  public void edgesReturnsGoodIterable() {
    Vertex<String> v3 = graph.insert("v3");
    Vertex<String> v1 = graph.insert("v1");
    Vertex<String> v2 = graph.insert("v2");
    Edge<String> e1 = graph.insert(v1, v3, "v1-v3");
    Edge<String> e2 = graph.insert(v2, v3, "v3-v2");
    Edge<String> e3 = graph.insert(v1, v2, "v1-v2");
    Iterable<Edge<String>> it = graph.edges();
    Iterator<Edge<String>> its = it.iterator();
    ArrayList<Edge<String>> expect = new ArrayList<>();
    ArrayList<Edge<String>> output = new ArrayList<>();
    expect.add(e1);
    expect.add(e2);
    expect.add(e3);
    while (its.hasNext()) {
      output.add(its.next());
    }
    assertEquals(expect.size(),output.size());
    for (Edge<String> add : expect) {
      assertTrue(output.contains(add));
    }
  }

  @Test
  @DisplayName("iterate over edges in a graph after multiple insert/remove edges")
  public void edgesReturnsGoodIterableAfterMultipleRemoveAndInsert() {
    Vertex<String> v3 = graph.insert("v3");
    Vertex<String> v1 = graph.insert("v1");
    Vertex<String> v2 = graph.insert("v2");
    Edge<String> e1 = graph.insert(v1, v3, "v1-v3");
    Edge<String> e2 = graph.insert(v2, v3, "v3-v2");
    Edge<String> e3 = graph.insert(v1, v2, "v1-v2");
    graph.remove(e3);
    graph.remove(e1);
    Iterable<Edge<String>> it = graph.edges();
    Iterator<Edge<String>> its = it.iterator();
    ArrayList<Edge<String>> expect = new ArrayList<>();
    ArrayList<Edge<String>> output = new ArrayList<>();
    expect.add(e2);
    while (its.hasNext()) {
      output.add(its.next());
    }
    assertEquals(expect.size(),output.size());
    for (Edge<String> add : expect) {
      assertTrue(output.contains(add));
    }
    Vertex<String> v6 = graph.insert("v6");
    Edge<String> e4 = graph.insert(v2, v6, "v1-v3");
    expect.add(e4);
    Edge<String> e5 = graph.insert(v6, v3, "v1-v3");
    expect.add(e5);
    ArrayList<Edge<String>> output2 = new ArrayList<>();
    Iterator<Edge<String>> its2 = graph.edges().iterator();
    while (its2.hasNext()) {
      output2.add(its2.next());
    }
    assertEquals(expect.size(),output2.size());
    for (Edge<String> add : expect) {
      assertTrue(output2.contains(add));
    }
  }

  @Test
  @DisplayName("edges() returns good Iterable")
  public void edgesReturnsGoodIterableWhenMultipleEdges() {
    Vertex<String> v3 = graph.insert("v3");
    Vertex<String> v1 = graph.insert("v1");
    Vertex<String> v2 = graph.insert("v2");
    Edge<String> e1 = graph.insert(v1, v3, "v1-v3");
    Edge<String> e2 = graph.insert(v2, v3, "v3-v2");
    Edge<String> e3 = graph.insert(v1, v2, "v1-v2");
    Iterable<Edge<String>> it = graph.edges();
    Iterator<Edge<String>> its = it.iterator();
    ArrayList<Edge<String>> expect = new ArrayList<>();
    ArrayList<Edge<String>> output = new ArrayList<>();
    expect.add(e1);
    expect.add(e2);
    expect.add(e3);
    while (its.hasNext()) {
      output.add(its.next());
    }
    assertEquals(expect.size(),output.size());
    for (Edge<String> add : expect) {
      assertTrue(output.contains(add));
    }
  }

  @Test
  @DisplayName("outgoing(v) returns good Iterable")
  public void outgoingReturnsGoodIterable() {
    Vertex<String> v1 = graph.insert("v1");
    Vertex<String> v2 = graph.insert("v2");
    Vertex<String> v3 = graph.insert("v3");
    Edge<String> e1 = graph.insert(v1, v2, "v1-v2");
    Edge<String> e2 = graph.insert(v1, v3, "v1-v3");
    Iterable<Edge<String>> it = graph.outgoing(v1);
    Iterator<Edge<String>> its = it.iterator();
    ArrayList<Edge<String>> expect = new ArrayList<>();
    ArrayList<Edge<String>> output = new ArrayList<>();
    expect.add(e2);
    expect.add(e1);
    while (its.hasNext()) {
      output.add(its.next());
    }
    assertEquals(expect.size(),output.size());
    for (Edge<String> add : expect) {
      assertTrue(output.contains(add));
    }
  }

  @Test
  @DisplayName("outgoing(null) throw PositionException")
  public void outgoingThrowExceptionWhenNull() {
    try {
      graph.outgoing(null);
    } catch (PositionException ex) {
      return;
    }
  }

  @Test
  @DisplayName("outgoing(v) throw PositionException")
  public void outgoingThrowExceptionWhenIsRemoved() {
    try {
      Vertex<String> v1 = graph.insert("v1");
      graph.remove(v1);
      graph.outgoing(v1);
    } catch (PositionException ex) {
      return;
    }
  }

  @Test
  @DisplayName("outgoing(v) throw PositionException")
  public void outgoingThrowExceptionWhenNotExist() {
    try {
      Vertex<String> v1 = graph2.insert("v1");
      graph.outgoing(v1);
    } catch (PositionException ex) {
      return;
    }
  }

  @Test
  @DisplayName("incoming(v) returns good Iterable")
  public void incomingReturnsGoodIterable() {
    Vertex<String> v1 = graph.insert("v1");
    Vertex<String> v2 = graph.insert("v2");
    Vertex<String> v3 = graph.insert("v3");
    Edge<String> e1 = graph.insert(v2, v1, "v1-v2");
    Edge<String> e2 = graph.insert(v3, v1, "v1-v3");
    Iterable<Edge<String>> it = graph.incoming(v1);
    Iterator<Edge<String>> its = it.iterator();
    ArrayList<Edge<String>> expect = new ArrayList<>();
    ArrayList<Edge<String>> output = new ArrayList<>();
    expect.add(e2);
    expect.add(e1);
    while (its.hasNext()) {
      output.add(its.next());
    }
    assertEquals(expect.size(),output.size());
    for (Edge<String> add : expect) {
      assertTrue(output.contains(add));
    }
  }

  @Test
  @DisplayName("incoming(V) works correctly after multiple insert/remove")
  public void incomingReturnsGoodIterableAfterMultipleInsertRemove() {
    Vertex<String> v1 = graph.insert("v1");
    Vertex<String> v2 = graph.insert("v2");
    Vertex<String> v3 = graph.insert("v3");
    Vertex<String> v4 = graph.insert("v4");
    Edge<String> e1 = graph.insert(v2, v1, "v1-v2");
    Edge<String> e2 = graph.insert(v3, v1, "v1-v3");
    Iterable<Edge<String>> it = graph.incoming(v1);
    Iterator<Edge<String>> its = it.iterator();
    ArrayList<Edge<String>> expect = new ArrayList<>();
    ArrayList<Edge<String>> output = new ArrayList<>();
    expect.add(e1);
    expect.add(e2);
    while (its.hasNext()) {
      output.add(its.next());
    }
    assertEquals(expect.size(),output.size());
    for (Edge<String> add : expect) {
      assertTrue(output.contains(add));
    }
    graph.remove(e1);
    graph.remove(e2);
    Edge<String> e4 = graph.insert(v4, v1, "v4-v1");
    expect.remove(e2);
    expect.remove(e1);
    expect.add(e4);
    ArrayList<Edge<String>> output2 = new ArrayList<>();
    Iterator<Edge<String>> its2 = graph.incoming(v1).iterator();
    while (its2.hasNext()) {
      output2.add(its2.next());
    }
    assertEquals(expect.size(),output2.size());
    for (Edge<String> add : expect) {
      assertTrue(output2.contains(add));
    }
  }

  @Test
  @DisplayName("outgoing(V) works correctly after multiple insert/remove")
  public void outgoingReturnsGoodIterableAfterMultipleInsertRemove() {
    Vertex<String> v1 = graph.insert("v1");
    Vertex<String> v2 = graph.insert("v2");
    Vertex<String> v3 = graph.insert("v3");
    Vertex<String> v4 = graph.insert("v4");
    Edge<String> e1 = graph.insert(v1, v2, "v1-v2");
    Edge<String> e2 = graph.insert(v1, v3, "v1-v3");
    Iterable<Edge<String>> it = graph.outgoing(v1);
    Iterator<Edge<String>> its = it.iterator();
    ArrayList<Edge<String>> expect = new ArrayList<>();
    ArrayList<Edge<String>> output = new ArrayList<>();
    expect.add(e1);
    expect.add(e2);
    while (its.hasNext()) {
      output.add(its.next());
    }
    assertEquals(expect.size(),output.size());
    for (Edge<String> add : expect) {
      assertTrue(output.contains(add));
    }
    graph.remove(e1);
    graph.remove(e2);
    Edge<String> e4 = graph.insert(v1, v4, "v4-v1");
    expect.remove(e2);
    expect.remove(e1);
    expect.add(e4);
    ArrayList<Edge<String>> output2 = new ArrayList<>();
    Iterator<Edge<String>> its2 = graph.outgoing(v1).iterator();
    while (its2.hasNext()) {
      output2.add(its2.next());
    }
    assertEquals(expect.size(),output2.size());
    for (Edge<String> add : expect) {
      assertTrue(output2.contains(add));
    }
  }

  @Test
  @DisplayName("incoming(v) throw PositionException")
  public void incomingThrowExceptionWhenNull() {
    try {
      graph.incoming(null);
    } catch (PositionException ex) {
      return;
    }
  }

  @Test
  @DisplayName("incoming(v) throw PositionException")
  public void incomingThrowExceptionWhenIsRemoved() {
    try {
      Vertex<String> v1 = graph.insert("v1");
      graph.remove(v1);
      graph.incoming(v1);
    } catch (PositionException ex) {
      return;
    }
  }

  @Test
  @DisplayName("incoming(v) throw PositionException")
  public void incomingThrowExceptionWhenNotExist() {
    try {
      Vertex<String> v1 = graph2.insert("v1");
      graph.incoming(v1);
    } catch (PositionException ex) {
      return;
    }
  }

  @Test
  @DisplayName("Iterate over outgoing edges of a vertex with no outgoing edge returns hasNext = false iterable")
  public void outGoingHasNextEqualToFalseIterable() {
    Vertex<String> v1 = graph.insert("v1");
    Iterable<Edge<String>> it = graph.outgoing(v1);
    assertFalse(it.iterator().hasNext());
  }

  @Test
  @DisplayName("Iterate over outgoing edges of a vertex with no incoming edge returns hasNext = false iterable")
  public void incomingHasNextEqualToFalseIterable() {
    Vertex<String> v1 = graph.insert("v1");
    Iterable<Edge<String>> it = graph.incoming(v1);
    assertFalse(it.iterator().hasNext());
  }


  @Test
  @DisplayName("Iterate over edges in a graph with vertices but no edge returns hasNext = false iterable")
  public void whenNoEdgesIterableHasNextEqualToFalse() {
    graph.insert("v1");
    graph.insert("v2");
    Iterable<Edge<String>> it = graph.edges();
    assertFalse(it.iterator().hasNext());
  }


  @Test
  @DisplayName("remove the same edge again throws exception")
  public void removeTheSameEdgeAgainThroughException() {
    try {
      Vertex<String> v1 = graph.insert("v1");
      Vertex<String> v2 = graph.insert("v2");
      Edge<String> e1 = graph.insert(v1, v2, "v1-v2");
      graph.remove(e1);
      graph.remove(e1);
    } catch (PositionException ex) {
      return;
    }
  }

  @Test
  @DisplayName("remove the same edge again throws exception")
  public void removeTheSameVertexAgainThroughException() {
    try {
      Vertex<String> v1 = graph.insert("v1");
      graph.remove(v1);
      graph.remove(v1);
    } catch (PositionException ex) {
      return;
    }
  }

  @Test
  @DisplayName("insert(V, U, e) throws exception (opposite edge)")
  public void InsertOppositeEdgesThrowException() {
    try {
      Vertex<String> v1 = graph.insert("v1");
      Vertex<String> v2 = graph.insert("v2");
      graph.insert(v1, v2, "v1-v2");
      graph.insert(v2, v1, "v1-v2");
    } catch (PositionException ex) {
      return;
    }
  }

  @Test
  @DisplayName("insert(V, U, e) throws exception (parallel edge)")
  public void InsertParallelEdgesThrowException() {
    try {
      Vertex<String> v1 = graph.insert("v1");
      Vertex<String> v2 = graph.insert("v2");
      graph.insert(v1, v2, "v1-v2");
      graph.insert(v1, v2, "v1-v2");
    } catch (InsertionException ex) {
      return;
    }
  }

  @Test
  @DisplayName("insert(V, U, e) throws exception (self-loop)")
  public void InsertSelfLoopEdgesThrowException() {
    try {
      Vertex<String> v1 = graph.insert("v1");
      graph.insert(v1, v1, "v1-v2");
    } catch (InsertionException ex) {
      return;
    }
  }

  @Test
  @DisplayName("insert(V, U, null) does not throw exception")
  public void InsertNullNotThrowException() {
    try {
      Vertex<String> v1 = graph.insert("v1");
      Vertex<String> v2 = graph.insert("v2");
      graph.insert(v1, v2, null);
    } catch (InsertionException ex) {
      return;
    }
  }

  @Test
  @DisplayName("it.remove() throw UnsupportedOperationException when it = graph.vertices().iterator()")
  public void ThrowUnsupportedOperationExceptionWhenTryRemove() {
    try {
      Iterator<Vertex<String>> it = graph.vertices().iterator();
      it.remove();
    } catch (UnsupportedOperationException ex) {
      return;
    }
  }

}
