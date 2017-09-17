package ru.spbau.zhidkov

object CalculatorDouble extends Calculator[Double] {

  protected override def mul(a: Double, b: Double) : Double = a * b

  protected override def div(a: Double, b: Double) : Double = a / b

  protected override def sum(a: Double, b: Double) : Double = a + b

  protected override def sub(a: Double, b: Double) : Double = a - b

  protected override def unMinus(a: Double): Double = -a

  protected override def number: Parser[Double] = """-?[0-9]+(\.[0-9]*)?""".r ^^ (s => s.toDouble)

}
