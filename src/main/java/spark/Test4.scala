package spark

import org.apache.spark.{SparkConf, SparkContext}

object Test4 {
  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf().setAppName("Test4").setMaster("local[*]")
    val sparkContext = new SparkContext(sparkConf)
    sparkContext.setLogLevel("WARN")

    val rdd = sparkContext.textFile("./src/main/java/data/log")
    val time = rdd.map(_.split(",")).map(x => ((x(0),x(2)),if (x(3) == "1") -x(1).toLong else x(1).toLong)).reduceByKey(_+_).map(x => (x._1._2,(x._1._1,x._2)))
    val rdd2 = sparkContext.textFile("./src/main/java/data/lac_info.txt")
    rdd2.map(_.split(",")).map(x => (x(0),(x(1),x(2)))).join(time).map(x => (x._2._2._1,(x._2._2._2,x._2._1))).groupByKey().mapValues(x => x.toList.sortBy(_._1).reverse.take(2)).foreach(println)
    sparkContext.stop()
  }
}
