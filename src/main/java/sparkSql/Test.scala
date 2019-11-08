package sparkSql

import org.apache.spark.sql.{DataFrame, SparkSession}

object Test {
  case class StudentAge(sno: Int, sname: String, age: Int)
  case class StudentHeight(sname: String, height: Int)
  def main(args: Array[String]): Unit = {
    val sparkSession: SparkSession = SparkSession.builder().appName("1_2_2").master("local[*]").getOrCreate()
    sparkSession.sparkContext.setLogLevel("WARN")
    import sparkSession.implicits._


    val lst = List(StudentAge(1,"Alice", 18), StudentAge(2,"Andy", 19), StudentAge(3,"Bob", 17), StudentAge(4,"Justin", 21), StudentAge(5,"Cindy", 20))
    val ds1 = sparkSession.createDataset(lst)


    val rdd = sparkSession.sparkContext.makeRDD(List(StudentHeight("Alice", 160), StudentHeight("Andy", 159),
      StudentHeight("Bob", 170), StudentHeight("Cindy", 165), StudentHeight("Rose", 160)))
    val ds2 = rdd.toDS


    /*ds1.join(ds2, "sname").show
    ds1.join(ds2, Seq("sname"), "inner").show
    ds1.join(ds2, ds1("sname")===ds2("sname"), "inner").show*/
    ds1.na.replace("sno" :: "age" :: Nil,Map(1 -> 2,17 -> 207)).show



    sparkSession.stop
  }
}
