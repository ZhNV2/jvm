package ru.spbau.zhidkov

import org.junit.Test
import org.junit.Assert.assertEquals

import CalculatorDouble.eval

class CalculatorDoubleTest {

  private val EPS: Double = 1e-3

  @Test def testDigit() = assertEquals(2, eval("2"), EPS)

  @Test def testDoubleNumber1() = assertEquals(2.5, eval("2.5"), EPS)

  @Test def testDoubleNumber2() = assertEquals(0.532, eval("0.532"), EPS)

  @Test def testDoubleNumber3() = assertEquals(1, eval("1."), EPS)

  @Test def testExpr1() = assertEquals(2.5, eval("5/2"), EPS)

  @Test def testExpr2() = assertEquals(10, eval("2.5+3*2.5"), EPS)

  @Test def testExpr3() = assertEquals(1.19, eval("sqr(1.2)-0.5*0.5"), EPS)

  @Test def testExpr4() = assertEquals(-15.318, eval("(2.1+0.12)*(2.3-9.2)"), EPS)

  @Test def testExpr5() = assertEquals(16.5, eval("31/2+sqr(0.5)*-2.*-2.0"), EPS)
}
