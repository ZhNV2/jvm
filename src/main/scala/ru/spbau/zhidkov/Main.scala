package ru.spbau.zhidkov
import akka.actor.{ActorSystem, Props}
import ru.spbau.zhidkov.bot.DebtBot
import ru.spbau.zhidkov.database.DebtActor

object Main extends App {

  private val token = "436386239:AAEKNdLatdIhf-g-Ch1u8FcS3ASiih0Oics"

  private val database = ActorSystem().actorOf(Props(classOf[DebtActor]))

  private val bot = new DebtBot(token, database)

  bot.run()

}
