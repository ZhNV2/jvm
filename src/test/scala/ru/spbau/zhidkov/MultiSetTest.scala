package ru.spbau.zhidkov

import org.junit.Test
import org.junit.Assert.assertEquals

class MultiSetTest {

  @Test
  def testAddNewElement(): Unit = {
    assertEquals(MultiSet(1, 2, 3, 4), MultiSet(1, 2, 3) + 4)
  }

  @Test
  def testAddExistingElement(): Unit = {
    assertEquals(MultiSet(2, 2, 1), MultiSet(1, 2) + 2)
  }

  @Test
  def testDeleteMultipleElement(): Unit = {
    assertEquals(MultiSet(1, 1), MultiSet(1, 1, 1) - 1)
  }

  @Test
  def testDeleteUniqueElement(): Unit = {
    assertEquals(MultiSet(1, 2), MultiSet(1, 2, 3) - 3)
  }

  @Test
  def testFindTrue(): Unit = {
    assertEquals(Some(3), MultiSet(1, 2, 3).find(3))
  }

  @Test
  def testFindFalse(): Unit = {
    assertEquals(None, MultiSet(1, 2, 3).find(4))
  }

  @Test
  def testMap(): Unit = {
    assertEquals(MultiSet("11", "11", "22"), MultiSet(2, 1, 1).map(x => x.toString + x.toString))
  }

  @Test
  def testFilter(): Unit = {
    assertEquals(MultiSet(1, 1, 3), MultiSet(2, 1, 1, 3, 2).filter(_ % 2 != 0))
  }

  @Test
  def testFlatMap(): Unit = {
    assertEquals(MultiSet(1, 1, 2, 1, 1, 2), MultiSet(1, 1, 2).flatMap(t => List(t, t)))
  }

  @Test
  def testForComprehension(): Unit = {
    assertEquals(MultiSet(2, 2, 4), for (elem <- MultiSet(1, 1, 2, 2, 3) if elem != 2) yield elem + 1)
  }

  @Test
  def testPatternMatching(): Unit = {
    assertEquals(startsFromOne(MultiSet.empty), false)
    assertEquals(startsFromOne(MultiSet(1)), true)
    assertEquals(startsFromOne(MultiSet(1, 1, 1, 1)), true)
  }

  private def startsFromOne(multiSet: MultiSet[Int]): Boolean = {
    multiSet match {
      case MultiSet(1, _*) => true
      case _ => false
    }
  }

  @Test
  def testUnion(): Unit = {
    assertEquals(MultiSet(1, 1, 2, 3, 3), MultiSet(1, 1, 2) | MultiSet(1, 2, 3, 3))
  }

  @Test
  def testIntersection(): Unit = {
    assertEquals(MultiSet(1, 1), MultiSet(1, 2, 3, 4, 1) & MultiSet(5, 1, 1))
  }

  @Test
  def testSizeOfEmptySet(): Unit = {
    assertEquals(0, MultiSet.empty.size)
  }

  @Test
  def testSizeOfNonEmptySet(): Unit = {
    assertEquals(3, MultiSet(1, 1, 2).size)
  }

}
