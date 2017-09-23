package ru.spbau.zhidkov.database

import akka.persistence.PersistentActor
import ru.spbau.zhidkov.database.DebtActor.{AddDebt, Event, GetDebt, GetSumDebts}

import scala.collection.mutable

class DebtActor extends PersistentActor {

  private val debtMap: mutable.HashMap[Long, mutable.HashMap[String, Int]] = mutable.HashMap.empty

  private def receiveEvent(event: Event): Unit = {
    event match {
      case AddDebt(chatId, name, amount) =>
        val chatMap = debtMap.getOrElseUpdate(chatId, mutable.HashMap.empty)
        val newDebt = chatMap.getOrElseUpdate(name, 0) + amount
        if (newDebt == 0) {
          chatMap.remove(name)
          if (chatMap.isEmpty) {
            debtMap.remove(chatId)
          }
        } else {
          chatMap.update(name, newDebt)
        }
    }
  }

  override def receiveRecover: Receive = {
    case event: Event => receiveEvent(event)
  }

  override def receiveCommand: Receive = {
    case evt: Event => persist(evt)(receiveEvent)
    case GetDebt(chatId, name) =>
      debtMap.get(chatId) match {
        case Some(map) =>
          map.get(name) match {
            case Some(value) =>
              sender ! value
            case None =>
              sender ! 0
          }
        case None =>
          sender ! 0
      }
    case GetSumDebts(chatId) =>
      debtMap.get(chatId) match {
        case Some(map) =>
          sender ! map.values.sum
        case None =>
          sender ! 0
      }
  }

  override def persistenceId = "hangman-database"

}

object DebtActor {

  trait Event

  case class AddDebt(chatId: Long, name: String, amount: Integer) extends Event

  trait Query

  case class GetDebt(chatId: Long, name: String) extends Query

  case class GetSumDebts(chatId: Long) extends Query

}
