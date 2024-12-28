typealias Vertex = String;

//1, 3
class Problem16 {
  class DirectedGraph(vertices: List<Vertex>, edges: List<Pair<Vertex, Vertex>>) {
    private val adjacencyMap: Map<Vertex, List<Vertex>> = run {
      val map = mutableMapOf<Vertex, List<Vertex>>().withDefault { mutableListOf() }
      for ((u, v) in edges) {
        map[u] = map.getValue(u).plus(v)
      }
      map
    }
    fun depthFirstSearchAvailablePath(source: String, target: String): Boolean {
      val visitedVertices = mutableSetOf<Vertex>()
      fun dfs(currentVertex: Vertex): Boolean {
        if (currentVertex == target) {
          return true
        }else{
          visitedVertices.add(currentVertex)
          for (vertex in adjacencyMap.getValue(currentVertex)) {
            if (!visitedVertices.contains(vertex)) {
              visitedVertices.add(vertex)
              val success = dfs(vertex)
              visitedVertices.remove(vertex)
              if(success){
                return true;
              }
            }
          }
          return false
        }
      }
      return dfs(source)
    }
  }

  fun undirectedGraphTest(){
    val vertices = listOf("a", "b", "c", "d")
    val edges = listOf("a" to "b", "b" to "a", "a" to "c", "c" to "a")
    val graph = DirectedGraph(vertices, edges)
    require(graph.depthFirstSearchAvailablePath("a", "b") == true, {"a should connect to b."})
    require(graph.depthFirstSearchAvailablePath("c", "d") == false, {"c should not connect to d."})
    println("All test pass.")
  }

  sealed interface FatherNode
  object Empty: FatherNode
  data class NonEmpty(val value: String, val father: FatherNode) : FatherNode
  fun calculateLowestCommonAncestorBruteForce(node1: FatherNode, node2: FatherNode): FatherNode? {
    fun getRootPath(node: FatherNode): List<FatherNode> = when(node){
      is Empty -> listOf()
      is NonEmpty -> getRootPath(node.father).plus(node)
    }
    val path1 = getRootPath(node1)
    val path2 = getRootPath(node2)
    //println("path1: $path1\npath2: $path2\n")
    val count = path1.zip(path2).count{it.first == it.second}
    return if(count == 0){
      null
    }else{
      path1[count-1]
    }
  }

  fun lcaTest(){
    val node1 = NonEmpty("1", Empty)
    val node3 = NonEmpty("3", NonEmpty("2", node1))
    val node4 = NonEmpty("4", node1)
    println(calculateLowestCommonAncestorBruteForce(node4, node3))
  }

  companion object {
    @JvmStatic
    fun main(args: Array<String>) {
      //Problem16().undirectedGraphTest()
      Problem16().lcaTest()
    }
  }
}