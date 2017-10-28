package ru.spbau.zhidkov


import java.util.function.Consumer

import ru.spbau.zhidkov.MultiSet.CountMapBuilder

import scala.collection.mutable

class MultiSet[T] private (private val values: Map[T, Int] = Map.empty[T, Int]) {

  def apply(elem: T): Boolean = values.contains(elem)

  def +(elem: T): MultiSet[T] = {
    incSize(1)
    new MultiSet[T](values + (elem -> (count(elem) + 1)))
  }

  def -(elem: T): MultiSet[T] = {
    incSize(-1)
    val cnt = count(elem)
    new MultiSet[T](if (cnt > 1) values + (elem -> (cnt - 1)) else values - elem)
  }

  def iterator: Iterator[T] = values.flatMap(t => List.fill(t._2)(t._1)).iterator

  def filter(predicate: T => Boolean): MultiSet[T] = {
    withBuilder(builder => {
      values.foreach {
        case (elem, cnt) => if (predicate(elem)) builder.add(elem, cnt)
      }
    })
  }

  def map[A](func: T => A): MultiSet[A] = {
    withBuilder(builder => {
      values.foreach {
        case (elem, cnt) => builder.add(func(elem), cnt)
      }
    })
  }

  def flatMap[A](func: T => Seq[A]): MultiSet[A] = {
    MultiSet(map(func).iterator.toSeq.flatten.toList)
  }

  def find(value: T): Option[T] = {
    Some(value).filter(apply)
  }

  def foreach[A](f: T => A): Unit = {
    values.foreach { p =>
      (1 to p._2) foreach { n => f(p._1) }
    }
  }

  def withFilter(predicate: T => Boolean): WithFilter = new WithFilter(predicate)

  class WithFilter(predicate: T => Boolean) {
    def map[A](f: T => A): MultiSet[A] = filter(predicate).map(f)
    def flatMap[A](f: T => Seq[A]): MultiSet[A] = filter(predicate).flatMap(f)
    def foreach[A](f: T => A): Unit = filter(predicate).foreach(f)
    def withFilter(q: T => Boolean): WithFilter = new WithFilter(x => predicate(x) && q(x))
  }

  override def equals(obj: scala.Any): Boolean = {
    obj match {
      case multiSet: MultiSet[T] =>
        ((this | multiSet).size == size) && ((this & multiSet).size == size)
      case _ => false
    }
  }

  def & (multiSet: MultiSet[T]): MultiSet[T] = {
    withBuilder(builder => {
      values.foreach {
        case (elem, cnt) =>
          val newCount: Int = Math.min(cnt, multiSet.count(elem))
          if (newCount > 0) builder.add(elem, newCount)
      }
    })
  }

  def | (multiSet: MultiSet[T]): MultiSet[T] = {
    withBuilder(builder => {
      for (elem <- (iterator.toSeq ++ multiSet.iterator.toSeq).toSet[T]) {
        builder.add(elem, Math.max(count(elem), multiSet.count(elem)))
      }
    })
  }

  def count(elem: T): Int = values.getOrElse(elem, 0)

  private var sz = -1

  def size: Int = {
    if (sz != -1) sz else {
      sz = 0
      values.foreach(p => sz += p._2)
      sz
    }
  }

  private def incSize(delta: Int): Unit = {
    sz = size + delta
  }

  private def withBuilder[A](consumer: Consumer[CountMapBuilder[A]]): MultiSet[A] = {
    val builder = new CountMapBuilder[A]()
    consumer.accept(builder)
    new MultiSet(builder.build())
  }

}

object MultiSet {

  private val emptyMultiset:MultiSet[Any] = new MultiSet[Any]()

  def empty[T]: MultiSet[T] = emptyMultiset.asInstanceOf[MultiSet[T]]

  def apply[T](elems: T*): MultiSet[T] = {
    apply(elems.toList)
  }

   def apply[T](elems: List[T]): MultiSet[T] = {
     val builder: CountMapBuilder[T] = new CountMapBuilder[T]()
     elems.foreach(elem => builder.add(elem))
     new MultiSet(builder.build())
   }


  def unapplySeq[T](multiSet: MultiSet[T]): Option[Seq[T]] = {
    Some(multiSet.iterator.toList)
  }

  private class CountMapBuilder[T]() {

    private var map: mutable.Map[T, Int] = mutable.Map.empty

    def add(value: T, count: Int = 1): Unit = {
      map += (value -> (map.getOrElse(value, 0) + count))
    }

    def build(): Map[T, Int] = {
      Map.empty ++ map
    }

  }

}
