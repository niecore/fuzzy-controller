package model.fuzzyModel

import model.fuzzyModel.entity.{FuzzyBool, FuzzyConfig}
import model.physicalModel.Drivable

import scala.util.Random

/**
  * Created by nico on 12.01.17.
  */
class FuzzyCarController(logic: FuzzyConfig, controlledCar: Drivable, chasedCar: Drivable) {

  var random = new Random(1234)

  def tick(): Unit = {
    controlledCar.tick()
    chasedCar.tick()
    // measure
    var distance = chasedCar.position - controlledCar.position
    var speed = controlledCar.speed

    var outputRules = logic.rules.map(
      rule => {
        var acceptance = rule.inputs.map(
          e => {
            e.adapter.name match {
              case "Distance" => e.func.apply(distance)
              case "Speed" => e.func.apply(speed)
            }
          }
        )
        var minAcceptance = acceptance.foldLeft(Double.MaxValue)(_ min _.value)
        print(rule.name + ": " + minAcceptance + " ")
        ((x: Double) => new FuzzyBool(Math.min(minAcceptance,rule.outputs.func.apply(x).value)), rule.outputs.adapter)
      }
    )
    def combineOutputRules(outputFunctions: List[(Double) => FuzzyBool]): ((Double) => FuzzyBool) = {
      (x) => new FuzzyBool(outputFunctions.foldLeft(Double.MinValue)((a, b) => Math.max(a, b(x).value)))
    }

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

        e._1.name match {
          case "Force" => controlledCar.engineForce = (e._1.minVal to e._1.maxVal)(momMethod.apply(output)); println(" Force: " + controlledCar.engineForce)
        }
      }
    )
  }
}