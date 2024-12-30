import java.util.LinkedList


/*
举例:
1. 打印下面的图案:
     *
    ***
   *****
  *******
*********
行号 开头空格个数 *个数
1      4             1
2      3             3
3      2             5
4      1             7
5      0             9
-------------------------
i      5-i           2i-1
for (i = 1; i <= 5; i++) {
     for (j = 1; j <= 5 - i; j++) {
         System.out.print(" ");
     }
     for (j = 1; j <= 2 * i - 1; j++) {
         System.out.print("*");
     }
     System.out.println();
}
2. 打印下面的图案:
     *
    ***
   *****
  *******
*********
  *******
   *****
    ***
     *
3. 打印下面的图案:
     *
    *#*
   *#*#*
  *#*#*#*
*#*#*#*#*
  *#*#*#*
   *#*#*
   *#*
    *
上机题:
1. 打印下面的图案:
      **
    ** **
  ***    ***
****      ****
  ***    ***
    ** **
      **
2. 某人岁数的二次方是个四位数,三次方是个六位数,该四位数和六位数恰好用遍 0 至 9
共 10 个数字。求该人岁数。
3. 输入 20 个整数,按最小值、最大值、次最小值、次最大值......的次序输出。
4. 已知一个递增序列,元素两两不等,它们满足下面的条件:       (1)数 1 在序列中。
                                               (2)若
数 x 在序列中,则 2x+1,3x+1 也在序列中。(3)除此之外,序列中无其他数。求该序列开头
的 100 个元素。
*5. 有一个无穷的巢脾,我们对巢房用下图所示的规则进行编号(该图只展示了其中很小的
一部分)   。给出两个巢房的编号,求从其中一个巢房到另一个巢房至少要经过几个其他巢房。
               25
           26     24
       27      11    23
  28       12     10    22
       13       3     9
  29        4     2     21
       14       1     8
  30        5     7     20
       15      6     19
  31       16     18    37
       32      17    36
           33     35
               34
 */

class Problem4 {
  fun triangleAsciiArt(triangleHeight: Int): String {
    fun calculateBlankChar(x: Int) = triangleHeight - x;
    fun calculateStarChar(x: Int) = 2 * x - 1;
    val stringBuilder = StringBuilder();
    for (i in 1..triangleHeight) {
      stringBuilder.append(" ".repeat(calculateBlankChar(i)) + "*".repeat(calculateStarChar(i)) + "\n");
    }
    return stringBuilder.toString();
  }

  fun simpleDiamondAsciiArt(diamondSize: Int): String {
    fun calculateBlankChar(x: Int) = Math.abs(x - diamondSize);
    fun calculateStarChar(x: Int) = if (x <= diamondSize) 2 * x - 1 else -2 * x + 4 * diamondSize - 1
    val stringBuilder = StringBuilder();
    for (i in 1..diamondSize * 2 - 1) {
      stringBuilder.append(" ".repeat(calculateBlankChar(i)) + "*".repeat(calculateStarChar(i)) + "\n");
    }
    return stringBuilder.toString();
  }

  fun solidFancyDiamondAsciiArt(diamondSize: Int): String {
    fun calculateBlankChar(x: Int) = Math.abs(x - diamondSize);
    fun generateFancyLine(length: Int): String {
      val stringBuilder = StringBuilder();
      for (i in 1..length) {
        if (i % 2 == 1)
          stringBuilder.append("*")
        else
          stringBuilder.append("#")
      }
      return stringBuilder.toString();
    }
    fun calculateFancyLineNumber(x: Int): Int = if(x <= diamondSize) 2 * x - 1 else -2 * x + 4* diamondSize - 1

    val stringBuilder = StringBuilder();
    for (i in 1..diamondSize * 2 - 1) {
      stringBuilder.append(" ".repeat(calculateBlankChar(i)) + generateFancyLine(calculateFancyLineNumber(i)) + "\n")
    }
    return stringBuilder.toString();
  }

  fun hollowFancyDiamondAsciiArt(diamondSize: Int): String {
    fun blankNumber(x: Int): Int = if (x <= diamondSize) -2 * x + 2 * diamondSize else (x - diamondSize) * 2
    fun starNumber(x: Int): Int = if (x <= diamondSize) x else -x + 2 * diamondSize
    fun middleBlankNumber(x: Int): Int = if (x <= diamondSize) 2 * (x - 1) else -2 * x + 4 * diamondSize - 2

    val stringBuilder = StringBuilder();
    for (i in 1..diamondSize * 2 - 1) {
      stringBuilder.append(
        " ".repeat(blankNumber(i)) + "*".repeat(starNumber(i)) + " ".repeat(middleBlankNumber(i)) + "*".repeat(
          starNumber(i)
        ) + "\n"
      );
    }
    return stringBuilder.toString();
  }

  fun calculateSomeoneAge(): Int {
    for (potentialAge in 1..200) {
      val squareOfAge = potentialAge * potentialAge;
      val cubeOfAge = potentialAge * potentialAge * potentialAge;
      if (squareOfAge.toString().length == 4 && cubeOfAge.toString().length == 6 && (squareOfAge.toString() + cubeOfAge.toString()).toSet().size == 10) {
        return potentialAge;
      }
    }
    return -1;
  }

  fun reorderSequence(sequence: List<Int>): List<Int> {
    val deque = LinkedList(sequence.sorted());
    val outputList = LinkedList<Int>();
    for (i in deque.indices) {
      if (i % 2 == 0)
        outputList.add(deque.pollFirst())
      else
        outputList.add(deque.pollLast())
    }
    return outputList;
  }

  fun generateSequence(number: Int): List<Int> {
    val sequence = mutableListOf<Int>()
    val queue = LinkedList<Int>()
    queue.add(1)

    while (sequence.size < number && queue.isNotEmpty()) {
      val current = queue.poll()
      sequence.add(current)

      val next1 = 2 * current + 1
      val next2 = 3 * current + 1

      if (next1 !in sequence) queue.add(next1)
      if (next2 !in sequence) queue.add(next2)
    }

    return sequence.sorted().take(number)
  }


  companion object {
    @JvmStatic
    fun main(args: Array<String>) {
      val problem4 = Problem4()

      println("1. Triangle Pattern:")
      println(problem4.triangleAsciiArt(5))

      println("2. Simple Diamond Pattern:")
      println(problem4.simpleDiamondAsciiArt(5))

      println("3. Solid Fancy Diamond Pattern:")
      println(problem4.solidFancyDiamondAsciiArt(5))

      println("4. Hollow Fancy Diamond Pattern:")
      println(problem4.hollowFancyDiamondAsciiArt(5))

      println("5. Calculate Someone's Age:")
      val age = problem4.calculateSomeoneAge()
      if (age != -1) {
        println("The age is: $age")
      } else {
        println("No valid age found.")
      }

      println("6. Reorder Sequence:")
      val sequence = listOf(5, 3, 8, 1, 9, 2, 7, 4, 6, 0, 10, 15, 12, 11, 13, 14, 16, 17, 18, 19)
      val reorderedSequence = problem4.reorderSequence(sequence)
      println("Reordered sequence: $reorderedSequence")

      println("7. Generate Sequence:")
      val generatedSequence = problem4.generateSequence(100)
      println("Generated sequence: $generatedSequence")
    }
  }
}