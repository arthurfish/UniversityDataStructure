import java.util.*
import kotlin.math.abs

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
  fun preorderTraverse(root: BinaryTree, nonEmptyHandler: (String) -> Unit, emptyHandler: () -> Unit): Unit = when(root){
    is EmptyTreeNode -> emptyHandler()
    is TreeNode -> {
      nonEmptyHandler(root.name)
      preorderTraverse(root.left, nonEmptyHandler, emptyHandler)
      preorderTraverse(root.right, nonEmptyHandler, emptyHandler)
    }
  }
  fun inorderTraverse(root: BinaryTree, nonEmptyHandler: (String) -> Unit, emptyHandler: () -> Unit): Unit = when(root){
    is EmptyTreeNode -> emptyHandler()
    is TreeNode -> {
      inorderTraverse(root.left, nonEmptyHandler, emptyHandler)
      nonEmptyHandler(root.name)
      inorderTraverse(root.right, nonEmptyHandler, emptyHandler)
    }
  }
  fun postOrderTraverse(root: BinaryTree, nonEmptyHandler: (String) -> Unit, emptyHandler: () -> Unit): Unit = when(root){
    is EmptyTreeNode -> emptyHandler()
    is TreeNode -> {
      postOrderTraverse(root.left, nonEmptyHandler, emptyHandler)
      postOrderTraverse(root.right, nonEmptyHandler, emptyHandler)
      nonEmptyHandler(root.name)
    }
  }
  fun swapLeftAndRight(root: BinaryTree): BinaryTree = when(root){
    is EmptyTreeNode -> root
    is TreeNode -> TreeNode(root.name, swapLeftAndRight(root.right), swapLeftAndRight(root.left))
  }
  fun removeValueWithSameAbsolute(sequence: List<Int>): List<Int>{
    val set = mutableSetOf<Int>()
    val outputList = mutableListOf<Int>()
    for(element in sequence){
      if(!set.contains(abs(element))){
        outputList.add(element)
        set.add(abs(element))
      }
    }
    return outputList
  }
  fun flatExpressionTree(root: BinaryTree): String{
    val stringBuilder = StringBuilder()
    inorderTraverse(root, {s -> stringBuilder.append(s)}, {})
    return stringBuilder.toString()
  }

  fun test(){
    fun testBuildAndTraverse() {
      // Test case 1: Basic binary tree
      val sequence1 = arrayOf("A", "B", "C", "D", "E", "#", "F")
      val tree1 = buildBinaryTreeFromExtendedLayerOrder(sequence1)

      val preorder = mutableListOf<String>()
      preorderTraverse(tree1, { preorder.add(it) }, {})
      assert(preorder.joinToString("") == "ABDECF") { "Preorder traversal failed" }

      val inorder = mutableListOf<String>()
      inorderTraverse(tree1, { inorder.add(it) }, {})
      assert(inorder.joinToString("") == "DBEACF") { "Inorder traversal failed" }

      val postorder = mutableListOf<String>()
      postOrderTraverse(tree1, { postorder.add(it) }, {})
      assert(postorder.joinToString("") == "DEBFCA") { "Postorder traversal failed" }

      println("Test case 1 passed")
    }

    fun testSwapLeftAndRight() {
      // Test case 2: Swap left and right subtrees
      val sequence2 = arrayOf("A", "B", "C", "D", "E", "#", "F")
      val tree2 = buildBinaryTreeFromExtendedLayerOrder(sequence2)
      val swappedTree = swapLeftAndRight(tree2)

      val inorderOriginal = mutableListOf<String>()
      val inorderSwapped = mutableListOf<String>()

      inorderTraverse(tree2, { inorderOriginal.add(it) }, {})
      inorderTraverse(swappedTree, { inorderSwapped.add(it) }, {})

      assert(inorderOriginal.joinToString("") == "DBEACF") { "Original inorder traversal failed" }
      assert(inorderSwapped.joinToString("") == "FCAEBD") { "Swapped inorder traversal failed" }

      println("Test case 2 passed")
    }

    fun testRemoveValueWithSameAbsolute() {
      // Test case 3: Remove absolute value duplicates
      val sequence3 = listOf(21, -15, 15, -7, 15)
      val result = removeValueWithSameAbsolute(sequence3)

      assert(result == listOf(21, -15, -7)) {
        "Remove absolute value failed. Expected [21, -15, -7], got $result"
      }

      println("Test case 3 passed")
    }

    fun testFlatExpressionTree() {
      // Test case 4: Expression tree flattening
      val expressionTree = Problem11.TreeNode(
        "*",
        Problem11.TreeNode("+",
          Problem11.TreeNode("a", Problem11.EmptyTreeNode, Problem11.EmptyTreeNode),
          Problem11.TreeNode("b", Problem11.EmptyTreeNode, Problem11.EmptyTreeNode)
        ),
        Problem11.TreeNode("*",
          Problem11.TreeNode("c", Problem11.EmptyTreeNode, Problem11.EmptyTreeNode),
          Problem11.TreeNode("-",
            Problem11.EmptyTreeNode,
            Problem11.TreeNode("d", Problem11.EmptyTreeNode, Problem11.EmptyTreeNode)
          )
        )
      )

      val result = flatExpressionTree(expressionTree)
      assert(result == "a+b*c*-d") {
        "Expression tree flattening failed. Expected 'a+b*c-d', got '$result'"
      }

      println("Test case 4 passed")
    }

    fun runAllTests() {
      testBuildAndTraverse()
      testSwapLeftAndRight()
      testRemoveValueWithSameAbsolute()
      testFlatExpressionTree()
      println("All tests passed successfully!")
    }
    runAllTests()
  }

  companion object{
    @JvmStatic
    fun main(args: Array<String>) {
      Problem11().test()
    }
  }
}