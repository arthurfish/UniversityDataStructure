import java.io.OutputStream
import java.util.Scanner

/*
上机题:
☆1. 要求用带头结点的单链表(引入存储线性表长度的整型变量 curLen)实现线性表。对
线性表进行若干次插入和删除,对每一次插入或删除,若成功,则输出线性表,否则输出出
n错信息。
执行示例: (加下划线部分为输入内容)
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
☆2. 有两个元素都是整数的非递减有序表 A 和 B。求由 A 和 B 的所有元素组成的线性表 C
的第 k 小元素。说明:数据自定。
☆3. 有一个未知长度的线性表。该线性表用带头结点的单链表实现。取该线性表的倒数第
k 个元素。说明:数据自定。
 */
//2

class Problem6 {
  fun interactiveListDemonstration(scanner: Scanner, outputStream: OutputStream): Unit {
    data class Node(val value: Int, val next: Node? = null){
      override fun toString(): String = if(next == null) "$value" else "$value, $next"
      fun inserted(index: Int, insertValue: Int): Node = if (index == 0) Node(insertValue, this) else Node(value, this.next?.inserted(index - 1, value))
      fun removed(index: Int): Node? = if(index == 0) this.next else Node(this.value, this.next?.removed(index - 1))
    }
    var list = Node(2333)
    fun interactiveListInsert(list: Node): Node {
      outputStream.write("输入插入位置:\n".toByteArray())
      val insertPos = try {
        scanner.next().toInt()
      }catch(e: NumberFormatException){
        e.printStackTrace()
        return list
      }
      outputStream.write("输入插入元素:\n".toByteArray())
      val insertElem = try{
        scanner.next().toInt()
      }catch (e: NumberFormatException){
        outputStream.write("You should enter an integer.".toByteArray())
        return list
      }
      val result = list.inserted(insertPos, insertElem)
      outputStream.write(("Insert Success. List: ${list}\n").toByteArray())
      return result ?: list
    }

    fun interactiveListRemove(list: Node): Node {
      outputStream.write("输入删除位置:\n".toByteArray())
      val removePos = try{
        scanner.nextLine().toInt()
      }catch (e: NumberFormatException){
        outputStream.write("You should enter an integer.".toByteArray())
        return list
      }
      val result = list.removed(removePos)
      outputStream.write((if (result == null) "Remove out of the index" else "Insert Success. List: ${list}").toByteArray())
      return result ?: list
    }
    fun listCorrectnessTest(){
      val list = Node(1, Node(2, Node(3, Node(4, Node(5, Node(6, Node(7, Node(8, Node(9)))))))))
      println("insert 1 at pos 1: ${list.inserted(1, 1)}")
      println("insert 2 at pos 2: ${list.inserted(2, 2)}")
      println("remove elems at 3: ${list.removed(3)}")
    }
    //listCorrectnessTest()
    //return

    while(true){
      outputStream.write("是否要对线性表进行插入和删除?(Y/N)\n".toByteArray())
      val yesOrNo = scanner.next()
      if (yesOrNo != "Y") return
      outputStream.write("进行插入还是删除?(1--插入,2--删除)\n".toByteArray())
      val insertOrRemove = scanner.next()
      if (insertOrRemove == "1"){
        list = interactiveListInsert(list)
      }else if (insertOrRemove == "2"){
        list = interactiveListRemove(list)
      }else{
        return
      }
    }
  }

  fun mergeSortedList(sortedList1: List<Int>, sortedList2: List<Int>): List<Int>{
    fun mergeTailrec(list1: List<Int>, list2: List<Int>, resultList: List<Int>): List<Int> = when{
      list1.isEmpty() && list2.isEmpty() -> resultList
      list1.isEmpty() -> resultList.plus(list2)
      list2.isEmpty() -> resultList.plus(list1)
      else -> {
        if(list1.first() <= list2.first()){
          mergeTailrec(list1.drop(1), list2, resultList.plus(list1.first()))
        }else{
          mergeTailrec(list1, list2.drop(1), resultList.plus(list2.first()))
        }
      }
    }
    return mergeTailrec(sortedList1, sortedList2, listOf())
  }

  companion object{
    @JvmStatic
    fun main(args: Array<String>){
      Problem6().interactiveListDemonstration(scanner = Scanner("Y\n1\n0\n2333\nN"), outputStream = System.out)
      println(Problem6().mergeSortedList(listOf(1, 3, 5, 7), listOf(2, 4, 6)))
    }
  }
}

