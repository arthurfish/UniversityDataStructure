import scala.annotation.tailrec
import scala.reflect.ClassTag

object Expr4 {
  class SequentialList[A: ClassTag](length: Int, zeroElement: A) {
    private val array: Array[A] = Array.fill[A](length)(zeroElement)

    private def isLegal(addr: Int): Boolean = {
      if 0 <= addr && addr < length then true else false
    }

    def randomAccessOption(addr: Int): Option[A] = {
      if !isLegal(addr) then None
      else Some(array(addr))
    }

    def randomUpdate(addr: Int, value: A): Boolean = {
      if !isLegal(addr) then return false
      array(addr) = value
      true
    }

    def remove(addr: Int): Boolean = {
      def moveNextToCurr(curr: Int): Unit = {
        array(curr) = array(curr + 1)
      }
      if !isLegal(addr) then return false
      for i <- addr to length - 2 do
        moveNextToCurr(i)
      array(length - 1) = zeroElement
      true
    }
  }

  enum LinkedList[A] {
    case Empty extends LinkedList
    case NonEmpty(value: A, next: LinkedList[A]) extends LinkedList[A]

    def headOption(): Option[A] = this match {
      case Empty => None;
      case NonEmpty(a, _) => Some(a)
    }

    def tailOption(): Option[LinkedList[A]] = this match {
      case Empty => None
      case NonEmpty(_, next) => Some(next)
    }

    def prepended(another: LinkedList[A]): LinkedList[A] = (another, this) match {
      case (Empty, _) => this
      case (_, Empty) => another
      case (NonEmpty(anotherValue, anotherNext), _) => NonEmpty(anotherValue, this)
    }

    def foldLeft[B](zeroElement: B)(reduceFn: (B, A) => B): B = {
      @tailrec
      def traverseAndAccumulate(node: LinkedList[A], acc: B): B = node match{
        case Empty => acc
        case NonEmpty(value, next) => traverseAndAccumulate(next, reduceFn(acc, value))
      }
      traverseAndAccumulate(this, zeroElement)
    }
  }

  class DoublyLinkedList[A]() {
    //Its operation is not idempotent, referential transparency.
    sealed trait Node;
    object Empty extends Node
    case class NonEmpty(var left: Node, var value: A, var right: Node) extends Node

    sealed trait Direction
    object ToRight extends Direction
    object ToLeft extends Direction

    var head: Node = Empty;
    var last: Node = Empty;
    var length: Int = 0

    def nextNodeByDirection(node: Node, direction: Direction): Node =
      require(node != Empty, "Require not satisfied!")
      val nonEmptyNode = node.asInstanceOf[NonEmpty]
      direction match {
        case ToRight => nonEmptyNode.right
        case ToLeft => nonEmptyNode.left
      }

    def isEmpty(): Boolean = (head, last) match {
      case (Empty, Empty) => true
      case (NonEmpty, NonEmpty) => false
      case _ => throw new VerifyError("Specific violated!")
    }

    def prepend(value: A): this.type =
      this.length += 1
      head match {
        case Empty =>
          head = NonEmpty(Empty, value, Empty)
          last = head
        case NonEmpty(_, _, _) =>
          var prevHead = head.asInstanceOf[NonEmpty]
          val newHeadElem = NonEmpty(Empty, value, prevHead)
          prevHead.left = newHeadElem
          this.head = newHeadElem
      }
      this

    def append(value: A): this.type = {
      this.length += 1
      last match {
        case Empty => prepend(value)
        case NonEmpty(_, _, _) =>
          var prevLast = last.asInstanceOf[NonEmpty]
          val newLastElem = NonEmpty(prevLast, value, Empty)
          prevLast.right = newLastElem
          this.last = newLastElem
      }
      this
    }

    def randomAccess(index: Int, direction: Direction = ToRight): NonEmpty = {
      require(0 <= index && index < length, s"index $index is out of bounds")
      require(!isEmpty(), s"index $index is out of bounds of empty list")
      val source = direction match
        case ToRight => head.asInstanceOf[NonEmpty]
        case ToLeft => last.asInstanceOf[NonEmpty]
      var currentNode: NonEmpty = source
      var currentIndex = 0
      @tailrec
      def iter(node: Node, currIndex: Int): NonEmpty = {
        require(node != Empty && currIndex < length, "Assumption failed in iter/getByIndex")
        if currIndex == index then return node.asInstanceOf[NonEmpty]
        else return iter(nextNodeByDirection(node, direction), currIndex + 1)
      }
      return iter(source, 0)
    }

    def insert(offset: Int, value: A): this.type = {
      length += 1
      if offset == 0 then prepend(value)
      else if offset == length then append(value)
      else
        var elementBeforeInsert = randomAccess(offset, ToRight)
        var elementAfterInsert = elementBeforeInsert.right.asInstanceOf[NonEmpty]
        val newElement = NonEmpty(elementBeforeInsert, value, elementAfterInsert)
        elementBeforeInsert.right = newElement
        elementAfterInsert.left = newElement
      this
    }

    def popLeft(): A = {
      require(head != Empty)
      length -= 1
      val oldHeadValue = head.asInstanceOf[NonEmpty].value
      var newHead = head.asInstanceOf[NonEmpty].right
      newHead.asInstanceOf[NonEmpty].left = Empty
      head = newHead
      return oldHeadValue
    }

    def popRight(): A = {
      require(last != Empty)
      length -= 1
      val oldLastValue = last.asInstanceOf[NonEmpty].value
      var newLast = last.asInstanceOf[NonEmpty].left
      newLast.asInstanceOf[NonEmpty].right = Empty
      last = newLast
      oldLastValue
    }

    def removeInnerItemByIndex(index: Int): Unit = {
      require(0 < index && index < length - 1)
      length -= 1
      var targetItem = randomAccess(index)
      var leftNode = targetItem.left.asInstanceOf[NonEmpty]
      var rightNode = targetItem.right.asInstanceOf[NonEmpty]
      leftNode.right = rightNode
      rightNode.left = leftNode
      targetItem.left = null
      targetItem.right = null
    }

    def remove(index: Int): Unit = {
      require(0 <= index && index < length)
      if index == 0 then popLeft()
      else if index == length - 1 then popRight()
      else removeInnerItemByIndex(index)
    }
  }



}
