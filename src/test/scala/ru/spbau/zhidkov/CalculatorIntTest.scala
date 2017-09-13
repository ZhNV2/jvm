package ru.spbau.zhidkov

import org.junit.Test
import org.junit.Assert.assertEquals

import CalculatorInt.eval

class CalculatorIntTest {

  @Test def testOneDigit() = assertEquals(1, eval("1"))

  @Test def testOneNumber() = assertEquals(1234, eval("1234"))

  @Test def testNegativeNumber() = assertEquals(-2, eval("-2"))

  @Test def testSimpleProd() = assertEquals(4, eval("2*2"))

  @Test def testProdOfTwoNumbers() = assertEquals(156, eval("12*13"))

  @Test def testDivOfTwoNumbres() = assertEquals(13, eval("156/12"))

  @Test def testComplexMultiplier1() = assertEquals(16, eval("2*2*2*2"))

  @Test def testComplexMultiplier2() = assertEquals(1, eval("50/2/5/5"))

  @Test def testComplexMultiplier3() = assertEquals(66, eval("11*12/2"))

  @Test def testComplexMultiplier4() = assertEquals(5, eval("10/2*2/2"))

  @Test def testSimpleSum() = assertEquals(4, eval("2+2"))

  @Test def testSumOfTwoNumbers() = assertEquals(16, eval("-15+31"))

  @Test def testSubOfTwoNumbers() = assertEquals(-16, eval("15-31"))

  @Test def testBrackets1() = assertEquals(2, eval("(2)"))

  @Test def testBrackets2() = assertEquals(6, eval("(2+2*2)"))

  @Test def testBrackets3() = assertEquals(8, eval("(2+2)*2"))

  @Test def testSqr() = assertEquals(1600, eval("sqr(40)"))

  @Test def testExpr1() = assertEquals(14, eval("2+(3+3)*2"))

  @Test def testExpr2() = assertEquals(6, eval("(10+sqr(2)*-(3-2))"))

  @Test def testExpr3() = assertEquals(0, eval("sqr(17)-17*17"))

  @Test def testExpr4() = assertEquals(12, eval("-1-((2*6)-sqr(13-7-1))"))

  @Test def testExpr5() = assertEquals(31, eval("((((31-sqr(2-2)))))"))

}
