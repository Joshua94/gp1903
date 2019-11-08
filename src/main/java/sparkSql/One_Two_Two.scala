package sparkSql

import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types.{DoubleType}

object One_Two_Two {
  def main(args: Array[String]): Unit = {
    val sparkSession: SparkSession = SparkSession.builder().appName("1_2_2").master("local[*]").getOrCreate()
    sparkSession.sparkContext.setLogLevel("WARN")

    val dataFrame: DataFrame = sparkSession.read.option("header", true).option("delimiter", " ").csv("./src/main/java/data/SparkSql/122.txt")
    //dataFrame.show

    //dataFrame.groupBy("userid").pivot("subject", Seq("语文", "数学", "英语", "政治")).agg(sum("score").cast(DoubleType)).na.fill(0).sort("userid")
    //   .show
    dataFrame.createTempView("Test122")
    sparkSession.sql("select userid, sum(case when subject = '语文' then score else 0 end) as `语文`" +
      ", sum(case when subject = '数学' then score else 0 end) as `数学`" +
      ", sum(case when subject = '英语' then score else 0 end) as `英语`" +
      ", sum(case when subject = '政治' then score else 0 end) as `政治` from Test122 group by userid")
        .show
    sparkSession.stop
  }
}
