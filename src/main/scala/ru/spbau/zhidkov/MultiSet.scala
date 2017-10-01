package ru.spbau.zhidkov


import scala.collection.generic.CanBuildFrom
import scala.collection.{SetLike, mutable}

class MultiSet[T] (private val values: Map[T, Int] = Map.empty[T, Int]) extends SetLike[T, MultiSet[T]] with Set[T] {

  override def empty: MultiSet[T] = new MultiSet[T]

  override def contains(elem: T): Boolean = values.contains(elem)

  override def +(elem: T): MultiSet[T] = new MultiSet[T](values + (elem -> (count(elem) + 1)))

  override def -(elem: T): MultiSet[T] = {
    val cnt = count(elem)
    new MultiSet[T](if (cnt > 1) values + (elem -> (cnt - 1)) else values - elem)
  }

  override def iterator: Iterator[T] = values.flatMap(t => List.fill(t._2)(t._1)).iterator

  override def seq: MultiSet[T] = this

  override def newBuilder: mutable.Builder[T, MultiSet[T]] = MultiSet.newBuilder

  private def count(elem: T): Int = values.getOrElse(elem, 0)

}

object MultiSet {

  def empty[T]: MultiSet[T] = new MultiSet[T]

  def apply[T](elems: T*): MultiSet[T] = MultiSet.empty ++ elems

  def apply[T](elems: List[T]): MultiSet[T] = MultiSet.empty ++ elems

  def unapplySeq[T](multiSet: MultiSet[T]): Option[Seq[T]] = {
    Some(multiSet.iterator.toList)
  }

  implicit def canBuildFrom[A,B]: CanBuildFrom[MultiSet[B], A, MultiSet[A]] =
    new CanBuildFrom[MultiSet[B], A, MultiSet[A]] {
      def apply(from: MultiSet[B]) = newBuilder
      def apply() = newBuilder
    }

  def newBuilder[T]: mutable.Builder[T, MultiSet[T]] = new mutable.Builder[T, MultiSet[T]] {
    var multiSet: MultiSet[T] = MultiSet.empty

    override def +=(elem: T): this.type = {
      multiSet += elem
      this
    }

    override def clear(): Unit = multiSet = MultiSet.empty

    override def result(): MultiSet[T] = multiSet
  }

}
