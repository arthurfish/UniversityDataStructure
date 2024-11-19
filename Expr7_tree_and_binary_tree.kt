/*
实验7 二叉树和二叉查找树

（1）二叉树的结点为单个字符。二叉树用链式存储结构实现。编写菜单驱动的二叉树演示程序，用户能演示的二叉树操作至少包括：构造二叉树，先序、中序、后序和层序遍历二叉树，求二叉树的高度和宽度（各层结点个数的最大值），以及统计度为0，1，2的结点个数等。
（2）对二叉查找树作上述同样处理，并增加以下操作：查找给定值的结点，以及插入和删除给定值的结点。
 */

import java.lang.Integer.max
import java.util.*

sealed interface BinaryTree<out T>
class NonEmpty<T>(val left: BinaryTree<T>, val value: T, val right: BinaryTree<T>) : BinaryTree<T> {
    override fun toString(): String = "($left $value $right)"
}
data object Empty : BinaryTree<Nothing> {
    override fun toString() = "."
}

object BinaryTreeOperations {
    fun <T> preorderTraverse(tree: BinaryTree<T>, fn: (T) -> Unit): Unit {
        if (tree is NonEmpty) {
            fn(tree.value)
            preorderTraverse(tree.left, fn)
            preorderTraverse(tree.right, fn)
        }
    }

    fun <T> inorderTraverse(tree: BinaryTree<T>, fn: (T) -> Unit): Unit {
        if (tree is NonEmpty) {
            inorderTraverse(tree.left, fn)
            fn(tree.value)
            inorderTraverse(tree.right, fn)
        }
    }

    fun <T> postorderTraverse(tree: BinaryTree<T>, fn: (T) -> Unit): Unit {
        if (tree is NonEmpty) {
            postorderTraverse(tree.left, fn)
            postorderTraverse(tree.right, fn)
            fn(tree.value)
        }
    }

    fun <T> levelOrderTraverse(tree: BinaryTree<T>, fn: (T) -> Unit): Unit {
        if (tree is NonEmpty) {
            val queue = LinkedList<BinaryTree<T>>()
            queue.add(tree)
            while (queue.isNotEmpty()) {
                val current = queue.removeFirst()
                if (current is NonEmpty) {
                    fn(current.value)
                    queue.add(current.left)
                    queue.add(current.right)
                }
            }
        }
    }

    fun <T> calculateHeight(tree: BinaryTree<T>): Int = when (tree) {
        is Empty -> 0
        is NonEmpty -> max(calculateHeight(tree.left), calculateHeight(tree.right)) + 1
    }

    fun <T> calculateWidth(tree: BinaryTree<T>): Int {
        if (tree is NonEmpty) {
            val queue: Queue<Pair<NonEmpty<T>, Int>> = LinkedList()
            val countPerLayer: MutableMap<Int, Int> = mutableMapOf<Int, Int>()
            queue.add(Pair(tree, 0));
            while (queue.isNotEmpty()) {
                val (currentTree, level) = queue.poll()
                countPerLayer[level] = countPerLayer.getOrDefault(level, 0) + 1
                val (left, right) = currentTree.left to currentTree.right
                if (left is NonEmpty) queue.add(left to level + 1)
                if (right is NonEmpty) queue.add(right to level + 1)
            }
            return countPerLayer.values.maxOrNull() ?: 0
        } else return 0
    }

    /**
     * @return Triple(DegreeZero, DegreeOne, DegreeTwo)
     */
    fun <T> calculateNodeDegreeOld(tree: BinaryTree<T>): Triple<Int, Int, Int> {
        if (tree is NonEmpty) {
            var condCount = 0
            if (tree.left is NonEmpty) condCount++
            if (tree.right is NonEmpty) condCount++
            val v0 = when (condCount) {
                0 -> Triple(1, 0, 0)
                1 -> Triple(0, 1, 0)
                2 -> Triple(0, 0, 1)
                else -> throw AssertionError()
            }
            val v1 = calculateNodeDegreeOld(tree.left)
            val v2 = calculateNodeDegreeOld(tree.right)
            val result = Triple(
                v0.first + v1.first + v2.first, v0.second + v1.second + v2.second, v0.third + v1.third + v2.third
            )
            return result
        } else return Triple(0, 0, 0)
    }

    fun <T> calculateNodeDegree(tree: BinaryTree<T>): Array<Int> = when (tree) {
        is Empty -> arrayOf(0, 0, 0)
        is NonEmpty -> {
            var condCount = 0
            if (tree.left is NonEmpty) condCount++
            if (tree.right is NonEmpty) condCount++
            val v0 = Array<Int>(3, { 0 })
            v0[condCount] = 1
            val v1 = calculateNodeDegree(tree.left)
            val v2 = calculateNodeDegree(tree.right)
            arrayOf(v0[0] + v1[0] + v2[0], v0[1] + v1[1] + v2[1], v0[2] + v1[2] + v2[2])
        }
    }

    tailrec fun <T : Comparable<T>> bstSearch(tree: BinaryTree<T>, value: T): BinaryTree<T>? = when (tree) {
        is Empty -> null
        is NonEmpty -> when {
            tree.value == value -> tree
            value < tree.value -> bstSearch(tree.left, value)
            value > tree.value -> bstSearch(tree.right, value)
            else -> throw AssertionError()
        }
    }

    fun <T : Comparable<T>> bstInsert(tree: BinaryTree<T>, value: T): BinaryTree<T> = when (tree) {
        is Empty -> NonEmpty(Empty, value, Empty)
        is NonEmpty -> when {
            value < tree.value -> NonEmpty(bstInsert(tree.left, value), tree.value, tree.right)
            value == tree.value -> tree
            value > tree.value -> NonEmpty(tree.left, tree.value, bstInsert(tree.right, value))
            else -> throw AssertionError()
        }
    }

    private fun <T> removeLeftMostNode(tree: BinaryTree<T>): Pair<BinaryTree<T>, T> = when (tree) {
        is NonEmpty -> with(tree) {
            when (left) {
                is Empty -> Pair(right, value)
                is NonEmpty -> {
                    val (newLeft, leftMostValue) = removeLeftMostNode(left)
                    Pair(NonEmpty(newLeft, value, right), leftMostValue)
                }
            }
        }

        is Empty -> throw AssertionError()
    }

    fun <T : Comparable<T>> bstRemove(tree: BinaryTree<T>, valueToFind: T): BinaryTree<T> = when (tree) {
        is Empty -> Empty
        is NonEmpty -> with(tree) {
            when {
                valueToFind < value -> NonEmpty(bstRemove(left, valueToFind), value, right)
                valueToFind > value -> NonEmpty(left, value, bstRemove(right, valueToFind))
                valueToFind == value -> when (listOf(left, right).filterIsInstance<NonEmpty<T>>().count()) {
                    0 -> Empty
                    1 -> if (left is NonEmpty) left else right
                    2 -> {
                        val (newRight, leftMostValue) = removeLeftMostNode(right)
                        NonEmpty(left, leftMostValue, newRight)
                    }
                    else -> throw AssertionError()
                }
                else -> throw AssertionError()
            }
        }
    }

    fun test(){
        var binarySearchTree: BinaryTree<Int> = NonEmpty(Empty, 3, Empty)
        for(i in arrayOf(4, 2, 1, 5)){
            binarySearchTree = bstInsert(binarySearchTree, i)
        }
        var preorderResult = mutableListOf<Int>()
        var inorderResult = mutableListOf<Int>()
        var postorderResult = mutableListOf<Int>()
        var levelOrderResult = mutableListOf<Int>()
        preorderTraverse(binarySearchTree) { preorderResult.add(it) }
        inorderTraverse(binarySearchTree) { inorderResult.add(it) }
        postorderTraverse(binarySearchTree) { postorderResult.add(it) }
        levelOrderTraverse(binarySearchTree) { levelOrderResult.add(it) }
        println("""
            The Binary Search Tree: ${binarySearchTree}
            Traverse it in preorder: ${preorderResult.joinToString()}
            Traverse it in inorder: ${inorderResult.joinToString()}
            Traverse it in postorder: ${postorderResult.joinToString()}
            Traverse it in level order: ${levelOrderResult.joinToString()}
            Its height: ${calculateHeight(binarySearchTree)}
            Its width: ${calculateWidth(binarySearchTree)}
            Its degree statistics (ZeroDegree, OneDegree, TwoDegree): (${calculateNodeDegree(binarySearchTree).joinToString()})
            When one of its elements [2] is removed: ${bstRemove(binarySearchTree, 2)}
            When one of its elements [5] found: ${bstSearch(binarySearchTree, 5) ?: "None"}
            
        """.trimIndent())
    }
}

fun main(args: Array<String>) {
    BinaryTreeOperations.test()
}


