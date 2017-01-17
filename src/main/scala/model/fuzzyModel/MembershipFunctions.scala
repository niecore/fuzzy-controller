package model.fuzzyModel

/**
  * Created by nico on 17.01.17.
  */
object MembershipFunctions {
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
