package scala.day01

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Test5 {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("Test5").setMaster("local[2]")
    val sc = new SparkContext(conf)
    sc.setLogLevel("WARN")
    /*sc.setCheckpointDir("./src/main/java/Dir1")
    val rdd = sc.parallelize(Array(("spark",2),("hadoop",6),("hadoop",4),("spark",6)))
    val rdd2 = rdd.groupByKey().map(x => (x._1, x._2.sum / x._2.size))
    rdd2.checkpoint()
    rdd2.saveAsTextFile("./src/main/java/Dir2")
    println(rdd2.getCheckpointFile)*/
    val rdd =sc.parallelize(Array(("a", 88), ("b", 95), ("a", 91), ("b", 93), ("a", 95), ("b", 98)))
    rdd.combineByKey((_, 1),(a:(Int, Int),v) => (a._1 + v, a._2 + 1),(a:(Int, Int), b:(Int, Int)) => (a._1 + b._1, a._2 + b._2)).map(x => (x._1,x._2._1 / x._2._2)).foreach(println)

    sc.stop()
  }
}
