package model.fuzzyModel

import model.fuzzyModel.entity._

/**
  * Created by nico on 17.01.17.
  */

object DefaultConfig extends FuzzyConfig(RuleBase.fuzzyValueConnector, RuleBase.terms, RuleBase.rules, RuleBase.defuzzy)

object RuleBase {
  // Fuzzyfication
  val distanceInput = new FuzzyValueConnector("Distance", 0, 500, true)
  val speedInput = new FuzzyValueConnector("Speed", 0, 250, true)
  val forceOutput = new FuzzyValueConnector("Force", -8000, 4000, false)

  val fuzzyValueConnector = distanceInput :: speedInput :: forceOutput :: Nil

  val isVeryClose = new FuzzyTerm("isVeryClose", distanceInput, (x) => new FuzzyBool(MembershipFunctions.triangle(x, None, 100, Some(200))))
  val isClose = new FuzzyTerm("isClose", distanceInput,         (x) => new FuzzyBool(MembershipFunctions.triangle(x, Some(100), 200, Some(300))))
  val isNormal = new FuzzyTerm("isNormal", distanceInput,       (x) => new FuzzyBool(MembershipFunctions.triangle(x, Some(250), 300, Some(350))))
  val isFar = new FuzzyTerm("isFar", distanceInput,             (x) => new FuzzyBool(MembershipFunctions.triangle(x, Some(300), 400, Some(500))))
  val isVeryFar = new FuzzyTerm("isVeryFar", distanceInput,     (x) => new FuzzyBool(MembershipFunctions.triangle(x, Some(400), 500, None)))

  val isFixed = new FuzzyTerm("isFixed", speedInput,        (x) => new FuzzyBool(MembershipFunctions.triangle(x, Some(0), 0.5, Some(1))))
  val isSlow = new FuzzyTerm("isSlow", speedInput,          (x) => new FuzzyBool(MembershipFunctions.triangle(x, None, 15, Some(30))))
  val isFast = new FuzzyTerm("isFast", speedInput,          (x) => new FuzzyBool(MembershipFunctions.triangle(x, Some(15), 50, Some(100))))
  val isExtreme = new FuzzyTerm("isExtreme", speedInput,    (x) => new FuzzyBool(MembershipFunctions.triangle(x, Some(50), 150, None)))

  // Defuzzyfication
  val fullBrake = new FuzzyTerm("fullBrake", forceOutput, (x) => new FuzzyBool(MembershipFunctions.triangle(x, None, -8000, Some(-7000))))
  val medBrake = new FuzzyTerm("medBrake", forceOutput,   (x) => new FuzzyBool(MembershipFunctions.triangle(x, Some(-8000), -5000, Some(-2000))))
  val brake = new FuzzyTerm("brake", forceOutput,         (x) => new FuzzyBool(MembershipFunctions.triangle(x, Some(-5000), -2000, Some(0))))
  val roll = new FuzzyTerm("roll", forceOutput,           (x) => new FuzzyBool(MembershipFunctions.triangle(x, Some(-500), 0, Some(500))))
  val speed = new FuzzyTerm("speed", forceOutput,         (x) => new FuzzyBool(MembershipFunctions.triangle(x, Some(0), 1000, Some(2000))))
  val medSpeed = new FuzzyTerm("medSpeed", forceOutput,   (x) => new FuzzyBool(MembershipFunctions.triangle(x, Some(1000), 2000, Some(3000))))
  val fullSpeed = new FuzzyTerm("fullSpeed", forceOutput, (x) => new FuzzyBool(MembershipFunctions.triangle(x, Some(2000), 3000, None)))

  val terms =  medSpeed :: medBrake :: isVeryClose :: isClose :: isNormal :: isFar :: isVeryFar :: isSlow :: isFast :: isExtreme :: fullBrake :: brake :: roll :: speed :: fullSpeed :: Nil

  // Rules
  val rules = List[FuzzyRule](

    new FuzzyRule("isVeryFar, isSlow", List(isVeryFar, isSlow), fullSpeed),
    new FuzzyRule("isVeryFar, isFast", List(isVeryFar, isFast), fullSpeed),
    new FuzzyRule("isVeryFar, isExtreme", List(isVeryFar, isExtreme), fullSpeed),

    new FuzzyRule("isFar, isSlow", List(isFar, isSlow), fullSpeed),
    new FuzzyRule("isFar, isFast", List(isFar, isFast), medSpeed),
    new FuzzyRule("isFar, isExtreme", List(isFar, isExtreme), speed),

    new FuzzyRule("isNormal, isSlow", List(isNormal, isSlow), roll),
    new FuzzyRule("isNormal, isFast", List(isNormal, isFast), roll),
    new FuzzyRule("isNormal, isExtreme", List(isNormal, isExtreme), roll),

    new FuzzyRule("isClose, isSlow", List(isClose, isSlow), brake),
    new FuzzyRule("isClose, isFast", List(isClose, isFast), medBrake),
    new FuzzyRule("isClose, isExtreme", List(isClose, isExtreme), fullBrake),

    new FuzzyRule("isVeryClose, isSlow", List(isVeryClose, isSlow), fullBrake),
    new FuzzyRule("isVeryClose, isFast", List(isVeryClose, isFast), fullBrake),
    new FuzzyRule("isVeryClose, is Extreme", List(isVeryClose, isExtreme), fullBrake)
  )

  val defuzzy = DefuzzyficationFunctions.functionList.find(p => p.name == "Center of Gravity").get
}
