package hw5;

import exceptions.EmptyException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public abstract class DequeTest {

  private Deque<String> deque;

  @BeforeEach
  public void setUp() {
    this.deque = createDeque();
  }

  protected abstract Deque<String> createDeque();

  @Test
  @DisplayName("Deque is empty after construction.")
  public void testConstructor() {
    assertTrue(deque.empty());
    assertEquals(0, deque.length());
  }

  @Test
  @DisplayName("front() throws EmptyException after construction.")
  public void frontEmptyException() {
    int check;
    try{
      deque.front();
    } catch (EmptyException Obj) {
      check = 1;
      assertEquals(1, check);
    }
  }

  @Test
  @DisplayName("back() throws EmptyException after construction.")
  public void backEmptyException() {
    int check;
    try{
      deque.back();
    } catch (EmptyException Obj) {
      check = 1;
      assertEquals(1, check);
    }
  }

  @Test
  @DisplayName("front() works after insert.")
  public void testFrontOne() {
    deque.insertFront("2");
    deque.insertFront("8");
    deque.insertFront("10");
    assertEquals("10", deque.front());
  }

  @Test
  @DisplayName("back() works after insert.")
  public void testBackOne() {
    deque.insertFront("2");
    deque.insertFront("8");
    deque.insertFront("10");
    assertEquals("2", deque.back());
  }

  @Test
  @DisplayName("empty() works after insert one.")
  public void testEmptyOne() {
    deque.insertFront("2");
    assertFalse(deque.empty());
  }

  @Test
  @DisplayName("insertFront() works after insert multiple.")
  public void testInsertFront() {
    deque.insertFront("11");
    deque.insertFront("6");
    deque.insertFront("9");
    deque.insertFront("1");
    assertEquals("1", deque.front());
    deque.insertFront("2");
    deque.insertFront("3");
    deque.insertFront("4");
    deque.insertFront("5");
    assertEquals("5", deque.front());
    assertEquals("11", deque.back());
  }

  @Test
  @DisplayName("insertBack() works after insert three.")
  public void testInsertBack() {
    deque.insertBack("3");
    deque.insertBack("6");
    assertEquals("6", deque.back());
    deque.insertBack("9");
    assertEquals("9", deque.back());
    deque.insertBack("1");
    assertEquals("1", deque.back());
    deque.insertBack("2");
    assertEquals("2", deque.back());
    deque.insertBack("7");
    assertEquals("3", deque.front());
    assertEquals("7", deque.back());
  }

  @Test
  @DisplayName("length() works after multiple insertFront.")
  public void testLengthFront() {
    deque.insertFront("3");
    deque.insertFront("6");
    deque.insertFront("9");
    deque.insertFront("1");
    deque.insertFront("2");
    deque.insertFront("3");
    deque.insertFront("4");
    deque.insertFront("5");
    deque.insertFront("10");
    assertEquals(9, deque.length());
  }

  @Test
  @DisplayName("length() works after multiple insertBack.")
  public void testLengthBack() {
    deque.insertFront("10");
    deque.insertFront("10");
    deque.insertBack("0");
    deque.insertBack("1");
    deque.insertBack("2");
    deque.insertBack("5");
    deque.insertBack("6");
    deque.insertBack("7");
    deque.insertBack("8");
    deque.insertBack("8");
    deque.insertBack("9");
    assertEquals(11, deque.length());
  }

  @Test
  @DisplayName("length() works after insert and delete.")
  public void testLengthDelete() {
    deque.insertBack("3");
    deque.removeFront();
    assertEquals(0, deque.length());
    deque.insertBack("2");
    deque.insertBack("2");
    assertEquals(2, deque.length());
    deque.removeBack();
    assertEquals(1, deque.length());
  }

  @Test
  @DisplayName("removeFront() works when more than one elements in deque.")
  public void testRemoveFront() {
    deque.insertFront("2");
    deque.insertBack("6");
    deque.insertBack("9");
    assertEquals("2", deque.front());
    assertEquals("9", deque.back());
    deque.removeFront();
    assertEquals("6", deque.front());
  }

  @Test
  @DisplayName("removeBack() works when more than one elements in deque.")
  public void testRemoveBack() {
    deque.insertFront("2");
    deque.insertBack("6");
    deque.insertBack("9");
    deque.removeBack();
    assertEquals("6", deque.back());
    deque.removeBack();
    assertEquals("2", deque.back());
    deque.removeBack();
  }

  @Test
  @DisplayName("removeFront() works when empty.")
  public void testRemoveFrontEmpty() {
    deque.insertFront("2");
    int check;
    try{
      deque.removeFront();
      deque.removeFront();
    } catch (EmptyException Obj) {
      check = 1;
      assertEquals(1, check);
    }
  }

  @Test
  @DisplayName("removeBack() works when empty.")
  public void testRemoveBackEmpty() {
    deque.insertFront("2");
    int check;
    try{
      deque.removeBack();
      deque.removeBack();
    } catch (EmptyException Obj) {
      check = 1;
      assertEquals(1, check);
    }
  }

  @Test
  @DisplayName("multiple operations work under complex condition.")
  public void testMultiple() {
    deque.insertFront("2");
    deque.insertFront("2");
    deque.insertBack("8");
    deque.insertFront("9");
    assertEquals("9", deque.front());
    assertEquals("8", deque.back());
    assertEquals(4, deque.length());
    deque.removeFront();
    assertEquals("2", deque.front());
    assertEquals(3, deque.length());
    deque.insertBack("10");
    assertEquals("10", deque.back());
    deque.removeFront();
    deque.removeBack();
    assertEquals(2, deque.length());
    deque.removeBack();
    deque.removeFront();
    assertEquals(0, deque.length());
  }

}
