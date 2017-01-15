/**
  * Created by nico on 12.01.17.
  */
class FuzzyKnowledgeDriving {

  // Fuzzyfication
  // Einheit; Meter
  // Wertebeich: 0-55
  def isVeryClose(distance: Double): FuzzyBool = {
    new FuzzyBool(triangle(distance, None, 10, Some(20)))
  }

  def isClose(distance: Double): FuzzyBool = {
    new FuzzyBool(triangle(distance, Some(15), 25, Some(35)))
  }

  val isNormal = (input: Double) => {
    new FuzzyBool(triangle(input, Some(25), 35, Some(45)))
  }


  def isFar(distance: Double): FuzzyBool = {
    new FuzzyBool(triangle(distance, Some(35), 45, Some(55)))
  }

  def isVeryFar(distance: Double): FuzzyBool = {
    new FuzzyBool(triangle(distance, Some(45), 55, None))
  }

  // Defuzzyfication
  // Einheit: m/s^2
  // Wertbereich: [-1...1]
  def brake(alpha: Double): Double => FuzzyBool = {
    return (x: Double) => new FuzzyBool(triangle(x, Some(-1), -0.5, Some(0)))
  }

  def roll(alpha: Double): Double => FuzzyBool = {
    return (x: Double) => new FuzzyBool(triangle(x, Some(-0.5), 0, Some(+0.5)))
  }

  def speed(alpha: Double): Double => FuzzyBool = {
    return (x: Double) => new FuzzyBool(triangle(x, Some(0), 0.5, Some(1)))
  }


  // Rules
  var rules = List[FuzzyRule](  new FuzzyRule(List(isFar), speed),
                                new FuzzyRule(List(isNormal), roll),
                                new FuzzyRule(List(isClose), brake))
}

def triangle(x: Double, a: Option[Double], m: Double, b: Option[Double]): Double = {
  // source:
  // http://www.dma.fi.upm.es/recursos/aplicaciones/logica_borrosa/web/fuzzy_inferencia/funpert_en.htm

  (a, m, b) match {
    case (None, m, Some(b)) => {
      if(x <= m) {
        1
      } else if(x <= b) {
        b-x / b-m
      } else {
        0
      }
    }
    case (Some(a), m, Some(b)) => {
      if(a >= x || x >= b) {
        0
      } else if (x <= m) {
        x-a / m-a
      } else {
        b-x / b-m
      }
    }
    case (Some(a), m, None) => {
      if(x <= a) {
        0
      } else if(x <= m) {
        x-a / m-a
      } else {
        1
      }
    }
  }
}