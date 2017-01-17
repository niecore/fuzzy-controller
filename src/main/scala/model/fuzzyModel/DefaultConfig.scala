package model.fuzzyModel

import model.fuzzyModel.entity.{FuzzyBool, FuzzyRule, FuzzyTerm, FuzzyValueConnector, FuzzyConfig}

/**
  * Created by nico on 17.01.17.
  */

object DefaultConfig extends FuzzyConfig(RuleBase.fuzzyValueConnector, RuleBase.terms, RuleBase.rules)

object RuleBase {
  // Fuzzyfication
  val distanceInput = new FuzzyValueConnector("Distance", 0, 1000, true)
  val speedInput = new FuzzyValueConnector("Speed", 0, 250, true)
  val forceOutput = new FuzzyValueConnector("Force", -3000, 7000, false)

  val fuzzyValueConnector = distanceInput :: speedInput :: forceOutput :: Nil

  val isVeryClose = new FuzzyTerm("isVeryClose", distanceInput, (x) => new FuzzyBool(MembershipFunctions.triangle(x, None, 30, Some(60))))
  val isClose = new FuzzyTerm("isClose", distanceInput, (x) => new FuzzyBool(MembershipFunctions.triangle(x, Some(30), 60, Some(90))))
  val isNormal = new FuzzyTerm("isNormal", distanceInput, (x) => new FuzzyBool(MembershipFunctions.triangle(x, Some(60), 90, Some(120))))
  val isFar = new FuzzyTerm("isFar", distanceInput, (x) => new FuzzyBool(MembershipFunctions.triangle(x, Some(90), 120, Some(150))))
  val isVeryFar = new FuzzyTerm("isVeryFar", distanceInput, (x) => new FuzzyBool(MembershipFunctions.triangle(x, Some(120), 150, None)))

  val isSlow = new FuzzyTerm("isVeryClose", speedInput, (x) => new FuzzyBool(MembershipFunctions.triangle(x, None, 50, Some(100))))
  val isFast = new FuzzyTerm("isNormal", speedInput, (x) => new FuzzyBool(MembershipFunctions.triangle(x, Some(50), 100, Some(150))))
  val isExtreme = new FuzzyTerm("isVeryFar", speedInput, (x) => new FuzzyBool(MembershipFunctions.triangle(x, Some(100), 150, None)))

  // Defuzzyfication
  val fullStop = new FuzzyTerm("fullStop", forceOutput, (x) => new FuzzyBool(MembershipFunctions.triangle(x, None, -2000, Some(-1500))))
  val brake = new FuzzyTerm("brake", forceOutput, (x) => new FuzzyBool(MembershipFunctions.triangle(x, Some(-2000), -500, Some(0))))
  val roll = new FuzzyTerm("roll", forceOutput, (x) => new FuzzyBool(MembershipFunctions.triangle(x, Some(-500), 0, Some(500))))
  val speed = new FuzzyTerm("speed", forceOutput, (x) => new FuzzyBool(MembershipFunctions.triangle(x, Some(0), 500, Some(1000))))
  val fullSpeed = new FuzzyTerm("fullSpeed", forceOutput, (x) => new FuzzyBool(MembershipFunctions.triangle(x, Some(500), 2000, None)))

  val terms = isVeryClose :: isClose :: isNormal :: isFar :: isVeryFar :: isSlow :: isFast :: isExtreme :: fullStop :: brake :: roll :: speed :: fullSpeed :: Nil

  // Rules
  val rules = List[FuzzyRule](new FuzzyRule("Rule1", List(isVeryFar), fullSpeed),
    new FuzzyRule("Rule2", List(isFar), speed),
    new FuzzyRule("Rule3", List(isNormal), roll),
    new FuzzyRule("Rule4", List(isClose), brake),
    new FuzzyRule("Rule5", List(isVeryClose), fullStop))
}
