import java.lang.Integer.max
import java.util.*

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

    /**
     * @return Triple(DegreeZero, DegreeOne, DegreeTwo)
     */
    fun <T> calculateNodeDegree(tree: BinaryTree<T>): Triple<Int, Int, Int> {
        if (tree is NonEmpty) {
            var condCount = 0
            if (tree.left is NonEmpty)
                condCount++
            if (tree.right is NonEmpty)
                condCount++
            val v0 = when(condCount) {
                0 -> Triple(1, 0, 0)
                1 -> Triple(0, 1, 0)
                2 -> Triple(0, 0, 1)
                else -> throw AssertionError()
            }
            val v1 = calculateNodeDegree(tree.left)
            val v2 = calculateNodeDegree(tree.right)
            val result = Triple(v0.first + v1.first + v2.first,
                v0.second + v1.second + v2.second,
                v0.third + v1.third + v2.third)
            return result
        }else return Triple(0, 0, 0)
    }
}


