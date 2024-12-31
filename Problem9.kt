class Problem9 {
  fun translateMagicSpeech(input: String): String {
    val rules = mapOf(
      "A" to "sye",
      "B" to "tAdA"
    )

    var result = input
    var prevResult = ""

    while (prevResult != result) {
      prevResult = result


      while (result.contains("(")) {
        result = result.replace(Regex("\\(([^()]+)\\)")) { matchResult ->
          val content = matchResult.groupValues[1]
          val firstChar = content[0]
          val restChars = content.substring(1)

          firstChar + restChars.reversed().map { "$firstChar$it" }.joinToString("") + firstChar
        }
      }

      for ((key, value) in rules) {
        result = result.replace(key, value)
      }
    }

    return result
  }


  companion object {
    @JvmStatic
    fun main(args: Array<String>) {

      val testInput = "B(ehnxgz)B"
      val result = Problem9().translateMagicSpeech(testInput)
      println("Input: $testInput")
      println("Output: $result")

      val expected = "tsyedsyeezegexenehetsyedsye"
      println("测试${if (result == expected) "通过" else "失败"}")

      // 添加中文对照显示
      val chineseMap = mapOf(
        't' to "天",
        'd' to "地",
        's' to "上",
        'y' to "一",
        'e' to "鹅",
        'z' to "追",
        'g' to "赶",
        'x' to "下",
        'n' to "蛋",
        'h' to "恨"
      )

      val chineseResult = result.map { chineseMap[it] ?: it.toString() }.joinToString("")
      println("中文结果: $chineseResult")
    }
  }
}
//Assisted by Claude.