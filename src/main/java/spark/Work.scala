package spark

import java.time.{LocalDateTime, ZoneOffset}
import java.time.format.DateTimeFormatter

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Work {
  def main(args: Array[String]): Unit = {
    val sparkConf: SparkConf = new SparkConf().setAppName("Work").setMaster("local[*]")
    val sparkContext: SparkContext = new SparkContext(sparkConf)
    sparkContext.setLogLevel("WARN")

    //解析数据
    val rdd1: RDD[String] = sparkContext.textFile("date/uservisitinfo.csv")

    //获得步长统计
    rdd1.map(_.split(",")).map(x => (x(1),1)).reduceByKey(_ + _).mapValues(x => {
      var flag = "10-30"
      if (x < 11)
        flag = "-10"
      else if (x > 30)
        flag = "30-"
      flag
    }).groupBy(_._2).map(x => (x._1,x._2.size)).saveAsTextFile("date/result/")

    //获得时长统计
    rdd1.map(_.split(",")).map(x => (x(1),string2Date(x(4)).toInstant(ZoneOffset.of("+8")).toEpochMilli())).groupByKey().mapValues(x => {
      val time = x.max-x.min
      var flag = "5-20"
      if (time < 5 * 60 * 1000)
        flag = "-5"
      else if(time > 20 * 60 * 1000)
        flag = "20-"
      flag
    }).groupBy(_._2).map(x => (x._1,x._2.size)).saveAsTextFile("date/result/")

    //优化
    //时长统计可以不使用groupByKey,而是使用aggregateByKey同时计算出最大和最小时间

    sparkContext.stop()
  }
  def string2Date(str: String): LocalDateTime = {
    val validStr = if (str.length==19) str else str.replace(" ", " 0")
    LocalDateTime.parse(validStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
  }
}