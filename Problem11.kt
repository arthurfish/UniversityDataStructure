import java.util.*

class Problem11 {
  sealed interface BinaryTree
  data class TreeNode(val name: String, val left: BinaryTree, val right: BinaryTree) : BinaryTree{
    override fun toString(): String = "($name, $left, $right)"
  }
  data object EmptyTreeNode: BinaryTree{
    override fun toString(): String = "#"

  }
  fun buildBinaryTreeFromExtendedLayerOrder(sequence: Array<String>): BinaryTree {
    fun buildSubtree(index: Int): BinaryTree {
      if (index >= sequence.size || sequence[index] == "#") return EmptyTreeNode

      val leftIndex = 2 * index + 1
      val rightIndex = 2 * index + 2

      return TreeNode(
        name = sequence[index],
        left = buildSubtree(leftIndex),
        right = buildSubtree(rightIndex)
      )
    }
    return buildSubtree(0);
  }
}