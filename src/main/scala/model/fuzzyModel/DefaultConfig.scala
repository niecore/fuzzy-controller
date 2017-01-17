package model.fuzzyModel

import model.fuzzyModel.entity._

/**
  * Created by nico on 17.01.17.
  */

object DefaultConfig extends FuzzyConfig(RuleBase.fuzzyValueConnector, RuleBase.terms, RuleBase.rules, RuleBase.defuzzy)

object RuleBase {
  // Fuzzyfication
  val distanceInput = new FuzzyValueConnector("Distance", 0, 300, true)
  val speedInput = new FuzzyValueConnector("Speed", 0, 250, true)
  val forceOutput = new FuzzyValueConnector("Force", -8000, 8000, false)

  val fuzzyValueConnector = distanceInput :: speedInput :: forceOutput :: Nil

  val isVeryClose = new FuzzyTerm("isVeryClose", distanceInput, (x) => new FuzzyBool(MembershipFunctions.triangle(x, None, 50, Some(100))))
  val isClose = new FuzzyTerm("isClose", distanceInput,         (x) => new FuzzyBool(MembershipFunctions.triangle(x, Some(50), 100, Some(150))))
  val isNormal = new FuzzyTerm("isNormal", distanceInput,       (x) => new FuzzyBool(MembershipFunctions.triangle(x, Some(100), 150, Some(200))))
  val isFar = new FuzzyTerm("isFar", distanceInput,             (x) => new FuzzyBool(MembershipFunctions.triangle(x, Some(150), 200, Some(250))))
  val isVeryFar = new FuzzyTerm("isVeryFar", distanceInput,     (x) => new FuzzyBool(MembershipFunctions.triangle(x, Some(200), 250, None)))

  val isStanding = new FuzzyTerm("isStanding", speedInput,  (x) => new FuzzyBool(MembershipFunctions.triangle(x, None, 0, Some(5))))
  val isSlow = new FuzzyTerm("isSlow", speedInput,          (x) => new FuzzyBool(MembershipFunctions.triangle(x, Some(0), 15, Some(30))))
  val isFast = new FuzzyTerm("isFast", speedInput,          (x) => new FuzzyBool(MembershipFunctions.triangle(x, Some(15), 50, Some(100))))
  val isExtreme = new FuzzyTerm("isExtreme", speedInput,    (x) => new FuzzyBool(MembershipFunctions.triangle(x, Some(50), 150, None)))

  // Defuzzyfication
  val fullBrake = new FuzzyTerm("fullBrake", forceOutput, (x) => new FuzzyBool(MembershipFunctions.triangle(x, None, -6000, Some(-5000))))
  val medBrake = new FuzzyTerm("medBrake", forceOutput,   (x) => new FuzzyBool(MembershipFunctions.triangle(x, Some(-6000), -4000, Some(-2000))))
  val brake = new FuzzyTerm("brake", forceOutput,         (x) => new FuzzyBool(MembershipFunctions.triangle(x, Some(-4000), -2000, Some(0))))
  val roll = new FuzzyTerm("roll", forceOutput,           (x) => new FuzzyBool(MembershipFunctions.triangle(x, Some(-500), 0, Some(500))))
  val speed = new FuzzyTerm("speed", forceOutput,         (x) => new FuzzyBool(MembershipFunctions.triangle(x, Some(0), 2000, Some(4000))))
  val medSpeed = new FuzzyTerm("medSpeed", forceOutput,   (x) => new FuzzyBool(MembershipFunctions.triangle(x, Some(2000), 4000, Some(6000))))
  val fullSpeed = new FuzzyTerm("fullSpeed", forceOutput, (x) => new FuzzyBool(MembershipFunctions.triangle(x, Some(4000), 6000, None)))

  val terms = isVeryClose :: isClose :: isNormal :: isFar :: isVeryFar :: isSlow :: isFast :: isExtreme :: fullBrake :: brake :: roll :: speed :: fullSpeed :: Nil

  // Rules
  val rules = List[FuzzyRule](
    new FuzzyRule("isVeryFar, isStanding", List(isVeryFar, isStanding), fullSpeed),
    new FuzzyRule("isVeryFar, isSlow", List(isVeryFar, isSlow), medSpeed),
    new FuzzyRule("isVeryFar, isFast", List(isVeryFar, isFast), speed),
    new FuzzyRule("isVeryFar, isExtreme", List(isVeryFar, isExtreme), speed),

    new FuzzyRule("isFar, isStanding", List(isFar, isStanding), medSpeed),
    new FuzzyRule("isFar, isSlow", List(isFar, isSlow), speed),
    new FuzzyRule("isFar, isFast", List(isFar, isFast), roll),
    new FuzzyRule("isFar, isExtreme", List(isFar, isExtreme), roll),

    new FuzzyRule("isNormal, isStanding", List(isNormal, isStanding), speed),
    new FuzzyRule("isNormal, isSlow", List(isNormal, isSlow), roll),
    new FuzzyRule("isNormal, isFast", List(isNormal, isFast), roll),
    new FuzzyRule("isNormal, isExtreme", List(isNormal, isExtreme), brake),

    new FuzzyRule("isClose, isStanding", List(isClose, isStanding), speed),
    new FuzzyRule("isClose, isSlow", List(isClose, isSlow), brake),
    new FuzzyRule("isClose, isFast", List(isClose, isFast), medBrake),
    new FuzzyRule("isClose, isExtreme", List(isClose, isExtreme), fullBrake),

    new FuzzyRule("isVeryClose, isStanding", List(isVeryClose, isStanding), brake),
    new FuzzyRule("isVeryClose, isSlow", List(isVeryClose, isSlow), medBrake),
    new FuzzyRule("isVeryClose, isFast", List(isVeryClose, isFast), fullBrake),
    new FuzzyRule("isVeryClose, is Extreme", List(isVeryClose, isExtreme), fullBrake)
  )

  val defuzzy = DefuzzyficationFunctions.functionList.find(p => p.name == "Center of Gravity").get
}
