/*
求所有满足下面条件的三位数:它的各位数字的三次方之和恰好等于自己(即水仙花数)。

(1)
for (n = 100; n <= 999; n++) {
    i = n / 100;
    j = n / 10 - i * 10;
    k = n % 10;
    if (i * i * i + j * j * j + k * k * k == n) {
        System.out.println(n);
    }
}

(2)
for (i = 1; i <= 9; i++) {
    for (j = 0; j <= 9; j++) {
        for (k = 0; k <= 9; k++) {
            if (i * i * i + j * j * j + k * k * k == i * 100 + j * 10 + k) {
                System.out.println(i * 100 + j * 10 + k);
            }
        }
    }
}

(3)
n = 99;
for (i = 1; i <= 9; i++) {
    ic = i * i * i;
    for (j = 0; j <= 9; j++) {
        jc = j * j * j;
        for (k = 0; k <= 9; k++) {
            n++;
            if (ic + jc + k * k * k == n) {
                System.out.println(n);
            }
        }
    }
}

(4)
for (a = 0; a <= 9; a++) {
    c[a] = a * a * a;
}
n = 99;
for (i = 1; i <= 9; i++) {
    ic = c[i];
    for (j = 0; j <= 9; j++) {
        jc = c[j];
        for (k = 0; k <= 9; k++) {
            n++;
            if (ic + jc + c[k] == n) {
                System.out.println(n);
            }
        }
    }
}

上机题(题号前带*为选做题):
1. 求所有满足下面条件的四位数:它的各位数字的四次方之和恰好等于自己。

2. 求所有满足下面条件的三位数:它能被 11 整除,且所得的商恰好等于它的各位数字的平方和。

3. 设 a,b 都是不超过 100 的正整数。在整数范围内,设 $(a^2+b^2)$ 除以 $(a+b)$ 所得的商为 q,余数为 r。
求满足 $q^2+r=2008$ 的所有正整数对 $(a,b)$。

4. 已知一个递增序列,元素两两不等,它们满足下面的条件:
(1)数 1 在序列中。
(2)若数 x 在序列中,则 2x,3x,5x 也在序列中。
(3)除此之外,序列中无其他数。
求该序列开头的 100 个元素。

5. Finding the gcd
Given n positive integers between 1 and 200000 $(1 \leq n \leq 100)$, you are required to find the
greatest common divisor of the n integers.

For example, if n = 3, and the integers are 18, 63, 36, then the greatest common divisor is 9.

*6. Number Lists
Given P integers 1, 2, 3, ..., P, you can construct a list which contains L integers chosen from the
P integers, but the list can not have K or more than K consecutive 1's. $(1 \leq P < 10, 1 < L < 31,
1 < K < L + 1)$

For example, when P = 2, L = 3, and K = 2, the lists can be
121  122  212  221  222
There are 5 lists.

In the case of P = 3, L = 3, and K = 3, the lists can be
112  211  311
113  212  312
121  213  313
122  221  321
123  222  322
131  223  323
132  231  331
133  232  332
     233  333
There are 26 lists.

Given three integers P, L, and K, you are required to calculate the total number of allowable lists
as described above.
*/
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

  def main(): Unit = {
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
