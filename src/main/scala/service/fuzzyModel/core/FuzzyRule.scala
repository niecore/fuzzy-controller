package service.fuzzyModel.core

/**
  * Created by nico on 15.01.17.
  */
class FuzzyRule(in: List[Double => FuzzyBool], out: Double => (Double => FuzzyBool)) {
  val inputs = in
  val outputs = out
}