/*
//编写程序，实现顺序表、单链表、双链表、单循环链表和双循环链表的操作。
//（2）在（1）的基础上，编写程序，实现两个长整数的加、减和乘运算。
 */
import scala.annotation.tailrec
import scala.reflect.ClassTag

object Expr4_Lists {
  object Lists {
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
        def traverseAndAccumulate(node: LinkedList[A], acc: B): B = node match {
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

      def insert(index: Int, value: A): this.type = {
        length += 1
        if index == 0 then prepend(value)
        else if index == length then append(value)
        else
          var elementBeforeInsert = randomAccess(index, ToRight)
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

      def removeInnerElemByIndex(index: Int): Unit = {
        require(0 < index && index < length - 1)
        length -= 1
        var targetElem = randomAccess(index)
        var leftNode = targetElem.left.asInstanceOf[NonEmpty]
        var rightNode = targetElem.right.asInstanceOf[NonEmpty]
        leftNode.right = rightNode
        rightNode.left = leftNode
        targetElem.left = null
        targetElem.right = null
      }

      def remove(index: Int): Unit = {
        require(0 <= index && index < length)
        if index == 0 then popLeft()
        else if index == length - 1 then popRight()
        else removeInnerElemByIndex(index)
      }
    }

    class CircularSinglyLinkedList[A] {
      case class Node(var value: A | Null, var next: Node);
      var length: Int = 0
      //Make sure there at least one element in the list
      //Index can be any Integer including negatives
      var head: Node = Node(null, head)


      def getElemByIndex(index: Int): Node = {
        val clippedIndex = (index % length + length) % length

        @tailrec
        def traverse(currNode: Node, currIndex: Int, requiredIndex: Int): Node = {
          require(0 <= currIndex && currIndex < length)
          if currIndex == requiredIndex then currNode
          else traverse(currNode.next, currIndex + 1, requiredIndex)
        }

        traverse(this.head, 0, clippedIndex)
      }

      def randomAccess(index: Int): A = {
        require(this.length > 0)
        getElemByIndex(index).value.asInstanceOf[A]
      }

      def randomUpdate(index: Int, newValue: A): Unit = {
        require(this.length > 0)
        getElemByIndex(index).value = newValue
      }


      def remove(index: Int): Unit = {
        require(this.length != 0)

        def removeOnlyElem(): Unit = {
          require(this.length == 1)
          this.length = 0
          this.head = Node(null, this.head)
        }

        if this.length == 1 then removeOnlyElem()
        var beforeTarget = getElemByIndex(index - 1)
        var target = beforeTarget.next
        val afterTarget = target.next
        beforeTarget.next = afterTarget
        target.next = null
        this.length -= 1
      }

      def insert(index: Int, value: A): Unit = {
        def insertToEmptyList(value: A): Unit = {
          require(this.length == 0 && this.head.value == null)
          this.length = 1
          this.head.value = value
        }

        def insertToOneElementList(value: A): Unit = {
          require(this.length == 1 && this.head.value != null)
          val newElem = Node(value, this.head)
          this.head.next = newElem
          this.length = 2
        }

        def insertToMoreThanOneElementList(value: A, index: Int): Unit = {
          var target = getElemByIndex(index)
          val newElem = Node(value, target.next)
          target.next = newElem
          this.length += 1
        }
      }
    }

    class CircularDoublyLinkedList[A] {
      // Assumptions:
      // There must be at least one element in List.
      // Insert should insert after the index arguments
      // Index can go beyond the boundary, can be negative
      private case class Node(var left: Node, var value: A | Null, var right: Node)

      private var length: Int = 0;
      private var head: Node = Node(head, null, head)

      private def empty(): Boolean = {
        this.length != 0 && this.head.value != null
      }

      private def indexInsideBound(index: Int) = {
        0 <= index && index < this.length
      }

      private def clipIndex(index: Int, length: Int): Int = {
        ((index % length) + length) % length
      }

      private def getElementByIndex(index: Int): Node = {
        require(this.length != 0)
        val clippedIndex = clipIndex(index, this.length)

        @tailrec
        def traverse(node: Node, currIndex: Int, requiredIndex: Int): Node = {
          require(currIndex <= requiredIndex)
          if currIndex == requiredIndex then node
          else traverse(node.right, currIndex + 1, requiredIndex)
        }

        traverse(this.head, 0, clippedIndex)
      }

      def insert(index: Int, value: A): Unit = {
        def insertToEmptyList(value: A): Unit = {
          require(this.length == 0 && this.head.value == null)
          this.head.value = value
          this.length = 1
        }

        def insertToOneElemList(value: A): Unit = {
          val newElem = Node(head, value, head)
          this.head.left = newElem
          this.head.right = newElem
          this.length = 2
        }

        def insertToMoreThanOneElemList(index: Int, value: A): Unit = {
          require(this.length > 1)
          var target = getElementByIndex(index)
          var next = target.right
          val newElem = Node(target, value, next)
          target.right = newElem
          next.left = newElem
          this.length += 1
        }

        if this.length == 0 then insertToEmptyList(value)
        else if this.length == 1 then insertToOneElemList(value)
        else insertToMoreThanOneElemList(index, value)
      }

      def remove(index: Int): Unit = {
        require(!empty() && indexInsideBound(index))

        def removeInOneElemList(): Unit = {
          this.length = 0
          this.head.value = null
        }

        def removeInMoreThanOneElemList(index: Int): Unit = {
          //It looks like two-elem-list situation satisfies this function
          var middleOne = getElementByIndex(index)
          var leftOne = middleOne.left
          var rightOne = middleOne.right
          leftOne.right = rightOne
          rightOne.left = leftOne
          middleOne.left = null
          middleOne.right = null
        }

        if this.length == 0 then removeInOneElemList()
        else removeInMoreThanOneElemList(index)
      }


      def randomAccess(index: Int): A = {
        require(!empty() && indexInsideBound(index))
        getElementByIndex(index).value.asInstanceOf[A]
      }

      def randomUpdate(index: Int, value: A): Unit = {
        require(!empty() && indexInsideBound(index))
        getElementByIndex(index).value = value
      }
    }
  }
}
