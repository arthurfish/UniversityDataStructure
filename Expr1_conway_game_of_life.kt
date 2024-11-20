/*
生命游戏在一个无边界的矩形网格上进行，这个矩形网格中的每个单元可被占据，也可不被占据。被占据的单元称为活的，而不被占据的单元称为死的。一个单元是活的还是死的是根据其周围活的邻居单元的数目而一代一代地发生变化的。一代一代变化的具体规则如下：
给定单元的邻居单元指的是与它在垂直、水平或对角方向上相接的8个单元。
如果一个单元是活的，则如果它具有2个或3个活的邻居单元，则该单元在下一代还是活的。
如果一个单元是活的，则如果它具有0个或1个、4个或4个以上活的邻居单元，则该单元在下一代会因为孤独或拥塞而死亡。
如果一个单元是死的，则如果它恰好具有3个活的邻居单元，则该单元在下一代会复活，否则该单元在下一代仍然是死的。
（1）编写程序，模拟任意一个初始输入状态和代代更替的不同状态并显示。
（2）修改程序，要求显示时用“空格”和“x”分别表示网格中死的和活的单元，并可根据用户的选择从键盘或文件读入初始状态。
 */
import kotlin.collections.ArrayList

fun countLiveNeighbours(xy: Pair<Int, Int>, graph: Array<Array<Boolean>>): Int {
  val (w, h) = Pair(graph[0].size, graph.size)
  val (x, y) = xy
  val directions = ArrayList<Pair<Int, Int>>()
  for (i in -1..1) for (j in -1..1) if (!(i == 0 && j == 0)) directions.add(Pair(i, j))
  val neighbours =
    directions.map { (dx, dy) -> x + dx to y + dy }.filter { (x, y) -> (0 <= x && x < h) && (0 <= y && y < w) }
  val liveNeighbours = neighbours.map { (x, y) -> graph[x][y] }.count { it }
  return liveNeighbours
}

fun liveOrDie(isNowLive: Boolean, liveNeighbours: Int): Boolean =
  if (isNowLive) when (liveNeighbours) {
    2, 3 -> true
    0, 1, 4, 5, 6, 7, 8 -> false
    else -> throw AssertionError()
  } else when (liveNeighbours) {
    3 -> true
    else -> false
  }


fun transformGraph(inGraph: Array<Array<Boolean>>, outGraph: Array<Array<Boolean>>): Unit {
  val (w, h) = Pair(inGraph[0].size, inGraph.size)
  for (row in 0..<h) for (col in 0..<w) outGraph[row][col] =
    liveOrDie(inGraph[row][col], countLiveNeighbours(row to col, inGraph))
  //println("In transform: \n${graphToString(outGraph)}")
}

fun graphToString(graph: Array<Array<Boolean>>): String {
  return graph.map{it.map{x->if(x)'#' else '.'}.joinToString(separator = "")}.joinToString(separator = "\n")
}

fun main(){
  val steps = 10
  val graph1 = """
    ......
    ####..
    ..#...
    ..#...
  """.trimIndent().lines()
    .map{it.toCharArray()
      .toTypedArray()
      .map { it == '#' }
      .toTypedArray()}
    .toTypedArray()

  println("graph1: \n${graphToString(graph1)}")
  val graph2 = graph1.clone()
  var inputGraph = graph1
  var outputGraph = graph2
  for(i in 1..steps){
    transformGraph(inputGraph, outputGraph);
    println(graphToString(outputGraph)+"\n")
    val stash = outputGraph
    outputGraph = inputGraph
    inputGraph = stash
  }
}