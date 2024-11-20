/*
中缀表达式是我们所熟悉的表达式形式。为了能正确表示运算的先后顺序，中缀表达式中难免要出现括号，假设只允许用圆括号。
编写程序，读入一个操作数为实数的中缀表达式，求该表达式的值。
要求中缀表达式以字符串的形式读入，可含有加、减、乘、除运算符和左、右括号，并假设以“#”作为结束符。
如输入“3.5*(20+4)-1#”，则程序运行结果应为83。
要求一步一步显示输入序列和栈的变化过程。
考虑算法的健壮性，当表达式错误时，要给出错误种类的提示。
 */
import OPERATOR.*
import java.util.*
import kotlin.math.abs

sealed interface Token
data class NUMBER(val value: Double) : Token
enum class OPERATOR : Token {
  PLUS, MINUS, MULTIPLY, DIVIDE
}


enum class PARENTHESES : Token {
  LEFT, RIGHT
}

fun approximatelyEqual(a: Double, b: Double): Boolean {
  return abs(a - b) < 0.01
}

//
fun tokenizeExpression(expression: String): List<Token> {
  val tokens = mutableListOf<Token>()
  val stash = mutableListOf<Char>()
  for (char in expression) when {
    char.isDigit() || char == '.' -> stash.add(char)
    else -> {
      if (stash.isNotEmpty()) {
        try {
          val number = stash.joinToString(separator = "").toDouble()
          stash.clear()
          tokens.add(NUMBER(number))
        } catch (e: NumberFormatException) {
          println("Number parsing error in tokenizing process. stash: ${stash}")
        }
      }
      val symbolToken: Token = when (char) {
        '+' -> OPERATOR.PLUS
        '-' -> OPERATOR.MINUS
        '*' -> OPERATOR.MULTIPLY
        '/' -> OPERATOR.DIVIDE
        '(' -> PARENTHESES.LEFT
        ')' -> PARENTHESES.RIGHT
        else -> throw AssertionError(
          "Unsupported character: $char in tokenizing process."
        )
      }
      tokens.add(symbolToken)
    }
  }
  if (stash.isNotEmpty())
    tokens.add(NUMBER(stash.joinToString(separator = "").toDouble()))
  return tokens
}

fun matchParenthesis(expression: Array<Token>): Map<Int, Int> {
  val assistIndexStack = LinkedList<Int>()
  val toRightParenthesisIndex = TreeMap<Int, Int>()
  for ((index, token) in expression.withIndex()) when (token) {
    PARENTHESES.LEFT -> assistIndexStack.push(index)
    PARENTHESES.RIGHT -> {
      val matchedLeftParenthesisIndex = assistIndexStack.pop() ?: throw AssertionError(
        "No matched LEFT parenthesis for RIGHT parenthesis at $index in parenthesis matching process."
      )
      toRightParenthesisIndex[matchedLeftParenthesisIndex] = index
    }

    else -> {}
  }
  assert(assistIndexStack.isEmpty()) {
    "Assist stack is expected to be empty in parenthesis matching process."
  }
  return toRightParenthesisIndex
}

//With two stacks like this: |[1+2-6] <-> [/] <-> [2+3]|
fun evaluatePlainTokenizedExpression(tokenizedPlainExpression: List<Token>): Token {
  fun evaluateTriple(operator: OPERATOR, operant1: NUMBER, operant2: NUMBER): NUMBER {
    val a = operant1.value
    val b = operant2.value
    val c = when (operator) {
      PLUS -> a + b
      MINUS -> a - b
      MULTIPLY -> a * b
      DIVIDE -> a / b
    }
    return NUMBER(c)
  }

  val leftDeque = ArrayDeque<Token>()
  val rightDeque = ArrayDeque<Token>()
  rightDeque.addAll(tokenizedPlainExpression)
  while (rightDeque.isNotEmpty()) {
    val theMiddleOne = rightDeque.removeFirst()
    when (theMiddleOne) {
      PLUS, MINUS, is NUMBER -> leftDeque.addLast(theMiddleOne)
      MULTIPLY, DIVIDE -> leftDeque.addLast(
        evaluateTriple(
          theMiddleOne as OPERATOR,
          leftDeque.pollLast() as NUMBER,
          rightDeque.pollFirst() as NUMBER
        )
      )

      else -> throw AssertionError("Encountered unexpected token:${theMiddleOne} in evaluate plain expression.")
    }
  }
  rightDeque.addAll(leftDeque)
  leftDeque.clear()
  while (rightDeque.isNotEmpty()) {
    val theMiddleOne = rightDeque.removeFirst()
    when (theMiddleOne) {
      is NUMBER -> leftDeque.addLast(theMiddleOne)
      PLUS, MINUS -> leftDeque.addLast(
        evaluateTriple(
          theMiddleOne as OPERATOR,
          leftDeque.pollLast() as NUMBER,
          rightDeque.pollFirst() as NUMBER
        )
      )
      else -> throw AssertionError("Encountered unexpected token:${theMiddleOne} in evaluate plain expression.")
    }
  }

  return leftDeque.first()
}

fun evaluateTokenizedExpressionWithParenthesis(
  expression: Array<Token>,
  toRightParenthesisIndex: Map<Int, Int>
): Double {
  fun evaluate(leftInclusive: Int, rightExclusive: Int): Token {
    val tokensWithoutParenthesis = LinkedList<Token>()
    var index = leftInclusive
    while (index < rightExclusive) {
      when (expression[index]) {
        PARENTHESES.LEFT -> {
          val rightParenthesisIndex = toRightParenthesisIndex[index]
            ?: throw AssertionError(
              "Matched right parenthesis is missing in evaluation process"
            )
          val token = evaluate(index + 1, rightParenthesisIndex)
          tokensWithoutParenthesis.add(token)
          index = rightParenthesisIndex + 1
        }

        PARENTHESES.RIGHT -> throw AssertionError(
          "Encountered right parenthesis unexpectedly in matching process."
        )

        else -> {
          tokensWithoutParenthesis.add(expression[index])
          index++
        }
      }
    }//Parenthesis is removed in the token list.
    return evaluatePlainTokenizedExpression(tokensWithoutParenthesis)
  }
  return (evaluate(0, expression.size) as NUMBER).value
}

fun testTokenizeProcess(): Unit {
  val expr = "3.5*(20+4)-1"
  val result = tokenizeExpression(expr)
  println("TokenizeTest: expr:$expr result:$result")
  assert(
    result == listOf(
      NUMBER(3.5), OPERATOR.MULTIPLY,
      PARENTHESES.LEFT, NUMBER(20.toDouble()), OPERATOR.PLUS, NUMBER(4.toDouble()), PARENTHESES.RIGHT,
      OPERATOR.MINUS, NUMBER(1.toDouble())
    )
  ) { "tokenize test failed." }
}

fun testParenthesisMatching(): Unit {
  val expr = "3.5*(20+4)-1"
  val result = matchParenthesis(tokenizeExpression(expr).toTypedArray())
  println("ParenthesisTest: expr:$expr result:$result")
  val expectedResult = TreeMap<Int, Int>()
  expectedResult[2] = 6
  assert(result == expectedResult)
}

fun testPlainEvaluation(): Unit {
  val expr = "1+2-3*4/5+6-7/8*2*3+9-2"
  val result = evaluatePlainTokenizedExpression(tokenizeExpression(expr)) as NUMBER
  println("PlainEvaluationTest: expr:$expr result:$result")
  assert(approximatelyEqual(result.value, 8.35.toDouble()))
}

fun testFullEvaluation(): Unit {
  val expr = "3.5*(20+4)-1"
  val parenthesesMatching = matchParenthesis(tokenizeExpression(expr).toTypedArray())
  val result = evaluateTokenizedExpressionWithParenthesis(tokenizeExpression(expr).toTypedArray(), parenthesesMatching)
  println("FullEvaluationTest: expr:$expr result:$result")
  assert(approximatelyEqual(result, 83.0))
}

fun main() {
  testFullEvaluation()
}