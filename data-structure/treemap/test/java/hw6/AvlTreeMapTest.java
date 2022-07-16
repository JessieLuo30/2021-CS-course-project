package hw6;

import hw6.bst.AvlTreeMap;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * In addition to the tests in BinarySearchTreeMapTest (and in OrderedMapTest & MapTest),
 * we add tests specific to AVL Tree.
 */
@SuppressWarnings("All")
public class AvlTreeMapTest extends BinarySearchTreeMapTest {

  @Override
  protected Map<String, String> createMap() {
    return new AvlTreeMap<>();
  }

  @Test
  public void insertLeftRotation() {
    map.insert("1", "a");
    // System.out.println(avl.toString());
    // must print
    /*
        1:a
     */

    map.insert("2", "b");
    // System.out.println(avl.toString());
    // must print
    /*
        1:a,
        null 2:b
     */

    map.insert("3", "c"); // it must do a left rotation here!
    // System.out.println(avl.toString());
    // must print
    /*
        2:b,
        1:a 3:c
     */

    String[] expected = new String[]{
        "2:b",
        "1:a 3:c"
    };
    assertEquals((String.join("\n", expected) + "\n"), map.toString());
  }
  
  @Test
  public void testGet() {
    map.insert("9", "k");
    String get = map.get("9");
    assertEquals(get, "k");
  }

  @Test
  public void testHas() {
    map.insert("7", "k");
    map.insert("9", "k");
    assertEquals(map.has("14"), false);
    assertEquals(map.has("7"), true);
    assertEquals(map.has("9"), true);
  }

  @Test
  public void testPut() {
    map.insert("7", "k");
    assertEquals(map.get("7"), "k");
    map.put("7", "e");
    assertEquals(map.get("7"), "e");
  }

  @Test
  public void testSize() {
    map.insert("8", "k");
    map.insert("7", "k");
    map.remove("8");
    map.insert("10", "k");
    assertEquals(map.size(), 2);
  }

  @Test
  public void testRemoveReturn() {
    map.insert("8", "q");
    map.insert("7", "k");
    String test = map.remove("8");
    assertEquals(test, "q");
  }

  @Test
  public void insertRightRotation() {
    map.insert("7", "a");
    map.insert("5", "b");
    map.insert("3", "c");
    String[] expected = new String[]{
            "5:b",
            "3:c 7:a"
    };
    assertEquals((String.join("\n", expected) + "\n"), map.toString());
  }

  @Test
  public void deleteLeaf() {
    map.insert("7", "a");
    map.insert("5", "b");
    map.insert("3", "c");
    String test = map.remove("3");
    String[] expected = new String[]{
            "5:b",
            "null 7:a"
    };
    assertEquals(test, "c");
    assertEquals((String.join("\n", expected) + "\n"), map.toString());
  }


  @Test
  public void insertRightLeftRotation() {
    map.insert("3", "a");
    map.insert("7", "b");
    map.insert("5", "c");
    String[] expected = new String[]{
            "5:c",
            "3:a 7:b"
    };
    assertEquals((String.join("\n", expected) + "\n"), map.toString());
  }

  @Test
  public void insertLeftRightRotation() {
    map.insert("7", "a");
    map.insert("3", "b");
    map.insert("5", "c");
    String[] expected = new String[]{
            "5:c",
            "3:b 7:a"
    };
    assertEquals((String.join("\n", expected) + "\n"), map.toString());
  }

  @Test
  public void insertLeftLeftRotation() {
    map.insert("3", "a");
    map.insert("5", "b");
    map.insert("7", "c");
    String[] expected = new String[]{
            "5:b",
            "3:a 7:c"
    };
    assertEquals((String.join("\n", expected) + "\n"), map.toString());
  }

  @Test
  public void removeNodeWithOneKid() {
    map.insert("15", "a");
    map.insert("12", "b");
    map.insert("19", "c");
    map.insert("03", "a");
    map.insert("13", "b");
    map.insert("16", "c");
    String test = map.remove("19");
    String[] expected = new String[]{
            "15:a",
            "12:b 16:c",
            "03:a 13:b null null"
    };
    assertEquals(test, "c");
    assertEquals((String.join("\n", expected) + "\n"), map.toString());
  }

  @Test
  public void removeBST() {
    map.insert("15", "a");
    map.insert("12", "b");
    map.insert("19", "c");
    map.insert("03", "a");
    map.insert("13", "b");
    map.insert("16", "c");
    map.insert("42", "a");
    map.insert("1", "b");
    map.insert("21", "c");
    String test = map.remove("19");
    String[] expected = new String[]{
            "15:a",
            "12:b 21:c",
            "03:a 13:b 16:c 42:a",
            "null 1:b null null null null null null"
    };
    assertEquals(test, "c");
    assertEquals((String.join("\n", expected) + "\n"), map.toString());
  }

  @Test
  public void removeLeaf() {
    map.insert("15", "a");
    map.insert("12", "b");
    map.insert("19", "c");
    map.insert("03", "a");
    map.insert("14", "b");
    map.insert("16", "c");
    map.insert("21", "c");
    map.insert("01", "c");
    map.insert("05", "c");
    map.insert("13", "c");
    map.remove("05");
    String[] expected = new String[]{
            "15:a",
            "12:b 19:c",
            "03:a 14:b 16:c 21:c",
            "01:c null 13:c null null null null null"
    };
    assertEquals((String.join("\n", expected) + "\n"), map.toString());
  }

  @Test
  public void insertMultipleNodes() {
    map.insert("15", "a");
    map.insert("20", "b");
    map.insert("24", "c");
    map.insert("10", "d");
    map.insert("13", "e");

    String[] expected = new String[]{
            "20:b",
            "13:e 24:c",
            "10:d 15:a null null"
    };
    assertEquals((String.join("\n", expected) + "\n"), map.toString());
    map.insert("50", "e");
    map.insert("17", "e");
    map.insert("19", "e");
    expected = new String[]{
            "20:b",
            "13:e 24:c",
            "10:d 17:e null 50:e",
            "null null 15:a 19:e null null null null"
    };
    assertEquals((String.join("\n", expected) + "\n"), map.toString());
  }

  @Test
  public void MultipleRotations() {
    map.insert("15", "a");
    map.insert("20", "b");
    map.insert("24", "c");
    map.insert("10", "d");
    map.insert("13", "e");
    map.insert("50", "e");
    map.insert("17", "e");
    map.insert("19", "e");
    map.remove("13");
    String[] expected = new String[]{
            "20:b",
            "17:e 24:c",
            "10:d 19:e null 50:e",
            "null 15:a null null null null null null"
    };
    assertEquals((String.join("\n", expected) + "\n"), map.toString());
    String test1 = map.remove("10");
    String test2 = map.remove("20");
    String test3 = map.remove("50");
    expected = new String[]{
            "19:e",
            "17:e 24:c",
            "15:a null null null"
    };
    assertEquals((String.join("\n", expected) + "\n"), map.toString());
  }

  @Test
  public void removeNodeWithTwoKid() {
    map.insert("15", "a");
    map.insert("20", "b");
    map.insert("24", "c");
    map.insert("10", "d");
    map.insert("13", "e");
    map.insert("50", "p");
    map.insert("17", "e");
    map.insert("19", "e");
    map.remove("13");
    String[] expected = new String[]{
            "20:b",
            "17:e 24:c",
            "10:d 19:e null 50:p",
            "null 15:a null null null null null null"
    };
    assertEquals((String.join("\n", expected) + "\n"), map.toString());
    map.remove("20");
    map.insert("02", "a");
    map.insert("06", "a");
    map.insert("88", "k");
    map.remove("17");
    map.insert("12", "b");
    map.remove("50");
    map.remove("10");
    map.insert("16", "c");
    map.put("19", "e");
    map.remove("19");
    expected = new String[]{
            "16:c",
            "06:a 24:c",
            "02:a 15:a null 88:k",
            "null null 12:b null null null null null"
    };
    assertEquals((String.join("\n", expected) + "\n"), map.toString());
  }

  @Test
  public void putNoSuchElement() {
    try {
      map.put("key1", "updated3");
      fail("Failed to throw IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      return;
    }
  }

  @Test
  public void MultipleOperatrionComplexCase() {
    map.insert("15", "a");
    map.insert("88", "k");
    map.insert("12", "b");
    map.remove("88");
    map.insert("19", "c");
    map.put("12", "e");
    map.insert("16", "c");
    map.put("19", "e");
    map.remove("19");
    String[] expected = new String[]{
            "15:a",
            "12:e 16:c"
    };
    assertEquals((String.join("\n", expected) + "\n"), map.toString());
  }

  @Test
  public void SimpleBST() {
    map.insert("08", "a");
    map.insert("04", "k");
    map.insert("12", "b");
    map.insert("02", "c");
    map.insert("06", "a");
    map.insert("10", "k");
    map.insert("16", "b");
    map.remove("08");
    String[] expected = new String[]{
            "06:a",
            "04:k 12:b",
            "02:c null 10:k 16:b"
    };
    String get = map.get("02");
    assertEquals(get, "c");
    assertEquals(map.has("111"), false);
    assertEquals(map.has("06"), true);
    assertEquals((String.join("\n", expected) + "\n"), map.toString());
  }

}
