package ru.spbau.zhidkov

import scala.util.parsing.combinator.RegexParsers

abstract class Calculator[T] extends RegexParsers {

  protected def mul(a: T, b: T) : T

  protected def div(a: T, b: T) : T

  protected def sum(a: T, b: T) : T

  protected def sub(a: T, b: T) : T

  protected def unMinus(a: T) : T

  protected def number: Parser[T]

  private def rightMultiplier: Parser[T => T] =
    "*" ~> atomicTerm ~ rightMultiplier ^^ { case a ~ f => x: T => f(mul(x, a)) } |
    "/" ~> atomicTerm ~ rightMultiplier ^^ { case a ~ f => x: T => f(div(x, a)) } |
    "" ^^ { s => x: T => x }

  private def multiplier: Parser[T] = atomicTerm ~ rightMultiplier ^^ { case a ~ f => f(a) }

  private def rightTerm: Parser[T => T] =
    "+" ~> term ~ rightTerm ^^ { case a ~ f => x: T => f(sum(x, a)) } |
    "-" ~> term ~ rightTerm ^^ { case a ~ f => x: T => f(sub(x, a)) } |
    "" ^^ { s => x: T => x }

  private def atomicTerm: Parser[T] = "(" ~> expr <~ ")" | "-" ~> atomicTerm ^^ (x => unMinus(x)) | sqr | number

  private def term: Parser[T] = multiplier | atomicTerm

  private def expr: Parser[T] = term ~ rightTerm ^^ { case a ~ f => f(a) } | term

  private def sqr: Parser[T] = "sqr(" ~> expr <~ ")" ^^ { x => mul(x, x) }

  def eval(stringExpression: String): T = parseAll(expr, stringExpression).get
}