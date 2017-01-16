package service.fuzzyModel

import service.fuzzyModel.core.{FuzzyBool, FuzzyKnowledgeBase}
import service.physicalModel.Drivable

/**
  * Created by nico on 12.01.17.
  */
class FuzzyCarController(logic: FuzzyKnowledgeBase, controlledCar: Drivable, chasedCar: Drivable) {

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

    var output = (-1000 to 2000).map(e => combineOutputRules(test, Math.max)(e.toDouble))
      .map(f => f.value)

    for(i <- (0 to 2999))println(i + ";" + output(i).toString)

    var max = output.zipWithIndex.maxBy(_._1)._2

    var maxList = for {
      i <- output.indices

    }
    if(max != 500){
      println("here")
    }
    print(max)
  }
}