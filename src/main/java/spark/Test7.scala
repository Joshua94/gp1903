package spark

import org.apache.spark.{SparkConf, SparkContext}

object Test7 {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("Test7").setMaster("local[*]")
    val sc = new SparkContext(conf)
    sc.setLogLevel("WARN")

    val rdd1 = sc.textFile("./src/main/java/data/http.log")
    val rdd2 = sc.textFile("./src/main/java/data/ip.txt")
    val bc = sc.broadcast(rdd2.map(elem => {val x = elem.split("\\|");(x(2).toLong, x(3).toLong, x(6))}).collect())
    rdd1.map(elem => {val x = elem.split("\\|")(1).split("\\.");(x(0).toLong << 24) + (x(1).toLong << 16) + (x(2).toLong << 8) + x(3).toLong})
        .map(x => (search(bc.value, x), 1)).reduceByKey(_+_).foreach(println)
    sc.stop()
  }
  def search(rdd: Array[(Long, Long, String)], num: Long): String = {
    var left = 0
    var right = rdd.length - 1
    var middle = 0

    while (left <= right) {
      middle = (left + right) / 2
      if (num >= rdd(middle)._1 && num <= rdd(middle)._2) {
        return rdd(middle)._3
      } else if (num < rdd(middle)._1) {
        right = middle - 1
      } else {
        left = middle + 1
      }
    }
    "UNKNOWN"
  }
}
