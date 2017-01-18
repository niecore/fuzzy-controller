package model.fuzzyModel

import model.fuzzyModel.entity.{FuzzyBool, FuzzyConfig}
import model.physicalModel.Drivable

/**
  * Created by nico on 12.01.17.
  */
class FuzzyCarController(logic: FuzzyConfig, controlledCar: Drivable, chasedCar: Drivable) {

  def combineOutputRules(outputFunctions: List[(Double) => FuzzyBool]): ((Double) => FuzzyBool) = {
    (x) => new FuzzyBool(outputFunctions.foldLeft(Double.MinValue)((a, b) => Math.max(a, b(x).value)))
  }

  def tick(): Unit = {
    controlledCar.tick()
    chasedCar.tick()


    // Measure Input Values
    var distance = chasedCar.position - controlledCar.position
    var speed = controlledCar.speed
    
    val outputRules = logic.rules.map(
      rule => {
        val acceptance = rule.inputs.map(
          e => {
            e.adapter.name match {
              case "Distance" => e.func.apply(distance)
              case "Speed" => e.func.apply(speed)
            }
          }
        )

        val minAcceptance = acceptance.foldLeft(Double.MaxValue)(_ min _.value)
        ((x: Double) => new FuzzyBool(Math.min(minAcceptance,rule.outputs.func.apply(x).value)), rule.outputs.adapter)
      }
    )

    // group OutputRules by outputAdapters
    var groupedOutput = outputRules.groupBy(_._2).transform(
      (k,v) => v.map( e => e._1)
    )

    // calculate output rules
    groupedOutput.foreach(
      (e)=> {
        var output = (e._1.minVal to e._1.maxVal).map(
          x => combineOutputRules(e._2)(x.toDouble)
        ).map(
          f => f.value
        ).toList

        // defuzzyfication
        var setpoint = (e._1.minVal to e._1.maxVal)(logic.defuzzy.func.apply(output))

        // find adapter for output
        e._1.name match {
          case "Force" => controlledCar.engineForce = setpoint; println(" Force: " + setpoint)
        }
      }
    )
  }
}