package service.fuzzyModel.core

/**
  * Created by nico on 15.01.17.
  */
class FuzzyRule(id: String, in: List[Double => FuzzyBool], out: Double => (Double => FuzzyBool)) {
  val name = id
  val inputs = in
  val output = out
}