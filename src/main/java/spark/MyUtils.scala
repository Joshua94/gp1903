package spark

import java.time.format.DateTimeFormatter
import java.time.{LocalDateTime, ZoneOffset}

object MyUtils {
  private val customFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
  private val zone = ZoneOffset.of("+8")

  // 将字符串转为LocalDateTime（使用java8的时间日期类型，这种类型是线程安全的）
  def string2Date(str: String): LocalDateTime = {
    val validStr = if (str.length==19) str else str.replace(" ", " 0")
    LocalDateTime.parse(validStr, customFormatter)
  }

  // 得到 两个时间 相差的秒数
  def getTimeDiff(t1: LocalDateTime, t2: LocalDateTime): Long = {
    Math.abs(t1.toInstant(zone).toEpochMilli() - t2.toInstant(zone).toEpochMilli())
  }

  def main(args: Array[String]): Unit = {
    val t1 = string2Date("2099-12-31 00:00:00")
    val t2 = string2Date("2099-12-31 00:05:00")
    val t3 = string2Date("2018-02-03 11:59:29")
    val t4 = string2Date("2018-02-03 3:00:29")

    println(getTimeDiff(t1, t2))

    println(t4.isBefore(t1))
    println(t4.isAfter(t1))

    println(t1.toInstant(zone).toEpochMilli())
    println(t2.toInstant(zone).toEpochMilli())
    println(t3.toInstant(zone).toEpochMilli())
    println(t4.toInstant(zone).toEpochMilli())
  }
}
