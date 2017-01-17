package model.physicalModel

/**
  * Created by nico on 12.01.17.
  */
object Physics{
  // kg / m^3
  val airDensity = 1.2
  // no dimension
  val friction = 0.02
  // m/s^2
  val gravity = 9.81

  def speedToKmh(ms:Double): Double = {
    ms * 3.6
  }
}

class Car extends Drivable{
  private var past: Long = System.currentTimeMillis()
  private var paused = false
  // kg
  val mass: Double = 1540.0
  // no dimension
  val windDragCoefficient: Double = 0.67
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
  // km/h
  var speedKmh: Double = 0;
  // s
  var totalTime: Double = 0;

  def reset(): Unit = {
    acceleration = 0;
    speed = 0;
    speedKmh = 0;
    engineForce = 0;
    totalTime = 0;
    paused = false;
    past = System.currentTimeMillis()
  }

  def pause(): Unit = {
    paused = true;
  }

  def resume(): Unit = {
    past = System.currentTimeMillis()
    paused = false;
  }

  def tick(): Unit = {
    if(paused) {
      return
    }

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

    speedKmh = Physics.speedToKmh(speed);

    var posDelta = ((0.5 * acceleration * sampleTime * sampleTime) + speed * sampleTime)
    if(posDelta >= 0) {
      position += posDelta
    }

    totalTime += sampleTime
    println("Acc: " + BigDecimal(acceleration).setScale(2, BigDecimal.RoundingMode.HALF_UP) + " Speed: " + BigDecimal(Physics.speedToKmh(speed)).setScale(2, BigDecimal.RoundingMode.HALF_UP) + " Pos: " + BigDecimal(position).setScale(2, BigDecimal.RoundingMode.HALF_UP) + " SampleTime: " + sampleTime + " Total: " + BigDecimal(totalTime).setScale(2,BigDecimal.RoundingMode.HALF_UP))

    past = now;
  }
}
