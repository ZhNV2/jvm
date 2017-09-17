package ru.spbau.zhidkov

import scala.io.StdIn.readLine

object Main {
  def main(args: Array[String]): Unit = {
    println(CalculatorDouble.eval(readLine()))
  }
}
