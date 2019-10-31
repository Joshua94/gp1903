package spark

import org.apache.spark.{SparkConf, SparkContext}

object Test1 {
  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf().setAppName("Test1").setMaster("local[*]")
    val sparkContext = new SparkContext(sparkConf)
    sparkContext.setLogLevel("WARN")

    val rdd = sparkContext.parallelize(Array(("spark",2),("hadoop",6),("hadoop",4),("spark",6)))
    rdd.groupByKey().map(x => (x._1, x._2.sum / x._2.size)).foreach(println)
    sparkContext.stop()
  }
}
