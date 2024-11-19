//编写程序，实现顺序表、单链表、双链表、单循环链表和双循环链表的操作。
//（2）在（1）的基础上，编写程序，实现两个长整数的加、减和乘运算。


import PList.*

import scala.annotation.tailrec

object PList{
  def fromSeq[A](seq: Seq[A]): PList_java_ver1[A] = {
    @tailrec
    def fromRec(seq: Seq[A], accList: PList_java_ver1[A]): PList_java_ver1[A] = seq match{
      case Seq() => accList
      case head +: tail => fromRec(tail, Cons(head, accList))
    }
    fromRec(seq, Nil).reversed()
  }

  def fromRepeatedValue[A, B](value: A, count: B)(using num: Numeric[B]): PList_java_ver1[A] = {
    require(num.gteq(count, num.zero))
    @tailrec
    def fromRepeatedValueRec(count: B, accList: PList_java_ver1[A]): PList_java_ver1[A] = {
      if num.equiv(count, num.zero) then accList
      else fromRepeatedValueRec(num.minus(count, num.one), Cons(value, accList))
    }
    fromRepeatedValueRec(count, Nil)
  }

  def createNumberPList[A: Numeric](s: String): PList_java_ver1[A] = {
    @tailrec
    def createNumberPListRec(s: Seq[Char], accList: PList_java_ver1[A]): PList_java_ver1[A] = s match{
      case Seq() => accList
      case head +: tail => createNumberPListRec(tail, Cons((head.toInt - '0'.toInt).asInstanceOf[A], accList))
    }
    createNumberPListRec(s.toSeq, Nil).reversed()
  }

  def createIntPList(s: String): PList_java_ver1[Int] = createNumberPList[Int](s)
}

enum PList[+A] {
  case Nil;
  case Cons(head: A, tail: PList_java_ver1[A]);

  override def toString: String = {
    @tailrec
    def toStringRec(list: PList_java_ver1[A], acc: String): String = list match{
      case Nil => acc
      case Cons(head, tail) => toStringRec(tail, acc + head.toString)
    }
    toStringRec(this, "")
  }

  def reversed(): PList_java_ver1[A] = foldLeft(Nil)((elem, acc) => Cons(elem, acc))
  def getLength(): Int = foldLeft(0)((_, len) => len+1)

  def appended[B >: A](that: PList_java_ver1[B]): PList_java_ver1[B] = {
    @tailrec
    def appendRec(list1: PList_java_ver1[B], list2: PList_java_ver1[B], accList: PList_java_ver1[B]): PList_java_ver1[B] = {
      (list1, list2) match
        case (Nil, Nil) => accList
        case (Nil, Cons(list2Head, list2Tail)) => appendRec(Nil, list2Tail, Cons(list2Head, accList))
        case (Cons(list1Head, list1Tail), Nil) => appendRec(list1Tail, Nil, Cons(list1Head, accList))
        case (Cons(head1, tail1), Cons(head2, tail2)) => appendRec(tail1, Cons(head2, tail2), Cons(head1, accList))
    }
    appendRec(this, that, Nil).reversed()
  }

  def foldLeft[B](zeroElement: B)(reducer: (A, B) => B): B = {
    @tailrec
    def foldLeftRec(currNode: PList_java_ver1[A], reducer: (A, B) => B, acc: B): B = {
      currNode match
        case Nil => acc
        case Cons(head, tail) => foldLeftRec(tail, reducer, reducer(head, acc))
    }
    foldLeftRec(this, reducer, zeroElement)
  }

  infix def map[B](mapper: A => B): PList_java_ver1[B] = {
    @tailrec
    def mapRec(list: PList_java_ver1[A], accList: PList_java_ver1[B]): PList_java_ver1[B] = list match{
      case Nil => accList
      case Cons(head, tail) => mapRec(tail, Cons(mapper(head), accList))
    }
    mapRec(this, Nil).reversed()
  }

  infix def zip[B](that: PList_java_ver1[B]): PList_java_ver1[(A, B)] = {
    require(this.getLength() == that.getLength())
    @tailrec
    def zipRec(list1: PList_java_ver1[A], list2: PList_java_ver1[B], accList: PList_java_ver1[(A, B)]): PList_java_ver1[(A, B)] = (list1, list2) match {
      case (Nil, Nil) => accList
      case (Cons(h1, t1), Cons(h2, t2)) => zipRec(t1, t2, Cons((h1, h2), accList))
      case _ => throw Error("Length not equal!")
    }
    zipRec(this, that, Nil).reversed()
  }

  def padding[B >: A](value: B, targetLength: Int): PList_java_ver1[B] = {
    val extraLength = targetLength - this.getLength()
    @tailrec
    def paddingTailRec(list: PList_java_ver1[B], targetLength: Int, accList: PList_java_ver1[B]): PList_java_ver1[B] = list match{
      case Cons(head, tail) => paddingTailRec(tail, targetLength - 1, Cons(head, accList))
      case Nil => PList.fromRepeatedValue(value, extraLength).appended(accList)
    }
    paddingTailRec(this, targetLength, Nil).reversed()
  }

}




