package service.fuzzyModel

import service.fuzzyModel.core.{FuzzyBool, FuzzyKnowledgeBase, FuzzyRule}

/**
  * Created by nico on 15.01.17.
  */
object FuzzyKnowledgeBaseCar extends FuzzyKnowledgeBase {
  // Fuzzyfication
  // Einheit; Meter
  // Wertebeich: 0-55
  def isVeryClose(distance: Double): FuzzyBool = {
    var value = new FuzzyBool(triangle(distance, None, 10, Some(20)))
    //println("isVeryClose " + value.value)
    value
  }

  def isClose(distance: Double): FuzzyBool = {
    var value = new FuzzyBool(triangle(distance, Some(15), 25, Some(35)))
    //println("isClose: " + value.value)
    value
  }

  def isNormal(distance: Double): FuzzyBool = {
    var value = new FuzzyBool(triangle(distance, Some(25), 35, Some(45)))
    //println("isNormal " + value.value)
    value
  }

  def isFar(distance: Double): FuzzyBool = {
    var value = new FuzzyBool(triangle(distance, Some(35), 45, Some(55)))
    //println("isFar " + value.value)
    value
  }

  def isVeryFar(distance: Double): FuzzyBool = {
    var value = new FuzzyBool(triangle(distance, Some(45), 55, None))
    //println("isVeryFar " + value.value)
    value
  }

  // Defuzzyfication
  // Einheit: m/s^2
  // Wertbereich: [-1...1]
  def brake(alpha: Double): Double => FuzzyBool = {
    //println("Break: " + alpha)
    return (x: Double) => new FuzzyBool(triangle(x, Some(-1000), -500, Some(0)))
  }

  def roll(alpha: Double): Double => FuzzyBool = {
    //println("Roll: " + alpha)
    return (x: Double) => new FuzzyBool(triangle(x, Some(-500), 0, Some(500)))
  }

  def speed(alpha: Double): Double => FuzzyBool = {
    //println("Speed: " + alpha)
    return (x: Double) => new FuzzyBool(triangle(x, Some(0), 2000, Some(5000)))
  }

  // Rules
  var rules = List[FuzzyRule](  new FuzzyRule("Rule1", List(isVeryFar), speed),
                                new FuzzyRule("Rule2", List(isFar), speed),
                                new FuzzyRule("Rule3", List(isNormal), roll),
                                new FuzzyRule("Rule4", List(isClose), brake),
                                new FuzzyRule("Rule5", List(isVeryClose), brake))

  def triangle(x: Double, a: Option[Double], m: Double, b: Option[Double]): Double = {
    // source:
    // http://www.dma.fi.upm.es/recursos/aplicaciones/logica_borrosa/web/fuzzy_inferencia/funpert_en.htm

    (a, m, b) match {
      case (None, m, Some(b)) => {
        if(x <= m) {
          return 1
        } else if(x <= b) {
          return (b-x) / (b-m)
        } else {
          return 0
        }
      }
      case (Some(a), m, Some(b)) => {
        if(a >= x || x >= b) {
          return 0
        } else if (x <= m) {
          return (x-a) / (m-a)
        } else {
          return (b-x) / (b-m)
        }
      }
      case (Some(a), m, None) => {
        if(x <= a) {
          return 0
        } else if(x <= m) {
          return (x-a) / (m-a)
        } else {
          return 1
        }
      }
    }
  }
}

