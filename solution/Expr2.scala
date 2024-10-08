import scala.annotation.tailrec
import scala.language.postfixOps

object Expr2 {
  def f1(x: Int): Double = if x == 1 then 1 else Math.sqrt(x + f1(x-1))

  def f1_iter(x: Int): Double =
    @tailrec
    def g(curr: Int, target: Int, acc: Double): Double =
      if curr > target then acc
      else g(curr + 1, target, math.sqrt(curr + acc))
    g(1, x, 0)


  def f2(x: Int): Double =
    def g(x: Int): Double = if x == 1 then math.sqrt(2) else math.sqrt(2 + g(x-1))
    def u(x: Int): Double = if x == 1 then 2 / g(1) else u(x-1) * 2 / g(x)
    u(x)

  def f2_iter(x: Int): Double =
    def g(x: Int): Double =
      @tailrec
      def g_iter(curr: Int, target: Int, acc: Double): Double =
        if curr > target then acc
        else g_iter(curr + 1, target, math.sqrt(2 + acc))

      g_iter(1, x, 0)

    def u(x: Int): Double =
      @tailrec
      def u_iter(curr: Int, target: Int, acc: Double): Double =
        if curr > target then acc
        else u_iter(curr+1, target, acc * 2 / g(x))

      u_iter(1, x, 1)

    u(x)

  def f3(x: Int): Double = {
    def g(x: Int): Double = if x == 1 then 1 else g(x-1) * ((x-1) / (2*x-1))
    def u(x: Int): Double = if x == 1 then 1 else g(x) + u(x-1)
    u(x)
  }

  def f4(): Option[(Int, Int)] = {
    val upperBound = 10000 * 10000
    @tailrec
    def g(x: Int): Option[Int] =
      if x == upperBound then None
      else
        val j = x
        val i = j + 56
        if i * i % 100 == j * j % 100 then Some(j)
        else g(x+1)

    g(1).map(x => (x + 56, x))
  }

  def f5(x: Int): Int = if x == 1 then 1 else 1 + ((1 to x/2) map (x => f5(x)) sum)

  def main(args: Array[String]): Unit = {
    println(f5(8))
  }
}
