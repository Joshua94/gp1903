package sparkSql

import org.apache.spark.SparkContext
import org.apache.spark.sql.SparkSession

object Test2 {
  def main(args: Array[String]): Unit = {
    val sparkSession = SparkSession.builder()
      .appName("Spark Hive Demo")
      .master("spark://master:7077")
      .enableHiveSupport()
      .getOrCreate()

    sparkSession.sparkContext.setLogLevel("WARN")
    sparkSession.sql("show databases").show
    sparkSession.sql("use hql")
    sparkSession.sql("select * from st").show
    sparkSession.stop()
  }
}
