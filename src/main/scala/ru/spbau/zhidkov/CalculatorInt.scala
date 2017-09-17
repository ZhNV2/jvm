package ru.spbau.zhidkov

object CalculatorInt extends Calculator[Int] {

  protected override def mul(a: Int, b: Int) : Int = a * b

  protected override def div(a: Int, b: Int) : Int = a / b

  protected override def sum(a: Int, b: Int) : Int = a + b

  protected override def sub(a: Int, b: Int) : Int = a - b

  protected override def unMinus(a: Int): Int = -a

  protected override def number: Parser[Int] = """-?[0-9]+""".r ^^ (s => s.toInt)
}
