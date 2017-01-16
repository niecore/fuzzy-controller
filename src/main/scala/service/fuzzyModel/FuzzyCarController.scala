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

    var test2: (Double => FuzzyBool) = null

    // fuzzyfication
    // fuzzy logic
    // defuzzyfication
    logic.rules.foreach(
      rule => {
        var alpha = rule.inputs.map(
          e => {
            e.apply(distance)
          }
        )

        var min = alpha.foldLeft(Double.MaxValue)(_ min _.value)

        yield rule.output.apply(min)
      }
    )


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