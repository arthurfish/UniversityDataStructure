import java.lang.Integer.max
import java.util.*
import java.util.ArrayDeque

sealed interface BinaryTree<T>
data class NonEmpty<T>(val left: BinaryTree<T>, val value: T, val right: BinaryTree<T>): BinaryTree<T>
data object Empty: BinaryTree<Nothing>

object BinaryTreeOperations {
    fun <T> preorderTraverse(tree: BinaryTree<T>, fn: (T)->Unit): Unit {
        if (tree is NonEmpty) {
            fn(tree.value)
            preorderTraverse(tree.left, fn)
            preorderTraverse(tree.right, fn)
        }
    }

    fun <T> inorderTraverse(tree: BinaryTree<T>, fn: (T)->Unit): Unit {
        if (tree is NonEmpty) {
            inorderTraverse(tree.left, fn)
            fn(tree.value)
            inorderTraverse(tree.right, fn)
        }
    }

    fun <T> postorderTraverse(tree: BinaryTree<T>, fn: (T)->Unit): Unit {
        if (tree is NonEmpty) {
            postorderTraverse(tree.left, fn)
            postorderTraverse(tree.right, fn)
            fn(tree.value)
        }
    }

    fun <T> levelOrderTraverse(tree: BinaryTree<T>, fn: (T)->Unit): Unit {
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
                if (left is NonEmpty)
                    queue.add(left to level+1)
                if (right is NonEmpty)
                    queue.add(right to level+1)
            }
            return countPerLayer.values.maxOrNull() ?: 0
        }
        else return 0
    }

    fun <T> calculateNodeDegree(tree: BinaryTree<T>): Int {

    }
}


