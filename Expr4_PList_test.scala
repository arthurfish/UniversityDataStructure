//编写程序，实现顺序表、单链表、双链表、单循环链表和双循环链表的操作。
//（2）在（1）的基础上，编写程序，实现两个长整数的加、减和乘运算。
import collection.mutable.Stack
import org.scalatest._
import flatspec._
import matchers._
import PList_java_ver1.*

class PListTest extends AnyFlatSpec with should.Matchers {
  val intList = PList_java_ver1.createIntPList

  "A PList" should "display properly" in {
    val list = intList("1234")
    assert(list.toString === "1234")
  }

  it should "constructed correctly" in {
    assert(PList_java_ver1.createNumberPList[Int]("12345").toString === "12345")
  }

  it should "be reversed correctly" in {
    val list = intList("1234")
    val reversed = list.reversed()
    assert(reversed.toString === "4321")
  }

  it should "append another PList correctly" in {
    val list = intList("1234")
    val another = intList("789")
    assert(list.appended(another).toString === "1234789")
  }

  it should "foldLeft" in {
    val list = intList("1234")
    assert(list.foldLeft(0)((a, b) => a + b) === 10)
    val list2 = PList_java_ver1.fromSeq("what")
    assert(list2.foldLeft("")((a, acc) => s"$acc $a") === " w h a t")
  }

  it should "padding right" in {
    val list = intList("1234")
    val result = list.padding(0, 10)
    assert(result.toString === "1234000000")
  }

  it should "repead single value" in {
    val list = PList_java_ver1.fromRepeatedValue(0, 4)
    assert(list.toString === "0000")
  }

  it should "zip another list" in {
    val list1 = intList("1234")
    assert(list1.toString === "1234")
    val list2 = intList("6789")
    assert(list2.toString === "6789")
    val result = list1 zip list2
    assert(result.toString === "(1,6)(2,7)(3,8)(4,9)")
  }
}