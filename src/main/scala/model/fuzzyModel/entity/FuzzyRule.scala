package model.fuzzyModel.entity

/**
  * Created by nico on 15.01.17.
  */
class FuzzyRule(val name: String,
                val inputs: List[FuzzyTerm],
                val op: List[String],
                val outputs: FuzzyTerm) {

  override def toString = {
    var s: StringBuilder = new StringBuilder

    s ++=  "IF "
    inputs.foreach( e => s++= e.toString + " AND ")

    s.setLength(s.length() - 4);

    s ++= "THEN "

    s ++= outputs.toString

    s.toString()
  }
}