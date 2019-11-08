package spark

import org.apache.spark.sql.{Row, SparkSession}
import org.apache.spark.sql.functions._

object Test01 {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder().appName("Test1").master("local[*]").getOrCreate()
    spark.sparkContext.setLogLevel("WARN")

    import spark.implicits._
    val seq = Seq(Course("aa", "English", 75), Course("bb", "math", 85), Course("aa", "math", 90))
    val ds1 = spark.createDataset(seq)
    ds1.groupBy("name").pivot("course").sum("score").sort("name").na.fill(0).show()

    spark.stop()
  }
  case class Course(name: String, course: String, score: Int)
}
