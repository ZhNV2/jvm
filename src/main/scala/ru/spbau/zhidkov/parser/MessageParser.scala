package ru.spbau.zhidkov.parser

import ru.spbau.zhidkov.parser.MessageParser.{AddDebt, GetDebt, GetSumDebts, UserMessage}

import scala.util.matching.Regex
import scala.util.parsing.combinator.RegexParsers

class MessageParser extends RegexParsers {

  override def skipWhitespace = true

  override val whiteSpace: Regex = "[ \t\r\f]+".r

  private val nameParser: Parser[String] = "[a-я]+".r

  private val amountParser: Parser[Int] = """-?[0-9]+""".r ^^ (s => s.toInt)

  private val addDebtParser: Parser[AddDebt] = nameParser ~ amountParser ^^ {
    case name ~ amount => AddDebt(name, amount)
  }

  private val getDebtParser: Parser[GetDebt] = nameParser <~ "?" ^^ (name => GetDebt(name))

  private val getSumDebtsParser: Parser[UserMessage] = "=".r ^^ { _ => GetSumDebts }

  val userMessage: Parser[UserMessage] = getSumDebtsParser | getDebtParser | addDebtParser

}

object MessageParser extends MessageParser {

  def parse(text: String): UserMessage = {
    parse(userMessage, text) match {
      case Success(message, _) => message
      case _ => WrongMessage
    }
  }

  trait UserMessage

  case class AddDebt(name: String, amount: Integer) extends UserMessage

  case class GetDebt(name: String) extends UserMessage

  case object GetSumDebts extends UserMessage

  case object WrongMessage extends UserMessage

}
