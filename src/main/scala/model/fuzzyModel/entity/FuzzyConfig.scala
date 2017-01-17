package model.fuzzyModel.entity

import model.fuzzyModel.MembershipFunctions

/**
  * Created by nico on 17.01.17.
  */
case class FuzzyConfig(fuzzyValueConnectors : List[FuzzyValueConnector], fuzzyTerms: List[FuzzyTerm], rules: List[FuzzyRule]) {

  def filterFuzzyTerms(x: FuzzyValueConnector): List[FuzzyTerm] = {
    fuzzyTerms.filter(e => e.adapter == x)
  }
}
