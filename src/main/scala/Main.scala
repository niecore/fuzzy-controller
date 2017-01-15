/**
  * Created by nico on 12.01.17.
  */

object Main extends App {


  val carFront, carBack = new Car
  val controller = new FuzzyCarController(FuzzyKnoledgeChaseCar, carBack, carFront)

  carFront.acceleration = 0.5;

  while(true){
    carFront.tick()
    carBack.tick()
    controller.tick()
    Thread.sleep(1000)
  }
}
