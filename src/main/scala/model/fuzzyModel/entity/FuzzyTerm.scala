package model.fuzzyModel.entity

/**
  * Created by nico on 16.01.17.
  */
case class FuzzyTerm (val name: String,
                      val adapter: FuzzyValueConnector,
                      val func: Double => FuzzyBool) {

  override def toString = "" + adapter.name + " = " + name
}
