object Expr4Previous {
  def generateSqure(n: Int): Array[Array[Char]] = {
    val width = n * 2 - 1
    val height = n * 2 - 1
    val graph = Array.ofDim[Char](width, height)
    def axisTransfer(row: Int, col: Int): (Int, Int) = {
      (row, col + n - 1)
    }
  }
}
