package model.fuzzyModel.entity

import scala.util.Random

/**
  * Created by nico on 17.01.17.
  */

case class FuzzyDefuzzyficationFunc(val name: String, val func: (List[Double]) => Int)

object DefuzzyficationFunctions {

  private def getMaxValuesWithIndixes: List[Double] => List[(Double, Int)] = {
    (list) => list.zipWithIndex.filter(e=> e._1 == list.max)
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
                            new FuzzyDefuzzyficationFunc("Maxium criteria", maxMethod)
  )
}