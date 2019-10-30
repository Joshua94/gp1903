package scala.day01

import org.apache.spark.{SparkConf, SparkContext}

object Test5 {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("Test5").setMaster("local[*]")
    val sc = new SparkContext(conf)
    sc.setLogLevel("WARN")

    /*val lines = sc.textFile("./src/main/LICENSE.txt")
    lines.flatMap(line => line.split(" ")).map(work => (work,1))
      .mapValues(x => x + 100).foreach(x => print(x + ","))*/
    val pairRDD1 = sc.parallelize(Array(("spark",1),("spark",2),("hadoop",3),("hadoop",5)))
    val pairRDD2 = sc.parallelize(Array(("spark","fast")))
    pairRDD1.join(pairRDD2).foreach(x => print(x + " "))
  }
}
