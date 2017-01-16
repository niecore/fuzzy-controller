/**
  * Created by nico on 12.01.17.
  */

object Main extends App {


  val carFront, carBack = new Car
  val controller = new FuzzyCarController(FuzzyKnoledgeChaseCar, carBack, carFront)

  carFront.engineForce = 5000;

  while(true){
    if(carFront.speed >= 100)
      carFront.engineForce = 0
    carFront.tick()
    //carBack.tick()
    controller.tick()
    Thread.sleep(1000)
  }
}
