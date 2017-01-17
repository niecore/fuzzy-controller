package model.fuzzyModel.entity

/**
  * Created by nico on 12.01.17.
  */
object Fuzzy {
  val MIN_VALUE = 0.0
  val MAX_VALUE = 1.0
  val MIN = new FuzzyBool(MIN_VALUE)
  val MAX = new FuzzyBool(MAX_VALUE)
  def apply() = MIN
  def apply(v: Double) = new FuzzyBool(v)
}

class FuzzyBool(v: Double) {
  val value: Double = v;
}
