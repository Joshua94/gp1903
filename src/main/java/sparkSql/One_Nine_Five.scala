package sparkSql

import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types._

object One_Nine_Five {
  def main(args: Array[String]): Unit = {
    val sparkSession: SparkSession = SparkSession.builder().appName("1_9_5").master("local[*]").getOrCreate()
    sparkSession.sparkContext.setLogLevel("WARN")

    //登录表
    val dataFrame: DataFrame = sparkSession.read.option("header",true).csv("./src/main/java/data/SparkSql/195.csv")
    //dataFrame.show

    //阅读表
    val dataFrame2: DataFrame = sparkSession.read.option("header", true).csv("./src/main/java/data/SparkSql/195_2.csv")
    //dataFrame2.show

    //付费表
    val dataFrame3: DataFrame = sparkSession.read.option("header", true).csv("./src/main/java/data/SparkSql/195_3.csv")
    //dataFrame3.show

    //用户登录并且当天有阅读的用户数以及阅读书籍数量
    dataFrame.join(dataFrame2, "user_id")
      .agg(count("user_id") as "count_user_id", sum("read_num").cast(IntegerType) as "sum_read_num")
    //  .show

    val dataFrame4: DataFrame = dataFrame.join(dataFrame2, "user_id").join(dataFrame3, Seq("user_id"), "left").na.drop(Array("price"))
    //用户登录并且阅读，但是没有付费的用户数
    dataFrame4.agg(count("user_id") as "count_user_id")
     //   .show

    //用户登录并且付费，付费用户书籍和金额
    dataFrame4.select("user_id", "read_num", "price")
        .show

    sparkSession.stop()
  }
}
