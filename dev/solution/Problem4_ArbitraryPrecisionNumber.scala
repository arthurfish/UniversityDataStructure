import PList.*

import scala.annotation.{tailrec, targetName}

object Expr4_ArbitraryPrecisionNumber{
  def fromString(s: String): Expr4_ArbitraryPrecisionNumber = Expr4_ArbitraryPrecisionNumber(PList.createIntPList(s).reversed())
}

case class Expr4_ArbitraryPrecisionNumber(numberList: PList[Int]){
  val base: Int = 10

  override def toString: String = numberList.reversed().toString

  infix def +(that: Expr4_ArbitraryPrecisionNumber): Expr4_ArbitraryPrecisionNumber = {
    val thisLen = this.numberList.getLength()
    val thatLen = that.numberList.getLength()
    val resultLen = math.max(thisLen, thatLen)
    val tuples = this.numberList.padding(0, resultLen) zip that.numberList.padding(0, resultLen)
    val (reversedResult, carry) = tuples.foldLeft((Nil, 0): (PList[Int], Int)) { case ((a, b), (acc, carry)) => (Cons((a + b + carry) % base, acc), (a + b + carry) / base) }
    val result = reversedResult.reversed()
    val carryAppended = if carry != 0 then result.appended(Cons(carry, Nil)) else result
    return Expr4_ArbitraryPrecisionNumber(carryAppended)
  }

  infix def -(that: Expr4_ArbitraryPrecisionNumber): Expr4_ArbitraryPrecisionNumber = {
    this + Expr4_ArbitraryPrecisionNumber(that.numberList map (- _))
  }


}

@main
def main(): Unit = {
  print((Expr4_ArbitraryPrecisionNumber.fromString("46524562435") + Expr4_ArbitraryPrecisionNumber.fromString("9870876")).toString)
}