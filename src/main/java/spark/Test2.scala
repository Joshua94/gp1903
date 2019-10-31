package spark

import org.apache.spark.{SparkConf, SparkContext}

object Test2 {
  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf().setAppName("Test2").setMaster("local[*]")
    val sparkContext = new SparkContext(sparkConf)
    sparkContext.setLogLevel("WARN")

    val rdd = sparkContext.textFile("./src/main/java/data/Test2.txt")
    val rdd2 = sparkContext.textFile("./src/main/java/data/StopWords.txt")
    val stopWords= rdd2.map(_.toLowerCase).collect().toList
    rdd.flatMap(_.split("[ ,.?!:—\"]")).filter(!_.isEmpty).map(_.toLowerCase).filter(!stopWords.contains(_)).map((_, 1)).reduceByKey(_+_).sortBy(_._2,false).take(50).foreach(println)
    sparkContext.stop()
  }
}
