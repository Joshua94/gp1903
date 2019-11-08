package spark

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}
import scala.math.{pow, sqrt}

object KMeansDemo {
  case class LabeledPoint(label: String, point: List[Double])
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName(s"${this.getClass.getCanonicalName}").setMaster("local[1]")
    val sc = new SparkContext(conf)
    sc.setLogLevel("WARN")
    val K = 3
    val minDists = 0.000001
    var distSum = 1.0

    //  1 读数据、封装数据，得到数据集D1
    val sampleRDD: RDD[LabeledPoint] = sc.textFile(".\\src\\main\\java\\data\\iris.csv")
      .map(line => {
        val fields = line.split(",")
        val label: String = fields.last
        val point = fields.tail.init.map(_.toDouble).toList
        LabeledPoint(label, point)
      })

    //  2 随机得到K个点
    var centerPoints: Array[List[Double]] = sampleRDD.takeSample(false, K, 100).map { case LabeledPoint(_, point) => point }

    while (distSum > minDists) {
      //  3 将D1分成K份（计算每个点离K个随机点的距离，离随近就属于哪个点）
      // D1中的数据：4个特征值
      val categoryRDD: RDD[(Int, List[Double])] = sampleRDD.map { case LabeledPoint(_, point) => point }
        .map((elem: List[Double]) => {
          // 求D1中每个点与中心的距离（有K个值）
          val dists: Array[Double] = getDistances(elem, centerPoints)
          // 求最小距离对应的编号
          val idx = dists.indexOf(dists.min)
          // 编号（分类）；坐标值
          (idx, elem)
        })

      //  4 求k个分类的中心点(新的中心点)
      val newCenterPoints: collection.Map[Int, List[Double]] = categoryRDD.groupByKey()
        .mapValues(buffer => {
          val count = buffer.size
          val listSum: List[Double] = buffer.toList.reduce(addList(_, _))
          val centerPoint: List[Double] = listSum.map(_ / count)
          centerPoint
        }).collectAsMap()

      //  5 如果中心点发生移动，转3；否则迭代结束
      // 如何判断中心点是否发生移动？新旧中心点距离之和小于某个常数（0.00001）
      distSum = centerPoints.zipWithIndex.map { case (point, idx) => getDist(point, newCenterPoints(idx)) }.sum

      // 中心点的坐标重新赋值
      centerPoints = centerPoints.zipWithIndex.map{ case (_, idx) => newCenterPoints(idx)}
      //centerPoints.foreach(println)
      //println(distSum)
    }

    // 计算中心点的坐标
    centerPoints.foreach(println)

    // 我们计算的分类 与 实际分类的对应情况
    sampleRDD.map{case LabeledPoint(label, point) =>
      val dists = getDistances(point, centerPoints)
      val idx = dists.indexOf(dists.min)
      (point, label, idx)
    }.sortBy(_._2).saveAsTextFile(".\\src\\main\\java\\data\\KMeansResult")

    sc.stop()
  }

  // 某个点与K个中心点计算距离
  def getDistances(p: List[Double], points: Array[List[Double]]): Array[Double] = {
    points.map(x => getDist(x, p))
  }

  // 求两点之间的欧式距离
  def getDist(p1: List[Double], p2: List[Double]): Double = {
    sqrt(p1.zip(p2).map(elem => pow(elem._1 - elem._2, 2)).sum)
  }

  def addList(lst1: List[Double], lst2: List[Double]): List[Double] = lst1.zip(lst2).map(elem => elem._1+elem._2)
}

//  1 读数据、封装数据，得到数据集D1
//  2 随机得到K个点
//  3 将D1分成K份（计算每个点离K个随机点的距离，离随近就属于哪个点）
//  4 求k个分类的中心点
//  5 如果中心点发生移动，转3；否则迭代结束

//
//
//
////  3 将D1分成K份（计算每个点离K个随机点的距离，离随近就属于哪个点）
//// D1中的数据：4个特征值
//val categoryRDD: RDD[(Int, List[Double])] = sampleRDD.map { case LabeledPoint(_, point) => point }
//.map((elem: List[Double]) => {
//// 求D1中每个点与中心的距离（有K个值）
//val dists: Array[Double] = getDistances(elem, centerPoints)
//// 求最小距离对应的编号
//val idx = dists.indexOf(dists.min)
//// 编号（分类）；坐标值
//(idx, elem)
//})
//
////  4 求k个分类的中心点(新的中心点)
//val newCenterPoints: collection.Map[Int, List[Double]] = categoryRDD.groupByKey()
//.mapValues(buffer => {
//val count = buffer.size
//val listSum: List[Double] = buffer.toList.reduce(addList(_, _))
//val centerPoint: List[Double] = listSum.map(_ / count)
//centerPoint
//}).collectAsMap()
//
////  5 如果中心点发生移动，转3；否则迭代结束
//// 如何判断中心点是否发生移动？新旧中心点距离之和小于某个常数（0.00001）
//distSum: Double = centerPoints.zipWithIndex.map { case (point, idx) => getDist(point, newCenterPoints(idx)) }.sum
