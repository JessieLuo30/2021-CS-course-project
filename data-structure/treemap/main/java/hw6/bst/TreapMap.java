package hw6.bst;

import hw6.OrderedMap;
import java.util.Iterator;
import java.util.Random;
import java.util.Stack;

/**
 * Map implemented as a Treap.
 *
 * @param <K> Type for keys.
 * @param <V> Type for values.
 */
public class TreapMap<K extends Comparable<K>, V> implements OrderedMap<K, V> {

  /*** Do not change variable name of 'rand'. ***/
  private static Random rand;
  /*** Do not change variable name of 'root'. ***/
  private Node<K, V> root;
  private int size;

  /**
   * Make a TreapMap.
   */
  public TreapMap() {
    rand = new Random();
  }

  /**
   * Make a TreapMap with fixed seed.
   * @param i seed.
   */
  public TreapMap(int i) {
    rand = new Random(i);
  }

  @Override
  public void insert(K k, V v) throws IllegalArgumentException {
    if (k == null) {
      throw new IllegalArgumentException("cannot handle null key");
    }
    if (root == null) {
      root =  new Node<>(k, v);
    } else {
      root = insert(root, k, v);
    }
    size++;
  }

  private Node<K, V> insert(Node<K, V> t, K k, V v) {
    if (t == null) {
      return new Node<>(k, v);
    }
    if (find(t, k) != null) {
      throw new IllegalArgumentException("cannot insert same");
    }
    if (k.compareTo(t.key) <= 0) {
      t.left = insert(t.left, k, v);
      if (t.left.priority < t.priority) {
        t = rotateWithLeftChild(t);
      }
    } else {
      t.right = insert(t.right, k, v);
      if (t.right.priority < t.priority) {
        t = rotateWithRightChild(t);
      }
    }
    return t;
  }

  @Override
  public V remove(K k) throws IllegalArgumentException {
    if (k == null) {
      throw new IllegalArgumentException("cannot handle null key");
    }
    Node<K, V> node = findForSure(k);
    node.priority = 1000000000;
    root = remove(k, root);
    size--;
    return node.value;
  }

  /**
   * Internal method to remove from a subtree.
   *
   * @param k The item to remove.
   * @param node The node that roots the subtree.
   * @return The new node of the subtree.
   */
  private Node<K, V> remove(K k, Node<K, V> node) {
    if (k.compareTo(node.key) < 0) {
      node.left = remove(k, node.left);
    } else if (k.compareTo(node.key) > 0) {
      node.right = remove(k, node.right);
    } else {
      if (node.left == null && node.right == null) {
        return null;
      } else if (node.left == null || node.right == null) {
        node = oneKidHelper(node);
      } else if (node.left.priority < node.right.priority) {
        node = rotateWithLeftChild(node);
        node.right = remove(k, node.right);
      } else {
        node = rotateWithRightChild(node);
        node.left = remove(k, node.left);
      }
    }
    return node;
  }

  private Node<K, V> oneKidHelper(Node<K, V> node) {
    if (node.left == null) {
      Node<K, V> rightNode = node.right;
      node.right = null;
      return rightNode;
    } else if (node.right == null) {
      Node<K, V> leftNode = node.left;
      node.left = null;
      return leftNode;
    }
    return null;
  }

  private Node<K, V> findForSure(K k) {
    Node<K, V> n = find(root, k);
    if (n == null) {
      throw new IllegalArgumentException("cannot find key " + k);
    }
    return n;
  }

  private Node<K, V> find(Node<K, V> x, K k) {
    if (x == null) {
      return null;
    }
    int cmp = k.compareTo(x.key);
    if (cmp < 0) {
      return find(x.left, k);
    } else if (cmp > 0) {
      return find(x.right, k);
    } else {
      return x;
    }
  }

  @Override
  public void put(K k, V v) throws IllegalArgumentException {
    if (k == null) {
      throw new IllegalArgumentException("cannot handle null key");
    }
    Node<K, V> node = find(root, k);
    if (node == null) {
      throw new IllegalArgumentException("cannot find key " + k);
    }
    node.value = v;
  }

  @Override
  public V get(K k) throws IllegalArgumentException {
    if (k == null) {
      throw new IllegalArgumentException("cannot handle null key");
    }
    Node<K, V> node = find(root, k);
    if (node == null) {
      throw new IllegalArgumentException("cannot find key " + k);
    }
    return node.value;
  }

  @Override
  public boolean has(K k) {
    if (k == null) {
      return false;
    }
    return find(root, k) != null;
  }

  @Override
  public int size() {
    return size;
  }

  /**
   * Rotate binary tree node with left child.
   */
  private Node<K, V> rotateWithLeftChild(Node<K, V> k2) {
    Node<K, V> k1 = k2.left;
    k2.left = k1.right;
    k1.right = k2;
    return k1;
  }

  /**
   * Rotate binary tree node with right child.
   */
  private Node<K, V> rotateWithRightChild(Node<K, V> k1) {
    Node<K, V> k2 = k1.right;
    k1.right = k2.left;
    k2.left = k1;
    return k2;
  }

  @Override
  public Iterator<K> iterator() {
    return new InorderIterator();
  }
  // Iterative in-order traversal over the keys

  private class InorderIterator implements Iterator<K> {
    private final Stack<Node<K, V>> stack;

    InorderIterator() {
      stack = new Stack<>();
      pushLeft(root);
    }

    private void pushLeft(Node<K, V> curr) {
      while (curr != null) {
        stack.push(curr);
        curr = curr.left;
      }
    }

    @Override
    public boolean hasNext() {
      return !stack.isEmpty();
    }

    @Override
    public K next() {
      Node<K, V> top = stack.pop();
      pushLeft(top.right);
      return top.key;
    }
  }

  /*** Do not change this function's name or modify its code. ***/
  @Override
  public String toString() {
    return BinaryTreePrinter.printBinaryTree(root);
  }


  /**
   * Feel free to add whatever you want to the Node class (e.g. new fields).
   * Just avoid changing any existing names, deleting any existing variables,
   * or modifying the overriding methods.
   *
   * <p>Inner node class, each holds a key (which is what we sort the
   * BST by) as well as a value. We don't need a parent pointer as
   * long as we use recursive insert/remove helpers. Since this is
   * a node class for a Treap we also include a priority field.
   **/
  private static class Node<K, V> implements BinaryTreeNode {
    Node<K, V> left;
    Node<K, V> right;
    K key;
    V value;
    int priority;

    // Constructor to make node creation easier to read.
    Node(K k, V v) {
      // left and right default to null
      key = k;
      value = v;
      priority = generateRandomInteger();
    }

    // Use this function to generate random values
    // to use as node priorities as you insert new
    // nodes into your TreapMap.
    private int generateRandomInteger() {
      return rand.nextInt();
    }

    @Override
    public String toString() {
      return key + ":" + value + ":" + priority;
    }

    @Override
    public BinaryTreeNode getLeftChild() {
      return left;
    }

    @Override
    public BinaryTreeNode getRightChild() {
      return right;
    }

  }
}
