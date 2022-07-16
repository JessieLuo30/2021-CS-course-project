package hw6.bst;

import hw6.OrderedMap;
import java.util.Iterator;
import java.util.Stack;

/**
 * Map implemented as an AVL Tree.
 *
 * @param <K> Type for keys.
 * @param <V> Type for values.
 */
@SuppressWarnings("checkstyle:CommentsIndentation")
public class AvlTreeMap<K extends Comparable<K>, V> implements OrderedMap<K, V> {

  /*** Do not change variable name of 'root'. ***/
  private Node<K, V> root;
  private int size;

  @Override
  public void insert(K k, V v) throws IllegalArgumentException {
    if (k == null) {
      throw new IllegalArgumentException("cannot handle null key");
    }
    if (root == null) {
      root =  new Node<>(k, v);
    } else {
      root = insertNew(root, k, v);
    }
    size++;
  }

  private Node<K, V> insertNew(Node<K, V> n, K k, V v) {
    if (n == null) {
      return new Node<>(k, v);
    }
    int cmp = k.compareTo(n.key);
    n = insertHelper(n, k, v, cmp);
    if (cmp == 0) {
      throw new IllegalArgumentException("cannot insert same");
    }
    n.height = max(n.left, n.right) + 1;
    return n;
  }

  private Node<K, V> insertHelper(Node<K, V> n, K k, V v, int cmp) {
    if (cmp < 0) {
      n.left = insertNew(n.left, k, v);
      if (computeBF(n.left,n.right) == 2 && k.compareTo(n.left.key) < 0) {
        n = leftLeftKid(n);
      } else if (computeBF(n.left,n.right) == 2) {
        n = leftRightKid(n);
      }
    } else if (cmp > 0) {
      n.right = insertNew(n.right, k, v);
      if (computeBF(n.right, n.left) == 2) {
        if (k.compareTo(n.right.key) > 0) {
          n = rightRightKid(n);
        } else {
          n = rightLeftKid(n);
        }
      }
    }
    return n;
  }

  private int computeBF(Node<K, V> n1, Node<K, V> n2) {
    int bf1;
    int bf2;
    if (n1 == null) {
      bf1 = -1;
    } else {
      bf1 = n1.height;
    }
    if (n2 == null) {
      bf2 = -1;
    } else {
      bf2 = n2.height;
    }
    return bf1 - bf2;
  }

  private Node<K, V> leftLeftKid(Node<K, V> node) {
    Node<K, V> left = node.left;
    node.left = left.right;
    left.right = node;
    node.height = max(node.left, node.right) + 1;
    left.height = max(left.left, node) + 1;
    return left;
  }

  private Node<K, V> rightRightKid(Node<K, V> node) {
    Node<K, V> right = node.right;
    node.right = right.left;
    right.left = node;

    node.height = max(node.left, node.right) + 1;
    right.height = max(right.right, node) + 1;
    return right;
  }

  private Node<K, V> leftRightKid(Node<K, V> node) {
    node.left = rightRightKid(node.left);
    return leftLeftKid(node);
  }

  private Node<K, V> rightLeftKid(Node<K, V> node) {
    node.right = leftLeftKid(node.right);
    return rightRightKid(node);
  }

  private int max(Node<K, V> n1, Node<K, V> n2) {
    if (n1 == null && n2 == null) {
      return -1;
    }
    if (n1 == null) {
      return n2.height;
    }
    if (n2 == null) {
      return n1.height;
    }
    return Math.max(n1.height, n2.height);
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
  public V remove(K k) throws IllegalArgumentException {
    if (k == null) {
      throw new IllegalArgumentException("cannot handle null key");
    }
    Node<K, V> n = find(root, k);
    if (n == null) {
      throw new IllegalArgumentException("cannot find key " + k);
    }
    root = remove(root, k);
    size--;
    return n.value;
  }

  private Node<K, V> remove(Node<K, V> node, K key) {
    Node<K, V> retNode;
    if (key.compareTo(node.key) < 0) {
      node.left = remove(node.left, key);
      retNode = node;
    } else if (key.compareTo(node.key) > 0) {
      node.right = remove(node.right, key);
      retNode = node;
    } else {
      if (node.left == null && node.right == null) {
        return null;
      }
      retNode = calRetNode(node);
    }
    return removeReturn(retNode);
  }

  private Node<K, V> calRetNode(Node<K, V> node) {
    if (node.left == null) {
      Node<K, V> rightNode = node.right;
      node.right = null;
      return rightNode;
    } else if (node.right == null) {
      Node<K, V> leftNode = node.left;
      node.left = null;
      return leftNode;
    } else {
      Node<K, V> predecessor = maximum(node.left);
      predecessor.left = remove(node.left, predecessor.key);
      predecessor.right = node.right;
      node.left = null;
      node.right = null;
      return predecessor;
    }
  }

  private Node<K, V> removeReturn(Node<K, V> retNode) {
    if (retNode == null) {
      return null;
    }
    retNode.height = 1 + max(retNode.left, retNode.right);
    int balanceFactor = computeBF(retNode.left,retNode.right);
    if (balanceFactor == 2 && computeBF(retNode.left.left,retNode.left.right) >= 0) {
      return leftLeftKid(retNode);
    }
    if (balanceFactor == -2 && computeBF(retNode.right.left, retNode.right.right) <= 0) {
      return rightRightKid(retNode);
    }
    if (balanceFactor == 2 && computeBF(retNode.left.left,retNode.left.right) < 0) {
      return leftRightKid(retNode);
    }
    if (balanceFactor == -2 && computeBF(retNode.right.left, retNode.right.right) > 0) {
      return rightLeftKid(retNode);
    }
    return retNode;
  }

  private Node<K, V> maximum(Node<K, V> node) {
    if (node.right == null) {
      return node;
    }
    return maximum(node.right);
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

  @Override
  public Iterator<K> iterator() {
    return new InorderIterator();
  }

  /*** Do not change this function's name or modify its code. ***/
  @Override
  public String toString() {
    return BinaryTreePrinter.printBinaryTree(root);
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

  /**
   * Feel free to add whatever you want to the Node class (e.g. new fields).
   * Just avoid changing any existing names, deleting any existing variables,
   * or modifying the overriding methods.
   *
   * <p>Inner node class, each holds a key (which is what we sort the
   * BST by) as well as a value. We don't need a parent pointer as
   * long as we use recursive insert/remove helpers.</p>
   **/
  private static class Node<K, V> implements BinaryTreeNode {
    Node<K, V> left;
    Node<K, V> right;
    K key;
    V value;
    int height;

    // Constructor to make node creation easier to read.
    Node(K k, V v) {
      // left and right default to null
      key = k;
      value = v;
      height = 0;
    }

    @Override
    public String toString() {
      return key + ":" + value;
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
