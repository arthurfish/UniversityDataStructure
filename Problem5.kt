/*
上机题(题号前带☆为数据结构题)
 :
☆1. 要求用顺序存储结构实现线性表。对线性表进行若干次插入和删除,对于每一次插入
或删除,若成功,则输出线性表,否则输出出错信息。
执行示例:
 (加下划线部分为输入内容)
是否要对线性表进行插入和删除?(Y/N)
Y
进行插入还是删除?(1--插入,2--删除)
1
输入插入位置:
0
输入插入元素:
8
线性表为(8)
是否要对线性表进行插入和删除?(Y/N)
Y
进行插入还是删除?(1--插入,2--删除)
1
输入插入位置:
0
输入插入元素:
2
线性表为(2,8)
是否要对线性表进行插入和删除?(Y/N)
Y
进行插入还是删除?(1--插入,2--删除)
1
输入插入位置:
3
输入插入元素:
5
插入位置有误
是否要对线性表进行插入和删除?(Y/N)
Y
进行插入还是删除?(1--插入,2--删除)
1
输入插入位置:
2
输入插入元素:
5
线性表为(2,8,5)
是否要对线性表进行插入和删除?(Y/N)
Y
进行插入还是删除?(1--插入,2--删除)
2
输入删除位置:
2
线性表为(2,8)
是否要对线性表进行插入和删除?(Y/N)
Y
进行插入还是删除?(1--插入,2--删除)
2
输入删除位置:
8
删除位置有误
是否要对线性表进行插入和删除?(Y/N)
Y
进行插入还是删除?(1--插入,2--删除)
2
输入删除位置:
0
线性表为(8)
......
是否要对线性表进行插入和删除?(Y/N)
N
对线性表处理完毕
 */

import java.util.Scanner
class Problem5 {
  companion object{
    @JvmStatic
    fun main(args: Array<String>) {
      val input = """
        Y
        1
        0
        8
        Y
        1
        0
        2
        Y
        1
        3
        5
        Y
        1
        2
        5
        Y
        2
        2
        Y
        2
        8
        Y
        2
        0
        N
    """.trimIndent()

      val scanner = Scanner(input)
      val list = mutableListOf<Int>()

      while (true) {
        println("是否要对线性表进行插入和删除?(Y/N)")
        when (scanner.nextLine().uppercase()) {
          "Y" -> {
            println("进行插入还是删除?(1--插入,2--删除)")
            when (scanner.nextLine().toInt()) {
              1 -> {
                println("输入插入位置:")
                val pos = scanner.nextLine().toInt()
                println("输入插入元素:")
                val elem = scanner.nextLine().toInt()
                if (pos in 0..list.size) {
                  list.add(pos, elem)
                  println("线性表为(${list.joinToString(",")})")
                } else {
                  println("插入位置有误")
                }
              }
              2 -> {
                println("输入删除位置:")
                val pos = scanner.nextLine().toInt()
                if (pos in 0 until list.size) {
                  list.removeAt(pos)
                  println("线性表为(${list.joinToString(",")})")
                } else {
                  println("删除位置有误")
                }
              }
              else -> throw Error("输入错误")
            }
          }
          "N" -> {
            println("对线性表处理完毕")
            return
          }
          else -> throw Error("输入错误")
        }
      }
    }
  }
}
//Assisted by Deepseek.