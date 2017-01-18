package model.fuzzyModel.entity

import model.fuzzyModel.MembershipFunctions

/**
  * Created by nico on 17.01.17.
  */
case class FuzzyConfig(var fuzzyValueConnectors : List[FuzzyValueConnector],
                       var fuzzyTerms: List[FuzzyTerm],
                       var rules: List[FuzzyRule],
                       var defuzzy: FuzzyDefuzzyficationFunc) {

  def filterFuzzyTerms(x: FuzzyValueConnector): List[FuzzyTerm] = {
    fuzzyTerms.filter(e => e.adapter.name == x.name)
  }
}
