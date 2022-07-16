package hw5;

import exceptions.EmptyException;

/**
 * Generic unbounded double-ended queue abstraction.
 *
 * @param <T> Element type.
 */
public interface Deque<T> {

  /**
   * Checks if empty.
   *
   * @return true if this deque is empty and false otherwise.
   */
  boolean empty();

  /**
   * Number of elements.
   *
   * @return Number of elements in the deque, always greater equal to 0.
   */
  int length();

  /**
   * Peeks at the front value without removing it.
   *
   * @return the value at the front of this deque.
   * @throws EmptyException when empty() == true.
   */
  T front() throws EmptyException;

  /**
   * Peeks at the last value without removing it.
   *
   * @return the value at the back of this deque.
   * @throws EmptyException when empty() == true.
   */
  T back() throws EmptyException;

  /**
   * Insert a new front element.
   *
   * @param t Element to insert.
   */
  void insertFront(T t);

  /**
   * Insert a new back element.
   *
   * @param t Element to insert.
   */
  void insertBack(T t);

  /**
   * Remove front element.
   *
   * @throws EmptyException when empty() == true.
   */
  void removeFront() throws EmptyException;

  /**
   * Remove back element.
   *
   * @throws EmptyException when empty() == true.
   */
  void removeBack() throws EmptyException;
}
