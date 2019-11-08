package spark

import org.apache.spark.{SparkConf, SparkContext}

object Test1 {
  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf().setAppName("Test1").setMaster("local[*]")
    val sparkContext = new SparkContext(sparkConf)
    sparkContext.setLogLevel("WARN")

    val rdd = sparkContext.parallelize(Array(("spark",2),("hadoop",6),("hadoop",4),("spark",6)))
    //rdd.groupByKey().map(x => (x._1, x._2.sum / x._2.size)).foreach(println)
    //rdd.groupByKey().mapValues(x => x.sum / x.size).foreach(println)
      /*rdd.aggregateByKey((0, 0))(
      // 分区内数据的合并
      (buffer: (Int, Int), elem: Int) => (buffer._1 + elem, buffer._2 + 1),
      (buffer1: (Int, Int), buffer2: (Int, Int)) => (buffer1._1 + buffer2._1, buffer1._2 + buffer2._2)
    ).mapValues{case (sum, count) => sum.toDouble / count}
      .foreach(println)*/
    /*rdd.combineByKey(
      value => (value, 1),
      (buffer: (Int, Int), elem: Int) => (buffer._1 + elem, buffer._2 + 1),
      (buffer1: (Int, Int), buffer2: (Int, Int)) => (buffer1._1 + buffer2._1, buffer1._2 + buffer2._2)
    ).mapValues{case (sum, count) => sum.toDouble / count}
      .foreach(println)*/
    sparkContext.stop()
  }
}
