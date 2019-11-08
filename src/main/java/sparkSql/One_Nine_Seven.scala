package sparkSql

import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.sql.functions._

object One_Nine_Seven {
  def main(args: Array[String]): Unit = {
    val sparkSession: SparkSession = SparkSession.builder().appName("1_9_7").master("local[*]").getOrCreate()
    sparkSession.sparkContext.setLogLevel("WARN")

    val dataFrame: DataFrame = sparkSession.read.option("header", true).option("delimiter", " ").csv("./src/main/java/data/SparkSql/197.txt")
    //dataFrame.show

    dataFrame.na.drop(Array("url")).groupBy("date").agg(countDistinct("uid") as "UV", count("url") as "PV")
        .show

    sparkSession.stop()
  }
}
