package spark

import java.time.{LocalDateTime, ZoneOffset}

import org.apache.spark.{SparkConf, SparkContext}

object Test3 {
  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf().setAppName("Test3").setMaster("local[*]")
    val sparkContext = new SparkContext(sparkConf)
    sparkContext.setLogLevel("WARN")

    val rdd = sparkContext.textFile("./src/main/java/data/Advert.log")
    rdd.map(_.split("\t")).map(x => ((x(1),x(4)),1)).reduceByKey(_+_).map(x => (x._1._1,(x._1._2,x._2))).groupByKey().mapValues(_.toList.sortBy(_._2).reverse.take(3)).foreach(println)
    println("------------------------------------------------")
    rdd.map(_.split("\t")).map(x => ((LocalDateTime.ofEpochSecond(x(0).toLong / 1000,0,ZoneOffset.ofHours(8)).getHour,x(1),x(4)),1)).reduceByKey(_+_).map(x => ((x._1._1,x._1._2),(x._1._3,x._2))).groupByKey().mapValues(x => x.toList.sortBy(_._2).reverse.take(3)).foreach(println)
    sparkContext.stop()
  }
}
