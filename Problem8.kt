import java.util.*


/*
上机题:
☆1. 求后缀表达式的值。
说明:
(1)后缀表达式中的操作数只有 0,1,2,...,9 共 10 种可能。中间结果和最后结果可以是其
他数。
(2)后缀表达式中的运算符只有+,-,*,/共 4 种可能。其中+,-只表示双目加、减,不表示
单目正、负。
(3)后缀表达式是正确的。除数不为 0。
☆2. 求中缀表达式的值。
说明:
(1)中缀表达式中的操作数只有 0,1,2,...,9 共 10 种可能。中间结果和最后结果可以是其
他数。
(2)中缀表达式中的运算符只有+,-,*,/共 4 种可能。其中+,-只表示双目加、减,不表示
单目正、负。
(3)中缀表达式中的圆括号可以并列和嵌套。
(4)中缀表达式是正确的。除数不为 0。
(5)要求不先把中缀表达式完全转化为后缀表达式,而是一边转化一边求值。
*/

class Problem8 {
  fun evaluateSuffixExpression(expression: List<Char>): Int{
    fun arithmeticCalculate(operator: Char, operand1: Int, operand2: Int) = when (operator) {
      '+' -> operand1 + operand2
      '-' -> operand1 - operand2
      '*' -> operand1 * operand2
      '/' -> operand1 / operand2
      else -> throw IllegalArgumentException("Invalid operator: $operator")
    }
    val assistStack: Stack<Int> = Stack();
    for(term in expression) when{
      term.isDigit()  -> assistStack.push(term.digitToInt())
      else -> {
        val operand2 = assistStack.pop()
        val operand1 = assistStack.pop()
        assistStack.push(arithmeticCalculate(term, operand1, operand2 ))
      }
    }
    return assistStack.pop()
  }

  //It is not correct. Although I have finished a correct one,
  // I do want to make some change. Unfortunately I failed.
  fun evaluateInfixExpression(expression: List<Char>): Int{
    fun tripleCalculate(operator: Char, operand1: Int, operand2: Int) = when (operator) {
      '+' -> operand1 + operand2
      '-' -> operand1 - operand2
      '*' -> operand1 * operand2
      '/' -> operand1 / operand2
      else -> throw IllegalArgumentException("Invalid operator: $operator")
    }
    data class Expr(val isOperator: Boolean=false, val isOperand: Boolean=false, val operator: Char='!', val operand: Int=0){
      override fun toString(): String = if(isOperator) operator.toString() else operand.toString()
    }
    fun isTimeOrDiv(term: Expr): Boolean =
      term.operator == '*' || term.operator == '/';
    fun isPlusOrMinus(term: Expr): Boolean =
      term.operator == '+' || term.operator == '-';
    fun visualizeState(left: LinkedList<Expr>, right: LinkedList<Expr>): Unit = println(left.joinToString() + " | " + right.joinToString())
    fun reduceSpecificOperation(expression: LinkedList<Expr>, isConcernedTerm: (Expr) -> Boolean): LinkedList<Expr>{
      val assistStack = LinkedList<Expr>();
      while(expression.isNotEmpty()){
        visualizeState(assistStack, expression)
        val exprHead = expression.removeFirst()
        if(isConcernedTerm(exprHead)){
          val assistPeek = assistStack.peekLast()
          val exprPeek = expression.peekFirst()
          if(assistPeek.isOperand && exprPeek.isOperand){
            assistStack.removeLast()
            expression.removeFirst()
            val tripleResult = tripleCalculate(exprHead.operator, assistPeek.operand, exprPeek.operand)
            assistStack.addLast(Expr(isOperand = true, operand = tripleResult))
          }else{
            assistStack.addLast(exprHead)
          }
        }else{
          assistStack.addLast(exprHead)
        }
      }
      return assistStack as LinkedList<Expr>
    }
    fun removeRedundantParenthesis(expression: LinkedList<Expr>): LinkedList<Expr>{
      fun isParenthesis(term: Expr): Boolean =
        if(!term.isOperator) false
        else if(term.operator == '(' || term.operator == ')') true;
        else false;
      val assistStack = LinkedList<Expr>();
      print("Removing parenthesis expression: ")
      while(expression.isNotEmpty()){
        visualizeState(assistStack, expression);
        val exprHead = expression.removeFirst()
        if(exprHead.isOperand){
          val leftNeighbor = assistStack.peekLast() ?: Expr()
          val rightNeighbor = expression.peekFirst() ?: Expr()
          if(leftNeighbor.operator == '(' && rightNeighbor.operator == ')'){
            assistStack.removeLast()
            expression.removeFirst()
            assistStack.addLast(exprHead)
          }else{
            assistStack.addLast(exprHead)
          }
        }else{
          assistStack.addLast(exprHead)
        }
      }
      return assistStack
    }
    var workingExpression = LinkedList(expression.map {
      var isOperator=false;
      var isOperand=false;
      var operator = ' '
      var operand = 0
      if(it.isDigit()){
        isOperand = true
        operand = it.digitToInt()
      }else{
        isOperator = true;
        operator = it;
      }
      Expr(isOperator = isOperator, isOperand = isOperand, operator = operator, operand = operand)
    })
    while(true) {
      val outerStashedLength = workingExpression.size
      while (true) {
        val reducedTimeAndDiv = reduceSpecificOperation(workingExpression, ::isTimeOrDiv)
        val stashedSize = reducedTimeAndDiv.size
        val reducedParenthesis = removeRedundantParenthesis(reducedTimeAndDiv)
        workingExpression = reducedParenthesis
        if (reducedParenthesis.size == stashedSize) {
          break
        }
      }
      while (true) {
        val reducedTimeAndDiv = reduceSpecificOperation(workingExpression, ::isPlusOrMinus)
        val stashedSize = reducedTimeAndDiv.size
        val reducedParenthesis = removeRedundantParenthesis(reducedTimeAndDiv)
        workingExpression = reducedParenthesis
        if (reducedParenthesis.size == stashedSize) {
          break
        }
      }
      if(outerStashedLength == workingExpression.size){
        break
      }
    }
    println("workingExpression: $workingExpression")
    return workingExpression.first().operand
  }

  companion object{
    @JvmStatic
    fun main(args: Array<String>) {
      val problem8 = Problem8()
    }
  }
}