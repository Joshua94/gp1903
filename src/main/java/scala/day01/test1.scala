package scala.day01


object test1 {
  def main(args: Array[String]): Unit = {
//    分数 < 60 不及格
//      分数 > 60 及格
//      分数 > 90 优

    val score = 90
    var scoreStr = ""

    if (score>=60 && score<90) scoreStr = "及格"
    else if (score>=90) scoreStr = "优"
    else scoreStr = "不及格"


    val z = if (score>=60 && score<90) "及格"
    else if (score>=90) "优"
    else "不及格"

    println(scoreStr)
    println(z)
  }
}