/**
  * Created by nico on 12.01.17.
  */
class Car extends Drivable{
  private var past: Long = System.currentTimeMillis()

  // m/s^2
  var acceleration: Double = 0
  // m*s
  var speed: Double = 0
  // m
  var position: Double = 0;

  def tick(): Unit = {
    val now = System.currentTimeMillis()
    val sampleTime = now -past
    speed += acceleration * sampleTime
    position += (0.5 * acceleration * sampleTime * sampleTime) * speed

    if (speed == 0) { // Set acceleration to 0 when we stopped the car.
      acceleration = 0;
    }
  }
}
