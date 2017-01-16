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
        rule.output.apply(min)
      }
    )

    def combineOutputRules(outputFunctions: List[(Double) => FuzzyBool], operator: (Double, Double)=> Double): ((Double)=> FuzzyBool) = {
      (d) => new FuzzyBool(outputFunctions.foldLeft(Double.MinValue)((a,b) => operator(a, b(d).value)))
    }

    var range = (-1000 to 5000)

    var test3 = range.map( e => test(0))

    print(test3)

    /*    var outputs : List[(Double => FuzzyBool)] = Nil
        outputs = logic.rules.map(
          rule => {
            rule.output.apply(
              rule.inputs.foldLeft(Double.MaxValue)(_ min _.apply(distance).value)
            )
          }
        )*/

    //println(outputs)
  }
}