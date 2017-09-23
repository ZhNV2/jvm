package ru.spbau.zhidkov.bot

import akka.actor.ActorRef
import akka.pattern.ask
import akka.util.Timeout
import info.mukel.telegrambot4s.api.{Polling, TelegramBot}
import info.mukel.telegrambot4s.api.declarative.Commands
import ru.spbau.zhidkov.database.DebtActor
import ru.spbau.zhidkov.parser.MessageParser

import scala.concurrent.Future
import scala.util.Success
import scala.concurrent.duration.DurationInt

class DebtBot(val token: String,
              val database: ActorRef) extends TelegramBot with Polling with Commands {

  onMessage {
    implicit message =>
      message.text.foreach { text =>
          MessageParser.parse(text) match {
            case MessageParser.AddDebt(name, amount) =>
              database ! DebtActor.AddDebt(message.chat.id, name, amount)
              reply("добавлено")
            case MessageParser.GetDebt(name) =>
              implicit val timeout: Timeout = Timeout(1.second)
              (database ? DebtActor.GetDebt(message.chat.id, name)).onComplete {
                case Success(value) =>
                  reply(value.toString)
                case _ =>
                  reply("Ошибка базы данных!:(")
              }
            case MessageParser.GetSumDebts() =>
              implicit val timeout: Timeout = Timeout(1.second)
              (database ? DebtActor.GetSumDebts(message.chat.id)).onComplete {
                case Success(value) =>
                  reply(value.toString)
                case _ =>
                  reply("Ошибка базы данных!:(")
              }
            case MessageParser.WrongMessage =>
              reply("Неверная команда:(")
          }
      }
  }
}
