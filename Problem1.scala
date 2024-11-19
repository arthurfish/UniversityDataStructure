/*
1. 求 1+2+3+...+100 的值。
sum = 0;
for (i = 1; i <= 100; i++) {
    sum += i;
}

           1     1             1
2. 求1 + ----- + --- + ... + ---- 的近似值。
           2     3           100
sum = 0.0;
for (i = 1; i <= 100; i++) {
    sum += 1.0 / i;
}

           1     1    1             1       1
3. 求1 - ----- + --- - ... + ----- - ----- 的近似值。
           2     3    4           99      100

                                1
(1)把原式看成∑100         i=1 (-1)
                             i+1
                                  。

                          1             1        1     1           1
(2)把原式看成(1 + ---------------- + ... + ----) - (2 + 4 + ... + 100)。
                          3            99

                    1     1        1     1               1     1
(3)把原式看成(---- - --) + (--- - --) + ... + (---- - ----)。
                    1     2        3     4              99   100

                  1    -1     1    -1             1     -1
(4)把原式看成 ---- + --- + ---- + --- + ... + ---- + ----。
                  1     2     3     4            99    100
j = 1.0;
sum = 0.0;
for (i = 1; i <= 100; i++) {
    sum += j / i;
    j = -j;
}

上机题:
             1      1       1              1
1. 求√6 (2 + ---- + 2 + ... + ----------------) 的近似值。
             1      22      3             1002

                1     1     1               1         1
2. 求4(1 - ----- + --- - ... + -------- - --------) 的近似值。
                3     5     7           19997     19999

          2     2    4     4    6     6     8    8           19998    19998
3. 求2(---- × ---- × ---- × ---- × ---- × ... × -------- × --------) 的近似值。
          1     3    3     5    5     7     7    9           19997    19999

       1000
4. 求 2     的末尾 3 位数字。

5. 在一个整数序列中,有一个整数的出现次数超过了一半。求该整数。说明:数据自定。
 */

import scala.annotation.tailrec

object Expr1 {
  def sumCloseInterval[A](from: Int, to: Int, getElemByIndex: Int => A)(implicit numeric: Numeric[A]): A = {
    import numeric._
    @tailrec
    def f(from: Int, to: Int, accumlator: A): A = {
      if from == to then getElemByIndex(from) + accumlator
      else f(from+1, to, accumlator + getElemByIndex(from))
    }
    f(from, to, numeric.zero)
  }

  def sumInt(to: Int): Int = sumCloseInterval(1, to, x => x.toInt)
  def sumHarmonicSeq(to: Int): Double = sumCloseInterval(1, to, x => 1.0 / x)
  def sumAlternatingSeq(to: Int): Double = sumCloseInterval(1, to, x => 1.0 / x * (if x % 2 == 0 then -1 else 1))
  def sumSpecialSeq1(to: Int): Double = Math.sqrt(6*sumCloseInterval(1, to, x => 1.0 / (x * x)))
  def sumSpecialSeq2(to: Int): Double = 4 * sumCloseInterval(1, (to + 1) / 2, x => (1.0 / (2 * x - 1) * (if x % 2 == 0 then -1 else 1)))
  def getLastThreeDigitsOfPowerOfTwo(power: Int): Int = {
    if power == 1 then 2
    else {
      val first = power / 2
      val second = power - first
      (getLastThreeDigitsOfPowerOfTwo(first) * getLastThreeDigitsOfPowerOfTwo(second)) % 1000
    }
  }
  def conwaysGameOfLife(sourceState: Array[Array[Boolean]], targetState: Array[Array[Boolean]]):Unit = {
    val rowNum = sourceState.length
    val colNum = sourceState.headOption match
      case None => 0
      case Some(head) => head.length
    require(rowNum >= 3)
    require(colNum >= 3)
    require(sourceState.length == targetState.length)
    require(sourceState.head.length == targetState.head.length)
    def displayPlane(arrayOfArray: Array[Array[Boolean]], name: String): Unit = {
      println(name + ":\n" + (for row <- arrayOfArray yield
            row.map(b => if b then '#' else "-").mkString).mkString("\n"))
    }
    displayPlane(sourceState, "SRC")
    def liveNeighborCounter(row: Int, col: Int): Int = {
      val eightDirection = for
        i <- Seq(-1, 0, 1)
        j <- Seq(-1, 0, 1)
        if !(i == 0 && j == 0)
      yield (i, j)
      assert(eightDirection.length == 8)
      def isLegal(row: Int, col: Int) = {
        (0 <= row && row < rowNum) && (0 <= col && col < colNum)
      }

      val count = (for (dx, dy) <- eightDirection yield
        (row + dx, col + dy))
        .count((r, c) => isLegal(r, c) && sourceState(r)(c))
      //println(s"In neighbor: (${row}, ${col}) => ${count}")
      count
    }

    def genNewState(): Unit = {
      for
        row <- 0 until rowNum
        col <- 0 until colNum
      do
        targetState(row)(col) =
          if sourceState(row)(col) == true then
            if Seq(2, 3) contains liveNeighborCounter(row, col) then true else false
          else
            if liveNeighborCounter(row, col) == 3 then true else false

    }
    genNewState()
    displayPlane(targetState, "TGT")
    println("-----End-----")
  }
}

object Main {
  def main(args: Array[String]): Unit = {
    import Expr1._
    import scala.util._
    val equationDisplayMessage =
      s"""
         | 1 + 2 + 3 + ... + 100 = ${sumInt(100)}
         | 1 + 1/2 + 1/3 + ... + 1/100 = ${sumHarmonicSeq(100)}
         | 1 - 1/2 + 1/3 + ... - 1/100 = ${sumAlternatingSeq(100)}
         | sqrt(6*(1/1**2 + 1/2**2 + ... + 1/100**2)) = ${sumSpecialSeq1(100)}
         | 4*(1-1/3+1/5+...+1/19997-1/19999) = ${sumSpecialSeq2(19999)}
         | 2**1000 % 1000 = ${getLastThreeDigitsOfPowerOfTwo(1000)}
         |""".stripMargin
    val source = scala.io.Source.fromFile("Expr1.FileInput.txt")
    val state1 = source.getLines().map(s => s.map(c => if c == '-' then false else true).toArray).toArray
    val state2 = state1.clone()
    source.close()
//    println((for row <- state1 yield
//      row.map(b => if b then '#' else "-").mkString).mkString("\n"))
    val step = 10
    val frames = for i <- 0 until step yield
      val (tempSource, tempTarget) = if i % 2 == 0 then (state1, state2) else (state2, state1)
      conwaysGameOfLife(tempSource, tempTarget)
      val lifeGameDisplayFrame = tempSource.map(seq => seq.map(b => if b then 'X' else ' ').mkString).mkString("\n")
      lifeGameDisplayFrame
    val composedFrame = frames.mkString("\n\n")
    //print(equationDisplayMessage + composedFrame)
  }
}