package hw6;

import hw6.bst.TreapMap;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * In addition to the tests in BinarySearchTreeMapTest (and in OrderedMapTest & MapTest),
 * we add tests specific to Treap.
 */
@SuppressWarnings("All")
public class TreapMapTest extends BinarySearchTreeMapTest{

  @Override
  protected Map<String, String> createMap() {
    return new TreapMap<>(40);
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
    map.insert("4", "a");
    map.insert("3", "b");
    map.insert("2", "c");
    String[] expected = new String[]{
            "3:b:-1749212617",
            "2:c:95830475 4:a:-1170874532"
    };
    assertEquals((String.join("\n", expected) + "\n"), map.toString());
  }

  @Test
  public void insertLeftRotation() {
    map.insert("4", "a");
    map.insert("3", "b");
    map.insert("2", "c");
    map.insert("9", "b");
    map.insert("11", "c");
    String[] expected = new String[]{
            "3:b:-1749212617",
            "2:c:95830475 9:b:-1502686769",
            "11:c:1929790192 null 4:a:-1170874532 null"
    };
    assertEquals((String.join("\n", expected) + "\n"), map.toString());
    String get = map.get("4");
    assertEquals(get, "a");
    assertEquals(map.has("111"), false);
    assertEquals(map.has("11"), true);
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
  public void removeLeaf() {
    map.insert("1", "a");
    map.insert("2", "b");
    map.insert("3", "c");
    map.insert("4", "d");
    map.insert("5", "e");
    map.insert("6", "f");
    map.remove("5");
    map.remove("4");
    String[] expected = new String[]{
            "2:b:-1749212617",
            "1:a:-1170874532 3:c:95830475",
            "null null null 6:f:1702710456"
    };
    assertEquals((String.join("\n", expected) + "\n"), map.toString());
  }

  @Test
  public void removeOneKid() {
    map.insert("1", "a");
    map.insert("2", "b");
    map.insert("3", "c");
    map.insert("4", "d");
    map.insert("5", "e");
    map.insert("6", "f");
    String test = map.remove("6");
    assertEquals(test, "f");
    String[] expected = new String[]{
            "2:b:-1749212617",
            "1:a:-1170874532 4:d:-1502686769",
            "null null 3:c:95830475 5:e:1929790192"
    };
    assertEquals((String.join("\n", expected) + "\n"), map.toString());
  }

  @Test
  public void MultipleRotations() {
    map.insert("7", "a");
    map.insert("3", "b");
    map.insert("5", "c");
    map.insert("8", "a");
    map.insert("9", "b");
    map.insert("10", "c");
    map.insert("11", "c");
    map.insert("22", "a");
    String[] expected = new String[]{
            "3:b:-1749212617",
            "22:a:-895793374 8:a:-1502686769",
            "11:c:-680157218 null 7:a:-1170874532 9:b:1929790192",
            "10:c:1702710456 null null null 5:c:95830475 null null null"
    };
    map.put("10", "e");
    expected = new String[]{
            "3:b:-1749212617",
            "22:a:-895793374 8:a:-1502686769",
            "11:c:-680157218 null 7:a:-1170874532 9:b:1929790192",
            "10:e:1702710456 null null null 5:c:95830475 null null null"
    };
    assertEquals((String.join("\n", expected) + "\n"), map.toString());
  }

  @Test
  public void removeMultipleNodes() {
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
            "20:b:-1749212617",
            "10:d:-1502686769 24:c:95830475",
            "null 15:a:-1170874532 null 50:e:1702710456",
            "null null null 19:e:-895793374 null null null null",
            "null null null null null null 17:e:-680157218 null null null null null null null null null"
    };
    assertEquals((String.join("\n", expected) + "\n"), map.toString());
    map.remove("10");
    map.remove("20");
    map.remove("50");
    expected = new String[]{
            "15:a:-1170874532",
            "null 19:e:-895793374",
            "null null 17:e:-680157218 24:c:95830475"
    };
    assertEquals((String.join("\n", expected) + "\n"), map.toString());
  }

  @Test
  public void removeTwoKids() {
    map.insert("7", "a");
    map.insert("3", "b");
    map.insert("5", "c");
    map.insert("8", "a");
    map.insert("9", "b");
    map.insert("10", "c");
    String[] expected = new String[]{
            "3:b:-1749212617",
            "10:c:1702710456 8:a:-1502686769",
            "null null 7:a:-1170874532 9:b:1929790192",
            "null null null null 5:c:95830475 null null null"
    };
    assertEquals((String.join("\n", expected) + "\n"), map.toString());
    map.remove("8");
    expected = new String[]{
            "3:b:-1749212617",
            "10:c:1702710456 7:a:-1170874532",
            "null null 5:c:95830475 9:b:1929790192",
    };
    map.insert("8", "a");
    map.remove("3");
    expected = new String[]{
            "7:a:-1170874532",
            "5:c:95830475 8:a:-680157218",
            "10:c:1702710456 null null 9:b:1929790192",
    };
    assertEquals((String.join("\n", expected) + "\n"), map.toString());
  }

  @Test
  public void MultipleOperatrionComplexCase() {
    map.insert("15", "a");
    map.insert("88", "k");
    map.insert("12", "b");
    String test = map.remove("88");
    assertEquals(test, "k");
    map.insert("19", "c");
    map.put("12", "e");
    map.insert("16", "c");
    map.put("19", "e");
    String test2 = map.remove("19");
    assertEquals(test2, "e");
    String[] expected = new String[]{
            "15:a:-1170874532",
            "12:e:95830475 16:c:1929790192"
    };
    assertEquals((String.join("\n", expected) + "\n"), map.toString());
  }

}