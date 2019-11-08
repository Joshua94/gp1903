package scala.day01

object HelloWorld {
  def main(args: Array[String]): Unit = {
    val a = (1 to 10).toList
    println(a.tail)
    println(a.init)
    println(a.head)
    println(a.last)
  }
}
