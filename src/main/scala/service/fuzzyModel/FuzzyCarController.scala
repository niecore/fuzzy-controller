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
        println(rule.name + ": " + min)
        rule.output.apply(min)
      }
    )

    def combineOutputRules(outputFunctions: List[(Double) => FuzzyBool], operator: (Double, Double) => Double = Math.max): ((Double) => FuzzyBool) = {
      (d) => new FuzzyBool(outputFunctions.foldLeft(Double.MinValue)((a, b) => operator(a, b(d).value)))
    }


    // take range from output
    var output = (-1000 to 2000).map(e => combineOutputRules(test, Math.max)(e.toDouble)).map(f => f.value).toList

    //for(i <- (0 to 2999))println(i + ";" + output(i).toString)

    def getMaxValuesWithIndixes: List[Double] => List[(Double, Int)] = {
      (list) => list.zipWithIndex.filter(e=> e._1 == list.max)
    }

    def maxMethod: List[Double] => Int = {
      (list) => Random.shuffle(getMaxValuesWithIndixes.apply(list).map(e => e._2).toList).head
    }

    def momMethod: List[Double] => Int = {
      (list) => (list.zipWithIndex.filter(e=> e._1 == list.max).map(e => e._2).foldLeft(0.toFloat){
        _ + _
      }).toInt
    }

    print(output)
    print("List: " + maxMethod.apply(output))
    print("List: " + momMethod.apply(output))
  }
}