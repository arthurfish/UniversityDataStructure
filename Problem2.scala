/*
求 sinsinsin...sinx 的值(有 n 个 sin;x 和 n 的值在执行时输入)。

(1) sin...sinsinsinx
                    |
                    s
                 ---|
                    t
              -----|
                   u
         ---------|
                ...

不断引入新变量,导致无法编写程序。

(2) sin...sinsinsinx
                    |
                    s
                 ---|
                    s
              -----|
                   s
         ---------|
                  s
     -------------|
                  s

用迭代方式编写程序。

s = x;
for (i = 1; i <= n; i++) {
    s = sin(s);
}

上机题:
1. 求√100 + √99 + √98 + ... + √1 的近似值。

       π    2        2          2
2. 根据 -- = -- × -------- × --------- × ... 求π的近似值,要求取前 100 个分式。
       2   √2    √2+√2     √2+√2+√2

       π        1    1  2    1   2  3    1  2  3  4
3. 根据 -- = 1 + - + - × - + - × - × - + - × - × - × - + ... 求π的近似值,要求取
       2        3    3  5    3   5  7    3  5  7  9
前 100 项。

                                                                2                    2
4. 有两个两位正整数 i 和 j, 已知 i 减去 j 等于 56, i 的末两位数字等于 j 的末两位数字。
求 i 和 j 的值。

5. Counting Numbers
Starting from a positive integer n (1 ≤ n ≤ 2001). On the left of the integer n, you can place
another integer m to form a new integer mn, where m must be less than or equal to half of the
integer n. If there is an integer k less than or equal to half of m, you can place k on the left of mn to
form a new integer kmn, ..., and so on. For example, you can place 12 on the left of 30 to form an
integer 1230, and you can place 6 to the left of 1230 to form an integer 61230, ..., and so on.

For example, start from n = 8, you can have the following 10 integers (including the integer you
start with): 8, 18, 28, 38, 48, 128, 138, 148, 248, 1248.

Given an integer n, find the number of integers you can get using the procedure described above.
 */

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
