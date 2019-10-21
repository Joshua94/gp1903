package scala.day01


object test2 {
  def main(args: Array[String]): Unit = {
    val x = 10
    val y = 100
    println("x = " + x + "; y = " + y)

    val str = s"x = $x; y = $y"
    println(str)

    val raw = raw"hello\t\nworld"
    println(raw)


    val height = 1.9
    val name = "James"
    println(f"$name%sis $height%-20.2f meters tall")

    val a = for (i <- 0 until 10; if i%2 == 0) yield {
      i * 2 + 1
    }
    println(a)


    for (i <- Range(1, 10, 2)) {
      print(i)
    }

//    for (i <-1 to 3; j<-1 to 3; if i != j) print((10*i+j) + " " )
  }
}
