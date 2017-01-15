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
    val sampleTime = (now - past) / 1000
    speed += acceleration * sampleTime
    position += (0.5 * acceleration * sampleTime * sampleTime) * speed

    println("Acc: " + acceleration + " Speed: " + speed + " Pos: " + position + " SampleTime: " + sampleTime)

    past = now;
  }
}
