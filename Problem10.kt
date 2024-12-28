//2, 3
class Problem10 {
  companion object{
    fun palindromeNumberPredicate(inputString: String): Boolean {
      val letters = inputString.filter { it.isLetter() }.lowercase()
      return letters.zip(letters.reversed()).all{it.first == it.second}
    }

    fun getCommonSuffixStartPos(word1: String, word2: String): Int =
      word1.lowercase().reversed().zip(word2.lowercase().reversed()).count { it.first == it.second }

    sealed interface Tree
    object Empty : Tree
    data class NonEmpty(val leftTree: Tree, val weight: Int = 0, val rightTree: Tree) : Tree
    fun calculateWeightedPathLength(tree: Tree): Int {
      fun calculateWplRec(tree: Tree, depth: Int): Int = when(tree){
        is Empty -> 0
        is NonEmpty ->
          when {
            tree.leftTree == Empty && tree.rightTree == Empty -> depth * tree.weight
            else -> calculateWplRec(tree.leftTree, depth + 1) + calculateWplRec(tree.rightTree, depth + 1)
          }
      }
      return calculateWplRec(tree, 0)
    }

    @JvmStatic
    fun main(args: Array<String>) {
      println(palindromeNumberPredicate("Was it a car or a cat I saw?"))
      println(getCommonSuffixStartPos("loading", "being"))
      val tree = NonEmpty(NonEmpty(NonEmpty(Empty, 3, Empty), 2, Empty), 1, NonEmpty(Empty, 4, Empty))
      println(calculateWeightedPathLength(tree))
    }
  }
}