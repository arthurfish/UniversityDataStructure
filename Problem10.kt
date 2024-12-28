/*
上机题：

☆1. 在忽略字符串中的非英语字母字符并把大小写英语字母字符看成等价后，正读和反读完全一样的字符串称为英语回文。如：
Rise to vote, sir.
No lemons no melon.
Was it a car or a cat I saw?
判断输入的字符串是否为英语回文。要求使用栈和队列。

☆2. 假定采用带头结点的单链表保存单词，当两个单词有相同的后缀时，则可共享相同的后缀存储空间，例如，"loading"和"being"的存储映像如下图所示。
[图中显示了两个单链表str1和str2的链接结构]
设str1和str2分别指向两个单词所在单链表的头结点，链表结点结构为[data|next]，请设计一个时间上尽可能高效的算法，找出由str1和str2所指向两个链表共同后缀的起始位置（如图中字符i所在结点的位置p）。要求：
1）给出算法的基本设计思想。
2）根据设计思想，采用C或C++或Java语言描述算法，关键之处给出注释。
3）说明你所设计算法的时间复杂度。

☆3. 二叉树的带权路径长度（WPL）是二叉树中所有叶结点的带权路径长度之和。
给定一棵二叉树T，采用二叉链表存储，结点结构如下：
[left|weight|right]
其中叶结点的weight域保存该结点的非负权值。设root为指向T的根结点的指针，请设计求T的WPL的算法。要求：
1）给出算法的基本设计思想。
2）使用C或C++语言，给出二叉树结点的数据类型定义。
3）根据设计思想，采用C或C++语言描述算法，关键之处给出注释。
 */

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