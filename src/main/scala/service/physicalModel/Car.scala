package service.physicalModel

/**
  * Created by nico on 12.01.17.
  */
object Physics{
  // kg / m^3
  val airDensity = 1.2
  // no dimension
  val friction = 0.05
  // m/s^2
  val gravity = 9.81

  def speedToKmh(ms:Double): Double = {
    ms * 3.6
  }
}

class Car extends Drivable{
  private var past: Long = System.currentTimeMillis()

  // kg
  val mass: Double = 1540.0
  // no dimension
  val windDragCoefficient: Double = 0.37
  // m^2
  val crossSectionalArea: Double = 1.86

  // m/s^2
  var acceleration: Double = 0
  // m*s
  var speed: Double = 0
  // m
  var position: Double = 0
  // N = kg * m / s^2
  var engineForce: Double = 0;

  var total: Double = 0;

  def tick(): Unit = {
    val now = System.currentTimeMillis()
    val sampleTime: Double = (now - past) / 1000.0

    // F = a * m
    var force = 0.0;
    // Fahrwiederstand = Luftwiederstand + Rollwiederstand + Steigungswiederstand + Beschleunigungswiederstand
    // Luftwiederstand
    force += ((Physics.airDensity / 2) * crossSectionalArea * windDragCoefficient * speed * speed)
    // Rollwiederstand
    if(speed > 0) {
      force += (mass * Physics.friction * Physics.gravity)
    }
    // Steigungswiederstand
    force += (mass * Physics.gravity * Math.sin(0))
    // Beschleunigungswiederstandi
    //force += (mass * acceleration)

    acceleration = (engineForce - force) / mass
    speed += (acceleration * sampleTime)

    if(speed < 0) {
      speed = 0;
    }

    position += ((0.5 * acceleration * sampleTime * sampleTime) + speed * sampleTime)

    total += sampleTime
    println("Acc: " + acceleration + " Speed: " + speed * 3.6 + " Pos: " + position + " SampleTime: " + sampleTime + " Total: " + total)

    past = now;
  }
}
