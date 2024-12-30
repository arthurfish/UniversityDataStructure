/*
上机题:
☆1. 求一元 n 次多项式 Pn(x)的导函数。
说明:
(1)Pn(x)=c1 x e1 +c2 x e2 +...+cm x em ,其中 ci(实数)是指数为 ei(非负整数)的项的非零系
数,0≤e1<e2<...<em=n。
(2)多项式和结果多项式都用带头结点的单链表实现,其结点包含 3 个域:系数域 coef,
指数域 expn 和指针域 next。
(3)输出部分要考虑结果为 0 的情况。
2. 有编号依次为 1 至 10 的 10 个人各拿一只水桶同时来到一只水龙头前打水,水龙头注满
各水桶所需的时间依次为 60,30,80,20,90,40,100,10,70,50 秒。对这 10 个人进行排队,让
他们所花时间的总和(包括每人等待和接水所花时间)最小。输出排队后的编号次序和该时
间总和。
3. 下面两个算式中的每个汉字都代表 0 至 9 中的数字(相同的汉字代表相同的数字,不同
的汉字代表不同的数字)                。破译这两个算式。
年年×岁岁=花相似
岁岁÷年年=人÷不同
4. 从 18,19,12,17,20,11,8,15,16,7 中找出所有两数之和为质数(素数)的数对,如(18,
19)。
5. 求 10 个最小的连续自然数,它们都是合数。
 */

class Problem7 {
  fun differentiatePolynomial(polynomial: List<Pair<Int, Int>>): String{
    abstract class PairList;
    class Empty(): PairList(){
      override fun toString(): String = "Empty Polynomial"
    }
    data class Node(val coef: Int, val expn: Int, val next: PairList): PairList() {
      override fun toString(): String = when(next){
        is Empty -> "${coef}x^${expn}"
        is Node -> "${coef}x^${expn} + ${next}"
        else -> throw Exception("Empty")
      }
    };
    fun constructPairList(list: List<Pair<Int, Int>>): PairList =
      if(list.isEmpty()) Empty() else Node(list.first().first, list.first().second, constructPairList(list.drop(1)))
    fun differentiate(polynomial: PairList): PairList{
      fun diffFirst(x: PairList): PairList = when(x){
        is Node -> Node(x.coef*x.expn, x.expn-1,diffFirst(x.next))
        is Empty -> Empty()
        else -> throw Exception()
      }
      fun removeEmpties(polynomial: PairList): PairList{
        fun f(x: PairList): PairList = when(x){
          is Node -> if(x.coef == 0) f(x.next) else Node(x.coef, x.expn, f(x.next))
          is Empty -> Empty()
          else -> throw Exception()
        }
        return f(polynomial)
      }
      return removeEmpties(diffFirst(polynomial))
    }
    return differentiate(constructPairList(polynomial)).toString()
  }

  fun calculateMinimumWaitingTimeSequence(): Pair<List<Int>, Int>{
    val timeCosts = listOf( 60,30,80,20,90,40,100,10,70,50);
    val quickestQueue = timeCosts.sorted()
    val totalCost = quickestQueue.withIndex().fold(0){accTime, (currTime, currIndex) -> accTime + currTime * (quickestQueue.size-currIndex+1)};
    return (quickestQueue to totalCost);
  }

  fun decryptChinesePuzzle(): Map<String, Int>?{
    fun <T> List<T>.permutations(k: Int): List<List<T>> {
      if (k == 0) return listOf(emptyList())
      return this.flatMapIndexed { index, element ->
        (this.subList(0, index) + this.subList(index + 1, this.size))
          .permutations(k - 1)
          .map { subPermutation -> listOf(element) + subPermutation }
      }
    }
    val variables = listOf("Nian", "Sui", "Hua", "Xiang", "Si", "Ren", "Bu", "Tong");
    for(combination in (0..9).toList().permutations(8)){
      val M = variables.zip(combination).toMap()
      val Nian = M["Nian"]!!
      val Sui = M["Sui"]!!
      val Hua = M["Hua"]!!
      val Xiang = M["Xiang"]!!
      val Si = M["Si"]!!
      val Ren = M["Ren"]!!
      val Bu = M["Bu"]!!
      val Tong = M["Tong"]!!
      if ((Nian*10+Nian)*(Sui*10+Sui) == (Hua*100+Xiang*10+Si*1) &&
        (Sui*10+Sui)*(Bu*10+Tong)==(Nian*10+Nian)*(Ren))
        return M
    }
    return null;
  }

  fun sieveOfEratosthenes(upperBound: Int): List<Int> {
    fun sieveRecur(numbers: List<Int>): List<Int> =
      if(numbers.isNotEmpty())
        sieveRecur(numbers.drop(1).filter{it % numbers.first() != 0}).plus(numbers.first())
      else
        listOf()
    return sieveRecur((2..<upperBound).toList())
  }

  fun cartesianProduct(list1: List<Int>, list2: List<Int>): List<Pair<Int, Int>>{
    val outputList = mutableListOf<Pair<Int, Int>>()
    for(i in list1)
      for(j in list2)
        outputList.add(i to j)
    return outputList
  }

  fun filterSpecialPair(list: List<Int>): List<Pair<Int, Int>> {
    val primers = sieveOfEratosthenes(list.max()*2).toSet()
    return cartesianProduct(list, list).filter{(a, b) -> primers.contains(a + b)}
  }

  companion object{
    @JvmStatic
    fun main(args: Array<String>) {
      val problem7 = Problem7()

      val polynomial = listOf(3 to 2, 2 to 1, 1 to 0)
      val derivative = problem7.differentiatePolynomial(polynomial)
      println("Derivative: $derivative")

      val (sequence, totalTime) = problem7.calculateMinimumWaitingTimeSequence()
      println("Sequence: $sequence")
      println("Total Time: $totalTime seconds")

      val puzzleSolution = problem7.decryptChinesePuzzle()
      if (puzzleSolution != null) {
        println("Puzzle Solution: $puzzleSolution")
      } else {
        println("No solution found")
      }

      val numbers = listOf(18, 19, 12, 17, 20, 11, 8, 15, 16, 7)
      val specialPairs = problem7.filterSpecialPair(numbers)
      println("Special Pairs: $specialPairs")
    }
  }
}