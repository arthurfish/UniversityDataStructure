import Math.*
import scala.annotation.tailrec
object Expr3 {
  def problem1 = (1000 to 9999).
    filter(x => x.
      toString.
      map(c => (c-'0').toInt).
      map(a => a*a*a*a).
      sum
      == x)
  def problem2 = (100 to 999).
    filter(x => x % 11 == 0).
    filter(x => x / 11 ==
      x.toString.
        map(c => (c-'0').toInt).
        map(a => a*a).
        sum)
  def problem3 = for
    a <- 1 to 100
    b <- 1 to 100
    q = (a*a + b*b) / (a + b)
    r = (a*a + b*b) % (a + b)
    if pow(q, 2) + r == 2008
  yield
    (a, b)

  def problem4 = {
    val history = collection.mutable.Set[BigInt]()
    val helperHeap = collection.mutable.PriorityQueue[BigInt]()(Ordering.BigInt.reverse)
    helperHeap.addOne(1)
    def loop(): Unit =
      if history.size == 100 then ()
      else
        val x = helperHeap.dequeue()
        history.addOne(x)
        Seq(2*x, 3*x, 5*x) foreach (helperHeap.addOne(_))
        loop()
    loop()
    history.toSeq.sorted
  }

  def problem5(integers: Seq[Int]) = {
    @tailrec
    def gcd(a: Int, b: Int): Int = if b == 0 then a else gcd(b, a % b)
    integers.reduce(gcd)
  }

  def problem6(P: Int, L: Int, K: Int) = {
    def fact(i: Int) = {
      var res = 1
      for (e <- i to 1 by -1)
        res = res * e
      res
    }
    def nCr(n: Int, r: Int) = {
      fact(n) / (fact(n - r) * fact(r))
    }
    def nPr(n: Int, r: Int) = {
      fact(n) / fact(n - r)
    }
    pow(P, 3) - (K to L).map(i => (L-i+1) * nPr(P-1, L - i)).sum
  }

  def main(args: Array[String]): Unit = {
    print(
      s"""
        | The result of the experiment:
        | 1. ${problem1}
        | 2. ${problem2}
        | 3. ${problem3}
        | 4. ${problem4.last}
        | 5. ${problem5(Seq(18, 63, 36))}
        | 6. ${problem6(3, 3, 3)}
        |""".stripMargin)
  }
}
