package service.fuzzyModel

import service.fuzzyModel.core.{FuzzyBool, FuzzyKnowledgeBase}
import service.physicalModel.Drivable

import scala.util.Random

/**
  * Created by nico on 12.01.17.
  */
class FuzzyCarController(logic: FuzzyKnowledgeBase, controlledCar: Drivable, chasedCar: Drivable) {

  var random = new Random()

  def tick(): Unit = {
    // measure
    var distance = chasedCar.position - controlledCar.position
    var speed = controlledCar.speed

    // fuzzyfication
    // fuzzy logic
    // defuzzyfication
    var test = logic.rules.map(
      rule => {
        var alpha = rule.inputs.map(
          e => {
            e.apply(distance)
          }
        )

        var min = alpha.foldLeft(Double.MaxValue)(_ min _.value)
        //println(rule.name + ": " + min)
        rule.output.apply(min)
      }
    )

    def combineOutputRules(outputFunctions: List[(Double) => FuzzyBool]): ((Double) => FuzzyBool) = {
      (x) => new FuzzyBool(outputFunctions.foldLeft(Double.MinValue)((a, b) => Math.max(a, b(x).value)))
    }



    // take range from output
    var output = (-1000 to 2000).map(e => combineOutputRules(test)(e.toDouble)).map(f => f.value).toList

    def getMaxValuesWithIndixes: List[Double] => List[(Double, Int)] = {
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

  }
}