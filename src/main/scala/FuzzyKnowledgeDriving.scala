/**
  * Created by nico on 12.01.17.
  */
class FuzzyKnowledgeDriving {

  // Fuzzyfication
  // Einheit; Meter
  // Wertebeich: 0-55
  def isVeryClose(distance: Double): FuzzyBool = {
    return new FuzzyBool(triangle(distance, None, 10, Some(20)))
  }

  def isClose(distance: Double): FuzzyBool = {
    return triangle(distance, 15, 25, 35)
  }

  def isNormal(distance: Double): FuzzyBool = {
    return triangle(distance, 25, 35, 45)
  }

  def isFar(distance: Double): FuzzyBool = {
    return triangle(distance, 35, 45, 55)
  }

  def isVeryFar(distance: Double): FuzzyBool = {
    return triangleRightOpen(distance, 45, 55)
  }

  // Defuzzyfication
  // Einheit: m/s^2
  // Wertbereich: [-1...1]

  def brake(alpha: Double): Double => FuzzyBool = {
    return (x: Double) => triangle(x, -1, -0.5, 0)
  }

  def roll(alpha: Double): Double => FuzzyBool = {
    return (x: Double) => triangle(x, -0.5, 0, +0.5)
  }

  def speed(alpha: Double): Double => FuzzyBool = {
    return (x: Double) => triangle(x, 0, 0.5, 1)
  }

}

def triangleLeftOpen(x: Double, m: Double, b: Double) : FuzzyBool = {
  // source:
  // http://www.dma.fi.upm.es/recursos/aplicaciones/logica_borrosa/web/fuzzy_inferencia/funpert_en.htm
  var retval: Double = 0
  if(x <= m) {
    retval = 1
  } else if(x <= b) {
    retval = b-x / b-m
  } else {
    retval = 0
  }
  return new FuzzyBool(retval)
}

def triangleRightOpen(x: Double, a: Double, m: Double) : FuzzyBool = {
  // source:
  // http://www.dma.fi.upm.es/recursos/aplicaciones/logica_borrosa/web/fuzzy_inferencia/funpert_en.htm
  var retval: Double = 0

  return new FuzzyBool(retval)
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