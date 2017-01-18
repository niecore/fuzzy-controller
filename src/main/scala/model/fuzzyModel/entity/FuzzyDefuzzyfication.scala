package model.fuzzyModel.entity

import scala.util.Random

/**
  * Created by nico on 17.01.17.
  */

case class FuzzyDefuzzyficationFunc(val name: String,
                                    val func: (List[Double]) => Int)

object DefuzzyficationFunctions {

  private def getMaxValuesWithIndixes: List[Double] => List[(Double, Int)] = {
    (list) => {
      var maxValue = list.max
      list.zipWithIndex.filter(e=> e._1 == maxValue)
    }
  }

  private def integrateSimpson(f:Double=>Double, a:Double, b:Double, steps:Double)={
    val delta:Double=(b-a)/steps
    delta*(a until b by delta).foldLeft(0.0)((s,x) => s + (f(x)+4*f((x+x+delta)/2)+f(x+delta))/6)
  }

  def cogMethod: List[Double] => Int = {
    (list) => ((list.zipWithIndex.foldLeft(0.toDouble)((s,x) => s + x._1 * x._2)) /
              (list.zipWithIndex.foldLeft(0.toDouble)((s,x) => s + x._1))).toInt
  }

  def maxMethod: List[Double] => Int = {
    (list) => Random.shuffle(getMaxValuesWithIndixes.apply(list).map(e => e._2).toList).head
  }

  def momMethod: List[Double] => Int = {
    (list) => {
      var total = getMaxValuesWithIndixes.apply(list)
      (total.map(e => e._2).foldLeft(0.toDouble) {
        _ + _
      } / total.length).toInt
    }
  }

  var functionList = List(  new FuzzyDefuzzyficationFunc("Mean of Maxima", momMethod),
                            new FuzzyDefuzzyficationFunc("Maxium criteria", maxMethod),
                            new FuzzyDefuzzyficationFunc("Center of Gravity", cogMethod)
  )
}