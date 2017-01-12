/**
  * Created by nico on 12.01.17.
  */
class Car {

  private var past: Long = System.currentTimeMillis()
  private var acceleration: Double = 0

  var speed: Double = 0
  var position: Double = 0 // observable

  def setAcc(a: Float): Unit = {
    acceleration = a
  }

  def tick(): Unit = {
    val now = System.currentTimeMillis()
    val sampleTime = now -past
    speed += acceleration * sampleTime
    position += (0.5 * acceleration * sampleTime * sampleTime) * speed
    past = now
  }



}
