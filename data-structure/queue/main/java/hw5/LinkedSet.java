package hw5;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Set implemented using a doubly linked list (no sentinels).
 *
 * @param <T> Element type.
 */
public class LinkedSet<T> implements Set<T> {
  protected Node<T> head;
  protected Node<T> tail;
  protected int numElements;

  @Override
  public void insert(T t) {
    if (has(t)) {
      return;
    }
    append(t);
    numElements++;
  }

  // Adds to the end of this linked-list in O(1)
  // Pre: t != null && has(t) == false
  // Post: has(t) == true && tail.data == t
  protected void append(T t) {
    if (tail == null) {
      tail = new Node<T>(t);
      head = tail;
    } else {
      Node<T> node = new Node<>(t);
      node.prev = tail;
      tail.next = node;
      tail = node;
    }
  }

  // Performs linear search.
  // Returns the target node containing t or null if t was not found.
  // Pre: t != null
  protected Node<T> find(T t) {
    for (Node<T> n = head; n != null; n = n.next) {
      if (n.data.equals(t)) {
        return n;
      }
    }
    return null;
  }

  @Override
  public void remove(T t) {
    Node<T> target = find(t);
    if (target == null) {
      return;
    }
    remove(target);
    numElements--;
  }

  // Removes the given target node in O(1)
  // Pre: target != null
  // Post: has(target.data) == false
  protected void remove(Node<T> target) {
    Node<T> prevNode = target.prev;
    Node<T> nextNode = target.next;

    if (prevNode != null) {
      prevNode.next = nextNode;
    }

    if (nextNode != null) {
      nextNode.prev = prevNode;
    }

    if (head == target) {
      head = target.next;
    }

    if (tail == target) {
      tail = target.prev;
    }
  }

  @Override
  public boolean has(T t) {
    return find(t) != null;
  }

  @Override
  public int size() {
    return numElements;
  }

  @Override
  public Iterator<T> iterator() {
    return new SetIterator();
  }

  // Doubly linked-list Node with 'next' and 'prev' reference variables
  protected static class Node<T> {
    T data;
    Node<T> next;
    Node<T> prev;

    Node(T data) {
      this.data = data;
    }
  }

  // Iterate from head to tail; all operations O(1)
  private class SetIterator implements Iterator<T> {
    private Node<T> current;

    SetIterator() {
      current = LinkedSet.this.head;
    }

    @Override
    public boolean hasNext() {
      return current != null;
    }

    @Override
    public T next() {
      if (!hasNext()) {
        throw new NoSuchElementException();
      }

      T t = current.data;
      current = current.next;
      return t;
    }
  }
}
