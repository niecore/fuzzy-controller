package service.fuzzyModel

import service.fuzzyModel.core._

/**
  * Created by nico on 15.01.17.
  */
object FuzzyKnowledgeBaseCar extends FuzzyKnowledgeBase {
  // Fuzzyfication
  val distanceInput = new FuzzyValueConnector("Distance", 0, 1000)
  val speedInput    = new FuzzyValueConnector("Speed", 0, 250)

  val isVeryClose = new FuzzyTerm("isVeryClose", distanceInput, (x) => new FuzzyBool(triangle(x, None, 10, Some(20))))
  val isClose     = new FuzzyTerm("isClose", distanceInput, (x) => new FuzzyBool(triangle(x, Some(15), 25, Some(35))))
  val isNormal    = new FuzzyTerm("isNormal", distanceInput, (x) => new FuzzyBool(triangle(x, Some(25), 35, Some(45))))
  val isFar       = new FuzzyTerm("isFar", distanceInput, (x) => new FuzzyBool(triangle(x, Some(35), 45, Some(55))))
  val isVeryFar   = new FuzzyTerm("isVeryFar", distanceInput, (x) => new FuzzyBool(triangle(x, Some(45), 55, None)))

  // Defuzzyfication
  val forceOutput = new FuzzyValueConnector("Force", -1000, 7000)

  val brake       = new FuzzyTerm("brake", forceOutput, (x) => new FuzzyBool(triangle(x, None, -500, Some(0))))
  val roll        = new FuzzyTerm("roll", forceOutput, (x) => new FuzzyBool(triangle(x, Some(-500), 0, Some(500))))
  val speed       = new FuzzyTerm("speed", forceOutput, (x) => new FuzzyBool(triangle(x, Some(0), 2000, None)))

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

