/**
  * Created by nico on 12.01.17.
  */
class FuzzyCarController(controlledCar: Drivable, chasedCar: Drivable) {



  def tick(): Unit = {
    // measure
    var distance = chasedCar.position - controlledCar.position
    var speed = controlledCar.speed

    // fuzzyfication



    // fuzzy logic

    // defuzzyfication

  }
}