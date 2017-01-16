package service.fuzzyModel

import service.fuzzyModel.core.FuzzyKnowledgeBase
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
    logic.rules.foreach{
      rule => {
        rule.outputs.apply(
          rule.inputs.foldLeft(Double.MaxValue)(_ min _.apply(distance).value)
        )
      }
    }
  }
}