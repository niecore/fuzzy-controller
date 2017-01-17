package model.physicalModel

/**
  * Created by nico on 12.01.17.
  */
trait Drivable {
  def tick(): Unit
  var acceleration: Double
  var speed: Double
  var position: Double
  var engineForce: Double
}
